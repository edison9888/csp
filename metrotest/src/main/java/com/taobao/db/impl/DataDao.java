package com.taobao.db.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.taobao.db.MysqlRouteBase;
import com.taobao.po.AuctionSeachHot;
import com.taobao.po.AuctionsHot;
import com.taobao.po.AuctionsRefSearchPo;
import com.taobao.po.CatHot;
import com.taobao.po.HotQueryPo;
import com.taobao.po.UmdAuction;
import com.taobao.po.UrlHot;

public class DataDao extends MysqlRouteBase {
	public static int MAXTAG = 30;
	public static int MAX_TABLE_NUMBER = 15;
	public List<HotQueryPo> getHotQueryPoList(){
		String sql = "select * from search_query_param_decode order by search_count desc limit " + MAXTAG;
		final List<HotQueryPo> list = new ArrayList<HotQueryPo>();
		try {
			this.query(sql, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					HotQueryPo po = new HotQueryPo();
					po.setItemName(rs.getString("search_param"));
					po.setQueryCount(rs.getLong("search_count"));
					list.add(po);
				}
				
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public List<AuctionsRefSearchPo> getAuctionsRefSearchPoList(String param){
		String sql = "select * from auctions_ref_search where search_param = ? order by num desc limit " + MAX_TABLE_NUMBER;
		System.out.println(sql + "->" + param);
		final List<AuctionsRefSearchPo> list = new ArrayList<AuctionsRefSearchPo>();
		try {
			this.query(sql, new Object[]{param}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					AuctionsRefSearchPo po = new AuctionsRefSearchPo();
					po.setSearchParam(rs.getString("search_param"));
					po.setAuctionTitle(rs.getString("auction_title"));
					po.setAuctionId(rs.getString("auction_id"));
					po.setNum(rs.getLong("num"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<CatHot> getCatHotPoList(){
		String sql = "select * from cat_hot order by num desc limit " + MAX_TABLE_NUMBER;
		final List<CatHot> list = new ArrayList<CatHot>();
		try {
			this.query(sql, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CatHot po = new CatHot();
					po.setCat1(rs.getString("cat_1"));
					po.setCat2(rs.getString("cat_2"));
					po.setCat3(rs.getString("cat_3"));
					po.setCat4(rs.getString("cat_4"));
					po.setCat5(rs.getString("cat_5"));
					po.setNum(rs.getLong("num"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}		
	
	public List<AuctionsHot> getAuctionsHotList(){
		String sql = "select * from auctions_hot order by v_num desc limit " + MAX_TABLE_NUMBER;
		final List<AuctionsHot> list = new ArrayList<AuctionsHot>();
		try {
			this.query(sql, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					AuctionsHot po = new AuctionsHot();
					po.setAuctionId(rs.getString("auction_id"));
					po.setTitle(rs.getString("title"));
					po.setvNumber(rs.getLong("v_num"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}		
	
	public List<UrlHot> getUrlHotList(){
		String sql = "select * from url_hot where url LIKE '%/go/%' order by num desc limit " + MAX_TABLE_NUMBER;
		final List<UrlHot> list = new ArrayList<UrlHot>();
		try {
			this.query(sql, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					UrlHot po = new UrlHot();
					po.setNum(rs.getLong("num"));
					po.setUrl(rs.getString("url"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}	
	
	public List<UmdAuction> quertUmdAuctionList(){
		String sql = "select * from umd_auction order by _c2 desc limit " + MAX_TABLE_NUMBER;
		final List<UmdAuction> list = new ArrayList<UmdAuction>();
		try {
			this.query(sql, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					UmdAuction po = new UmdAuction();
					po.setAuctionId(rs.getString("auction_id"));
					po.setNum(rs.getLong("_c2"));
					po.setTitle(rs.getString("title"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}	
	
	public List<AuctionSeachHot> quertAuctionSeachHotList(String auction_id){
		String sql = "select * from auction_seach_hot where auction_id = ? order by num desc limit " + MAX_TABLE_NUMBER;
		final List<AuctionSeachHot> list = new ArrayList<AuctionSeachHot>();
		try {
			this.query(sql, new Object[]{auction_id},new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					AuctionSeachHot po = new AuctionSeachHot();
					po.setAuctionId(rs.getString("auction_id"));
					po.setNum(rs.getLong("num"));
					po.setName(rs.getString("name"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}		
//	public void insertAuctionsCat(String[] array) throws NumberFormatException, SQLException {
//		String sql = "insert into auctions_cat(category_level1_name,category_level2_name,category_level3_name,category_level4_name,category_level5_name,_c5)values(?,?,?,?,?,?)";
//		try {
//			Integer.parseInt(array[5]);
//		} catch (Exception e) {
//			return;
//		}
//		this.execute(sql, new Object[]{
//				array[0],array[1],array[2],array[3],array[4],Integer.parseInt(array[5])
//		});
//	}
//	public void insertSearchQueryParam(String[] array) throws NumberFormatException, SQLException {
//		String sql = "insert into search_query_param_decode(param,query)values(?,?)";
//		try {
//			Integer.parseInt(array[1]);
//		} catch (Exception e) {
//			return;
//		}		
//		this.execute(sql, new Object[]{
//				array[0],Integer.parseInt(array[1])
//		});
//	}	
}
