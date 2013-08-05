package com.taobao.csp.alarm.util;

import ch.ethz.ssh2.Connection;
import com.taobao.csp.alarm.tddl.TargetInfo;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * @author : shuquan.ljh
 * Date: 12-7-30
 * Time: ÏÂÎç4:18
 */
public class SSHUtil {
    private static final Logger logger = Logger.getLogger(SSHUtil.class);

    private SSHUtil() {

    }

    public static Connection creatSshConnect(TargetInfo targetInfo) {
        logger.info("Connection start: connnect with " + targetInfo.getIp());

        Connection conn = new Connection(targetInfo.getIp());
        try {
            conn.connect(null, 2000, 2000);
            boolean isAuthenticated = conn.authenticateWithPassword(targetInfo.getSshUserName(), targetInfo.getSshPassWord());
            if(!isAuthenticated) {
                logger.error(getErrorLog(targetInfo));
                return null;
            }
        }catch (IOException e) {
            logger.error("Connection " + targetInfo.getIp() + "fail", e);
            try{
                conn.close();
            }catch (Exception e1) {
                e1.printStackTrace();
            }
            conn = null;
        }
        return conn;
    }

    private static String getErrorLog(TargetInfo targetInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("connect fail. target")
                .append(targetInfo.getIp())
                .append("with userName:")
                .append(targetInfo.getSshUserName())
                .append(" password:")
                .append(targetInfo.getSshPassWord());
        return sb.toString();
    }
}
