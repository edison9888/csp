package com.taobao.monitor.stat.analyse;

import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.stat.content.ReportContentInterface;
import com.taobao.monitor.stat.util.Config;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by IntelliJ IDEA. User: rentong Date: 2010-6-29 Time: 10:24:55
 */
public class WebWangWangOnlineAnalyse extends Analyse {

    /**
     * 未完成任务数：初始值为生产线程数
     */
    private static AtomicInteger unFinishedTaskCount = new AtomicInteger();

    public static AtomicInteger getUnFinishedTaskCount() {
        return unFinishedTaskCount;
    }

    public static void setUnFinishedTaskCount(AtomicInteger finishedTaskCount) {
        WebWangWangOnlineAnalyse.unFinishedTaskCount = finishedTaskCount;
    }

    /**
     * 日志的数据时间
     */
    private static String dataDate = null;

    public static String getDataDate() {
        return dataDate;
    }

    public static void setDataDate(String dataDate) {
        WebWangWangOnlineAnalyse.dataDate = dataDate;
    }

    /**
     * 临时文件存放目录
     */
    private static String tempFileRoot;

    public static String getTempFileRoot() {
        return tempFileRoot;
    }

    /**
     * 最终结果
     */
    UserVisitDO uvDO = new UserVisitDO();

    // 仁通定义的变量

    private File logFile;
    private String writeLogFile = null;
    private static final Logger logger = Logger
            .getLogger(WebWangWangOnlineAnalyse.class);

    public WebWangWangOnlineAnalyse(String appName) {
        super(appName);
        init();
    }

    /**
     * 初使化
     */
    public void init() {
    	File file = new File(Config.getValue("LOG_PATH") + "/"+ this.getAppName() + "/login");
        if(!file.exists()){
        	file.mkdirs();
        }
    	
        this.logFile = new File(Config.getValue("LOG_PATH") + "/"
                + this.getAppName());
        this.writeLogFile = Config.getValue("LOG_PATH") + "/"
                + this.getAppName() + "/login/login-user.log";

        if (!this.logFile.exists()) {
            logger.error("日志文件不存在。");
        }

        // 临时文件的目录
        tempFileRoot = Config.getValue("LOG_PATH") + "/" + this.getAppName()
                + "/temp/";

        /*  tempFileRoot = "D:\\output\\l\\temp\\";

     writeLogFile ="d:\\output\\l\\login\\login-user.log";
     this.logFile = new File("D:\\output\\l\\");*/

        // 创建目录
        makeDirs(tempFileRoot);


    }

    /**
     * 创建目录
     *
     * @param dir
     */
    private static void makeDirs(String dir) {
        File dirs = new File(dir);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
    }

    @Override
    public void analyseLogFile(ReportContentInterface content) {
        try {
            /**
             * 获取全局消息队列实例
             */
            MessageQueue mqueue = MessageQueue.getInstance();

            if (logFile.exists()) {

                /************************************************************************************
                 *
                 * 预处理过程（生成临时统计文件）
                 *
                 * *****************************************************************
                 * ****************
                 */

                // 过滤出需要分析的文件
                File[] wwFiles = logFile.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.indexOf("web-wangwang-login") > -1;
//					return name.indexOf("login.log") > -1;
                    }
                });

                // 文件个数
                final int fileNums = wwFiles.length;

                if (0 == fileNums)// 无分析的文件直接退出
                    return;

                /**
                 * 设置线程数，并由此得到循环遍历的批次,和余数
                 */
                int readThreadNums = 30;// 默认值30
                if (readThreadNums > fileNums)// 如果文件数小于分配的线程，则将线程数修改为文件数
                    readThreadNums = fileNums;

                final int batches = (int) Math.ceil((float) fileNums
                        / (float) readThreadNums);// 批次（小于30（默认值）个文件，则只做一趟完成）
                final int yushu = fileNums % readThreadNums;// 余数（只有一趟时，余数为0）
                int loop = readThreadNums;// 生产的线程个数，子循环的次数（初始值）

                ExecutorService exe = null;// 线程池
                ExecutorService exe2 = null;

