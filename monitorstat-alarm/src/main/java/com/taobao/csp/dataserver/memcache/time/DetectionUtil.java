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

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utility that detects various properties specific to the current runtime
 * environment, such as Java version and the availability of the
 * {@code sun.misc.Unsafe} object.
 * <p>
 * You can disable the use of {@code sun.misc.Unsafe} if you specify
 * the system property <strong>io.netty.noUnsafe</strong>.
 */
public class DetectionUtil {

	private static final int JAVA_VERSION = javaVersion0();
    private static final boolean HAS_UNSAFE = hasUnsafe(AtomicInteger.class.getClassLoader());
    private static final boolean IS_WINDOWS;
    private static final boolean IS_ROOT;

    static {
        String os = SystemPropertyUtil.get("os.name").toLowerCase();
        // windows
        IS_WINDOWS = os.contains("win");

        boolean root = false;
        //注释了，一般不会root运行的
//        if (!IS_WINDOWS) {
//            for (int i = 1023; i > 0; i --) {
//                ServerSocket ss = null;
//                try {
//                    ss = new ServerSocket();
//                    ss.setReuseAddress(true);
//                    ss.bind(new InetSocketAddress(i));
//                    root = true;
//                    break;
//                } catch (Exception e) {
//                    // Failed to bind.
//                    // Check the error message so that we don't always need to bind 1023 times.
//                    String message = e.getMessage();
//                    if (message == null) {
//                        message = "";
//                    }
//                    message = message.toLowerCase();
//                    if (message.matches(".*permission.*denied.*")) {
//                        break;
//                    }
//                } finally {
//                    if (ss != null) {
//                        try {
//                            ss.close();
//                        } catch (Exception e) {
//                            // Ignore.
//                        }
//                    }
//                }
//            }
//        }

        IS_ROOT = root;
    }

    /**
     * Return <code>true</code> if the JVM is running on Windows
     */
    public static boolean isWindows() {
        return IS_WINDOWS;
    }

    /**
     * Return {@code true} if the current user is root.  Note that this method returns
     * {@code false} if on Windows.
     */
    public static boolean isRoot() {
        return IS_ROOT;
    }

    public static boolean hasUnsafe() {
        return HAS_UNSAFE;
    }

    public static int javaVersion() {
        return JAVA_VERSION;
    }

    private static boolean hasUnsafe(ClassLoader loader) {
        boolean noUnsafe = SystemPropertyUtil.getBoolean("io.netty.noUnsafe", false);
        if (noUnsafe) {
            return false;
        }

        // Legacy properties
        boolean tryUnsafe = false;
        if (SystemPropertyUtil.contains("io.netty.tryUnsafe")) {
            tryUnsafe = SystemPropertyUtil.getBoolean("io.netty.tryUnsafe", true);
        } else {
            tryUnsafe = SystemPropertyUtil.getBoolean("org.jboss.netty.tryUnsafe", true);
        }

        if (!tryUnsafe) {
            return false;
        }

        try {
            Class<?> unsafeClazz = Class.forName("sun.misc.Unsafe", true, loader);
            return hasUnsafeField(unsafeClazz);
        } catch (Exception e) {
            // Ignore
        }

        return false;
    }

    private static boolean hasUnsafeField(final Class<?> unsafeClass) throws PrivilegedActionException {
        return AccessController.doPrivileged(new PrivilegedExceptionAction<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                unsafeClass.getDeclaredField("theUnsafe");
                return true;
            }
        });
    }

    private static int javaVersion0() {
        // Android
        try {
            Class.forName("android.app.Application", false, ClassLoader.getSystemClassLoader());
            return 6;
        } catch (Exception e) {
            // Ignore
        }

        try {
            Class.forName(
                    "java.util.concurrent.LinkedTransferQueue", false,
                    BlockingQueue.class.getClassLoader());
            return 7;
        } catch (Exception e) {
            // Ignore
        }

        return 6;
    }

    private DetectionUtil() {
        // only static method supported
    }
	
}

final class SystemPropertyUtil{
	
	
	 private static final Properties props = new Properties();
	 private static final Logger logger =
	            LoggerFactory.getLogger(SystemPropertyUtil.class);


	    // Retrieve all system properties at once so that there's no need to deal with
	    // security exceptions from next time.  Otherwise, we might end up with logging every
	    // security exceptions on every system property access or introducing more complexity
	    // just because of less verbose logging.
	    static {
	        refresh();
	    }

