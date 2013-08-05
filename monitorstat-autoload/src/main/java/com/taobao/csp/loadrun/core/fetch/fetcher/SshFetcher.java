package com.taobao.csp.loadrun.core.fetch.fetcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.taobao.csp.loadrun.core.fetch.HsfFetchTaskImpl;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.fetch.TairFetchTaskImpl;
import com.taobao.csp.loadrun.core.shell.BaseShell;

public class SshFetcher extends BaseShell implements IFetcher {
	
	public static Logger logger = Logger.getLogger(SshFetcher.class);
	
	/*** shell 命令 ***/
	private String command;
	
	/*** 需要回调analyse方法 ***/
	private IFetchTask task;
	
	private volatile boolean isRun;
	
	public SshFetcher(IFetchTask task, String command, String targetIp, String user, String pwd) {
		super(targetIp, user, pwd);
		this.task = task;
		this.command = command;
	}

	@Override
	public void fetch() throws IOException {
		char splitLine = getSplitLine();
		if (this.getTargetSSHConn() != null) {
			Session session = null;
			session = this.getTargetSSHConn().openSession();
			session.requestPTY("vt100", 90, 30, 0, 0, null);
			session.execCommand(this.command);
			
			logger.info("开始执行 "+this.getTargetIp()+" "+this.command);
			
			InputStream stdout = new StreamGobbler(session.getStdout());
			BufferedReader2 br = new BufferedReader2(new InputStreamReader(stdout), splitLine);
			String line = null;
			while ((line = br.readLine()) != null) {
				task.analyse(line);
			}	
			
			isRun = false;
		}
	}
	
	private char getSplitLine() {
		char split = '\n';
		
		if ((task instanceof HsfFetchTaskImpl) || (task instanceof TairFetchTaskImpl)) {
			split = '\02';
		}
		
		return split;
	}

	@Override
	public void start() {
		try {
			this.initSshConnect();
			isRun = true;
		} catch (Exception e) {
			logger.error("", e);
		}	
	}

	@Override
	public void end() {
		this.close();
	}

	@Override
	public boolean isRun() {
		return isRun;
	}
}
