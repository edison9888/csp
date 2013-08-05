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
     * δ�������������ʼֵΪ�����߳���
     */
    private static AtomicInteger unFinishedTaskCount = new AtomicInteger();

    public static AtomicInteger getUnFinishedTaskCount() {
        return unFinishedTaskCount;
    }

    public static void setUnFinishedTaskCount(AtomicInteger finishedTaskCount) {
        WebWangWangOnlineAnalyse.unFinishedTaskCount = finishedTaskCount;
    }

    /**
     * ��־������ʱ��
     */
    private static String dataDate = null;

    public static String getDataDate() {
        return dataDate;
    }

    public static void setDataDate(String dataDate) {
        WebWangWangOnlineAnalyse.dataDate = dataDate;
    }

    /**
     * ��ʱ�ļ����Ŀ¼
     */
    private static String tempFileRoot;

    public static String getTempFileRoot() {
        return tempFileRoot;
    }

    /**
     * ���ս��
     */
    UserVisitDO uvDO = new UserVisitDO();

    // ��ͨ����ı���

    private File logFile;
    private String writeLogFile = null;
    private static final Logger logger = Logger
            .getLogger(WebWangWangOnlineAnalyse.class);

    public WebWangWangOnlineAnalyse(String appName) {
        super(appName);
        init();
    }

    /**
     * ��ʹ��
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
            logger.error("��־�ļ������ڡ�");
        }

        // ��ʱ�ļ���Ŀ¼
        tempFileRoot = Config.getValue("LOG_PATH") + "/" + this.getAppName()
                + "/temp/";

        /*  tempFileRoot = "D:\\output\\l\\temp\\";

     writeLogFile ="d:\\output\\l\\login\\login-user.log";
     this.logFile = new File("D:\\output\\l\\");*/

        // ����Ŀ¼
        makeDirs(tempFileRoot);


    }

    /**
     * ����Ŀ¼
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
             * ��ȡȫ����Ϣ����ʵ��
             */
            MessageQueue mqueue = MessageQueue.getInstance();

            if (logFile.exists()) {

                /************************************************************************************
                 *
                 * Ԥ������̣�������ʱͳ���ļ���
                 *
                 * *****************************************************************
                 * ****************
                 */

                // ���˳���Ҫ�������ļ�
                File[] wwFiles = logFile.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.indexOf("web-wangwang-login") > -1;
//					return name.indexOf("login.log") > -1;
                    }
                });

                // �ļ�����
                final int fileNums = wwFiles.length;

                if (0 == fileNums)// �޷������ļ�ֱ���˳�
                    return;

                /**
                 * �����߳��������ɴ˵õ�ѭ������������,������
                 */
                int readThreadNums = 30;// Ĭ��ֵ30
                if (readThreadNums > fileNums)// ����ļ���С�ڷ�����̣߳����߳����޸�Ϊ�ļ���
                    readThreadNums = fileNums;

                final int batches = (int) Math.ceil((float) fileNums
                        / (float) readThreadNums);// ���Σ�С��30��Ĭ��ֵ�����ļ�����ֻ��һ����ɣ�
                final int yushu = fileNums % readThreadNums;// ������ֻ��һ��ʱ������Ϊ0��
                int loop = readThreadNums;// �������̸߳�������ѭ���Ĵ�������ʼֵ��

                ExecutorService exe = null;// �̳߳�
                ExecutorService exe2 = null;

                ReadMsgFromFileTask[] readMsgTasks;
                AnalysisMsgFromMQueue[] analysisTasks = null;

                /**
                 * �趨����������������˳��Լ��ִ��˳��
                 */
                CountDownLatch cdl;

                for (int i = 0; i < batches; i++) {// ��������

                    if (i == batches - 1 && yushu != 0) {// ������������ʱ��������ֵ
                        loop = yushu;
                    }

                    int analysisThreads = (int) Math.ceil((double) loop
                            / (double) 3);// (һ���з����߳�����Զ�Ƕ�ȡ�̵߳�1/3)

                    /*******************************************************************
                     *
                     * ��������
                     *
                     ******************************************************************/

                    // �趨δ�������ĳ�ʼֵ
                    setUnFinishedTaskCount(new AtomicInteger(loop));

                    // ��ѭ����������
                    cdl = new CountDownLatch(loop + analysisThreads);

                    readMsgTasks = new ReadMsgFromFileTask[loop];

                    for (int j = 0; j < loop; j++) {
                        readMsgTasks[j] = new ReadMsgFromFileTask(wwFiles[j
                                + readThreadNums * i], mqueue, cdl);
                    }

                    exe = Executors.newFixedThreadPool(loop);
                    for (ReadMsgFromFileTask rmt : readMsgTasks) {// ���͸�λ
                        exe.execute(rmt);
                    }

                    /*******************************************************************
                     *
                     * ���ѹ���
                     *
                     ******************************************************************/

                    analysisTasks = new AnalysisMsgFromMQueue[analysisThreads];
                    for (int j = 0; j < analysisThreads; j++) {
                        analysisTasks[j] = new AnalysisMsgFromMQueue(mqueue, cdl);
                    }

                    exe2 = Executors.newFixedThreadPool(analysisThreads);
                    for (AnalysisMsgFromMQueue amfmq : analysisTasks) {// ���͸�λ
                        exe2.execute(amfmq);
                    }

                    //
                    try {
                        cdl.await();//
                        // System.out.println("��" + (i + 1) + "�ֽ�����");
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

                // ִ����ȫ����Ԥ�������

                /************************************************************************************
                 *
                 * ����ͳ�ƹ��̣��������ս����
                 *
                 * *****************************************************************
                 * ****************
                 */

                /**
                 * ͳ��ָ��
                 */

                long uv = 0;

                long sumLoginTimes = 0;

                long maxLoginTimes = 0;

                long minLoginTimes = 10000;

                long sumOnlineMinutes = 0;

                long maxOnlineMinute = 0;

                long minOnlineMinute = 10000;

                // String inputFilePath = getTempFileRoot();

                // �����С
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

                // �õ�����

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
                        while ((contentStr = br.readLine()) != null) {// ������ȡ

                            sumLoginTimes++;// �ܵ�¼����

                            // System.out.println(contentStr);

                            params = contentStr.split(",");

                            userNick = params[0];
                            loginMinutes = Integer.valueOf(params[1]);

                            sumOnlineMinutes += loginMinutes;// ����������ʱ��

                            if (!result.containsKey(userNick)) {// �¼���һ������
                                uv++;// �û�������
                                userDO = new SingleUserStatisticVO(userNick, 1,
                                        loginMinutes);
                                result.put(userNick, userDO);

                                if (maxOnlineMinute < loginMinutes)// ȫ�����¼ʱ��
                                    maxOnlineMinute = loginMinutes;

                            } else {// ����ҵ��ˣ���Ҫ�������ݣ��ٱ���
                                SingleUserStatisticVO user1 = result
                                        .remove(userNick);

                                int loginTimes = user1.getLoginTimes() + 1;

                                int sumOnLineMinutes = user1.getSumOnLineMinutes()
                                        + loginMinutes;

                                if (maxLoginTimes < loginTimes) {// ȫ������¼����
                                    maxLoginTimes = loginTimes;
                                }
                                if (maxOnlineMinute < sumOnLineMinutes)// ����û�����ʱ��
                                    maxOnlineMinute = sumOnLineMinutes;

                                // �޸Ĳ�����д�ؽ����
                                user1.setLoginTimes(loginTimes);
                                user1.setSumOnLineMinutes(sumOnLineMinutes);
                                result.put(userNick, user1);
                            }

                        }// end while

                        // ���뵽�ļ��в����result
                        MinObject minObject = writeLog(result);

                        if (minLoginTimes > minObject.getMinLoginTimes())
                            minLoginTimes = minObject.getMinLoginTimes();
                        if (minOnlineMinute > minObject.getMinOnlineMinutes())
                            minOnlineMinute = minObject.getMinOnlineMinutes();

                        result.clear();

                        br.close();

                        // ɾ���ļ�
                        // if (tempFiles[i].isFile()) {
                        // System.out.println("��Ҫɾ���ˣ���");
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
                        + " ��\t��¼������" + sumLoginTimes + "\t��ʱ��:"
                        + sumOnlineMinutes);
                logger.warn("������:" + maxLoginTimes + "\t��С����:"
                        + minLoginTimes + "\t�ʱ��:" + maxOnlineMinute + "\t���ʱ��"
                        + minOnlineMinute);

            }
        } catch (Exception e) {
            logger.error("webww:", e);
        }
        
        insertToDb(content);
    }


    /**
     * ���������д���ļ�
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
            // ���Դ���
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
     * ���
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
     * ������
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
 * ҵ����
 *
 *******************************************************************************************/

