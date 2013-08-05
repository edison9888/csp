/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.taobao.csp.dataserver.memcache.time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.monitor.MonitorLog;

/**
 * A {@link Timer} optimized for approximated I/O timeout scheduling.
 *
 * <h3>Tick Duration</h3>
 *
 * As described with 'approximated', this timer does not execute the scheduled
 * {@link TimerTask} on time.  {@link HashedWheelTimer}, on every tick, will
 * check if there are any {@link TimerTask}s behind the schedule and execute
 * them.
 * <p>
 * You can increase or decrease the accuracy of the execution timing by
 * specifying smaller or larger tick duration in the constructor.  In most
 * network applications, I/O timeout does not need to be accurate.  Therefore,
 * the default tick duration is 100 milliseconds and you will not need to try
 * different configurations in most cases.
 *
 * <h3>Ticks per Wheel (Wheel Size)</h3>
 *
 * {@link HashedWheelTimer} maintains a data structure called 'wheel'.
 * To put simply, a wheel is a hash table of {@link TimerTask}s whose hash
 * function is 'dead line of the task'.  The default number of ticks per wheel
 * (i.e. the size of the wheel) is 512.  You could specify a larger value
 * if you are going to schedule a lot of timeouts.
 *
 * <h3>Do not create many instances.</h3>
 *
 * {@link HashedWheelTimer} creates a new thread whenever it is instantiated and
 * started.  Therefore, you should make sure to create only one instance and
 * share it across your application.  One of the common mistakes, that makes
 * your application unresponsive, is to create a new instance for every connection.
 *
 * <h3>Implementation Details</h3>
 *
 * {@link HashedWheelTimer} is based on
 * <a href="http://cseweb.ucsd.edu/users/varghese/">George Varghese</a> and
 * Tony Lauck's paper,
 * <a href="http://cseweb.ucsd.edu/users/varghese/PAPERS/twheel.ps.Z">'Hashed
 * and Hierarchical Timing Wheels: data structures to efficiently implement a
 * timer facility'</a>.  More comprehensive slides are located
 * <a href="http://www.cse.wustl.edu/~cdgill/courses/cs6874/TimingWheels.ppt">here</a>.
 */
public class HashedWheelTimer implements Timer {

	private static final Logger logger =
            LoggerFactory.getLogger(HashedWheelTimer.class);

    private static final SharedResourceMisuseDetector misuseDetector =
        new SharedResourceMisuseDetector(HashedWheelTimer.class);

    final Worker worker = new Worker();
    final Thread workerThread;
    final AtomicBoolean shutdown = new AtomicBoolean();

    //每一圈的总时间,比如512隔*100ms
    private final long roundDuration;
    //每一隔的时间间隔 比如100ms
    final long tickDuration;
    
    final Set<HashedWheelTimeout>[] wheel;
    final int mask;
    final ReadWriteLock lock = new ReentrantReadWriteLock();
    //当前位置
    volatile int wheelCursor;

    /**
     * Creates a new timer with the default thread factory
     * ({@link Executors#defaultThreadFactory()}), default tick duration, and
     * default number of ticks per wheel.
     */
    public HashedWheelTimer() {
        this(Executors.defaultThreadFactory());
    }

    
    /**
     * Creates a new timer with the default thread factory
     * ({@link Executors#defaultThreadFactory()}) and default number of ticks
     * per wheel.
     *
     * @param tickDuration   the duration between tick
     * @param unit           the time unit of the {@code tickDuration}
     */
    public HashedWheelTimer(long tickDuration, TimeUnit unit) {
        this(Executors.defaultThreadFactory(), tickDuration, unit);
    }

    /**
     * Creates a new timer with the default thread factory
     * ({@link Executors#defaultThreadFactory()}).
     *
     * @param tickDuration   the duration between tick
     * @param unit           the time unit of the {@code tickDuration}
     * @param ticksPerWheel  the size of the wheel
     */
    public HashedWheelTimer(long tickDuration, TimeUnit unit, int ticksPerWheel) {
        this(Executors.defaultThreadFactory(), tickDuration, unit, ticksPerWheel);
    }