	    /**
	     * Re-retrieves all system properties so that any post-launch properties updates are retrieved.
	     */
	    public static void refresh() {
	        Properties newProps = null;
	        try {
	            newProps = System.getProperties();
	        } catch (SecurityException e) {
	            logger.warn("Unable to retrieve the system properties; default values will be used.", e);
	            newProps = new Properties();
	        }

	        synchronized (props) {
	            props.clear();
	            props.putAll(newProps);
	        }
	    }

	    /**
	     * Returns {@code true} if and only if the system property with the specified {@code key}
	     * exists.
	     */
	    public static boolean contains(String key) {
	        if (key == null) {
	            throw new NullPointerException("key");
	        }
	        return props.containsKey(key);
	    }

	    /**
	     * Returns the value of the Java system property with the specified
	     * {@code key}, while falling back to {@code null} if the property access fails.
	     *
	     * @return the property value or {@code null}
	     */
	    public static String get(String key) {
	        return get(key, null);
	    }

	    /**
	     * Returns the value of the Java system property with the specified
	     * {@code key}, while falling back to the specified default value if
	     * the property access fails.
	     *
	     * @return the property value.
	     *         {@code def} if there's no such property or if an access to the
	     *         specified property is not allowed.
	     */
	    public static String get(String key, String def) {
	        if (key == null) {
	            throw new NullPointerException("key");
	        }

	        String value = props.getProperty(key);
	        if (value == null) {
	            return def;
	        }

	        return value;
	    }

	    /**
	     * Returns the value of the Java system property with the specified
	     * {@code key}, while falling back to the specified default value if
	     * the property access fails.
	     *
	     * @return the property value.
	     *         {@code def} if there's no such property or if an access to the
	     *         specified property is not allowed.
	     */
	    public static boolean getBoolean(String key, boolean def) {
	        if (key == null) {
	            throw new NullPointerException("key");
	        }

	        String value = props.getProperty(key);
	        if (value == null) {
	            return def;
	        }

	        value = value.trim().toLowerCase();
	        if (value.length() == 0) {
	            return true;
	        }

	        if (value.equals("true") || value.equals("yes") || value.equals("1")) {
	            return true;
	        }

	        if (value.equals("false") || value.equals("no") || value.equals("0")) {
	            return false;
	        }

	        logger.warn(
	                "Unable to parse the boolean system property '" + key + "':" + value + " - " +
	                "using the default value: " + def);

	        return def;
	    }

	    /**
	     * Returns the value of the Java system property with the specified
	     * {@code key}, while falling back to the specified default value if
	     * the property access fails.
	     *
	     * @return the property value.
	     *         {@code def} if there's no such property or if an access to the
	     *         specified property is not allowed.
	     */
	    public static int getInt(String key, int def) {
	        if (key == null) {
	            throw new NullPointerException("key");
	        }

	        String value = props.getProperty(key);
	        if (value == null) {
	            return def;
	        }

	        value = value.trim().toLowerCase();
	        if (value.matches("-?[0-9]+")) {
	            try {
	                return Integer.parseInt(value);
	            } catch (Exception e) {
	                // Ignore
	            }
	        }

	        logger.warn(
	                "Unable to parse the integer system property '" + key + "':" + value + " - " +
	                "using the default value: " + def);

	        return def;
	    }

	    /**
	     * Returns the value of the Java system property with the specified
	     * {@code key}, while falling back to the specified default value if
	     * the property access fails.
	     *
	     * @return the property value.
	     *         {@code def} if there's no such property or if an access to the
	     *         specified property is not allowed.
	     */
	    public static long getLong(String key, long def) {
	        if (key == null) {
	            throw new NullPointerException("key");
	        }

	        String value = props.getProperty(key);
	        if (value == null) {
	            return def;
	        }

	        value = value.trim().toLowerCase();
	        if (value.matches("-?[0-9]+")) {
	            try {
	                return Long.parseLong(value);
	            } catch (Exception e) {
	                // Ignore
	            }
	        }

	        logger.warn(
	                "Unable to parse the long integer system property '" + key + "':" + value + " - " +
	                "using the default value: " + def);

	        return def;
	    }

	    private SystemPropertyUtil() {
	        // Unused
	    }
	
}
