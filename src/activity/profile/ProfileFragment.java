package activity.profile;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.GetRequest;
import util.LoginUtil;
import util.PicUtil;
import util.PicUtil.AnimateFirstDisplayListener;
import activity.newsfeed.Constants;
import activity.newsfeed.NewsFeedItem;
import activity.newsfeed.PullAndLoadListView;
import activity.newsfeed.PullAndLoadListView.OnLoadMoreListener;
import activity.newsfeed.PullToRefreshListView.OnRefreshListener;
import activity.newsfeed.ReplyActivity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebay.ebayfriend.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileFragment extends Fragment {

	private PullAndLoadListView lv;
	private ProfileAdapter adapter;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private String name;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {
		// GET ARGUMENTS: NAME/PORTRAIT
		Bundle bundle = getArguments();
		name = "wzn";
		String portrait = "";
		if (bundle != null){
			name = bundle.getString("username");
			portrait = bundle.getString("portrait");
		}
		else{
			name = LoginUtil.USERNAME;
			portrait = LoginUtil.PORTRAIT;
		}
		View view = inflater.inflate(R.layout.profile,container,false);
		TextView windowTitleView = (TextView)getActivity().findViewById(R.id.window_title);
		windowTitleView.setText("Profile");
		
		lv = (PullAndLoadListView)view.findViewById(R.id.profileList);
		View profileHeader = inflater.inflate(R.layout.profile_header, null);
		TextView nameview = (TextView) profileHeader.findViewById(R.id.profileName);
		ImageView portraitview = (ImageView)profileHeader.findViewById(R.id.profileIcon);
		
		imageLoader = PicUtil.imageLoader;
		DisplayImageOptions iconOption = PicUtil.getIconOption();
		AnimateFirstDisplayListener animateListener = PicUtil.getAnimateListener();
		if (name.length() != 0){
			nameview.setText(name);
		}
		else{
			// set by current user
		}
		if (portrait.length() != 0){
			imageLoader.displayImage(portrait, portraitview, iconOption, animateListener);
		}
		else{
			// set current user portrait
		}
		lv.addHeaderView(profileHeader);
		//================================
		// header view set complete
		//================================
		adapter = new ProfileAdapter(getActivity(), imageLoader,lv);
		lv.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				new RefreshNewsFeedTask(adapter, lv).execute();
			}
		});
		lv.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				new MoreNewsFeedTask(adapter, lv).execute();
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(position > 1){
					Log.e("Profile", "position: " + position);
					Intent intent = new Intent(getActivity(), ReplyActivity.class);
					intent.putExtra("currentNewsFeed", adapter.getItemList().get(position - 2));
					getActivity().startActivity(intent);
				}
			}
		});
		new RefreshNewsFeedTask(adapter, lv).execute();
		lv.setAdapter(adapter);
		return view;
	}
	
	private class RefreshNewsFeedTask extends AsyncTask<Void, Void, List<NewsFeedItem>> {
		private ProfileAdapter adapter;
		private PullAndLoadListView lv;
		
		public RefreshNewsFeedTask(ProfileAdapter adapter, PullAndLoadListView lv) {
			this.adapter = adapter;
			this.lv = lv;
		}
		@Override
		protected List<NewsFeedItem> doInBackground(Void... params) {
			return getNewsFeedList(0);
		}

		@Override
		protected void onPostExecute(List<NewsFeedItem> result) {
			super.onPostExecute(result);
			adapter.updateList(result);
			adapter.resetPage();
			lv.onRefreshComplete();
		}
	}
	
	private class MoreNewsFeedTask extends AsyncTask<Void, Void, List<NewsFeedItem>> {
		private ProfileAdapter adapter;
		private PullAndLoadListView lv;
		
		public MoreNewsFeedTask(ProfileAdapter adapter, PullAndLoadListView lv) {
			this.adapter = adapter;
			this.lv = lv;
		}
		@Override
		protected List<NewsFeedItem> doInBackground(Void... params) {
			int currentPage = adapter.getCurrentPage() + 1;
			List<NewsFeedItem> fetchList = getNewsFeedList(currentPage);
			if(fetchList.size() > 0) adapter.incrementCurrentPage();
			return fetchList;
		}

		@Override
		protected void onPostExecute(List<NewsFeedItem> result) {
			super.onPostExecute(result);
			List<NewsFeedItem> currentList = adapter.getItemList();
			for(NewsFeedItem item: result){
				currentList.add(item);
			}
			adapter.notifyDataSetChanged();
			int index = lv.getFirstVisiblePosition();
			View v = lv.getChildAt(0);
			int top = (v == null) ? 0 : v.getTop();

			lv.setSelectionFromTop(index, top);
			if (result.size() == 0){
				lv.notifyNoMore();
			}
			lv.onLoadMoreComplete();
		}
	}
	
	private List<NewsFeedItem> getNewsFeedList(int page){
		List<NewsFeedItem> list = new ArrayList<NewsFeedItem>();
		String getURL = Constants.GET_PROFILE_URL_PREFIX + "num=" + page + "&name=" + name;
		GetRequest getRequest = new GetRequest(getURL);
		String jsonResult = getRequest.getContent();
		Log.v("ProfileFragment", "Request URL: " + getURL);
		Log.v("ProfileFragment", "Response: " + jsonResult);
		if (jsonResult == null) {
			Log.e("NewsFeedFragment", "Json Parse Error");
		} else {
			try {
				JSONArray itemArray = new JSONArray(jsonResult);
				for (int i = 0; i < itemArray.length(); i++) {
					JSONObject item = itemArray.getJSONObject(i);
					String imageURL = item.getString("picture");
					String voiceURL = item.getString("voice");
					JSONObject person = item.getJSONObject("author");
					String portraitURL = person.getString("portrait");
					String authorName = person.getString("name");
					String commentsURL = item.getString("comments");
					String goodURL = item.getString("good");
					NewsFeedItem newsFeedItem = new NewsFeedItem(imageURL,
							portraitURL, authorName, voiceURL, commentsURL, goodURL);
					list.add(newsFeedItem);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("ProfileFragement", "Parse Json Error");
			}
		}
		return list;
	}
}
