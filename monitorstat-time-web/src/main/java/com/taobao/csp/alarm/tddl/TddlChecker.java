package com.taobao.csp.alarm.tddl;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.alibaba.common.lang.StringUtil;
import com.taobao.csp.alarm.ao.DiamondServerAo;
import com.taobao.csp.alarm.util.SSHUtil;
import com.taobao.csp.common.ZKClient;
import com.taobao.monitor.common.ao.center.CspTimeKeyAlarmRecordAo;
import com.taobao.monitor.common.db.impl.center.AppInfoDao;
import com.taobao.monitor.common.messagesend.MessageSend;
import com.taobao.monitor.common.messagesend.MessageSendFactory;
import com.taobao.monitor.common.messagesend.MessageSendType;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by IntelliJ IDEA.
 *
 * @author : shuquan.ljh
 *         Date: 12-7-30
 *         Time: ����3:43
 */

public class TddlChecker implements Runnable {

    private Thread thread;

    private static final Logger logger = Logger.getLogger(TddlChecker.class);

    private static MessageSend messageSend = MessageSendFactory.create(MessageSendType.WangWang);

    private static List<String> acceptList = Arrays.asList("��ȫ", "����");

    private static DiamondServerAo diamondServerAo = new DiamondServerAo();

    //private static final String APP_NAME = "TCMAIN_MYSQL";

    //private static final int APP_ID = 322; //tradeplatform

    //private static final String IP = "172.23.183.141";    //tradeplatform

    private static final String PATH = "/home/admin/diamond/diamond/snapshot";

    private static final String GROUP = "DEFAULT_GROUP";

    private static final String DATA_ID_PREFIX = "com.taobao.tddl.v1_";

    private static final String DATA_ID_SUFFIX = "_dbgroups";

    private static final String GROUP_DATA_ID_PREFIX = "com.taobao.tddl.jdbc.group_V2.4.1_";

    private static final String GLOBAL_DATA_ID = "com.taobao.tddl.atom.global.";

    private static final String APP_DATA_ID = "com.taobao.tddl.atom.app.";

    private static final String USER_DATA_ID = "com.taobao.tddl.atom.passwd.";

    private static TddlChecker tddLListener = null;

    private static String TDDL_CONFIG_NODE = "/csp/monitor/tddl_config";

    private static final long PERIOD = 1 * 3600 * 1000L;



    public TddlChecker() {
        thread = new Thread(this);
        thread.setName("Diamond-Checker");
        thread.start();
    }

    private static void sendAlarm(String title, String info) {
        for (String accept : acceptList)
            messageSend.send(accept, title, info);
    }

    public void start() {
        logger.info("TddlChecker start");
        long start = System.currentTimeMillis();
        List<TddlRuleInfo> ruleInfoList = getTddlRuleInfoList();
        for(TddlRuleInfo ruleInfo : ruleInfoList) {
            check(ruleInfo);
        }
        long time = System.currentTimeMillis() - start;
        logger.info("TddlChecker end. Cost " + time + " millis");
        logger.info("�ٴ�ִ��TDDL���յ�Diamond����Դ������Ч��У����Ҫ�ȴ�ʱ��Ϊ��60����");
    }

    /**
     * �Ե�̨Ӧ�ý���У��
     * @param ruleInfo
     */
    public void check(TddlRuleInfo ruleInfo) {

        TargetInfo targetInfo = getAppInfo(ruleInfo);
        if(targetInfo == null) {
            logger.error("��ȡ��������:" + ruleInfo.getTargetIp() + "��Ϣ");
            return;
        }

        long start = System.currentTimeMillis();

        Connection conn = SSHUtil.creatSshConnect(targetInfo);
        if (conn == null) {
            return;
        }
        String result = excuteCommand("cat " + getMatrixFilePath(ruleInfo), conn);
        StringBuilder msg = new StringBuilder();
        if (StringUtil.isBlank(result)) {
            String error = "�ļ���" + getMatrixFilePath(ruleInfo) + "����Ϊ��";
            logger.error(error);
            msg.append(error).append("</br>");
            String diamondReport = diamondServerAo.getRencentChangegReport(getMatrixDataId(ruleInfo));
            if (StringUtil.isNotBlank(diamondReport)) {
                msg.append(diamondReport).append("</br>");
            }

        } else {
            String valiGroupKeyResult = valiGroupKey(result, conn, ruleInfo);
            if (StringUtil.isNotBlank(valiGroupKeyResult)) {
                msg.append(valiGroupKeyResult).append("</br>");
            }
        }
        if (StringUtil.isNotBlank(msg.toString())) {
            sendAlarm(ruleInfo.getAppName() + "����Diamond����TDDL���������쳣", msg.toString());
            insertCspRecord(ruleInfo);
        }
        conn.close();
        long time = System.currentTimeMillis() - start;
        logger.info("Check" + ruleInfo.getAppName() +" end. Cost " + time + " millis");
    }


