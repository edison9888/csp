package com.taobao.monitor.web.report;

import com.taobao.monitor.web.ao.MonitorDayAo;
import com.taobao.monitor.web.ao.MonitorWebWWAo;
import com.taobao.monitor.web.vo.WebWWData;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: rentong
 * Date: 2010-7-23
 * Time: 13:59:25
 * WEB��������
 */
public class WebWWReport {
    private static Logger log = Logger.getLogger(WebWWReport.class);
    public static final int WEBWW_REPORT_NUMBER = 12;
    public static final long APP_ID = 22L;
    private static final long[] KEY_ID = new long[]{
            12224,//�û�UV    0
            12221,//�����ʱ��   1
            12225,//�������ʱ��    2
            12226,//������ʱ�䳤��    3
            12223,//��¼������       4
            12222,//һ���û����ٵ�¼������  5
            12220,//���û�����¼������    6
            12177,//дTFS������        7
            11858,//����Ϣ������         8
            11914,//������������         9
            11832,//������Ϣ�ɹ���         10
            15001 //������ƴ�����11
    };

    public static String getReportContents(String date) {
        Object[] values = new Object[WEBWW_REPORT_NUMBER];
         WebWWData data = MonitorWebWWAo.get().getWebWWOnlineMaxNumberByDay("1", date);
        if (data != null) {
            values[11] = data.getNumber();
        }
        try {
            DecimalFormat numberFormat = new DecimalFormat("#.##");
            Map<Long, Long> datas = MonitorDayAo.get().findMonitorCountMapAsValueByDate(APP_ID, date, KEY_ID);
            values[0] = datas.get(KEY_ID[0]);
            values[1] = numberFormat.format(datas.get(KEY_ID[4]) * 1.0 / datas.get(KEY_ID[0]));
            values[2] = datas.get(KEY_ID[6]);
            //  values[4] = datas.get(KEY_ID[5]);
            values[3] = numberFormat.format(datas.get(KEY_ID[3]) * 1.0 / datas.get(KEY_ID[4]));
            values[4] = datas.get(KEY_ID[1]);
            //values[7] = datas.get(KEY_ID[2]);
            values[5] = numberFormat.format(datas.get(KEY_ID[3]) * 1.0 / datas.get(KEY_ID[0]));
            values[6] = datas.get(KEY_ID[10]);
            values[7] = datas.get(KEY_ID[8]);
            values[8] = datas.get(KEY_ID[7]);
            values[9] = datas.get(KEY_ID[9]);
            values[10] = datas.get(KEY_ID[11]);
        } catch (Exception e) {
            log.error("error:", e);
            return null;
        }
        /*     for(int i = 0;i<values.length;i++){
            System.out.println(values[i]);
        }*/
        return renderReportHtml(values);
    }

    public static String renderReportHtml(Object[] values) {
        String html = contents;
        for (int i = 0; i < values.length; i++) {
            html = html.replace("${" + i + "}", values[i] == null ? "" : values[i].toString());
        }
        return html;
    }

    public static void main(String[] args) {
        System.out.println(getReportContents("2010-07-21"));
       // System.out.println(contents);
       // System.out.println("sdafsdfaasdfasdfasdf${0}".replace("${0}","--o--"));
    }

    private final static String contents = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
            "<html>\n" +
            " <head>\n" +
            "  <title>WEB�����ձ�</title>\n" +
            " </head>\n" +
            "\n" +
            " <body>\n" +
            "  <table style=\" font-size: 14px; border-collapse: collapse; font-family:΢���ź�\">\n" +
            "    <tr>\n" +
            "        <th style=\"width: 200px; height: 25px; border: solid 1px #000000;\">��������</th>\n" +
            "        <th style=\"width: 200px; height: 25px; border: solid 1px #000000;\">��ֵ</th>\n" +
            "    </tr>\n" +
            "\n" +
             "<tr>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "           �������������\n" +
            "        </td>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "${11}           ��\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "<tr>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "           �յ�¼�û�����\n" +
            "        </td>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "${0}           ��\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "            ��¼�������վ�����\n" +
            "        </td>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "${1}           ��\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "            ����ʱ����ÿ��ƽ������\n" +
            "        </td>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "${3}           ����\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "            ����ʱ����ÿ��ÿ��ƽ������\n" +
            "        </td>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "${5}          ����\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "            ������Ϣ������\n" +
            "        </td>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "${6}        ��</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "            ������Ϣ������\n" +
            "        </td>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "${7}        ��</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "            ������ͼ������\n" +
            "        </td>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "${8}        ��</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "            ������������������\n" +
            "        </td>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "${9}��</td>\n" +
            "    </tr>\n" +
            "\t <tr>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "           ������ƴ�����\n" +
            "        </td>\n" +
            "        <td style=\"width: 200px; height: 25px; border: solid 1px #000000;\">\n" +
            "\t${10}��\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "</table><div style=\"font-size: 12px;\"><br/>\n" +
            "WEB������������ʵʱͳ�ƣ�<a target=\"blank\" href=\"http://cm.taobao.net:9999/monitorstat/webww.html\">http://cm.taobao.net:9999/monitorstat/webww.html</a>\n" +
            "<br/>\n" +
            "������κ��������ң��г���Ʒ������--ҵ��ƽ̨�飺��ͨ(11278)<br/>\n" +
            "Email: rentong@taobao.com;hanzhang@taobao.com<br/>\n" +
            "Are you ready? �������������ʣ��� <br/>\n" +
            "�ԣ�����ϲ������<br/></div>\n" +
            " </body>\n" +
            "</html>";
}
