package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginUtil {

	private static HashMap<String, String> session;
	private static String FILE_NAME = "session";
	private String HTTP_ADDRESS = "http://192.168.47.19:8080/users/login";
	private static String filePath;
	public static String USERNAME, PORTRAIT;

	public static HashMap<String, String> getSession() {
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

	public String getUsername() {
		String res = "";

		try {

			FileInputStream fin = new FileInputStream(new File(filePath
					+ File.separator + "name"));
			ObjectInputStream objectInputStream = new ObjectInputStream(fin);
			res = (String) objectInputStream.readObject();
			fin.close();
		} catch (Exception e) {

			e.printStackTrace();

		}
		return res;

	}

	public String getPassword() {
		String res = "";

		try {

			FileInputStream fin = new FileInputStream(new File(filePath
					+ File.separator + "password"));
			ObjectInputStream objectInputStream = new ObjectInputStream(fin);
			res = (String) objectInputStream.readObject();
			fin.close();

		} catch (Exception e) {

			e.printStackTrace();

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

	private void storeUsernameAndPassword(String username, String password) {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					new FileOutputStream(filePath + File.separator + "name"));
			objectOutputStream.writeObject(username);
			objectOutputStream.close();

			ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(
					new FileOutputStream(filePath + File.separator + "password"));
			objectOutputStream2.writeObject(password);
			objectOutputStream2.close();

		} catch (Exception e) {
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
		JSONObject jo = null;
		try {
			jo = new JSONObject(postRequest.getContent());
			USERNAME = (String) jo.get("username");
			PORTRAIT = (String) jo.get("portrait");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Cookie> cookies = postRequest.getCookieList();
		if (cookies == null) {
			return false;
		}
		storeUsernameAndPassword(username, password);
		for (int i = 0; i < cookies.size(); i++) {
			if ("sessionid".equals(cookies.get(i).getName())) {
				session.put("s_sessionid", cookies.get(i).getValue());
				storeSessionInFile();
				return true;
			}
		}
		return false;
	}

	public static void removeSession() {
		// TODO Auto-generated method stub
		session.clear();
		session = null;
	}

	public static void deleteFile() {
		// TODO Auto-generated method stub
		File sessionFile = new File(filePath + File.separator + FILE_NAME);
		sessionFile.delete();
	}
}