    public static void addTddlRuleInfo(TddlRuleInfo tddlRuleInfo) {
        Object result = ZKClient.get().getData(TDDL_CONFIG_NODE + "/"+ tddlRuleInfo.getAppName() + "/" + tddlRuleInfo.getDbName());
        if(result != null) {
            ZKClient.get().delete(TDDL_CONFIG_NODE + "/" + tddlRuleInfo.getAppName() + "/" + tddlRuleInfo.getDbName());
        }
        ZKClient.get().mkdirPersistent(TDDL_CONFIG_NODE + "/" + tddlRuleInfo.getAppName() + "/" + tddlRuleInfo.getDbName(), tddlRuleInfo);
        logger.info("����TDDL������Ϣ��" + tddlRuleInfo.toString());
    }

    public static List<TddlRuleInfo> getTddlRuleInfoList() {
        List<TddlRuleInfo> ruleInfoList = new ArrayList<TddlRuleInfo>();
        List<String> tddlRuleInfoPath = ZKClient.get().list(TDDL_CONFIG_NODE);
        for(String infoPath : tddlRuleInfoPath) {
            List<String> ruleInfos = ZKClient.get().list(TDDL_CONFIG_NODE + "/" + infoPath);
            for(String ruleInfo : ruleInfos) {
                ruleInfoList.add((TddlRuleInfo)ZKClient.get().getData(TDDL_CONFIG_NODE + "/"+infoPath + "/" + ruleInfo));
            }
        }
        return ruleInfoList;
    }

    public static void deleteByAppName(String appName, String dbName) {
        ZKClient.get().delete(TDDL_CONFIG_NODE + "/" + appName + "/" + dbName);
        logger.info("ɾ��ZooKeeper ���" + TDDL_CONFIG_NODE + "/" + appName + "/" + dbName);
    }

    /**
     * �����ݿ����һ���澯��Ϣ
     * @param ruleInfo
     */
    private void insertCspRecord(TddlRuleInfo ruleInfo) {
        CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
        po.setApp_name("diamond");
        po.setKey_name("diamond���͵�TDDL���������쳣.");
        po.setKey_scope("APP");
        po.setAlarm_time(new Timestamp(System.currentTimeMillis()));
        po.setIp(ruleInfo.getTargetIp());
        po.setAlarm_cause("diamond���͵�TDDL���������쳣.");
        ArrayList<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
        list.add(po);
        CspTimeKeyAlarmRecordAo.get().insert(list);
    }


