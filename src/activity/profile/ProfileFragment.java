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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ebay.ebayfriend.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileFragment extends Fragment {

	private ListView lv;
	private ProfileAdapter adapter;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private String name;
	private static final int FOLLOW = 0;
	private static final int NOT_FOLLOW = 1;
	private int followState = NOT_FOLLOW;
	
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
		
		lv = (ListView)view.findViewById(R.id.profileList);
		View profileHeader = inflater.inflate(R.layout.profile_header, null);
		TextView nameview = (TextView) profileHeader.findViewById(R.id.profileName);
		ImageView portraitview = (ImageView)profileHeader.findViewById(R.id.profileIcon);
		Button followButton = (Button) profileHeader.findViewById(R.id.followButton);
		followButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// @TODO SEND REQUEST TO FOLLOW THIS PERSON
				Button button = (Button)v;
				if(followState == FOLLOW){
					button.setBackgroundResource(R.drawable.follow);
					followState = NOT_FOLLOW;
				}
				else{
					button.setBackgroundResource(R.drawable.unfollow);
					followState = FOLLOW;
				}
				new AsyncTask<Void, Void, Void>(){
					@Override
					protected Void doInBackground(Void... params) {
					    GetRequest request = new GetRequest(Constants.CHANGE_FOLLOW_PREFIX + name);
					    request.getContent();
						return null;
					}
				}.execute();
			}
		});
		
		imageLoader = PicUtil.imageLoader;
		DisplayImageOptions iconOption = PicUtil.getProfileIconOption();
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
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(position > 0){
					Log.e("Profile", "position: " + position);
					Intent intent = new Intent(getActivity(), ReplyActivity.class);
					intent.putExtra("currentNewsFeed", adapter.getItemList().get(position - 1));
					getActivity().startActivity(intent);
				}
			}
		});
		lv.setAdapter(adapter);
		new AsyncTask<Void, Void, List<NewsFeedItem>>(){
			@Override
			protected List<NewsFeedItem> doInBackground(Void... params) {
				return getNewsFeedList(0);
			}
			
			@Override
			protected void onPostExecute(List<NewsFeedItem> result) {
				super.onPostExecute(result);
				adapter.setList(result);
			}
		}.execute();
		return view;
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
