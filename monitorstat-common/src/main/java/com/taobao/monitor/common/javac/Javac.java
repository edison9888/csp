package com.taobao.monitor.common.javac;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 封装java编译类.
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-26 - 下午02:55:12
 * @version 1.0
 */
public final class Javac {
	private static final Log logger = LogFactory.getLog(Javac.class);

	String classpath;

	String outputdir;

	String sourcepath;

	String bootclasspath;

	String extdirs;

	String encoding;

	String target;

	public Javac(String classpath, String outputdir) {
		this.classpath = classpath;
		this.outputdir = outputdir;
	}

	/**
	 * 编译给出的源文件
	 * 
	 * @param srcFiles
	 * @return 如果成功返回null，或编译错误
	 */
	public String compile(String srcFiles[]) {
		//StringWriter err = new StringWriter();
		//PrintWriter errPrinter = new PrintWriter(err);
		ByteArrayOutputStream err=new ByteArrayOutputStream();
		

		String args[] = buildJavacArgs(srcFiles);
		//int resultCode = com.sun.tools.javac.Main.compile(args, errPrinter);
		logger.warn("Dyna Complie Java Source:---->"+args);						
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if(compiler==null){
			throw new NullPointerException("ToolProvider.getSystemJavaCompiler() return null,please use JDK replace JRE!");
		}
		int resultCode = compiler.run(null, null, err, args);
		
		return (resultCode == 0) ? null : err.toString();
	}

	public String compile(File srcFiles[]) {
		String paths[] = new String[srcFiles.length];
		for (int i = 0; i < paths.length; i++) {
			paths[i] = srcFiles[i].getAbsolutePath();
		}
		return compile(paths);
	}

	private String[] buildJavacArgs(String srcFiles[]) {
		ArrayList args = new ArrayList();

		if (classpath != null) {
			args.add("-classpath");
			args.add(classpath);
		}
		if (outputdir != null) {
			args.add("-d");
			args.add(outputdir);
		}
		if (sourcepath != null) {
			args.add("-sourcepath");
			args.add(sourcepath);
		}
		if (bootclasspath != null) {
			args.add("-bootclasspath");
			args.add(bootclasspath);
		}
		if (extdirs != null) {
			args.add("-extdirs");
			args.add(extdirs);
		}
		if (encoding != null) {
			args.add("-encoding");
			args.add(encoding);
		}
		if (target != null) {
			args.add("-target");
			args.add(target);
		}

		for (int i = 0; i < srcFiles.length; i++) {
			args.add(srcFiles[i]);
		}

		return (String[]) args.toArray(new String[args.size()]);
	}

	public String getBootclasspath() {
		return bootclasspath;
	}

	public void setBootclasspath(String bootclasspath) {
		this.bootclasspath = bootclasspath;
	}

	public String getClasspath() {
		return classpath;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getExtdirs() {
		return extdirs;
	}

	public void setExtdirs(String extdirs) {
		this.extdirs = extdirs;
	}

	public String getOutputdir() {
		return outputdir;
	}

	public void setOutputdir(String outputdir) {
		this.outputdir = outputdir;
	}

	public String getSourcepath() {
		return sourcepath;
	}

	public void setSourcepath(String sourcepath) {
		this.sourcepath = sourcepath;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
