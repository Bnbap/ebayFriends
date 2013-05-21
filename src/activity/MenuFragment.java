package activity;

import com.ebay.ebayfriend.R;

import activity.about.AboutFragment;
import activity.newsfeed.NewsFeedFragment;
import activity.notification.NotificationFragment;
import activity.profile.ProfileFragment;
import activity.search.SearchFragment;
import activity.setting.SettingFragment;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] colors = getResources().getStringArray(R.array.menu_items);
		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, colors);
		setListAdapter(colorAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		switch (position) {
		case 0:
			newContent = new NewsFeedFragment();
			break;
		case 1:
			newContent = new NotificationFragment();
			break;
		case 2:
			newContent = new ProfileFragment();
			break;
		case 3:
			newContent = new SettingFragment();
			break;
		case 4:
			newContent = new SearchFragment();
			break;
		case 5:
			newContent = new AboutFragment();
			break;
		}
		if (newContent != null)
			switchFragment(newContent);
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		((MainActivity)getActivity()).switchContent(fragment);
		
	}


}
