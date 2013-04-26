package activity.setting;

import util.LoginUtil;
import activity.login.LoginActivity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ebay.ebayfriend.R;

public class SettingFragment extends Fragment implements OnItemClickListener{
	private ListView lv;
	String[] settingItems = {"Log out","clean history"};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting, container, false);
		TextView windowTitleView = (TextView)getActivity().findViewById(R.id.window_title);
		windowTitleView.setText("Setting");

		lv = (ListView)view.findViewById(R.id.setting_listview);
		lv.setAdapter(new ArrayAdapter<String>(this.getActivity(), R.layout.item,
				R.id.tv_item, settingItems));
		lv.setOnItemClickListener(this);
		return view;
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
	long id) {
		// TODO Auto-generated method stub
		switch(position){
		case 0:
			LoginUtil.removeSession();
			LoginUtil.deleteFile();
			Intent intent = new Intent();
			intent.setClass(getActivity(), LoginActivity.class);
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		case 1:
			LoginUtil.deleteFile();
			Toast.makeText(getActivity(), "clean history successfully", Toast.LENGTH_SHORT).show();
			break;
			
		}
	}
}
