package com.taobao.csp.capacity;

import java.util.Calendar;
import java.util.Date;

/**
 * 计算农历
 * @author xiaodu
 * @version 2011-4-14 下午04:03:03
 */
public class NongliCalendar {
	
	public static NongliCalendar getInstance(){
		return new NongliCalendar();
	}
	
	
	public void setTime(Date date){
		lunar(date);
	}
	
	
	private NongliCalendar(){
		lunar(new Date());
	}
	
	
	
	
	

	private static int[] lunarInfo = { 0x04bd8, 0x04ae0, 0x0a570, 0x054d5,

	0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2, 0x04ae0,

	0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2,

	0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40,

	0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566, 0x0d4a0,

	0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7,

	0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0,

	0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550, 0x15355,

	0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,

	0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263,

	0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0,

	0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6, 0x095b0,

	0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46,

	0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50,

	0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960, 0x0d954,

	0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0,

	0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0,

	0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0, 0x0ad50,

	0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,

	0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6,

	0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2, 0x049b0,

	0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

	private  int monCyl, dayCyl, yearCyl;

	private  int year, month, day;

	private  boolean isLeap;
	
	public int getYear() {
		return year;
	}


	public int getMonth() {
		return month;
	}


	public int getDay() {
		return day;
	}
	
	
	public String getyyyyMMdd(){
		return year+""+(month<10?"0"+month:month)+""+(day<10?"0"+day:day);
	}
	public String getMMdd(){
		return (month<10?"0"+month:month)+""+(day<10?"0"+day:day);
	}
	
	
	 //====================================== 传回农历 y年的总天数  

	

	private int lYearDays(int y) {

		int i;

		int sum = 348; // 29*12

		for (i = 0x8000; i > 0x8; i >>= 1) {

			// OurLog.debug("i="+i);

			sum += (lunarInfo[y - 1900] & i) == 0 ? 0 : 1; // 大月+1天

		}

		return (sum + leapDays(y)); // +闰月的天数

	}

	// ====================================== 传回农历 y年闰月的天数

	private int leapDays(int y) {

		if (leapMonth(y) != 0)

			return ((lunarInfo[y - 1900] & 0x10000) == 0 ? 29 : 30);

		else

			return (0);

	}

	// ====================================== 传回农历 y年闰哪个月 1-12 , 没闰传回 0
	private int leapMonth(int y) {

		return (lunarInfo[y - 1900] & 0xf);

	}

	// ====================================== 传回农历 y年m月的总天数

	private  int monthDays(int y, int m) {

		return ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0 ? 29 : 30);

	}

	private  void lunar(Date objDate) {

		int i, leap = 0, temp = 0;

		// int monCyl,dayCyl,yearCyl;

		// int year,month,day;

		// boolean isLeap;

		Calendar cl = Calendar.getInstance();

		cl.set(1900, 0, 31); // 1900-01-31是农历1900年正月初一

		Date baseDate = cl.getTime();

		// 1900-01-31是农历1900年正月初一

		int offset = (int) ((objDate.getTime() - baseDate.getTime()) / 86400000); // 天数(86400000=24*60*60*1000)

		// System.out.println(offset);

		dayCyl = offset + 40; // 1899-12-21是农历1899年腊月甲子日

		monCyl = 14; // 1898-10-01是农历甲子月

		// 得到年数

		for (i = 1900; i < 2050 && offset > 0; i++) {

			temp = lYearDays(i); // 农历每年天数

			offset -= temp;

			monCyl += 12;

		}

		if (offset < 0) {

			offset += temp;

			i--;

			monCyl -= 12;

		}

		year = i; // 农历年份

		yearCyl = i - 1864; // 1864年是甲子年

		leap = leapMonth(i); // 闰哪个月

		isLeap = false;

		for (i = 1; i < 13 && offset > 0; i++) {

			// 闰月

			if (leap > 0 && i == (leap + 1) && isLeap == false) {

				--i;

				isLeap = true;

				temp = leapDays(year);

			} else {

				temp = monthDays(year, i);

			}

			// 解除闰月

			if (isLeap == true && i == (leap + 1))

				isLeap = false;

			offset -= temp;

			if (isLeap == false)

				monCyl++;

		}

		if (offset == 0 && leap > 0 && i == leap + 1)

			if (isLeap) {

				isLeap = false;

			} else {

				isLeap = true;

				--i;

				--monCyl;

			}

		if (offset < 0) {

			offset += temp;

			--i;

			--monCyl;

		}

		month = i; // 农历月份

		day = offset + 1; // 农历天份


	}

}
