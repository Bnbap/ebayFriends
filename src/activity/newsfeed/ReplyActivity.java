package activity.newsfeed;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.ebay.ebayfriend.R;

public class ReplyActivity extends Activity {
	private NewsFeedItem newsFeedItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply);
		this.newsFeedItem = (NewsFeedItem) getIntent().getSerializableExtra("currentNewsFeed");
		ListView replyList = (ListView) findViewById(R.id.replyList);
		ReplyAdapter replyAdapter = new ReplyAdapter(this, newsFeedItem);
		replyList.setAdapter(replyAdapter);
	}
}
