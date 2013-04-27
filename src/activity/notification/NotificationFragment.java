package activity.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ebay.ebayfriend.R;

public class NotificationFragment extends ListFragment {
	private List<Map<String, Object>> mData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.notification_view, container,
				false);
		TextView windowTitleView = (TextView) getActivity().findViewById(
				R.id.window_title);
		windowTitleView.setText("Notifications");

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new SimpleAdapter(getActivity(), getData(),
				R.layout.comments_list,
				new String[] { "title", "info", "img" }, new int[] {
						R.id.title, R.id.info, R.id.img }));
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		Toast.makeText(
				getActivity(),
				"You have selected "
						+ (String) mData.get(position).get("title"),
				Toast.LENGTH_SHORT).show();
	}

	/*
	 * // ListView ��������������������
	 * 
	 * @Override protected void onListItemClick(ListView l, View v, int
	 * position, long id) {
	 * 
	 * Log.v("MyListView4-click", (String) mData.get(position).get("title")); }
	 */

	// /**
	// * listview��������������������
	// */
	// public void showInfo() {
	// new AlertDialog.Builder(this.getActivity())
	// .setTitle("����listview")
	// .setMessage("����...")
	// .setPositiveButton("����",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// }
	// }).show();
	//
	// }

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "G1");
		map.put("info", "google 1");
		map.put("img", R.drawable.i1);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "G2");
		map.put("info", "google 2");
		map.put("img", R.drawable.i2);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "G3");
		map.put("info", "google 3");
		map.put("img", R.drawable.i3);
		list.add(map);

		return list;
	}
}
