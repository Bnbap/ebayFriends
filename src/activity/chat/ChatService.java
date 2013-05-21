package activity.chat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import notification.client.Constants;
import notification.client.NotificationService;
import notification.client.XmppManager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ChatService extends Service {
	private ExecutorService executorService;
	private BroadcastReceiver chatReceiver;
	private ClientConServer ccs;

	public ChatService() {
		executorService = Executors.newSingleThreadExecutor();
		chatReceiver = new ChatReceiver();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ccs = new ClientConServer(this);

		executorService.submit(new Runnable() {
			public void run() {
				ChatService.this.registerChatReceiver();
				ccs.connect();
			}
		});
	}

	public ClientConServer getClientConServer() {
		return ccs;
	}

	private void registerChatReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("chat.message");
		filter.addAction("chat.click");
		filter.addAction("chat.clear");
		registerReceiver(chatReceiver, filter);
	}

	private void unregisterChatReceiver() {
		unregisterReceiver(chatReceiver);
	}

	public static Intent getIntent() {
		return new Intent("chat.service");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
