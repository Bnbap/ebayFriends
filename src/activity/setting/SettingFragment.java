package activity.setting;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebay.ebayfriend.R;

public class SettingFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting, container, false);
		TextView windowTitleView = (TextView)getActivity().findViewById(R.id.window_title);
		windowTitleView.setText("Setting");

		return view;
	}
}
