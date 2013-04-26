package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

public class Request {
	protected String sessionid;
	protected String HTTP_URL;
	protected HttpRequestBase requestBase;
	public String getContent() {
		sessionid = LoginUtil.getSession().get("s_sessionid");
		try {
			DefaultHttpClient mHttpClient = new DefaultHttpClient();
			requestBase.setHeader("Cookie","sessionid="+sessionid);
			HttpResponse response = mHttpClient.execute(requestBase);
			int res = response.getStatusLine().getStatusCode();
			if (res == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					StringBuilder builder = new StringBuilder();
					BufferedReader bufferedReader2 = new BufferedReader(
							new InputStreamReader(entity
									.getContent()));
					for (String s = bufferedReader2.readLine(); s != null; s = bufferedReader2
							.readLine()) {
						builder.append(s);
					}

					return builder.toString();

				} else {
					return null;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
