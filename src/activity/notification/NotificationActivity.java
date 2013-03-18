package activity.notification;

import activity.MainActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ebay.ebayfriend.R;

public class NotificationActivity extends MainActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		InitView();
		TextView windowTitleView = (TextView)findViewById(R.id.window_title);
		windowTitleView.setText("Notifications");

	}
}
