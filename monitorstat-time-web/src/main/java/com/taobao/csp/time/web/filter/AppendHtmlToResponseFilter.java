package com.taobao.csp.time.web.filter;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
/**
 * 参考：org.springframework.web.filter.ShallowEtagHeaderFilter.ShallowEtagResponseWrapper
 * @author root
 *
 */
public class AppendHtmlToResponseFilter implements Filter {
	public static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";
	public static String htmlSegement;
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		GetResponseContentResponseWrapper responseWrapper = new GetResponseContentResponseWrapper(
				response);
		filterChain.doFilter(request, responseWrapper);
		
		byte[] body = responseWrapper.toByteArray();
		body = appendHtml(body,response.getCharacterEncoding());
		
		copyBodyToResponse(body, response);
	}

	private byte[] appendHtml(byte[] body,String charset) {
		if(charset==null)
			charset = DEFAULT_CHARACTER_ENCODING;
		try {
			String bodyStr = new String(body,charset );
			bodyStr = bodyStr.trim();
			//返回的html网页
			String htmlEndTag ="</html>";
			if(bodyStr.endsWith(htmlEndTag)){
				//插入要增加的片段
				bodyStr = bodyStr.substring(0, bodyStr.length()-htmlEndTag.length())+htmlSegement+htmlEndTag ;
				byte[] bodyNew = bodyStr.getBytes(charset);
				return bodyNew;
			}
			return body;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	private void copyBodyToResponse(byte[] body, HttpServletResponse response)
			throws IOException {
		if (body.length > 0) {
			response.setContentLength(body.length);
			copy(body, response.getOutputStream());
		}
	}

	/**
	 * Copy the contents of the given byte array to the given OutputStream.
	 * Closes the stream when done.
	 * 
	 * @param in
	 *            the byte array to copy from
	 * @param out
	 *            the OutputStream to copy to
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static void copy(byte[] in, OutputStream out) throws IOException {
		try {
			out.write(in);
		} finally {
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * {@link HttpServletRequest} wrapper that buffers all content written to
	 * the {@linkplain #getOutputStream() output stream} and
	 * {@linkplain #getWriter() writer}, and allows this content to be retrieved
	 * via a {@link #toByteArray() byte array}.
	 */
	private static class GetResponseContentResponseWrapper extends
			HttpServletResponseWrapper {

		public GetResponseContentResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		private final ByteArrayOutputStream content = new ByteArrayOutputStream();

		private final ServletOutputStream outputStream = new ResponseServletOutputStream();

		private PrintWriter writer;

		@Override
		public void setContentLength(int len) {
		}

		@Override
		public ServletOutputStream getOutputStream() {
			return this.outputStream;
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			if (this.writer == null) {
				String characterEncoding = getCharacterEncoding();
				this.writer = (characterEncoding != null ? new ResponsePrintWriter(
						characterEncoding) : new ResponsePrintWriter(
						DEFAULT_CHARACTER_ENCODING));
			}
			return this.writer;
		}

		@Override
		public void resetBuffer() {
			this.content.reset();
		}

		@Override
		public void reset() {
			super.reset();
			resetBuffer();
		}

		private byte[] toByteArray() {
			return this.content.toByteArray();
		}

		private class ResponseServletOutputStream extends ServletOutputStream {

			@Override
			public void write(int b) throws IOException {
				content.write(b);
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				content.write(b, off, len);
			}
		}

		private class ResponsePrintWriter extends PrintWriter {

			private ResponsePrintWriter(String characterEncoding)
					throws UnsupportedEncodingException {
				super(new OutputStreamWriter(content, characterEncoding));
			}

			@Override
			public void write(char buf[], int off, int len) {
				super.write(buf, off, len);
				super.flush();
			}

			@Override
			public void write(String s, int off, int len) {
				super.write(s, off, len);
				super.flush();
			}

			@Override
			public void write(int c) {
				super.write(c);
				super.flush();
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//从文件中读取html片段
		htmlSegement= stream2Str(getClass().getResourceAsStream("append.html"),DEFAULT_CHARACTER_ENCODING);
	
	}

	/**
	 *@param inCharset 输入流编码 
	 **/
		public String stream2Str(InputStream in,String inCharset){
		
			String str = null;
			BufferedInputStream buf = new BufferedInputStream(in);
			int len = 0;
			byte[] bs = new byte[1024];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			try {
			//	long start = System.currentTimeMillis();
				while ((len = buf.read(bs)) != -1) {
					baos.write(bs, 0, len);
				}
				baos.flush();
				str = new String(baos.toByteArray(),inCharset);
				baos.close();
				//System.out.println("共花费： " + (System.currentTimeMillis() - start)+ "毫秒");

			} catch (IOException e) {
				e.printStackTrace();
			}
			return str;
			
		}
	@Override
	public void destroy() {

	}

}