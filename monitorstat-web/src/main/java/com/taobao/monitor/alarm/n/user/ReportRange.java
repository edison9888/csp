
package com.taobao.monitor.alarm.n.user;
/**
 * 次数#星期#开始时间#结束时间
 * @author xiaodu
 * @version 2011-2-28 上午09:48:12
 */
public class ReportRange {
	  private int continueTimes;
	   private int weekDay;
	   private int start;
	   private int end;
		public int getContinueTimes() {
			return continueTimes;
		}
		public void setContinueTimes(int continueTimes) {
			this.continueTimes = continueTimes;
		}
		public int getWeekDay() {
			return weekDay;
		}
		public void setWeekDay(int weekDay) {
			this.weekDay = weekDay;
		}
		public int getStart() {
			return start;
		}
		public void setStart(int start) {
			this.start = start;
		}
		public int getEnd() {
			return end;
		}
		public void setEnd(int end) {
			this.end = end;
		}
}
