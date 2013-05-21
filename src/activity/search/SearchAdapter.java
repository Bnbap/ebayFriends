package activity.search;

import java.util.ArrayList;
import java.util.List;

import util.GetRequest;
import util.PicUtil;
import util.PicUtil.AnimateFirstDisplayListener;
import activity.newsfeed.Constants;
import activity.profile.ProfileFragment;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ebay.ebayfriend.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SearchAdapter extends ArrayAdapter<SearchItem> {

	private List<SearchItem> itemList;
	private Activity activity;
	private ListView lv;
	private ImageLoader imageLoader;
	private AnimateFirstDisplayListener animateListener;
	private DisplayImageOptions option;

	public SearchAdapter(Activity activity, final ListView lv) {
		super(activity, R.layout.newsfeeditem);
		this.itemList = new ArrayList<SearchItem>();
		this.activity = activity;
		this.lv = lv;
		this.imageLoader = PicUtil.imageLoader;
		this.animateListener = PicUtil.getAnimateListener();
		this.option = PicUtil.getIconOption();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View searchView = convertView;

		final SearchItem currentItem = itemList.get(position);
		final String name = currentItem.getName();
		String portrait = currentItem.getPortrait();
		final boolean ifFollow = currentItem.isFollow();
		ViewHolder holder;
		if (convertView == null) {
			searchView = inflater.inflate(R.layout.searchitem, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) searchView.findViewById(R.id.searchName);
			holder.icon = (ImageView) searchView.findViewById(R.id.searchIcon);
			holder.followButton = (Button) searchView
					.findViewById(R.id.followButton);
			searchView.setTag(holder);
		} else {
			holder = (ViewHolder) searchView.getTag();
		}
		holder.name.setText(name);
		imageLoader
				.displayImage(portrait, holder.icon, option, animateListener);
		if (ifFollow) {
			holder.followButton.setBackgroundResource(R.drawable.unfollow);
		} else
			holder.followButton.setBackgroundResource(R.drawable.follow);
		holder.followButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// @TODO SEND REQUEST TO FOLLOW THIS PERSON
				Button button = (Button) v;
				if (currentItem.isFollow() == true) {
					button.setBackgroundResource(R.drawable.follow);
					currentItem.setIfFollow(false);
				} else {
					button.setBackgroundResource(R.drawable.unfollow);
					currentItem.setIfFollow(true);
				}
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						GetRequest request = new GetRequest(
								Constants.CHANGE_FOLLOW_PREFIX + name);
						request.getContent();
						return null;
					}
				}.execute();
			}
		});
		holder.icon.setOnClickListener(new SearchIconOnclickListener(name, portrait));
		return searchView;
	}

	public void setList(List<SearchItem> itemList) {
		this.itemList = itemList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return itemList.size();
	}

	private class ViewHolder {
		public TextView name;
		public ImageView icon;
		public Button followButton;
	}
	
	private class SearchIconOnclickListener implements OnClickListener{
		private String username;
		private String portrait;
		
		public SearchIconOnclickListener(String username, String portrait) {
			this.username = username;
			this.portrait = portrait;
		}
		@Override
		public void onClick(View v) {
			ProfileFragment fragment = new ProfileFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("username", username);
			bundle.putString("portrait", portrait);
			fragment.setArguments(bundle);
			FragmentTransaction transaction = activity.getFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.content, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}
}
