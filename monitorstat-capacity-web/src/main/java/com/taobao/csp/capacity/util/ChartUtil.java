package com.taobao.csp.capacity.util;

import java.text.DecimalFormat;
import java.util.List;

import com.taobao.csp.capacity.po.CapacityRankingPo;
import com.taobao.csp.capacity.po.DependencyCapacityPo;

/***
 * ���ɸ���ͼ��Ĺ�����
 * ����ͼ��Ĵ���ȫ����ͳһ����
 * @author youji.zj
 *
 */
public class ChartUtil {
	
	/***
	 * ��������������ͼ
	 * @param pos
	 * @return
	 */
	public static String drawCapacityRankChart(List<CapacityRankingPo> pos) {
		StringBuffer sb = new StringBuffer();
		sb.append("<chart>");
		sb.append("<series>");
		for (int i = 0; i < pos.size(); i++) {
			CapacityRankingPo po = pos.get(i);
			sb.append("<value xid='" + (i + 1) + "'>" + po.getAppName()
					+ "</value>");
		}
		sb.append("</series>");

		sb.append("<graphs>");

		sb.append("<graph gid='pqs' title='��Ⱥ����' color='C49BEA'>");
		for (int i = 0; i < pos.size(); i++) {
			CapacityRankingPo po = pos.get(i);
			sb.append("<value xid='" + (i + 1) + "'>" + po.getCQps() / po.getCLoadrunQps() + "</value>");
		}
		sb.append("</graph>");

		sb.append("<graph gid='pqs' title='ʣ�༯Ⱥ����' color='A8D98B'>");
		for (int i = 0; i < pos.size(); i++) {
			CapacityRankingPo po = pos.get(i);
			sb.append("<value xid='" + (i + 1) + "'>" + (po.getCLoadrunQps() - po.getCQps()) / po.getCLoadrunQps()
					+ "</value>");
		}
		sb.append("</graph>");
		sb.append("</graphs>");
		sb.append("</chart>");

		return sb.toString();
	}
	
	/***
	 * ������Ԥ������ͼ
	 * @param pos
	 * @return
	 */
	public static String drawCapacityForestRankChart(List<CapacityRankingPo> pos) {
		StringBuffer sb = new StringBuffer();
		sb.append("<chart>");
		sb.append("<series>");
		for (int i = 0; i < pos.size(); i++) {
			CapacityRankingPo po = pos.get(i);
			sb.append("<value xid='" + (i + 1) + "'>" + po.getAppName()
					+ "</value>");
		}
		sb.append("</series>");

		sb.append("<graphs>");

		sb.append("<graph gid='pqs' title='��Ⱥ����' color='C49BEA'>");
		for (int i = 0; i < pos.size(); i++) {
			CapacityRankingPo po = pos.get(i);
			String levelDes = po.getFeatureData("Ԥ������ˮλ");
			double level = 0;
			if (levelDes != null && levelDes.length() > 1) {
				level = Double.parseDouble(levelDes.substring(0, levelDes.length() - 1));
			}
			
			sb.append("<value xid='" + (i + 1) + "'>" + level + "</value>");
		}
		sb.append("</graph>");

		sb.append("<graph gid='pqs' title='ʣ�༯Ⱥ����' color='A8D98B'>");
		for (int i = 0; i < pos.size(); i++) {
			CapacityRankingPo po = pos.get(i);
			String levelDes = po.getFeatureData("Ԥ������ˮλ");
			double level = 0;
			if (levelDes != null && levelDes.length() > 1) {
				level = Double.parseDouble(levelDes.substring(0, levelDes.length() - 1));
			}
			
			sb.append("<value xid='" + (i + 1) + "'>" + (100 - level)
					+ "</value>");
		}
		sb.append("</graph>");
		sb.append("</graphs>");
		sb.append("</chart>");

		return sb.toString();
	}
	
	/***
	 * ��������������״ͼ
	 * @param pos
	 * @return
	 */
	public static String drawDependCapacityChart(List<DependencyCapacityPo> pos) {
		StringBuffer sb = new StringBuffer();
		sb.append("<chart>");
		sb.append("<series>");
		for (int i = 0; i < pos.size(); i++) {
			DependencyCapacityPo po = pos.get(i);
			sb.append("<value xid='" + (i + 1) + "'>" + po.getDepApp()
					+ "</value>");
		}
		sb.append("</series>");

		sb.append("<graphs>");

		sb.append("<graph gid='pqs' title='����ˮλ' color='C49BEA'>");
		for (int i = 0; i < pos.size(); i++) {
			DependencyCapacityPo po = pos.get(i);
			sb.append("<value xid='" + (i + 1) + "'>" + po.getLevel() + "</value>");
		}
		sb.append("</graph>");

		sb.append("<graph gid='pqs' title='ʣ��ˮλ' color='A8D98B'>");
		for (int i = 0; i < pos.size(); i++) {
			DependencyCapacityPo po = pos.get(i);
			sb.append("<value xid='" + (i + 1) + "'>" + (100 - po.getLevel())
					+ "</value>");
		}
		sb.append("</graph>");
		sb.append("</graphs>");
		sb.append("</chart>");

		return sb.toString();
	}

}
