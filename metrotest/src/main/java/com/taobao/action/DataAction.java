package com.taobao.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.db.impl.DataDao;
import com.taobao.po.AuctionSeachHot;
import com.taobao.po.AuctionsHot;
import com.taobao.po.AuctionsRefSearchPo;
import com.taobao.po.CatHot;
import com.taobao.po.CloudTagPo;
import com.taobao.po.HotQueryPo;
import com.taobao.po.UmdAuction;
import com.taobao.po.UrlHot;
@Controller                 
@RequestMapping("/showdata.do")
public class DataAction extends BaseAction{
	Logger logger = Logger.getLogger(DataAction.class);
	DataDao dao = new DataDao();
	@RequestMapping(params = "method=showHotItemsList")
	public ModelAndView showHotItemsList() {
		ModelAndView view = new ModelAndView("/hotitem"); 	
		float maxFontSize = 25;
		float minFontSize = 8;
		float fontUnit = (maxFontSize - minFontSize)/DataDao.MAXTAG; 
		
		List<HotQueryPo> poList = dao.getHotQueryPoList();
		List<CloudTagPo> cloudList = new ArrayList<CloudTagPo>();
		for(int i=0; i<poList.size(); i++) {
			CloudTagPo po = new CloudTagPo();
			po.setItemId(i+"");
			po.setLinkCss("tag-link-" + (i+1));//tag-link-48
			po.setStyle("font-size: " + (maxFontSize - i*fontUnit) + "pt;");//font-size: 22pt;
			po.setTitle("查询次数" + poList.get(i).getQueryCount());
			po.setShowValue(poList.get(i).getItemName());
			cloudList.add(po);
		}
		
		if(poList.size()>0) {
			view.addObject("maxPo", poList.get(0));
		}
		
		Collections.sort(cloudList, new Comparator<CloudTagPo>() {
			@Override
			public int compare(CloudTagPo o1, CloudTagPo o2) {
				return o1.hashCode() - o2.hashCode();
			}
		});
		
		//最热类目
		List<CatHot> hotCatHotList = dao.getCatHotPoList();
		List<CloudTagPo> catHotCloudList = new ArrayList<CloudTagPo>();
		for(int i=0; i<hotCatHotList.size(); i++) {
			CatHot hotPo = hotCatHotList.get(i);
			CloudTagPo po = new CloudTagPo();
			po.setItemId(i+"");
			po.setLinkCss("tag-link-" + (i+1));//tag-link-48
			po.setStyle("font-size: " + (maxFontSize - i*fontUnit) + "pt;");//font-size: 22pt;
			po.setTitle("查询次数" + hotPo.getNum());
			if(hotPo.getCat5() != null) {
				po.setShowValue(hotPo.getCat5());	
			} else if(hotPo.getCat4() != null) {
				po.setShowValue(hotPo.getCat4());	
			} else if(hotPo.getCat3() != null) {
				po.setShowValue(hotPo.getCat3());	
			}  else if(hotPo.getCat2() != null) {
				po.setShowValue(hotPo.getCat2());	
			}  else if(hotPo.getCat1() != null) {
				po.setShowValue(hotPo.getCat1());	
			} 
			catHotCloudList.add(po);
		}
		
		Collections.sort(catHotCloudList, new Comparator<CloudTagPo>() {
			@Override
			public int compare(CloudTagPo o1, CloudTagPo o2) {
				return o1.hashCode() - o2.hashCode();
			}
		});
		view.addObject("cloudList", cloudList);
		view.addObject("catHotCloudList", catHotCloudList);
		return view;
	}
	
	//根据Tag查商品	
	@RequestMapping(params = "method=showHotItemTableList")
	public ModelAndView showHotItemTableList(String query, String count) {
		ModelAndView view = new ModelAndView("/hotitemlist"); 	
		List<AuctionsRefSearchPo> poList = dao.getAuctionsRefSearchPoList(query);
		view.addObject("poList", poList);
		view.addObject("count", count);
		view.addObject("query", query);
		return view;
	}
	
	//热点商品
	@RequestMapping(params = "method=showAuctionsHotTableList")
	public ModelAndView showAuctionsHotTableList() {
		ModelAndView view = new ModelAndView("/hotauctionlist"); 	
		List<AuctionsHot> auctionHotList = dao.getAuctionsHotList();
		view.addObject("auctionHotList", auctionHotList);
		return view;
	}
	
	//热点商品发差搜索词
	@RequestMapping(params = "method=showHotWordsByItemList")
	public ModelAndView showHotWordsByItemList(String id) {
		ModelAndView view = new ModelAndView("/hotwordlist"); 	
		List<AuctionSeachHot> auctionSearchHotList = dao.quertAuctionSeachHotList(id);
		view.addObject("auctionSearchHotList", auctionSearchHotList);
		return view;
	}	
	
	//热点活动页面
	@RequestMapping(params = "method=showUrlHotTableList")
	public ModelAndView showUrlHotTableList() {
		ModelAndView view = new ModelAndView("/hoturl_list"); 	
		List<UrlHot> urlHotList = dao.getUrlHotList();
		view.addObject("urlHotList", urlHotList);
		return view;
	}	
	
	//热点促销页面
	@RequestMapping(params = "method=showUmdAuctionTableList")
	public ModelAndView showUmdAuctionTableList() {
		ModelAndView view = new ModelAndView("/umdacutionlist"); 	
		List<UmdAuction> umdAuctionList = dao.quertUmdAuctionList();
		view.addObject("umdAuctionList", umdAuctionList);
		return view;
	}		
	
//	@RequestMapping(params = "method=loadData")
//	public void loadData(String index) throws Exception {
//		int iIndex = 0; 
//		iIndex = Integer.parseInt(index);
//		
//		String[] fileNameArray = new String[]{"F://data//tmp_auctions_cat.csv",
//				"F://data//tmp_search_query_param_decode.csv"};
//		DataInputStream in = new DataInputStream(new FileInputStream(new File("F://data//tmp_search_query_param_decode.csv")));
//		BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(in,"utf-8"));
//		String stemp;   
//		while((stemp = bufferedreader.readLine()) != null){ 
//			stemp = stemp.replaceAll("\"", "");
//			String[] array = stemp.split(",");
//			//dao.insertAuctionsCat(array);
//			dao.insertSearchQueryParam(array);
//		}
//	}
}
