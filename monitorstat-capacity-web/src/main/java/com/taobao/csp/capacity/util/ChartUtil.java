package com.taobao.csp.capacity.util;

import java.text.DecimalFormat;
import java.util.List;

import com.taobao.csp.capacity.po.CapacityRankingPo;
import com.taobao.csp.capacity.po.DependencyCapacityPo;

/***
 * 生成各种图像的工具类
 * 生成图像的代码全放这统一管理
 * @author youji.zj
 *
 */
public class ChartUtil {
	
	/***
	 * 画容量排行柱形图
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

		sb.append("<graph gid='pqs' title='集群负荷' color='C49BEA'>");
		for (int i = 0; i < pos.size(); i++) {
			CapacityRankingPo po = pos.get(i);
			sb.append("<value xid='" + (i + 1) + "'>" + po.getCQps() / po.getCLoadrunQps() + "</value>");
		}
		sb.append("</graph>");

		sb.append("<graph gid='pqs' title='剩余集群能力' color='A8D98B'>");
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
	 * 画容量预测柱形图
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

		sb.append("<graph gid='pqs' title='集群负荷' color='C49BEA'>");
		for (int i = 0; i < pos.size(); i++) {
			CapacityRankingPo po = pos.get(i);
			String levelDes = po.getFeatureData("预测容量水位");
			double level = 0;
			if (levelDes != null && levelDes.length() > 1) {
				level = Double.parseDouble(levelDes.substring(0, levelDes.length() - 1));
			}
			
			sb.append("<value xid='" + (i + 1) + "'>" + level + "</value>");
		}
		sb.append("</graph>");

		sb.append("<graph gid='pqs' title='剩余集群能力' color='A8D98B'>");
		for (int i = 0; i < pos.size(); i++) {
			CapacityRankingPo po = pos.get(i);
			String levelDes = po.getFeatureData("预测容量水位");
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
	 * 画依赖容量柱形状图
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

		sb.append("<graph gid='pqs' title='容量水位' color='C49BEA'>");
		for (int i = 0; i < pos.size(); i++) {
			DependencyCapacityPo po = pos.get(i);
			sb.append("<value xid='" + (i + 1) + "'>" + po.getLevel() + "</value>");
		}
		sb.append("</graph>");

		sb.append("<graph gid='pqs' title='剩余水位' color='A8D98B'>");
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
