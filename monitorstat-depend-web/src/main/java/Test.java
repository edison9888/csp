import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class Test {
	
	public void testDependTrigger() {
		String url = "http://localhost:8080/depend/manualtrigger.do?method=addCspCheckupDependConfig";
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		try {
			NameValuePair[] param = {
					new NameValuePair("uuid","uuidididididididi"),
					new NameValuePair("codeVersion", "1111"),
					new NameValuePair("opsName", "detail"),
					new NameValuePair("selfIp", "127.0.0.1"),
					new NameValuePair("targetOpsName", "deeee"),
					new NameValuePair("targetAppType", "gooddd"),
					new NameValuePair("targetIps", "127.0.0.2"),
					new NameValuePair("port", "1234"),
					new NameValuePair("startPreventIntensity", "ǿ"),
					new NameValuePair("runPreventIntensity", "ǿ"),
					new NameValuePair("startDelayIntensity", "ǿ"),
					new NameValuePair("runDelayIntensity", "ǿ"),
					new NameValuePair("checkdimension", "1"),
					new NameValuePair("codePath", "svn:////"),
					new NameValuePair("costtime", "1233333")
			} ;  

			//HttpMethodParams params = new HttpMethodParams();  �����������������
			post.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");
			post.setRequestBody(param);
			String response;
			try {
				client.executeMethod(post);
				response = post.getResponseBodyAsString();
				System.out.println(response);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				post.releaseConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// �ر�����,�ͷ���Դ
		}		
	}

	
	public void testTime() {
		String url = "http://localhost:8080/depend/manualtrigger.do?method=addCspCheckupDependConfig";
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		try {
			NameValuePair[] param = {
					new NameValuePair("uuid","uuidididididididi"),
					new NameValuePair("codeVersion", "1111"),
					new NameValuePair("opsName", "detail"),
					new NameValuePair("selfIp", "127.0.0.1"),
					new NameValuePair("targetOpsName", "deeee"),
					new NameValuePair("targetAppType", "gooddd"),
					new NameValuePair("targetIps", "127.0.0.2"),
					new NameValuePair("port", "1234"),
					new NameValuePair("startPreventIntensity", "ǿ"),
					new NameValuePair("runPreventIntensity", "ǿ"),
					new NameValuePair("startDelayIntensity", "ǿ"),
					new NameValuePair("runDelayIntensity", "ǿ"),
					new NameValuePair("checkdimension", "1"),
					new NameValuePair("codePath", "svn:////"),
					new NameValuePair("costtime", "1233333")
			} ;  

			//HttpMethodParams params = new HttpMethodParams();  �����������������
			post.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");
			post.setRequestBody(param);
			String response;
			try {
				client.executeMethod(post);
				response = post.getResponseBodyAsString();
				System.out.println(response);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				post.releaseConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// �ر�����,�ͷ���Դ
		}		
	}
	public static void main(String[] args) {

	}
}