    private String valiGroupKey(String dbGroupKeyString, Connection conn, TddlRuleInfo ruleInfo) {

        StringBuilder msg = new StringBuilder();

        msg.append(valiDbGroupKeyFormat(dbGroupKeyString, ruleInfo));

        String[] dbGroupKeys = dbGroupKeyString.split(",");

        for (String dbGroupKey : dbGroupKeys) {

            String filePath = getDirectory() + GROUP_DATA_ID_PREFIX + dbGroupKey;
            String result = excuteCommand("cat " + filePath, conn);

            //result�ĸ�ʽ  my225088_cm4_tcmain0:r10w10p0,my160120_cm6_tcmain0:r0w0p0
            if (StringUtil.isNotBlank(result)) {
                String[] contents = result.split(",");
                if (contents.length != 2) {
                    String error = "�ļ���" + filePath + "���ݸ�ʽ����";
                    logger.error(error);
                    msg.append(error).append("</br>");
                } else {
                    String[] k_v_1 = contents[0].split(":");
                    if (k_v_1.length != 2) {
                        String errot = "�ļ���" + filePath + "���ݸ�ʽ��������Ϊ" + contents[0];
                        logger.error(errot);
                        msg.append(errot).append("</br>");
                    } else if (!k_v_1[1].equals("r10w10p0")) {
                        String error = "�ļ���" + filePath + "���ݿ��дȨ��������������Ϊ" + contents[0];
                        logger.error(error);
                        msg.append(error).append("</br>");
                    } else {
                    	//k_v_1[0] = my225088_cm4_tcmain0
                        String valiAtomResult = valiAtom(k_v_1[0], conn, ruleInfo);
                        if (StringUtil.isNotBlank(valiAtomResult)) {
                            msg.append(valiAtomResult).append("</br>");
                        }
                    }

                    String[] k_v_2 = contents[1].split(":");
                    if (k_v_2.length != 2) {
                        String error = "�ļ���" + filePath + "���ݸ�ʽ��������Ϊ��" + contents[1];
                        logger.error(error);
                    } else if (!k_v_2[1].equals("r0w0p0")) {
                        String error = "�ļ���" + filePath + "���ݿ��дȨ��������������Ϊ" + contents[1];
                        logger.error(error);
                    } else {
                        String valiAtomResult = valiAtom(k_v_2[0], conn, ruleInfo);
                        if (StringUtil.isNotBlank(valiAtomResult)) {
                            msg.append(valiAtomResult).append("</br>");
                        }
                    }
                }
                logger.info("����" + dbGroupKey + "ͨ��");
            } else {
                String error = "�ļ���" + filePath + "����Ϊ��";
                logger.error(error);
                msg.append(error).append("</br>");
            }
        }

        return msg.toString();
    }

    private String valiAtom(String dbKey, Connection conn, TddlRuleInfo ruleInfo) {
        String filePathGobal = getDirectory() + GLOBAL_DATA_ID + dbKey;
        String resultGobal = excuteCommand("cat " + filePathGobal, conn);
        StringBuilder msg = new StringBuilder();
        //У��com.taobao.tddl.atom.global.my225088_cm4_tcmain0 �ļ������Ƿ�Ϊ��
        if (StringUtil.isBlank(resultGobal)) {
            String diamondReport = diamondServerAo.getRencentChangegReport(filePathGobal);
            String error = filePathGobal + "�ļ�����Ϊ��";
            logger.error(error);
            msg.append(error).append("</br>");
            if (StringUtil.isNotBlank(diamondReport)) {
                msg.append(diamondReport).append("</br>");
            }
        } else {
            String resultUser = valiAtomUser(resultGobal, conn);
            if (StringUtil.isNotBlank(resultUser)) {
                msg.append(resultUser);
            }
        }

        //com.taobao.tddl.atom.app.TCMAIN_MYSQL.my225088_cm4_tcmain0 У���ļ������Ƿ�Ϊ��
        String filePathApp = getDirectory() + APP_DATA_ID + ruleInfo.getDbName() + "." + dbKey;
        String resultApp = excuteCommand("cat " + filePathApp, conn);
        if (StringUtil.isBlank(resultApp)) {
            String diamondReport = diamondServerAo.getRencentChangegReport(filePathApp);
            String error = filePathApp + "�ļ�����Ϊ��";
            logger.error(error);
            msg.append(error).append("</br>");
            if (StringUtil.isNotBlank(diamondReport)) {
                msg.append(diamondReport).append("</br>");
            }
        }

        return msg.toString();
    }

