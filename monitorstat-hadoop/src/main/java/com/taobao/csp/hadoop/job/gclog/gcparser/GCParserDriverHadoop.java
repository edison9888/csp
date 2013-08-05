/*
 * 修改自 com.taobao.csp.hadoop.job.gclog.gcparser.GCParserDriver 这个类
 * 去掉了输出相关的代码，不读取文件，直接传入每行的内容
 */
package com.taobao.csp.hadoop.job.gclog.gcparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;

public class GCParserDriverHadoop
{
	public GCParserDriverHadoop() {
		_gc_stats = new GCDataStore(null, 1, true);
		_gc_parsers = create_gc_parsers(_gc_stats, false);
	}
	public static final int COLLECT_DATA		= 0x03;

	public static void main(String argv[]) throws IOException
	{
//		String[] lines = new String[]{"2013-02-01T22:37:25.332+0800: 40599.247: [GC 40599.247: [ParNew: 1372078K->6647K(1501888K), 0.0064990 secs] 2598078K->1232824K(4057792K), 0.0067300 secs] [Times: user=0.06 sys=0.00, real=0.01 secs]"};

		//		for(String line : lines) {
		//			driver.parseForOneLine(line);
		//		}

		File file = new File("D:\\tmp\\gc2.log");
		FileReader fr = new FileReader(file);
		BufferedReader r = new BufferedReader(fr);
		String s = r.readLine();
		int i=1;
		while (s != null)
		{
			GCParserDriverHadoop driver = new GCParserDriverHadoop();
			driver.parseForOneLine(s);
			s = r.readLine();
			GCDataStore gcData = (GCDataStore) driver.gc_stats();
			String time = null;		//FIXME 实现待斟酌
			if(gcData.data(GCMetric.ygc_logtime) != null) {
				time = gcData.data(GCMetric.ygc_logtime).longValue() + "";
			} else if(gcData.data(GCMetric.fgc_logtime) != null) {
				time = gcData.data(GCMetric.fgc_logtime).longValue() + "";
			}
			System.out.println(time);
			
			/** gc pauses */
			Double ygData = gcData.data(GCMetric.ygc_time);
			Double imData = gcData.data(GCMetric.cms_im_time);
			Double rmData = gcData.data(GCMetric.cms_rm_time);
			Double fgData = gcData.data(GCMetric.fgc_time);
			System.out.println("ygData=" + ygData + "\t " + "imData=" + imData + "\trmData=" + rmData + "\tfgData=" + fgData);
			/** concurrent gc activity */
			/*cms concurrent mark*/
			Double cmData = gcData.data(GCMetric.cms_cm_a_time);

			/*CMS-concurrent-preclean*/
			Double cpData = gcData.data(GCMetric.cms_cp_a_time);

			/*CMS-concurrent-sweep*/
			Double csData = gcData.data(GCMetric.cms_cs_a_time);

			/*CMS-concurrent-reset*/
			Double crData = gcData.data(GCMetric.cms_cr_a_time);
			
			System.out.println("cmData=" + cmData + "\t " + "cpData=" + cpData + "\tcsData=" + csData + "\tcrData=" + crData);
			System.out.println("************************line=" + i++);
			//			String time = null;		//FIXME 实现待斟酌
//			if(time != null) {
//				System.out.println("time=" + time);	
//			} else {
//				System.out.println(s);
//			}
		}

		
		
		
//		/** gc pauses */
//		ArrayList<Double> ygTimes = gcData.time(GCMetric.ygc_time);
//		ArrayList<Double> ygData = gcData.data(GCMetric.ygc_time);
//		MetricData youngGCData = new MetricData("Young GC", ygTimes, ygData);
//
//		ArrayList<Double> imTimes = gcData.time(GCMetric.cms_im_time);
//		ArrayList<Double> imData = gcData.data(GCMetric.cms_im_time);
//		MetricData initialMarkData = new MetricData("Initial Mark", imTimes, imData);
//
//		ArrayList<Double> rmTimes = gcData.time(GCMetric.cms_rm_time);
//		ArrayList<Double> rmData = gcData.data(GCMetric.cms_rm_time);
//		MetricData remarkData = new MetricData("Remark", rmTimes, rmData);
//
//		ArrayList<Double> fgTimes = gcData.time(GCMetric.fgc_time);
//		ArrayList<Double> fgData = gcData.data(GCMetric.fgc_time);
//		MetricData fullGCData = new MetricData("Full GC", fgTimes, fgData);
//
//		
//		/** concurrent gc activity */
//
//		/*cms concurrent mark*/
//		ArrayList<Double> cmTimes = gcData.time(GCMetric.cms_cm_a_time);
//		ArrayList<Double> cmData = gcData.data(GCMetric.cms_cm_a_time);
//		MetricData cmGcData = new MetricData("CMS-concurrent-mark", cmTimes, cmData);
//
//		/*CMS-concurrent-preclean*/
//		ArrayList<Double> cpTimes = gcData.time(GCMetric.cms_cp_a_time);
//		ArrayList<Double> cpData = gcData.data(GCMetric.cms_cp_a_time);
//		MetricData cpGcData = new MetricData("CMS-concurrent-preclean", cpTimes, cpData);
//
//		/*CMS-concurrent-sweep*/
//		ArrayList<Double> csTimes = gcData.time(GCMetric.cms_cs_a_time);
//		ArrayList<Double> csData = gcData.data(GCMetric.cms_cs_a_time);
//		MetricData csGcData = new MetricData("CMS-concurrent-sweep", csTimes, csData);
//
//		/*CMS-concurrent-reset*/
//		ArrayList<Double> crTimes = gcData.time(GCMetric.cms_cr_a_time);
//		ArrayList<Double> crData = gcData.data(GCMetric.cms_cr_a_time);
//		MetricData crGcData = new MetricData("CMS-concurrent-reset", crTimes, crData);				
//
//		/** heap size for young gc*/
//		ArrayList<Double> ygUsedBegTime = gcData.time(GCMetric.yg_used_beg);
//		ArrayList<Double> ygUsedBegData = gcData.data(GCMetric.yg_used_beg);
//		MetricData ygUsedMetric = new MetricData("young begin", ygUsedBegTime, ygUsedBegData);	
//		
//		ArrayList<Double> ygUsedEndTime = gcData.time(GCMetric.yg_used_end);
//		ArrayList<Double> ygUsedEndData = gcData.data(GCMetric.yg_used_end);
//		MetricData ygEndMetric = new MetricData("young end", ygUsedEndTime, ygUsedEndData);	
//
////		if(ygUsedBegData.size() == ygUsedEndData.size() && ygUsedEndData.size() != 0) {
////			for(int i=0; i<ygUsedBegData.size(); i++) {
////				System.out.println("----" + ygUsedBegData.get(i) + "\t" + ygUsedEndData.get(i) + "\t" + ( ygUsedEndData.get(i) - ygUsedBegData.get(i)));
////			}
////		}
//		
//		ArrayList<Double> youngTime = gcData.data(GCMetric.ygc_logtime);
//		ArrayList<Double> fullTime = gcData.data(GCMetric.fgc_logtime);
//		
//		
//		System.out.println("youngTime=" + youngTime.size() + "\t" + ygTimes.size() + "\t" + ygUsedBegTime.size());
//		//System.out.println("youngTime=" + youngTime.size() + "\t" + ygTimes.size());
//		
//		for(Double time : youngTime) {
//			System.out.println("*young******" + new Date(time.longValue()));
//		}
//		
//		for(Double time : fullTime) {
//			System.out.println("*old******" + new Date(time.longValue()));
//		}
////		while(ygUsedMetric.hasMore() && ygEndMetric.hasMore()) {
////			double time1 = ygUsedMetric.getTime();
////			double time2 = ygEndMetric.getTime();+ 
////			
////			double sizeStart = ygUsedMetric.getData();
////			double sizeEnd = ygEndMetric.getData();
////			
////			ygUsedMetric.moveToNext();
////			ygEndMetric.moveToNext();
////			System.out.println("time1=" + time1 + ";time2=" + time2);
////			System.out.println("ygUsedMetric=" + "\t" + sizeStart*1024 + "\t" + sizeEnd*1024);
////		}
//		
//		ArrayList<Double> thUsedBegTime = gcData.time(GCMetric.th_used_beg);
//		ArrayList<Double> thUsedBegData = gcData.data(GCMetric.th_used_beg);
//		MetricData thUsedMetric = new MetricData("young begin *", thUsedBegTime, thUsedBegData);	
//
//		ArrayList<Double> thUsedEndTime = gcData.time(GCMetric.th_used_end);
//		ArrayList<Double> thUsedEndData = gcData.data(GCMetric.th_used_end);
//		MetricData thEndMetric = new MetricData("young end *", thUsedEndTime, thUsedEndData);	
//
//		/** heap size for full gc*/
//		ArrayList<Double> pgUsedBegTime = gcData.time(GCMetric.pg_used_beg);
//		ArrayList<Double> pgUsedBegData = gcData.data(GCMetric.pg_used_beg);
//		MetricData pgUsedMetric = new MetricData("full gc begin", pgUsedBegTime, pgUsedBegData);	
//
//		ArrayList<Double> pgUsedEndTime = gcData.time(GCMetric.pg_used_end);
//		ArrayList<Double> pgUsedEndData = gcData.data(GCMetric.pg_used_end);
//		MetricData pgEndMetric = new MetricData("full gc end", pgUsedEndTime, pgUsedEndData);	
//		
//		//GCMetric.yg_commit_end, GCMetric.th_commit_end 分别代表实际的容量信息
//		//GCMetric.pg_commit_end
//		
//		MetricDataSet set = new MetricDataSet();
//		set.addMetricData(youngGCData);
//		set.addMetricData(initialMarkData);
//		set.addMetricData(remarkData);
//		set.addMetricData(fullGCData);
//		set.addMetricData(cmGcData);
//		set.addMetricData(cpGcData);
//		set.addMetricData(csGcData);
//		set.addMetricData(crGcData);
//
//		set.addMetricData(ygUsedMetric);
//		set.addMetricData(ygEndMetric);
//		set.addMetricData(thUsedMetric);
//		set.addMetricData(thEndMetric);
//		set.addMetricData(pgUsedMetric);
//		set.addMetricData(pgEndMetric);
//
//		while (set.hasMore()) {
//			MetricData data = set.getEarliest();
//			String activityName = data.getName();
//			while(data.hasMore()) {
//				double startSec = data.getTime();
//				double durationSec = data.getData();
//				data.moveToNext();
//				System.out.println(activityName + "\t" + startSec*1024 + "\t" + durationSec*1024);
//			}
//			set.moveToNext();
//		}
	}


