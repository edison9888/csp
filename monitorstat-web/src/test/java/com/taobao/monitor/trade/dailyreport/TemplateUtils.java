package com.taobao.monitor.trade.dailyreport;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplateUtils {
	public static Template getTemplate(String file) throws IOException {

		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(TfTemplateTest.class, "/tradereport");
		cfg.setObjectWrapper(new DefaultObjectWrapper());

		Template temp = cfg.getTemplate(file);
		return temp;
	}

	public static ByteArrayOutputStream process(String tplFile, Map<String, Object> root) throws IOException, TemplateException {

		Template temp = TemplateUtils.getTemplate(tplFile);
		ByteArrayOutputStream arrayOut = new ByteArrayOutputStream() ;
		Writer out = new OutputStreamWriter(arrayOut,Charset.forName("GBK"));
		temp.process(root, out);
		return arrayOut;
	}
}
