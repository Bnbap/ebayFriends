package activity.newsfeed;

import java.util.ArrayList;
import java.util.List;

import activity.newsfeed.PullToRefreshListView.OnRefreshListener;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebay.ebayfriend.R;
import com.nostra13.universalimageloader.core.ImageLoader;


public class NewsFeedFragment extends Fragment {

	private PullToRefreshListView lv;
	private List<NewsFeedItem> itemList;
	private NewsFeedItemAdapter adapter;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.newsfeed,container,false);
		TextView windowTitleView = (TextView)getActivity().findViewById(R.id.window_title);
		windowTitleView.setText("News Feeding");
		lv = (PullToRefreshListView) view.findViewById(R.id.listview);
		lv.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				new GetDataTask().execute();
			}
		});
		itemList = getlist();
		adapter = new NewsFeedItemAdapter(getActivity(), itemList, imageLoader);
		lv.setAdapter(adapter);
		return view;
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
        	itemList.get(0).setImage("http://cf3.thingd.com/default/261892199_c927b199208e.jpg");
        	adapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            lv.onRefreshComplete();

            super.onPostExecute(result);
        }
    }
	
	private List<NewsFeedItem> getlist(){
		List<NewsFeedItem> list = new ArrayList<NewsFeedItem>();
		for(String imageURL: Constants.IMAGES){
			NewsFeedItem item = new NewsFeedItem(imageURL, "http://www.unitedwayhp.org/images/10/Main/FacebookIcon_small.jpg", "Paul");
			list.add(item);
		}
		return list;
	}
}
