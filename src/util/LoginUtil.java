package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginUtil {

	private static HashMap<String, String> session;
	private String FILE_NAME = "session";
	private String HTTP_ADDRESS = "http://192.168.47.19:8080/users/login";
	private String filePath;

	public static HashMap<String,String> getSession(){
		return session;
	}
	public LoginUtil(String filePath) {
		this.filePath = filePath;
	}

	// check if the session object was stored in file before
	public boolean isSessionExist() {
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
			FileInputStream fin = new FileInputStream(new File(filePath
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
					new FileOutputStream(filePath + File.separator + FILE_NAME));
			objectOutputStream.writeObject(session);
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean checkUser(String username, String password) {

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

		PostRequest postRequest = new PostRequest(HTTP_ADDRESS, param);
		List<Cookie> cookies = postRequest.getCookieList();
		for (int i = 0; i < cookies.size(); i++) {
			if ("sessionid".equals(cookies.get(i).getName())) {
				session.put("s_sessionid", cookies.get(i).getValue());
				storeSessionInFile();
				return true;
			}
		}
		return false;
	}
}
