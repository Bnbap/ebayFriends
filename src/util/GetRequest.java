package util;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class GetRequest {
	protected String HTTP_URL;
	protected HttpGet mGet;

	public GetRequest(String url) {
		HTTP_URL = url;
		mGet = new HttpGet(url);
	}
	public List<Cookie> getCookie(){
		try{
			DefaultHttpClient mHttpClient = new DefaultHttpClient();
			HttpResponse response = mHttpClient.execute(mGet);
			int res = response.getStatusLine().getStatusCode();
			if (res == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String info = EntityUtils.toString(entity);
					Log.v("login", "info----------" + info);
					CookieStore mCookieStore = mHttpClient.getCookieStore();
					return mCookieStore.getCookies();

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
