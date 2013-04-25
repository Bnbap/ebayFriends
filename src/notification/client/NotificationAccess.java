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
		serviceManager.setNotificationIcon(R.drawable.notification);
		serviceManager.startService();
	}

	public void notifyUser() {
		try {
			socket = new Socket("10.0.2.2", 1234);
			// 向服务器发送消息
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())), true);
			out.println("haofohwohiowfhoiohwhfsjafhkjs-=-=-=-");

			// 接收来自服务器的消息
			BufferedReader br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String msg = br.readLine();

			if (msg != null) {
				Log.e(msg, msg);
			} else {
				Log.e("数据错误!", "数据错误!");
			}
			// 关闭流
			out.close();
			br.close();
			// 关闭Socket
			socket.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
