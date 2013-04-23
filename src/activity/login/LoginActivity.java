package activity.login;

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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.ebay.ebayfriend.R;

import activity.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class LoginActivity extends Activity {

	private String FILE_NAME = "session";
	EditText etUsername, etPassword;
	Button btLogin;
	private HashMap<String, String> session = new HashMap<String, String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isSessionExist()){
			startMainActivity();
		}
		setContentView(R.layout.login);
		initView();
	}

	//check if the session object was stored in file before
	private boolean isSessionExist(){
		session = readSession();
		if(session==null){
			return false;
		}else{
			return true;
		}
	}
	
	//read the session object from file
	// return: if session object exists, return it, else return null
	@SuppressWarnings("unchecked")
	private HashMap<String, String> readSession(){
		HashMap<String,String> res;
		try{
			FileInputStream fin = openFileInput(FILE_NAME);
			ObjectInputStream objectInputStream = new ObjectInputStream(fin);
			res = (HashMap<String,String>) objectInputStream.readObject();
			fin.close();
		}catch(Exception e){
			res = null;
		}
		return res;
	}
	
	//store the session object in file
	private void storeSessionInFile(){
		try{
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
			objectOutputStream.writeObject(session);
			objectOutputStream.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	protected void initView() {
		etUsername = (EditText) findViewById(R.id.login_username);
		etPassword = (EditText) findViewById(R.id.login_password);
		btLogin = (Button) findViewById(R.id.login_btLogin);
		btLogin.setOnClickListener(loginClick);
	}

	OnClickListener loginClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String username = etUsername.getText().toString();
			String password = etPassword.getText().toString();
			if (checkUser(username,password)) {
				// log in success, then send the session to MainActivity
				Toast.makeText(v.getContext(), "Login Successfully",
						Toast.LENGTH_SHORT).show();
				
				startMainActivity();
			} else {
				// log in failed, then inform user the error message
				Toast.makeText(v.getContext(), "login failed",
						Toast.LENGTH_SHORT).show();
			}
		}

	};
	
	//start MainActivity
	private void startMainActivity(){
		Bundle map = new Bundle();
		map.putSerializable("sessionid", session);
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("session", map);
		startActivity(intent);
	}

	private boolean checkUser(String username,String password) {
		
		DefaultHttpClient mHttpClient = new DefaultHttpClient();

		// post http request
		HttpPost mPost = new HttpPost(" ");
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("username", username));
		pairs.add(new BasicNameValuePair("password", password));
		try {
			mPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			HttpResponse response = mHttpClient.execute(mPost);
			int res = response.getStatusLine().getStatusCode();
			if (res == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String info = EntityUtils.toString(entity);
					Log.v("login", "info----------" + info);

					// JSON data
					JSONObject jsonObject = null;
					String flag = "";
					String name = "";
					String userid = "";
					String sessionid = "";
					try {
						jsonObject = new JSONObject(info);
						flag = jsonObject.getString("flag");
						name = jsonObject.getString("name");
						userid = jsonObject.getString("userid");
						sessionid = jsonObject.getString("sessionid");
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if (flag.equals("success")) {
						session.put("s_userid", userid);
						session.put("s_username", name);
						session.put("s_sessionid", sessionid);
						//store the session in file
						storeSessionInFile();
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
