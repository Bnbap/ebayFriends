package activity.login;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import util.PostRequest;

import com.ebay.ebayfriend.R;

import activity.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class LoginActivity extends Activity {

	private String FILE_NAME = "session";
	private String HTTP_ADDRESS = "http://192.168.47.19:8080/users/login";
	EditText etUsername, etPassword;
	MyHandler myHandler;
	Button btLogin;
	private HashMap<String, String> session = new HashMap<String, String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isSessionExist()) {
			startMainActivity();
		} else {
			setContentView(R.layout.login);
			initView();
		}
	}

	// check if the session object was stored in file before
	private boolean isSessionExist() {
		session = readSession();
		if (session == null) {
			session = new HashMap<String, String>();
			return false;
		} else {
			return true;
		}
	}

	// read the session object from file
	// return: if session object exists, return it, else return null
	@SuppressWarnings("unchecked")
	private HashMap<String, String> readSession() {
		HashMap<String, String> res;
		try {
			FileInputStream fin = new FileInputStream(new File(getFilesDir()
					+ File.separator + FILE_NAME));
			ObjectInputStream objectInputStream = new ObjectInputStream(fin);
			res = (HashMap<String, String>) objectInputStream.readObject();
			fin.close();
		} catch (Exception e) {
			res = null;
		}
		return res;
	}

	// store the session object in file
	private void storeSessionInFile() {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					new FileOutputStream(getFilesDir() + File.separator
							+ FILE_NAME));
			objectOutputStream.writeObject(session);
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void initView() {
		etUsername = (EditText) findViewById(R.id.login_username);
		etPassword = (EditText) findViewById(R.id.login_password);
		myHandler = new MyHandler();
		btLogin = (Button) findViewById(R.id.login_btLogin);
		btLogin.setOnClickListener(loginClick);
	}

	OnClickListener loginClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String username = etUsername.getText().toString();
			String password = etPassword.getText().toString();
			Thread checkThread = new CheckSessionThread(password, username);
			checkThread.start();

		}

	};

	// start MainActivity
	private void startMainActivity() {
		Bundle map = new Bundle();
		map.putSerializable("sessionid", session);
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("session", map);
		intent.putExtra("NOTIFICATION", false);
		startActivity(intent);
	}

	private boolean checkUser(String username, String password) {
		
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("username", username));
		pairs.add(new BasicNameValuePair("password", password));
		JSONObject param = new JSONObject();
		try {
			param.put("username", username);
			param.put("password", password);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PostRequest postRequest = new PostRequest(HTTP_ADDRESS,param);
		List<Cookie> cookies = postRequest.getCookie();
		for (int i = 0; i < cookies.size(); i++) {
			if ("sessionid".equals(cookies.get(i).getName())) {
				session.put("s_sessionid", cookies.get(i)
						.getValue());
				storeSessionInFile();
				return true;
			}
		}
		return false;
		
//		DefaultHttpClient mHttpClient = new DefaultHttpClient();
//
//		// post http request
//		HttpPost mPost = new HttpPost(HTTP_ADDRESS);
//		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
//		pairs.add(new BasicNameValuePair("username", username));
//		pairs.add(new BasicNameValuePair("password", password));
//		try {
//			mPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		try {
//			HttpResponse response = mHttpClient.execute(mPost);
//			int res = response.getStatusLine().getStatusCode();
//			if (res == 200) {
//				HttpEntity entity = response.getEntity();
//				if (entity != null) {
//					String info = EntityUtils.toString(entity);
//					Log.v("login", "info----------" + info);
//					CookieStore mCookieStore = mHttpClient.getCookieStore();
//					List<Cookie> cookies = mCookieStore.getCookies();
//					for (int i = 0; i < cookies.size(); i++) {
//						if ("sessionid".equals(cookies.get(i).getName())) {
//							session.put("s_sessionid", cookies.get(i)
//									.getValue());
//							storeSessionInFile();
//							return true;
//						}
//					}
//
//				} else {
//					return false;
//				}
//			}
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return false;
	}

	class CheckSessionThread extends Thread {
		private String password, username;

		public CheckSessionThread(String username, String password) {
			this.password = password;
			this.username = username;
		}

		public void run() {
			boolean isSessionAvailable = checkUser(username, password);
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putBoolean("isSessionAvailable", isSessionAvailable);
			msg.setData(b);
			LoginActivity.this.myHandler.sendMessage(msg);
		}
	}

	class MyHandler extends Handler {
		public MyHandler() {

		}

		public MyHandler(Looper l) {
			super(l);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle b = msg.getData();
			boolean isSessionAvailable = b.getBoolean("isSessionAvailable");
			if (isSessionAvailable) {
				// log in success, then send the session to MainActivity
				Toast.makeText(LoginActivity.this.getApplicationContext(),
						"Login Successfully", Toast.LENGTH_SHORT).show();

				startMainActivity();
			} else {
				// log in failed, then inform user the error message
				Toast.makeText(LoginActivity.this.getApplicationContext(),
						"login failed", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
