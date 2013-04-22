package activity.notification;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebay.ebayfriend.R;

public class NotificationFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.notification_view,container,false);
		TextView windowTitleView = (TextView)getActivity().findViewById(R.id.window_title);
		windowTitleView.setText("Notifications");
		
		return view;
	}
}
