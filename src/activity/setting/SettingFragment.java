package activity.setting;

import notification.client.Constants;
import util.LoginUtil;
import activity.login.LoginActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.TextView;
import android.widget.Toast;

import com.ebay.ebayfriend.R;

public class SettingFragment extends PreferenceFragment implements
		OnPreferenceChangeListener, OnPreferenceClickListener {
	private CheckBoxPreference notification;
	private CheckBoxPreference sound;
	private CheckBoxPreference vibrate;
	private Preference logout;
	private Preference clean;
	SharedPreferences sharedPreference;

	// private ListView lv;
	// String[] settingItems = { "Log out", "clean history",
	// "Notification Settings" };

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// View view = inflater.inflate(R.layout.setting, container, false);
	// TextView windowTitleView = (TextView) getActivity().findViewById(
	// R.id.window_title);
	// windowTitleView.setText("Setting");
	//
	// // lv = (ListView) view.findViewById(R.id.setting_listview);
	// // lv.setAdapter(new ArrayAdapter<String>(this.getActivity(),
	// // R.layout.item, R.id.tv_item, settingItems));
	// // lv.setOnItemClickListener(this);
	// return view;
	// }

	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// // TODO Auto-generated method stub
	// switch (position) {
	// case 0:
	// LoginUtil.removeSession();
	// LoginUtil.deleteFile();
	// Intent intent = new Intent();
	// intent.setClass(getActivity(), LoginActivity.class);
	// getActivity().startActivity(intent);
	// getActivity().finish();
	// break;
	// case 1:
	// LoginUtil.deleteFile();
	// Toast.makeText(getActivity(), "clean history successfully",
	// Toast.LENGTH_SHORT).show();
	// break;
	// case 2:
	// Intent intent2 = new Intent(getActivity(),
	// NotificationSettingsActivity.class);
	// getActivity().startActivity(intent2);
	// getActivity().finish();
	// break;
	//
	// }
	// }

	@Override
	public boolean onPreferenceClick(Preference arg0) {
		// TODO Auto-generated method stub
		if (arg0.getKey().equals("logout")) {
			LoginUtil.removeSession();
			LoginUtil.deleteFile();
			Intent intent = new Intent();
			intent.setClass(getActivity(), LoginActivity.class);
			getActivity().startActivity(intent);
			getActivity().finish();
		} else if (arg0.getKey().equals("cleanHistory")) {
			LoginUtil.deleteFile();
			Toast.makeText(getActivity(), "clean history successfully",
					Toast.LENGTH_SHORT).show();
		}

		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView windowTitleView = (TextView) getActivity().findViewById(
				R.id.window_title);
		windowTitleView.setText("Setting");
		addPreferencesFromResource(R.xml.settings);
		notification = (CheckBoxPreference) findPreference(Constants.SETTINGS_NOTIFICATION_ENABLED);
		sound = (CheckBoxPreference) findPreference(Constants.SETTINGS_SOUND_ENABLED);
		vibrate = (CheckBoxPreference) findPreference(Constants.SETTINGS_VIBRATE_ENABLED);
		logout = (Preference) findPreference("logout");
		clean = (Preference) findPreference("cleanHistory");

		if (notification.isChecked()) {
			notification.setTitle("Notifications Enabled");
		} else {
			notification.setTitle("Notifications Disabled");
		}

		setPreferenceDependencies();

		sharedPreference = getActivity().getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

		notification.setOnPreferenceChangeListener(this);
		notification.setOnPreferenceClickListener(this);
		sound.setOnPreferenceChangeListener(this);
		sound.setOnPreferenceClickListener(this);
		vibrate.setOnPreferenceChangeListener(this);
		vibrate.setOnPreferenceClickListener(this);
		logout.setOnPreferenceChangeListener(this);
		logout.setOnPreferenceClickListener(this);
		clean.setOnPreferenceChangeListener(this);
		clean.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		if (preference.getKey().equals(Constants.SETTINGS_NOTIFICATION_ENABLED)) {
			boolean value = (Boolean) newValue;
			Editor editor = sharedPreference.edit();
			editor.putBoolean(Constants.SETTINGS_NOTIFICATION_ENABLED, value);
			editor.commit();
			return true;
		} else if (preference.getKey().equals(Constants.SETTINGS_SOUND_ENABLED)) {
			boolean value = (Boolean) newValue;
			Editor editor = sharedPreference.edit();
			editor.putBoolean(Constants.SETTINGS_SOUND_ENABLED, value);
			editor.commit();
			return true;
		} else if (preference.getKey().equals(
				Constants.SETTINGS_VIBRATE_ENABLED)) {
			boolean value = (Boolean) newValue;
			Editor editor = sharedPreference.edit();
			editor.putBoolean(Constants.SETTINGS_VIBRATE_ENABLED, value);
			editor.commit();
			return true;
		}

		return true;
	}

	private void setPreferenceDependencies() {

		if (sound != null) {
			sound.setDependency(Constants.SETTINGS_NOTIFICATION_ENABLED);
		}

		if (vibrate != null) {
			vibrate.setDependency(Constants.SETTINGS_NOTIFICATION_ENABLED);
		}
	}
}