    /**
     * Creates a new timer with the default tick duration and default number of
     * ticks per wheel.
     *
     * @param threadFactory  a {@link ThreadFactory} that creates a
     *                       background {@link Thread} which is dedicated to
     *                       {@link TimerTask} execution.
     */
    public HashedWheelTimer(ThreadFactory threadFactory) {
        this(threadFactory, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * Creates a new timer with the default number of ticks per wheel.
     *
     * @param threadFactory  a {@link ThreadFactory} that creates a
     *                       background {@link Thread} which is dedicated to
     *                       {@link TimerTask} execution.
     * @param tickDuration   the duration between tick
     * @param unit           the time unit of the {@code tickDuration}
     */
    public HashedWheelTimer(
            ThreadFactory threadFactory, long tickDuration, TimeUnit unit) {
        this(threadFactory, tickDuration, unit, 512);
    }

    /**
     * Creates a new timer.
     * 确定时间论的时间间隔以及总节点数
     * 并开启后台线程
     *
     * @param threadFactory  a {@link ThreadFactory} that creates a
     *                       background {@link Thread} which is dedicated to
     *                       {@link TimerTask} execution.
     * @param tickDuration   the duration between tick
     * @param unit           the time unit of the {@code tickDuration}
     * @param ticksPerWheel  the size of the wheel
     */
    public HashedWheelTimer(
            ThreadFactory threadFactory,
            long tickDuration, TimeUnit unit, int ticksPerWheel) {

        if (threadFactory == null) {
            throw new NullPointerException("threadFactory");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (tickDuration <= 0) {
            throw new IllegalArgumentException(
                    "tickDuration must be greater than 0: " + tickDuration);
        }
        if (ticksPerWheel <= 0) {
            throw new IllegalArgumentException(
                    "ticksPerWheel must be greater than 0: " + ticksPerWheel);
        }

        // Normalize ticksPerWheel to power of two and initialize the wheel.
        wheel = createWheel(ticksPerWheel);
        mask = wheel.length - 1;

        // Convert tickDuration to milliseconds.
        this.tickDuration = tickDuration = unit.toMillis(tickDuration);

        // Prevent overflow.
        if (tickDuration == Long.MAX_VALUE ||
                tickDuration >= Long.MAX_VALUE / wheel.length) {
            throw new IllegalArgumentException(
                    "tickDuration is too long: " +
                    tickDuration +  ' ' + unit);
        }

        roundDuration = tickDuration * wheel.length;

        workerThread = threadFactory.newThread(worker);

        // Misuse check
        misuseDetector.increase();
    }

    @SuppressWarnings("unchecked")
    private static Set<HashedWheelTimeout>[] createWheel(int ticksPerWheel) {
        if (ticksPerWheel <= 0) {
            throw new IllegalArgumentException(
                    "ticksPerWheel must be greater than 0: " + ticksPerWheel);
        }
        if (ticksPerWheel > 1073741824) {
            throw new IllegalArgumentException(
                    "ticksPerWheel may not be greater than 2^30: " + ticksPerWheel);
        }
        int normalizedTicksPerWheel = 1;
        while (normalizedTicksPerWheel < ticksPerWheel) {
            normalizedTicksPerWheel <<= 1;
        }
        //ticksPerWheel = normalizeTicksPerWheel(ticksPerWheel);
        Set<HashedWheelTimeout>[] wheel = new Set[normalizedTicksPerWheel];
        for (int i = 0; i < wheel.length; i ++) {
            wheel[i] = Collections.newSetFromMap(
                    new ConcurrentHashMap<HashedWheelTimeout, Boolean>(1024, 0.95f));
        }
        return wheel;
    }

    /**
     * Starts the background thread explicitly.  The background thread will
     * start automatically on demand even if you did not call this method.
     *
     * @throws IllegalStateException if this timer has been
     *                               {@linkplain #stop() stopped} already
     */
    public synchronized void start() {
        if (shutdown.get()) {
            throw new IllegalStateException("cannot be started once stopped");
        }

        if (!workerThread.isAlive()) {
            workerThread.start();
        }
    }

    @Override
    public synchronized Set<Timeout> stop() {
        if (Thread.currentThread() == workerThread) {
            throw new IllegalStateException(
                    HashedWheelTimer.class.getSimpleName() +
                    ".stop() cannot be called from " +
                    AbstraceTimeTask.class.getSimpleName());
        }

        if (!shutdown.compareAndSet(false, true)) {
            return Collections.emptySet();
        }

        boolean interrupted = false;
        while (workerThread.isAlive()) {
            workerThread.interrupt();
            try {
                workerThread.join(100);
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }

        if (interrupted) {
            Thread.currentThread().interrupt();
        }

        misuseDetector.decrease();

        Set<Timeout> unprocessedTimeouts = new HashSet<Timeout>();
        for (Set<HashedWheelTimeout> bucket: wheel) {
            unprocessedTimeouts.addAll(bucket);
            bucket.clear();
        }

        return Collections.unmodifiableSet(unprocessedTimeouts);
    }

    @Override
//    public Timeout newTimeout(AbstraceTimeTask task, long delay, TimeUnit unit) {
//        final long currentTime = System.currentTimeMillis();
//        if (task == null) {
//            throw new NullPointerException("task");
//        }
//        if (unit == null) {
//            throw new NullPointerException("unit");
//        }
//
//        if (!workerThread.isAlive()) {
//            start();
//        }
//
//        delay = unit.toMillis(delay);
//
//        //每个task都关联一个Timeout对象用来调度
//        HashedWheelTimeout timeout = new HashedWheelTimeout(task, currentTime + delay);
//        //将任务加入时间轮，主要是定位到哪个位置，以及总共几轮
//        scheduleTimeout(timeout, delay,true);
//        
//        return timeout;
//    }
    
    public Timeout newTimeout(HashedWheelTimeout timeout, long delay, TimeUnit unit) {
        final long currentTime = System.currentTimeMillis();
        if (timeout == null) {
            throw new NullPointerException("task");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }

        if (!workerThread.isAlive()) {
            start();
        }

        delay = unit.toMillis(delay);

        //每个task都关联一个Timeout对象用来调度
        timeout.initStat(currentTime + delay);
       // HashedWheelTimeout timeout = new HashedWheelTimeout(task, currentTime + delay);
        //将任务加入时间轮，主要是定位到哪个位置，以及总共几轮
        scheduleTimeout(timeout, delay);
        
        return timeout;
    }

    /**
     * 调度timeout对象，在设置任务的位置时，因为有另一个线程在随着时间的推移，改变curor，所以有一个lock来保证并发的安全性
     * 
     * @param timeout
     * @param delay
     * @param isNew	重定位的还是新的
     */
    void scheduleTimeout(HashedWheelTimeout timeout, long delay) {
        // delay must be equal to or greater than tickDuration so that the
        // worker thread never misses the timeout.
        if (delay < tickDuration) {
            delay = tickDuration;
        }
        
        // Prepare the required parameters to schedule the timeout object.
        final long lastRoundDelay = delay % roundDuration;
        final long lastTickDelay = delay % tickDuration;
        
        //定位到在哪一片
         long relativeIndex =
            lastRoundDelay / tickDuration + (lastTickDelay != 0? 1 : 0);

        //需要几轮
        final long remainingRounds =
            delay / roundDuration - (delay % roundDuration == 0? 1 : 0);

        // Add the timeout to the wheel. not need lock bishan.ct
        lock.readLock().lock();
        try {
    		//避免在某些时间间隔下，大量任务需要重新计算
        	relativeIndex++;	
    	
            int stopIndex = (int) (wheelCursor + relativeIndex & mask);
            timeout.stopIndex = stopIndex;
            timeout.remainingRounds = remainingRounds;
            
            wheel[stopIndex].add(timeout);
        } finally {
            lock.readLock().unlock();
        }
    }

    private final class Worker implements Runnable {

        private long startTime;
        private long tick;
        //public AtomicInteger ai=new AtomicInteger(0);

        Worker() {
        }

        @Override
        public void run() {
//            List<HashedWheelTimeout> expiredTimeouts =
//                new ArrayList<HashedWheelTimeout>();

            startTime = System.currentTimeMillis();
            tick = 1;
            //System.out.println(startTime);
            while (!shutdown.get()) {
            	/**
            	 * 得到本次任务应该执行的最大时间，大于这个时间的要重新计算位置
            	 */
                final long deadline = waitForNextTick();
                if (deadline > 0) {
                    fetchExpiredTimeouts(deadline);
                    //notifyExpiredTimeouts(expiredTimeouts,s);
                }
            }
        }

        private void fetchExpiredTimeouts(long deadline) {

            // Find the expired timeouts and decrease the round counter
            // if necessary.  Note that we don't send the notification
            // immediately to make sure the listeners are called without
            // an exclusive lock.
            lock.writeLock().lock();
            try {
            	//System.out.println("==="+wheelCursor+":cu："+System.currentTimeMillis()+","+deadline);
            	//走到newWheelCursor，代表已经过了newWheelCursor*tickDuration的时间，比如
            	//tickDuration是9，当前位置在9，表示已经过了900ms，可以调度801-900的任务
                int newWheelCursor = wheelCursor = wheelCursor + 1 & mask;
                fetchExpiredTimeouts(wheel[newWheelCursor].iterator(), deadline);
            } finally {
                lock.writeLock().unlock();
            }
        }

        private void fetchExpiredTimeouts(
                Iterator<HashedWheelTimeout> i, long deadline) {

            List<HashedWheelTimeout> slipped = null;
            while (i.hasNext()) {
                HashedWheelTimeout timeout = i.next();
                if (timeout.remainingRounds <= 0) {
                    i.remove();
                   
                    if (timeout.deadline <= deadline) {
                        //expiredTimeouts.add(timeout);
                        timeout.expire();
                    } else {
                    	//ai.incrementAndGet();
                    	//System.out.println("=======================fuck come here");
                        // Handle the case where the timeout is put into a wrong
                        // place, usually one tick earlier.  For now, just add
                        // it to a temporary list - we will reschedule it in a
                        // separate loop.
                    	MonitorLog.addStat("datacache", new String[]{"timewheelredo"}, new Long[]{1L});
                        
                        if (slipped == null) {
                            slipped = new ArrayList<HashedWheelTimeout>();
                        }
                        slipped.add(timeout);
                    }
                } else {
                    timeout.remainingRounds --;
                }
            }

            // Reschedule the slipped timeouts.
            if (slipped != null) {
                for (HashedWheelTimeout timeout: slipped) {
                    scheduleTimeout(timeout, timeout.deadline - deadline);
                }
            }
        }

        //这种方式只会在有大量任务超时的时候增加内存消耗，增加GC
//        private void notifyExpiredTimeouts(
//                List<HashedWheelTimeout> expiredTimeouts,long doBefore) {
//            // Notify the expired timeouts.
//            for (int i = expiredTimeouts.size() - 1; i >= 0; i --) {
//                expiredTimeouts.get(i).expire();
//            }
//
//            MonitorLog.addStat("datacache", new String[]{"timewheel"}, 
//    				new Long[]{Long.valueOf(expiredTimeouts.size()),System.currentTimeMillis()-doBefore});
//            
//            // Clean up the temporary list.
//            expiredTimeouts.clear();
//        }

        private long waitForNextTick() {
            long deadline = startTime + tickDuration * tick;
            
            for (;;) {
                final long currentTime = System.currentTimeMillis();
                long sleepTime = tickDuration * tick - (currentTime - startTime);

                // Check if we run on windows, as if thats the case we will need
                // to round the sleepTime as workaround for a bug that only affect
                // the JVM if it runs on windows.
                //
                // See https://github.com/netty/netty/issues/356
                if (DetectionUtil.isWindows()) {
                    sleepTime = sleepTime / 10 * 10;
                }

                if (sleepTime <= 0) {
                    break;
                }
                //System.out.println("==="+wheelCursor+":cu:"+currentTime+":sleep"+sleepTime);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    if (shutdown.get()) {
                        return -1;
                    }
                }
            }

            // Increase the tick.
            tick ++;
            return deadline;
        }
    }

    
    public Set<HashedWheelTimeout>[] getWheel() {
		return wheel;
	}

	static class CSPThreadFactory implements ThreadFactory {
        static final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix="csp-timewheel";

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r,  namePrefix + 
                                  threadNumber.getAndIncrement());
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}