package activity.newsfeed;

import java.io.File;

import util.AudioUtil;

import activity.post.VoiceProcessFragment;
import activity.post.VoiceProcessFragment.VoiceHandler;
import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ebay.ebayfriend.R;

public class ReplyActivity extends Activity {
	private NewsFeedItem newsFeedItem;
	private static final int TEXT_INPUT = 0;
	private static final int AUDIO_INPUT = 1;
	private int barStatus = TEXT_INPUT;
	private String recordName = this.getFilesDir().toString() + File.separator
			+ "MyRecord";
	private AudioUtil audioUtility;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply);
		
		this.newsFeedItem = (NewsFeedItem) getIntent().getSerializableExtra("currentNewsFeed");
		ListView replyList = (ListView) findViewById(R.id.replyList);
		ReplyAdapter replyAdapter = new ReplyAdapter(this, newsFeedItem);
		replyList.setAdapter(replyAdapter);
		
		// Set input bar listener
		Button swapButton = (Button) findViewById(R.id.swapInputForm);
		Button sendButton = (Button) findViewById(R.id.sendReplyButton);
		final EditText textInput = (EditText) findViewById(R.id.textInput);
		final Button audioInput = (Button) findViewById(R.id.audioInput);
		
		swapButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if (barStatus == ReplyActivity.TEXT_INPUT){
					textInput.setVisibility(View.GONE);
					audioInput.setVisibility(View.VISIBLE);
					barStatus = AUDIO_INPUT;
				}
				else if (barStatus == ReplyActivity.AUDIO_INPUT){
					textInput.setVisibility(View.VISIBLE);
					audioInput.setVisibility(View.GONE);
					barStatus = TEXT_INPUT;
				}
			}
		});
		sendButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					au.recordVoice(recordName);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					au.storeVoice();
				}
				return false;
			}
		});
	}
}
