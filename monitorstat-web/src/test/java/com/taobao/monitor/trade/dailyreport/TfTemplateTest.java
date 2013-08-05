package com.taobao.monitor.trade.dailyreport;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TfTemplateTest {
	@Test
	public void test_case1() throws IOException, TemplateException {

		Template temp = getTemplate("TfTemplate.xml");
		
		Map<String, Object> root = new HashMap<String, Object>();
		Map<String,Object> perform = new HashMap<String, Object>();
		root.put("performance", perform);
		perform.put("cpu", 1-0);
		
		Map<String,Object> hsfout = new HashMap<String, Object>();
		
		List<Map<String,String>> outRecords= new ArrayList<Map<String, String>>();
		Map<String, String> outsRec = new HashMap<String, String>();
		outsRec.put("clz", TfTemplateTest.class.getSimpleName());
		outRecords.add(outsRec );
		hsfout.put("records", outRecords);
		
		root.put("hsfout", hsfout);
		
		ByteArrayOutputStream arrayOut = new ByteArrayOutputStream() ;
		Writer out = new OutputStreamWriter(arrayOut,Charset.forName("GBK"));
		temp.process(root, out );
		
		System.out.println(arrayOut.toString());
	}
	
	public static Template getTemplate(String path) throws IOException {
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(TfTemplateTest.class, "/tradereport");
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		
		Template temp = cfg.getTemplate(path);
		return temp ;
	}
}