/**
 * ��Ϣ����
 *
 * @author zhoushaotao.pt
 * @param
 */
class MessageQueue {
    private static final Logger logger = Logger
            .getLogger(WebWangWangOnlineAnalyse.class);
    /**
     * ****************************************************
     * �趨����Ϊ����
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
     * ��ʱ�������
     * ***************************************************
     */

    // ��ʱ���ɵ��ļ���
    private static final int temp_file_nums = 10;

    // ����ʱ�ļ���ԵĻ����б�
    List<List<FirstAnaResultDO>> tempResults = new ArrayList<List<FirstAnaResultDO>>(
            temp_file_nums);

    {
        for (int i = 0; i < temp_file_nums; i++) {
            List<FirstAnaResultDO> list = new ArrayList<FirstAnaResultDO>();
            tempResults.add(list);
        }
    }

    /*****************************************************
     * ʵ�ʴ洢����
     * **************************************************/

    /**
     * ������message��������ֵ,Ĭ��Ϊ10000
     */
    private final int MAX_MESSAGE_NUM = 200000;

    /**
     * ʵ�ʴ洢����
     */
    BlockingQueue<FirstAnaResultDO> queue = new ArrayBlockingQueue<FirstAnaResultDO>(
            MAX_MESSAGE_NUM);