                ReadMsgFromFileTask[] readMsgTasks;
                AnalysisMsgFromMQueue[] analysisTasks = null;

                /**
                 * 设定倒数计数器，用于顺序约定执行顺序
                 */
                CountDownLatch cdl;

                for (int i = 0; i < batches; i++) {// 逐批访问

                    if (i == batches - 1 && yushu != 0) {// 超过两批以上时的余数赋值
                        loop = yushu;
                    }

                    int analysisThreads = (int) Math.ceil((double) loop
                            / (double) 3);// (一轮中分析线程数永远是读取线程的1/3)

                    /*******************************************************************
                     *
                     * 生产过程
                     *
                     ******************************************************************/

                    // 设定未完成任务的初始值
                    setUnFinishedTaskCount(new AtomicInteger(loop));

                    // 行循环倒计数器
                    cdl = new CountDownLatch(loop + analysisThreads);

                    readMsgTasks = new ReadMsgFromFileTask[loop];

                    for (int j = 0; j < loop; j++) {
                        readMsgTasks[j] = new ReadMsgFromFileTask(wwFiles[j
                                + readThreadNums * i], mqueue, cdl);
                    }

                    exe = Executors.newFixedThreadPool(loop);
                    for (ReadMsgFromFileTask rmt : readMsgTasks) {// 各就各位
                        exe.execute(rmt);
                    }

                    /*******************************************************************
                     *
                     * 消费过程
                     *
                     ******************************************************************/

                    analysisTasks = new AnalysisMsgFromMQueue[analysisThreads];
                    for (int j = 0; j < analysisThreads; j++) {
                        analysisTasks[j] = new AnalysisMsgFromMQueue(mqueue, cdl);
                    }

                    exe2 = Executors.newFixedThreadPool(analysisThreads);
                    for (AnalysisMsgFromMQueue amfmq : analysisTasks) {// 各就各位
                        exe2.execute(amfmq);
                    }

                    //
                    try {
                        cdl.await();//
                        // System.out.println("第" + (i + 1) + "轮结束！");
                        analysisTasks = null;
                        readMsgTasks = null;
                        exe.shutdown();
                        exe2.shutdown();
                        cdl = null;
                        mqueue.queue.clear();

                    } catch (InterruptedException e) {
                        logger.error("webww:", e);
                    }

                }

                // 执行完全部的预处理过程

                /************************************************************************************
                 *
                 * 分析统计过程（生成最终结果）
                 *
                 * *****************************************************************
                 * ****************
                 */

                /**
                 * 统计指标
                 */

                long uv = 0;

                long sumLoginTimes = 0;

                long maxLoginTimes = 0;

                long minLoginTimes = 10000;

                long sumOnlineMinutes = 0;

                long maxOnlineMinute = 0;

                long minOnlineMinute = 10000;

                // String inputFilePath = getTempFileRoot();

                // 缓存大小
                /* private static */
                final int BUFFER_SIZE = 2000000;

                File[] tempFiles = new File(getTempFileRoot()).listFiles();

                // List<String> lists = FileOperaUtil.readDir(getTempFileRoot());
                BufferedReader br = null;
                String contentStr = null;
                String[] params = null;
                String userNick = null;
                int loginMinutes = 0;
                java.sql.Date dataDate = null;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                long timpstamps = 0;
                SingleUserStatisticVO userDO = null;
                // MinObject minObject = null;

                Map<String, SingleUserStatisticVO> result = new HashMap<String, SingleUserStatisticVO>();

                // 得到日期