    /**
     * У��com.taobao.tddl.atom.passwd.  �ļ������Ƿ�Ϊ��
     * ���ݴ�global�ļ���ȡ��������ƴ���ļ�����
     * @param content
     * @param conn
     * @return
     */
    private String valiAtomUser(String content, Connection conn) {
        String dbName_line = content.substring(content.indexOf("dbName"), content.indexOf("dbType"));
        String dbName = dbName_line.substring(dbName_line.indexOf("=") + 1, dbName_line.length());
        String dbType_line = content.substring(content.indexOf("dbType"), content.indexOf("dbStatus"));
        String dbType = dbType_line.substring(dbType_line.indexOf("=") + 1, dbType_line.length());

        String userName = getUserNameFromDbName(dbName);

        String filePathUser = getDirectory() + USER_DATA_ID + dbName + "." + dbType + "." + userName;
        String resultUser = excuteCommand("cat " + filePathUser, conn);
        
        if (StringUtil.isBlank(resultUser)) {
            String error = "�ļ���" + filePathUser + "Ϊ��";
            logger.error(error);
            StringBuilder sb = new StringBuilder(error).append("</br>");
            sb.append(diamondServerAo.getRencentChangegReport(filePathUser));
            return sb.toString();
        }
        return null;
    }

    private String getUserNameFromDbName(String dbName) {
        Matcher matcher = Pattern.compile("[\\d]+").matcher(dbName);
        if(matcher.find()) {
            String strNumber = matcher.group();
            return dbName.substring(0, dbName.indexOf(strNumber));
        }
        return "";
    }

    private String excuteCommand(String command, Connection conn) {
        Session session = null;
        try {
            session = conn.openSession();
            session.execCommand(command);
            InputStream stdout = session.getStdout();
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            logger.error("excute command: " + command + " fail");
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private String valiDbGroupKeyFormat(String dbGroupKeyString, TddlRuleInfo ruleInfo) {
        String [] dbGroupKeys = dbGroupKeyString.split(",");
        String dbGroupKeyFormat = ruleInfo.getDbGroupKeyFormat();
        int dbGroupQuantity = Integer.parseInt(ruleInfo.getDbGroupQuantity());
        StringBuilder msg = new StringBuilder();
        if(dbGroupKeys.length != dbGroupQuantity){
            msg.append("dbGroup����Ϊ��").
                    append(dbGroupKeys.length).
                    append(", ����ֵΪ:").
                    append(dbGroupQuantity).
                    append("</br>");
        }
        for(String dbGroupKey : dbGroupKeys) {
            if(!dbGroupKey.matches(dbGroupKeyFormat)) {
                msg.append(dbGroupKey).
                        append("��ʽ�����ϱ�׼:").
                        append(dbGroupKeyFormat).
                        append("</br>");
            }
        }
        if(StringUtil.isNotBlank(msg.toString())){
            logger.error(msg.toString());
        }
        return msg.toString();
    }

    private TargetInfo getAppInfo(TddlRuleInfo ruleInfo) {
        TargetInfo targetInfo = new TargetInfo();
        AppInfoDao appInfoDao = new AppInfoDao();
        AppInfoPo appInfo = appInfoDao.findAppInfoById(Integer.parseInt(ruleInfo.getAppId()));
        targetInfo.setIp(ruleInfo.getTargetIp());
        targetInfo.setAppName(appInfo.getAppName());
        targetInfo.setSshUserName(appInfo.getLoginName());
        targetInfo.setSshPassWord(appInfo.getLoginPassword());
        return targetInfo;
    }

    private static String getMatrixDataId(TddlRuleInfo tddlRuleInfo) {
        return DATA_ID_PREFIX + tddlRuleInfo.getDbName() + DATA_ID_SUFFIX;
    }

    private static String getMatrixFilePath(TddlRuleInfo tddlRuleInfo) {
        return getDirectory() + getMatrixDataId(tddlRuleInfo);
    }

    private static String getDirectory() {
        return PATH + "/" + GROUP + "/";
    }

    public static synchronized void startup() {
        if (tddLListener == null) {
            tddLListener = new TddlChecker();
        }
    }

    public void run() {
        while (true) {
            tddLListener.start();
            try {
                Thread.sleep(PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error("TDDL���յ�Diamond����Դ������Ч��У���߳̽���", e);
                break;
            }
        }
    }

}
