package notification.client;

import java.net.Socket;

import com.ebay.ebayfriend.R;

import android.app.Activity;
import android.content.Context;

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
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