                try {
                    timpstamps = sdf.parse(WebWangWangOnlineAnalyse.getDataDate())
                            .getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                dataDate = new java.sql.Date(timpstamps);

                for (int i = 0; i < tempFiles.length; i++) {

                    try {
                        br = new BufferedReader(new InputStreamReader(
                                new FileInputStream(tempFiles[i])), BUFFER_SIZE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    try {
                        while ((contentStr = br.readLine()) != null) {// 逐行提取

                            sumLoginTimes++;// 总登录次数

                            // System.out.println(contentStr);

                            params = contentStr.split(",");

                            userNick = params[0];
                            loginMinutes = Integer.valueOf(params[1]);

                            sumOnlineMinutes += loginMinutes;// 计算总在线时间

                            if (!result.containsKey(userNick)) {// 新加入一个变量
                                uv++;// 用户访问量
                                userDO = new SingleUserStatisticVO(userNick, 1,
                                        loginMinutes);
                                result.put(userNick, userDO);

                                if (maxOnlineMinute < loginMinutes)// 全局最长登录时间
                                    maxOnlineMinute = loginMinutes;

                            } else {// 如果找到了，则要更新数据，再保存
                                SingleUserStatisticVO user1 = result
                                        .remove(userNick);

                                int loginTimes = user1.getLoginTimes() + 1;

                                int sumOnLineMinutes = user1.getSumOnLineMinutes()
                                        + loginMinutes;

                                if (maxLoginTimes < loginTimes) {// 全局最大登录次数
                                    maxLoginTimes = loginTimes;
                                }
                                if (maxOnlineMinute < sumOnLineMinutes)// 最大用户在线时间
                                    maxOnlineMinute = sumOnLineMinutes;

                                // 修改并重新写回结果表
                                user1.setLoginTimes(loginTimes);
                                user1.setSumOnLineMinutes(sumOnLineMinutes);
                                result.put(userNick, user1);
                            }

                        }// end while

                        // 输入到文件中并清空result
                        MinObject minObject = writeLog(result);

                        if (minLoginTimes > minObject.getMinLoginTimes())
                            minLoginTimes = minObject.getMinLoginTimes();
                        if (minOnlineMinute > minObject.getMinOnlineMinutes())
                            minOnlineMinute = minObject.getMinOnlineMinutes();

                        result.clear();

                        br.close();

                        // 删除文件
                        // if (tempFiles[i].isFile()) {
                        // System.out.println("我要删除了！！");
                        tempFiles[i].delete();
                        // }

                    } catch (NumberFormatException e) {
                        logger.error("webww:", e);
                    } catch (IOException e) {
                        logger.error("webww:", e);
                    }

                }// end for

                uvDO.setDataDate(dataDate);
                uvDO.setUserVisit(uv);
                uvDO.setSumLoginTimes(sumLoginTimes);
                uvDO.setMaxLoginTimes(maxLoginTimes);
                uvDO.setMinLoginTimes(minLoginTimes);
                uvDO.setSumOnlineMinutes(sumOnlineMinutes);
                uvDO.setMaxOnlineMinute(maxOnlineMinute);
                uvDO.setMinOnLineMinute(minOnlineMinute);

                logger.warn("dataDate:" + dataDate + "\t uv:" + uv
                        + " 总\t登录次数：" + sumLoginTimes + "\t总时间:"
                        + sumOnlineMinutes);
                logger.warn("最大次数:" + maxLoginTimes + "\t最小次数:"
                        + minLoginTimes + "\t最长时间:" + maxOnlineMinute + "\t最短时间"
                        + minOnlineMinute);

            }
        } catch (Exception e) {
            logger.error("webww:", e);
        }
        
        insertToDb(content);
    }


    /**
     * 将分析结果写入文件
     *
     * @param
     */

    private MinObject writeLog(Map<String, SingleUserStatisticVO> results) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String postfix = format.format(new Date());
        FileWriter stream = null;
        BufferedWriter bufferWriter = null;
        MinObject minObject = new MinObject();
        int minOnlineMinutes = 10000, minLoginTimes = 10000;

