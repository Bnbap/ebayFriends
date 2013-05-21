package activity.chat;

import notification.client.Notifier;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ChatReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();

		if ("chat.message".equals(action)) {
			String msg = intent.getStringExtra("reMsg");

			Notifier notifier = new Notifier(context);
			notifier.notifyChat(msg);
		}
	}

}