    /******************************************************************************
     *
     * ==================================��������==================================
     *
     ****************************************************************************/

    /**
     * ������Ϣ����
     *
     * @param message
     */
    public void produce(FirstAnaResultDO message) {

        // �����Ϣ�����У�������
        try {
            queue.put(message);
        } catch (Exception e) {

            logger.error("�����쳣!!", e);
        }

        // System.out.println(Thread.currentThread().getName() + "��������"
        // + message.getUserNick() + "������  ,��ǰ����:" + getCount());

    }

    /**
     * ������Ϣ���� ����1 ���������� 0 �쳣������ -1 ����
     */
    public int consume() throws InterruptedException {

        FirstAnaResultDO message = queue.take();
        String userNick = message.getUserNick();

        /*********************************************************************
         * ���⴦���Ƿ�Ӧ�������������������������
         ********************************************************************/

        if (userNick == "--#--") {// ���������ʶ��ԭ������1
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
         * ��������ȷ��뻺��ռ��У����ﵽһ�������������
         ********************************************************************/

        // �ǳ����ļ�ӳ���ϵ
        int fileMapping = userNick.hashCode() % temp_file_nums;
        if (fileMapping < 0)
            fileMapping += temp_file_nums;

        synchronized (tempResults.get(fileMapping)) {
            tempResults.get(fileMapping).add(message);

            // ��ʱ�ռ��Ѿ����ˣ������
            if (tempResults.get(fileMapping).size() == 5000) {
                List<FirstAnaResultDO> resultList = tempResults
                        .get(fileMapping);

                String tempFilePath = new String(
                        WebWangWangOnlineAnalyse.getTempFileRoot()
                                + fileMapping);

                // ����д���ļ�
                batchWriteToFile(tempFilePath, resultList);
                tempResults.get(fileMapping).clear();
            }
        }

        // System.out.println(Thread.currentThread().getName() + "��������" +
        // userNick
        // + "������ ,��ǰ����: " + getCount());

        return 1;

    }

    /**
     * ������
     *
     * @param outputFilePath
     * @param writeContents
     */
    void batchWriteToFile(String outputFilePath,
                          List<FirstAnaResultDO> writeContents) {

        File file = null;

        try {
            file = new File(outputFilePath);
            // �����ڣ��򴴽��ļ�
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

        // д����
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(file, true));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ����д��
        for (int i = 0; i < writeContents.size(); i++) {
            FirstAnaResultDO temp = writeContents.get(i);

            int j = 0;

            try {
                output.write(temp.getUserNick() + "," + temp.getLoginMinutes());
                output.write("\n");
                j++;
                if (500 == j) {// ʮ�вŻ�д���̣���СIO������
                    output.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // �رջ���д����(���ڴ���)
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * �߳��˳�ǰ����
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

        // System.out.println("���޺�����Ϣ��" + Thread.currentThread().getName()
        // + "ֱ���˳�!!!");

        return -1;
    }

    /**
     * ��ȡ��ǰ��Ϣ��
     *
     * @return
     */
    public int getCount() {

        return queue.size();
    }
}

/**
 * *****************
 * �����ߵĶ�ȡ������
 * ******************
 */

class ReadMsgFromFileTask implements Runnable, Serializable {
    private static final Logger logger = Logger
            .getLogger(WebWangWangOnlineAnalyse.class);
    // �����С
    private static final int BUFFER_SIZE = 2000000;

    /**
     * ���л�UID
     */
    private static final long serialVersionUID = 1895703301952088394L;

    /**
     * ��Ϣ ǰ׺
     */
    final String PREFIX = "cntaobao";

    /**
     * �����ļ�·��
     */
    private File anaFile = null;

    // public String getTask() {
    // return this.fileFullPath;
    // }

    /**
     * ��Ϣ���ж���
     */
    private MessageQueue queue = null;

    public MessageQueue getQueue() {
        return queue;
    }

    /**
     * ��ʱ���ݴ��뼯��HashSet��
     */
    private Map<String, Long> maps = new HashMap<String, Long>();

    /**
     * ����������
     */
    private CountDownLatch counter;

    /**
     * ʱ���ʽ
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * ���캯��
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
        // System.out.println("���ڴ�����ļ���" + fileFullPath);

        BufferedReader br = null;

        try {

            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(anaFile)), BUFFER_SIZE);

            String contentStr = null;
            // String dataDate = null;// ��¼����
            String userNick = null;// �û��ǳ�
            boolean isLogin = true;// �ǵ�¼����ע��
            int loginMinutes = 0;// ����ʱ��
            int firstIndex, lastIndex;
            long loginTime, logoutTime;

            while ((contentStr = br.readLine()) != null) {// ������ȡ

                // ��ȡ��������(���ܿɸ��ƴ�������)
                if (WebWangWangOnlineAnalyse.getDataDate() == null)
                    WebWangWangOnlineAnalyse
                            .setDataDate(getDataDate(contentStr));

                // �������߶�ʮһ������ȡ�û��ǳ�
                firstIndex = contentStr.indexOf(PREFIX);
                lastIndex = contentStr.lastIndexOf(',');

                userNick = contentStr.substring(firstIndex + 8, lastIndex);

                if (contentStr.contains("- logout:"))// ������ϢΪ�û�ע����Ϣ�����������
                {
                    // ��������ڣ�˵�����û����˳���Ϣ��֮ǰ�ģ����账��
                    if (!maps.containsKey(userNick)) {
                        //continue;

                        //��ȡ���������ʱ��
                        int onlineMinutes = this.getOnlineMinutes(contentStr);

                        //System.out.println("userNick:"+userNick+"\tTime:"+onlineMinutes);

                        // ����һ��ÿһ�η����������
                        FirstAnaResultDO far = new FirstAnaResultDO();
                        far.setUserNick(userNick);
                        far.setLoginMinutes(onlineMinutes);// ����

                        queue.produce(far);

                    } else {// ������ڣ�����Ҫ��¼

                        // System.out.print("�û��ǳƣ�" + userNick);
                        // System.out.print("   --------Logout:");

                        isLogin = false;

                        logoutTime = getTimeByLine(isLogin, contentStr);// �õ������˳���¼��ʱ��

                        // �Ӳ�����һ��
                        loginTime = this.maps.remove(userNick);

                        loginMinutes = (int) Math
                                .ceil((double) ((float) (logoutTime - loginTime) / (float) (1000 * 60)));

                        // System.out.println(userNick + " " + loginMinutes);

                        // ����һ��ÿһ�η����������
                        FirstAnaResultDO far = new FirstAnaResultDO();
                        far.setUserNick(userNick);
                        far.setLoginMinutes(loginMinutes);// ����

                        queue.produce(far);

                    }

                } else {// ����Ϊ�û���¼��Ϣ

                    // System.out.print("�û��ǳƣ�"+userNick);
                    // System.out.print("   -------Login");

                    long beginLoginTime = getTimeByLine(isLogin, contentStr);
                    maps.put(userNick, beginLoginTime);
                }

            }// end while

            br.close();

        } catch (Exception e) {

            logger.error("���������쳣����", e);

        } finally {

            FirstAnaResultDO far = new FirstAnaResultDO();
            far.setUserNick("--#--");
            queue.produce(far);// �ڶ����в��������ʶ��#��

            /******************************************************************
             *
             * ���ڴ���ɾ���Ѿ�����������־��
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

            // ��������1
            counter.countDown();
//			System.out.println("��������ǰֵ�ǣ�" + counter.getCount());

        }

    }

    /**
     * ��ȡÿһ�е�ʱ��
     *
     * @return
     * @throws ParseException
     */
    private long getTimeByLine(boolean isLogin, String content)
            throws ParseException {

        long time = 0;

        if (isLogin) {// ��¼ʱ
            String temp = content.substring(content.lastIndexOf(",") + 1,
                    content.length());
            time = Long.valueOf(temp);
        } else {// ע����¼ʱ
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
     * ��ȡ��������
     */
    private static String getDataDate(String content) {
        String temp = content.substring(0, content.indexOf(" "));
        return temp;
    }

}

/**
 * �����ߵķ�������
 */
class AnalysisMsgFromMQueue implements Runnable, Serializable {

    private static final long serialVersionUID = -7465296515423636632L;

    /**
     * ��Ϣ���ж���
     */
    private MessageQueue queue = null;

    public MessageQueue getQueue() {
        return queue;
    }

    /**
     * ���������
     */
    private CountDownLatch counter = null;

    /**
     * ���캯��
     */
    public AnalysisMsgFromMQueue(MessageQueue mq, CountDownLatch counter) {
        this.queue = mq;
        this.counter = counter;
    }

    public void run() {

        while (true) {// ������������

            int state = 0;
            try {
                state = queue.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;// �����쳣�����߳�ֹͣ�����˳�
            }

            // ���������ɣ����˳�
            if (state == -1) {
                // System.out.println(Thread.currentThread().getName() +
                // "�˳�����");
                break;
            }
        }

        // ����ִ����ɣ���������1
        counter.countDown();
        // System.out.println("��������ǰֵ�ǣ�" + counter.getCount());

        // System.out.println(Thread.currentThread().getName() + "������ɣ�����");

    }

}

/********************************************************************************************
 *
 * ������
 *
 *******************************************************************************************/
// �Ѿ�ɾ��

/********************************************************************************************
 *
 * ʵ����
 *
 *******************************************************************************************/

/**
 * ��ʱ����ࣨ�����ǳƺʹ˴ε�¼ʱ�䣩
 *
 * @author zhoushaotao.pt
 */
class FirstAnaResultDO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3525832154106000939L;

    /**
     * �û��ǳ�
     */
    private String userNick;

    /**
     * ��¼ʱ��(��λ����)
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
 * ��Сֵͳ��ֵ�ࣨ�м����ࣩ
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
 * �û���ͳ��ֵ����д���ļ��У�
 *
 * @author zhoushaotao.pt
 */
class SingleUserStatisticVO implements Serializable {

    private static final long serialVersionUID = 6712960998693655582L;

    /**
     * �û��ǳ�
     */
    private String userNick;

    /**
     * �յ�¼����
     */
    private Integer loginTimes;

    /**
     * ������ʱ��
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
 * ��ͳ��ֵ�������ݿ��¼��
 *
 * @author zhoushaotao.pt
 */
class UserVisitDO implements Serializable {

    private static final long serialVersionUID = -4507933973602227360L;

    /**
     * ��������
     */
    private Date dataDate;

    /**
     * �û�������
     */
    private Long userVisit;

    /**
     * �ܵ�¼����
     */
    private Long sumLoginTimes;

    /**
     * �û�����¼����
     */
    private Long maxLoginTimes;

    /**
     * �û���̵�¼����
     */
    private Long minLoginTimes;

    /**
     * ������ʱ��
     */
    private Long sumOnlineMinutes;

    /**
     * �û��������ʱ��
     */
    private Long maxOnlineMinute;

    /**
     * �û�����С����ʱ��
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
     * ���췽��
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
