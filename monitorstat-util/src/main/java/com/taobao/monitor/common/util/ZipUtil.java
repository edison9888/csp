
package com.taobao.monitor.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

/**
 * Ñ¹Ëõ×Ö·û´®
 * @author xiaodu
 * @version 2010-4-13 ÉÏÎç11:49:26
 */
public class ZipUtil {
//	InputStream
	public static String compress(String line) {
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream outputStream = new GZIPOutputStream(out);
			
			outputStream.write(line.getBytes());
			outputStream.close();
			return out.toString("ISO-8859-1");
		}catch(Exception e){
			return null;
		}
	}
	
	
	public static String deCompress(String line){
		try{
			 ByteArrayOutputStream out = new ByteArrayOutputStream();  
	         ByteArrayInputStream in = new ByteArrayInputStream(line.getBytes("ISO-8859-1"));  
	         GZIPInputStream gunzip = new GZIPInputStream(in);  
	         byte[] buffer = new byte[256];  
	         int n;  
	         while ((n = gunzip.read(buffer)) >= 0) {  
	             out.write(buffer, 0, n);  
	         }  
	         return out.toString("GBK"); 
		}catch(Exception e){
			return null;
		}
	}
	
	
	
	public static void main(String[] s){
		
		String line = "select/*+ordereduse_nl(t2t1)*/biz_order_id,pay_order_id,logistics_order_id,out_order_id,seller_nick,buyer_nick,seller_id,buyer_id,auction_id,auction_title,auction_price,buy_amount,biz_type,sub_biz_type,goods_url,fail_reason,pay_status,logistics_status,memo,snap_path,gmt_create,status,nvl(buyer_rate_status,4)buyer_rate_status,nvl(seller_rate_status,4)seller_rate_status,auction_pict_url,seller_memo,buyer_memo,seller_flag,buyer_flag,buyer_message_path,refund_status,attributes,attributes_cc,gmt_modified,ip,end_time,pay_time,is_main,is_detail,point_rate,parent_id,adjust_fee,discount_fee,refund_fee,confirm_paid_fee,cod_status,gmt_timeout,trade_tag,shop_idfrom(selectridfrom(selecta1.rowidrid,rownumaslinenumfrom(selectrowidfromtc_biz_orderwhereis_main=1and(biz_type=:1orbiz_type=:2orbiz_type=:3orbiz_type=:4orbiz_type=:5orbiz_type=:6)and(logistics_status=:7orlogistics_status=:8)andpay_status=:9andseller_id=:10andstatus=:11orderbygmt_createdesc)a1whererownum<=:12)wherelinenum>=:13)t2,tc_biz_ordert1wheret1.rowid=t2.rid";
		System.out.println(line.length());
		String compressLine = ZipUtil.compress(line);		
		System.out.println(compressLine.length());
		System.out.println(ZipUtil.deCompress(compressLine));
		
	}

}
