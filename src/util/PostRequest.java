package util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Entity;
import android.util.Log;

public class PostRequest {

	protected String HTTP_URL;
	protected JSONObject param;
	protected HttpPost mPost;

	public PostRequest(String url, JSONObject param) {
		this.HTTP_URL = url;
		this.param = param;
		StringEntity entity;
		try {
			entity = new StringEntity(param.toString());
			mPost = new HttpPost(HTTP_URL);
			mPost.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public List<Cookie> getCookie() {
		try {
			DefaultHttpClient mHttpClient = new DefaultHttpClient();
			HttpResponse response = mHttpClient.execute(mPost);
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