	public GCStats gc_stats() { return _gc_stats; }

	public void parseForOneLine(String s) {
		parse(_gc_parsers, "", 1, s);
	}

	public boolean
	parse(List<GCParser> parsers, String filename, int line, String s)
	{
		Iterator<GCParser> iterator = parsers.iterator();
		boolean matched = false;
		do {
			matched = iterator.next().parse(filename, line, s);
		} while (!matched && iterator.hasNext());
		return matched;
	}


	protected GCStats
	create_gc_stats(BitSet actions, EnumMap<GCMetric, Boolean> enabled_map,
			int cpu_count, boolean input_has_time_zero)
	{
		return new GCDataStore(enabled_map, cpu_count,
				input_has_time_zero);
	}

	/**
	 * Create the set of GCParsers.  Subclasses wishing to add a new parser
	 * should override this method.
	 */
	protected ArrayList<GCParser>
	create_gc_parsers(GCStats gc_stats, boolean verbose)
	{
		ArrayList<GCParser> parsers = new ArrayList<GCParser>(7);
		parsers.add(new ParGCYoungGCParser(gc_stats, verbose));
		parsers.add(new FWYoungGCParser(gc_stats, verbose));
		parsers.add(new ParGCFullGCParser(gc_stats, verbose));
		parsers.add(new CMSGCParser(gc_stats, verbose));
		parsers.add(new ParCompactPhaseGCParser(gc_stats, verbose));
		parsers.add(new FWOldGCParser(gc_stats, verbose));
		parsers.add(new FWFullGCParser(gc_stats, verbose));
		parsers.add(new VerboseGCParser(gc_stats, verbose));
		return parsers;
	}

	private final GCStats _gc_stats;
	private ArrayList<GCParser> _gc_parsers;
}
