package activity.notification;

import activity.MainActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ebay.ebayfriend.R;

public class NotificationActivity extends MainActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		InitView();
		changeContentView();
	}
	/**
	 * Change the contentView which inherits from MainActivity.
	 * 	Add a Notification ListView in the ContentView. 
	 */
	private void changeContentView(){
		LayoutInflater vi = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ListView notificationView = (ListView) vi.inflate(R.layout.notification_view, null);
		LinearLayout contentLayout = (LinearLayout)findViewById(R.id.layout_content);
		contentLayout.addView(notificationView);
		TextView windowTitleView = (TextView)findViewById(R.id.window_title);
		windowTitleView.setText("Notifications");

	}
}