        try {
            // 测试代码
            // String output = "d:/log/" + this.getAppName() +
            // "/login-user.log."
            // + postfix;
            // File file = new File(output);


            stream = new FileWriter(new File(writeLogFile + "." + postfix), true);
            bufferWriter = new BufferedWriter(stream);

            for (Map.Entry<String, SingleUserStatisticVO> data : results
                    .entrySet()) {

                bufferWriter.write(data.getKey());
                bufferWriter.write(',');

                int sumLoginTimes = data.getValue().getLoginTimes();
                if (sumLoginTimes < minLoginTimes)
                    minLoginTimes = sumLoginTimes;
                bufferWriter.write(Integer.toString(sumLoginTimes));
                bufferWriter.write(',');

                int sumOnlineMinutes = data.getValue().getSumOnLineMinutes();
                bufferWriter.write(Long.toString(sumOnlineMinutes));

                if (sumOnlineMinutes < minOnlineMinutes)
                    minOnlineMinutes = sumOnlineMinutes;

                bufferWriter.write('\n');
            }
            bufferWriter.flush();

            minObject.setMinLoginTimes(minLoginTimes);
            minObject.setMinOnlineMinutes(minOnlineMinutes);
            return minObject;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("webwangwang log write error:", e);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("webwangwang log write error:", e);
            return null;
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException e) {
            }
            try {
                if (bufferWriter != null)
                    bufferWriter.close();
            } catch (IOException e) {
            }

        }
    }


    /**
     * 入库
     */
    @Override
    protected void insertToDb(ReportContentInterface content) {
        try {
            Map<String, Long> data = new HashMap<String, Long>();

            data.put("OTHER_WangWangLOGIN_TIMES_" + Constants.COUNT_TIMES_FLAG,
                    uvDO.getSumLoginTimes());
            data.put("OTHER_WangWangONLINE_TIME_" + Constants.COUNT_TIMES_FLAG,
                    uvDO.getSumOnlineMinutes());
            data.put("OTHER_WangWangONLINE_USERVIEW_" + Constants.COUNT_TIMES_FLAG,
                    (long) uvDO.getUserVisit());

            data.put("OTHER_WangWangONLINE_MAXTIME_" + Constants.COUNT_TIMES_FLAG,
                    uvDO.getMaxOnlineMinute());
            data.put("OTHER_WangWangONLINE_MINTIME_" + Constants.COUNT_TIMES_FLAG,
                    uvDO.getMinOnLineMinute());
            data.put("OTHER_WangWangLOGIN_MAXTIMES_" + Constants.COUNT_TIMES_FLAG,
                    uvDO.getMaxLoginTimes());
            data.put("OTHER_WangWangLOGIN_MINTIMES_" + Constants.COUNT_TIMES_FLAG,
                    uvDO.getMinLoginTimes());
            content.putReportDataByCount(this.getAppName(), data,
                    this.getCollectDate()); // YYYY-mm-dd
        } catch (Exception e) {
            logger.error("webww error:", e);
        }
    }

    /**
     * 主函数
     *
     * @param args
     */
    public static void main(String[] args) {
        WebWangWangOnlineAnalyse w = new WebWangWangOnlineAnalyse("webwangwang");
        w.init();
        w.analyseLogFile(null);

    }
}

/********************************************************************************************
 *
 * 业务类
 *
 *******************************************************************************************/

/**
 * 消息队列
 *
 * @author zhoushaotao.pt
 * @param
 */
class MessageQueue {
    private static final Logger logger = Logger
            .getLogger(WebWangWangOnlineAnalyse.class);
    /**
     * ****************************************************
     * 设定对象为单例
     * ***************************************************
     */
    private static MessageQueue mq = new MessageQueue();

    private MessageQueue() {
    }

    ;

    public static MessageQueue getInstance() {
        return mq;
    }

    /**
     * ****************************************************
     * 临时结果对象
     * ***************************************************
     */

    // 临时生成的文件数
    private static final int temp_file_nums = 10;

    // 与临时文件相对的缓存列表
    List<List<FirstAnaResultDO>> tempResults = new ArrayList<List<FirstAnaResultDO>>(
            temp_file_nums);

    {
        for (int i = 0; i < temp_file_nums; i++) {
            List<FirstAnaResultDO> list = new ArrayList<FirstAnaResultDO>();
            tempResults.add(list);
        }
    }

    /*****************************************************
     * 实际存储对象
     * **************************************************/

    /**
     * 队列中message对象的最大值,默认为10000
     */
    private final int MAX_MESSAGE_NUM = 200000;

