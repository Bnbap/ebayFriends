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

public class PostRequest extends Request {

	protected JSONObject param;

	public PostRequest(String url, JSONObject param) {
		this.HTTP_URL = url;
		this.param = param;
		StringEntity entity;
		try {
			entity = new StringEntity(param.toString());
			sessionid = LoginUtil.getSession().get("s_sessionid");
			requestBase = new HttpPost(HTTP_URL);
			((HttpPost) requestBase).setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public boolean post() {
		try {
			DefaultHttpClient mHttpClient = new DefaultHttpClient();
			requestBase.setHeader("Cookie","sessionid="+sessionid);
			HttpResponse response = mHttpClient.execute(requestBase);
			int res = response.getStatusLine().getStatusCode();
			if (res == 200) {
				
				return true;

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Cookie> getCookieList() {
		try {
			DefaultHttpClient mHttpClient = new DefaultHttpClient();
			HttpResponse response = mHttpClient.execute(requestBase);
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
