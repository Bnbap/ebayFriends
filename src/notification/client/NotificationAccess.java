package notification.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.ebay.ebayfriend.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class NotificationAccess {
	private Activity callBackActivity;
	private static NotificationAccess instance;
	private Socket socket = null;
	private NotifyUserThread nut = null;
	private NotifyManyUsersThread nmut = null;
	private String username;
	private String password;
	private String id;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String notifyUsername;
	private String[] notifyUsernames;

	/**
	 * Returns the singleton instance of NotificationAccess.
	 * 
	 * @return the instance
	 */
	public static NotificationAccess getInstance() {
		if (instance == null) {
			synchronized (NotificationAccess.class) {
				instance = new NotificationAccess();
			}
		}
		return instance;
	}

	private NotificationAccess() {
	}

	public void setCallBackActivity(Context context) {
		if (context instanceof Activity)
			callBackActivity = (Activity) context;
	}

	public void startNotificationService() {
		// Start the notification service
		ServiceManager serviceManager = new ServiceManager(callBackActivity);
		serviceManager.setUsername(username);
		serviceManager.setPassword(password);
		serviceManager.setNotificationIcon(R.drawable.notification);
		serviceManager.startService();
	}

	public void notifyUser(String username, String id) {
		this.notifyUsername = username;
		this.id = id;
		nut = new NotifyUserThread();
		nut.start();

	}

	public void notifyManyUsers() {
		String[] s = new String[3];
		s[0] = "123";
		s[1] = "456";
		s[2] = "789";
		this.notifyUsernames = s;
		nmut = new NotifyManyUsersThread();
		nmut.start();
	}

	private class NotifyUserThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				socket = new Socket("192.168.1.105", 1234);

				// 向服务器发送消息
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				out.println("notifyUser " + notifyUsername + " " + id);
				out.flush();

				// 接收来自服务器的消息
				// BufferedReader br = new BufferedReader(new InputStreamReader(
				// socket.getInputStream()));
				// String msg = br.readLine();
				//
				// if (msg != null) {
				// Log.e(msg, msg);
				// } else {
				// Log.e("数据错误!", "数据错误!");
				// }
				// 关闭流
				out.close();
				// br.close();

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("notifyUser失败", e.toString());
			}

		}

	}

	private class NotifyManyUsersThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				socket = new Socket("192.168.1.105", 1234);

				// 向服务器发送消息
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				out.println("notifyManyUsers " + notifyUsernames.toString());
				out.flush();

				// 接收来自服务器的消息
				// BufferedReader br = new BufferedReader(new
				// InputStreamReader(
				// socket.getInputStream()));
				// String msg = br.readLine();
				//
				// if (msg != null) {
				// Log.e(msg, msg);
				// } else {
				// Log.e("数据错误!", "数据错误!");
				// }
				// 关闭流
				out.close();
				// br.close();

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("notifyUser失败", e.toString());
			}

		}

	}
}