    /**
     * 实际存储队列
     */
    BlockingQueue<FirstAnaResultDO> queue = new ArrayBlockingQueue<FirstAnaResultDO>(
            MAX_MESSAGE_NUM);

    /******************************************************************************
     *
     * ==================================方法定义==================================
     *
     ****************************************************************************/

    /**
     * 生产消息方法
     *
     * @param message
     */
    public void produce(FirstAnaResultDO message) {

        // 添加消息到队列（生产）
        try {
            queue.put(message);
        } catch (Exception e) {

            logger.error("消费异常!!", e);
        }

        // System.out.println(Thread.currentThread().getName() + "正在生产"
        // + message.getUserNick() + "。。。  ,当前个数:" + getCount());

    }

    /**
     * 消费消息方法 返回1 正常，返回 0 异常，返回 -1 结束
     */
    public int consume() throws InterruptedException {

        FirstAnaResultDO message = queue.take();
        String userNick = message.getUserNick();

        /*********************************************************************
         * 特殊处理（是否应跳出分析，即分析任务结束）
         ********************************************************************/

        if (userNick == "--#--") {// 遇到特殊标识，原子数减1
            int count = WebWangWangOnlineAnalyse.getUnFinishedTaskCount()
                    .decrementAndGet();
            // System.out.println(count);
            if (count == 0) {
                return exitThread();
            } else {
                return 1;
            }
        } else if (userNick == "//@\\") {//
            return exitThread();
        }

        /*********************************************************************
         * 逐个处理（先放入缓存空间中，若达到一定数量则输出）
         ********************************************************************/

        // 昵称与文件映射关系
        int fileMapping = userNick.hashCode() % temp_file_nums;
        if (fileMapping < 0)
            fileMapping += temp_file_nums;

        synchronized (tempResults.get(fileMapping)) {
            tempResults.get(fileMapping).add(message);

            // 临时空间已经满了，则输出
            if (tempResults.get(fileMapping).size() == 5000) {
                List<FirstAnaResultDO> resultList = tempResults
                        .get(fileMapping);

                String tempFilePath = new String(
                        WebWangWangOnlineAnalyse.getTempFileRoot()
                                + fileMapping);

                // 批量写入文件
                batchWriteToFile(tempFilePath, resultList);
                tempResults.get(fileMapping).clear();
            }
        }

        // System.out.println(Thread.currentThread().getName() + "正在消费" +
        // userNick
        // + "。。。 ,当前个数: " + getCount());

        return 1;

    }

