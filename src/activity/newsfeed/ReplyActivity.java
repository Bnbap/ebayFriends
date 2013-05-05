package activity.newsfeed;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ebay.ebayfriend.R;

public class ReplyActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply);
		ListView replyList = (ListView) findViewById(R.id.replyList);
		ArrayList<Integer> array = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++){
			array.add(i);
		}
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, array);
		replyList.setAdapter(adapter);
	}
}
