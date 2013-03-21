package activity.newsfeed;

import java.util.ArrayList;
import java.util.List;

import activity.MainActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ebay.ebayfriend.R;

public class NewsFeedActivity extends MainActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		InitView();
		TextView windowTitleView = (TextView)findViewById(R.id.window_title);
		windowTitleView.setText("News Feed");
		
		View content = findViewById(R.id.content);
		LayoutInflater inflater = getLayoutInflater();
		((ViewGroup) content).addView(inflater.inflate(R.layout.newsfeed, null));
		ListView lv = (ListView) findViewById(R.id.listview);
		NewsFeedItemAdapter adapter = new NewsFeedItemAdapter(this, getlist(), imageLoader);
		lv.setAdapter(adapter);
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