    /**
     * 批处理
     *
     * @param outputFilePath
     * @param writeContents
     */
    void batchWriteToFile(String outputFilePath,
                          List<FirstAnaResultDO> writeContents) {

        File file = null;

        try {
            file = new File(outputFilePath);
            // 不存在，则创建文件
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 写内容
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(file, true));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 批量写入
        for (int i = 0; i < writeContents.size(); i++) {
            FirstAnaResultDO temp = writeContents.get(i);

            int j = 0;

            try {
                output.write(temp.getUserNick() + "," + temp.getLoginMinutes());
                output.write("\n");
                j++;
                if (500 == j) {// 十行才回写磁盘（减小IO次数）
                    output.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 关闭缓冲写入流(后期处理)
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 线程退出前处理
     *
     * @return
     */
    private int exitThread() {

        try {
            FirstAnaResultDO far = new FirstAnaResultDO();
            far.setUserNick("//@\\");
            queue.put(far);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // System.out.println("已无后续消息，" + Thread.currentThread().getName()
        // + "直接退出!!!");

        return -1;
    }

    /**
     * 获取当前消息数
     *
     * @return
     */
    public int getCount() {

        return queue.size();
    }
}

/**
 * *****************
 * 生产者的读取任务类
 * ******************
 */

class ReadMsgFromFileTask implements Runnable, Serializable {
    private static final Logger logger = Logger
            .getLogger(WebWangWangOnlineAnalyse.class);
    // 缓存大小
    private static final int BUFFER_SIZE = 2000000;

    /**
     * 序列化UID
     */
    private static final long serialVersionUID = 1895703301952088394L;

    /**
     * 信息 前缀
     */
    final String PREFIX = "cntaobao";

    /**
     * 完整文件路径
     */
    private File anaFile = null;

    // public String getTask() {
    // return this.fileFullPath;
    // }

    /**
     * 消息队列对象
     */
    private MessageQueue queue = null;

    public MessageQueue getQueue() {
        return queue;
    }

    /**
     * 临时数据存入集（HashSet）
     */
    private Map<String, Long> maps = new HashMap<String, Long>();

    /**
     * 倒数计数器
     */
    private CountDownLatch counter;

    /**
     * 时间格式
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 构造函数
     */
    public ReadMsgFromFileTask(File anaFile, MessageQueue mq,
                               CountDownLatch counter) {
        this.anaFile = anaFile;
        this.queue = mq;
        this.counter = counter;
    }

    public ReadMsgFromFileTask(File anaFile, CountDownLatch counter) {
        this.anaFile = anaFile;
        this.queue = null;
        this.counter = counter;
    }

    public ReadMsgFromFileTask(File anaFile) {
        this.anaFile = anaFile;
        this.queue = null;
        this.counter = null;
    }

    public void run() {
        // System.out.println("正在处理的文件：" + fileFullPath);

        BufferedReader br = null;

        try {

            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(anaFile)), BUFFER_SIZE);

            String contentStr = null;
            // String dataDate = null;// 登录日期
            String userNick = null;// 用户昵称
            boolean isLogin = true;// 是登录还是注销
            int loginMinutes = 0;// 在线时间
            int firstIndex, lastIndex;
            long loginTime, logoutTime;

            while ((contentStr = br.readLine()) != null) {// 逐行提取

                // 获取当天日期(性能可改善处。。。)
                if (WebWangWangOnlineAnalyse.getDataDate() == null)
                    WebWangWangOnlineAnalyse
                            .setDataDate(getDataDate(contentStr));

                // 不管三七二十一，先提取用户昵称
                firstIndex = contentStr.indexOf(PREFIX);
                lastIndex = contentStr.lastIndexOf(',');

                userNick = contentStr.substring(firstIndex + 8, lastIndex);

                if (contentStr.contains("- logout:"))// 此条消息为用户注销消息，分情况处理
                {
                    // 如果不存在，说明此用户的退出消息是之前的，不需处理
                    if (!maps.containsKey(userNick)) {
                        //continue;

                        //提取后面的在线时间
                        int onlineMinutes = this.getOnlineMinutes(contentStr);

                        //System.out.println("userNick:"+userNick+"\tTime:"+onlineMinutes);

                        // 构造一个每一次分析结果对象
                        FirstAnaResultDO far = new FirstAnaResultDO();
                        far.setUserNick(userNick);
                        far.setLoginMinutes(onlineMinutes);// 分钟

                        queue.produce(far);

                    } else {// 如果存在，则需要记录

                        // System.out.print("用户昵称：" + userNick);
                        // System.out.print("   --------Logout:");

                        isLogin = false;

                        logoutTime = getTimeByLine(isLogin, contentStr);// 得到的是退出登录的时间

                        // 加不加锁一样
                        loginTime = this.maps.remove(userNick);

                        loginMinutes = (int) Math
                                .ceil((double) ((float) (logoutTime - loginTime) / (float) (1000 * 60)));

                        // System.out.println(userNick + " " + loginMinutes);

                        // 构造一个每一次分析结果对象
                        FirstAnaResultDO far = new FirstAnaResultDO();
                        far.setUserNick(userNick);
                        far.setLoginMinutes(loginMinutes);// 分钟

                        queue.produce(far);

                    }

                } else {// 此条为用户登录消息

                    // System.out.print("用户昵称："+userNick);
                    // System.out.print("   -------Login");

                    long beginLoginTime = getTimeByLine(isLogin, contentStr);
                    maps.put(userNick, beginLoginTime);
                }

            }// end while

            br.close();

        } catch (Exception e) {

            logger.error("读对象中异常！！", e);

        } finally {

            FirstAnaResultDO far = new FirstAnaResultDO();
            far.setUserNick("--#--");
            queue.produce(far);// 在队列中插入特殊标识“#”

            /******************************************************************
             *
             * 后期处理（删除已经分析过的日志）
             *
             * ***************************************************************
             */
            try {
                br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            anaFile.delete();
            anaFile = null;

            // 计数器减1
            counter.countDown();
//			System.out.println("计数器当前值是：" + counter.getCount());

        }

    }

    /**
     * 获取每一行的时间
     *
     * @return
     * @throws ParseException
     */
    private long getTimeByLine(boolean isLogin, String content)
            throws ParseException {

        long time = 0;

        if (isLogin) {// 登录时
            String temp = content.substring(content.lastIndexOf(",") + 1,
                    content.length());
            time = Long.valueOf(temp);
        } else {// 注销记录时
            String temp = content.substring(0, content.indexOf(","));
            time = sdf.parse(temp).getTime();
        }
        return time;

    }

    private int getOnlineMinutes(String content) {
        String temp = content.substring(content.lastIndexOf(",") + 1,
                content.length());
        int time = Integer.valueOf(temp);
        return time;
    }

    /**
     * 获取数据日期
     */
    private static String getDataDate(String content) {
        String temp = content.substring(0, content.indexOf(" "));
        return temp;
    }

}

/**
 * 消费者的分析任务
 */
class AnalysisMsgFromMQueue implements Runnable, Serializable {

    private static final long serialVersionUID = -7465296515423636632L;

    /**
     * 消息队列对象
     */
    private MessageQueue queue = null;

    public MessageQueue getQueue() {
        return queue;
    }

    /**
     * 任务记数器
     */
    private CountDownLatch counter = null;

    /**
     * 构造函数
     */
    public AnalysisMsgFromMQueue(MessageQueue mq, CountDownLatch counter) {
        this.queue = mq;
        this.counter = counter;
    }

    public void run() {

        while (true) {// 反复分析任务

            int state = 0;
            try {
                state = queue.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;// 发生异常，此线程停止任务并退出
            }

            // 如果任务完成，则退出
            if (state == -1) {
                // System.out.println(Thread.currentThread().getName() +
                // "退出！！");
                break;
            }
        }

        // 任务执行完成，计数器减1
        counter.countDown();
        // System.out.println("计数器当前值是：" + counter.getCount());

        // System.out.println(Thread.currentThread().getName() + "任务完成！！！");

    }

}

/********************************************************************************************
 *
 * 工具类
 *
 *******************************************************************************************/
// 已经删除

/********************************************************************************************
 *
 * 实体类
 *
 *******************************************************************************************/

/**
 * 临时结果类（包括昵称和此次登录时间）
 *
 * @author zhoushaotao.pt
 */
class FirstAnaResultDO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3525832154106000939L;

    /**
     * 用户昵称
     */
    private String userNick;

    /**
     * 登录时间(单位：分)
     */
    private long loginMinutes;

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public long getLoginMinutes() {
        return loginMinutes;
    }

    public void setLoginMinutes(long loginMinutes) {
        this.loginMinutes = loginMinutes;
    }

    public String toString() {
        return "[FirstAndResult: UserNick=" + getUserNick()
                + ",  loginMinutes=" + getLoginMinutes() + "]";
    }
}

/**
 * 最小值统计值类（中间结果类）
 *
 * @author zhoushaotao.pt
 */
class MinObject {

    private int minLoginTimes;

    private int minOnlineMinutes;

    public int getMinLoginTimes() {
        return minLoginTimes;
    }

    public void setMinLoginTimes(int minLoginTimes) {
        this.minLoginTimes = minLoginTimes;
    }

    public int getMinOnlineMinutes() {
        return minOnlineMinutes;
    }

    public void setMinOnlineMinutes(int minOnlineMinutes) {
        this.minOnlineMinutes = minOnlineMinutes;
    }

}

/**
 * 用户日统计值对象（写入文件中）
 *
 * @author zhoushaotao.pt
 */
class SingleUserStatisticVO implements Serializable {

    private static final long serialVersionUID = 6712960998693655582L;

    /**
     * 用户昵称
     */
    private String userNick;

    /**
     * 日登录次数
     */
    private Integer loginTimes;

    /**
     * 总在线时间
     */
    private Integer sumOnLineMinutes;

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public Integer getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Integer loginTimes) {
        this.loginTimes = loginTimes;
    }

    public Integer getSumOnLineMinutes() {
        return sumOnLineMinutes;
    }

    public void setSumOnLineMinutes(Integer allOnLineMinutes) {
        this.sumOnLineMinutes = allOnLineMinutes;
    }

    public SingleUserStatisticVO(String userNick, Integer loginTimes,
                                 Integer allOnLineMinutes) {
        this.userNick = userNick;
        this.loginTimes = loginTimes;
        this.sumOnLineMinutes = allOnLineMinutes;
    }

    public SingleUserStatisticVO() {
    }
}

/**
 * 日统计值对象（数据库记录）
 *
 * @author zhoushaotao.pt
 */
class UserVisitDO implements Serializable {

    private static final long serialVersionUID = -4507933973602227360L;

    /**
     * 数据日期
     */
    private Date dataDate;

    /**
     * 用户访问量
     */
    private Long userVisit;

    /**
     * 总登录次数
     */
    private Long sumLoginTimes;

    /**
     * 用户最大登录次数
     */
    private Long maxLoginTimes;

    /**
     * 用户最短登录次数
     */
    private Long minLoginTimes;

    /**
     * 总在线时间
     */
    private Long sumOnlineMinutes;

    /**
     * 用户的最长在线时间
     */
    private Long maxOnlineMinute;

    /**
     * 用户的最小在线时间
     */
    private Long minOnLinuesMinute;

    public Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(Date dataDate) {
        this.dataDate = dataDate;
    }

    public Long getUserVisit() {
        return userVisit;
    }

    public void setUserVisit(Long userVisit) {
        this.userVisit = userVisit;
    }

    public Long getSumLoginTimes() {
        return sumLoginTimes;
    }

    public void setSumLoginTimes(Long sumLoginTimes) {
        this.sumLoginTimes = sumLoginTimes;
    }

    public Long getMaxLoginTimes() {
        return maxLoginTimes;
    }

    public void setMaxLoginTimes(Long maxLoginTimes) {
        this.maxLoginTimes = maxLoginTimes;
    }

    public Long getMinLoginTimes() {
        return minLoginTimes;
    }

    public void setMinLoginTimes(Long minLoginTimes) {
        this.minLoginTimes = minLoginTimes;
    }

    public Long getSumOnlineMinutes() {
        return sumOnlineMinutes;
    }

    public void setSumOnlineMinutes(Long sumOnlineMinutes) {
        this.sumOnlineMinutes = sumOnlineMinutes;
    }

    public Long getMaxOnlineMinute() {
        return maxOnlineMinute;
    }

    public void setMaxOnlineMinute(Long maxOnlineMinute) {
        this.maxOnlineMinute = maxOnlineMinute;
    }

    public Long getMinOnLineMinute() {
        return minOnLinuesMinute;
    }

    public void setMinOnLineMinute(Long minOnlineMinute) {
        this.minOnLinuesMinute = minOnlineMinute;
    }

    public UserVisitDO() {
    }

    /**
     * 构造方法
     *
     * @param dataDate
     * @param userVisits
     * @param maxLoginTimes
     * @param minLoginTimes
     * @param maxOnlineMinute
     * @param minOnlineMinute
     */
    public UserVisitDO(Date dataDate, Long userVisits, Long sumLoginTimes,
                       Long maxLoginTimes, Long minLoginTimes, Long sumOnlineMinutes,
                       Long maxOnlineMinute, Long minOnlineMinute) {
        this.dataDate = dataDate;
        this.userVisit = userVisits;
        this.sumLoginTimes = sumLoginTimes;
        this.maxLoginTimes = maxLoginTimes;
        this.minLoginTimes = minLoginTimes;
        this.sumOnlineMinutes = sumOnlineMinutes;

    }

}
