package activity.post;

import java.io.File;

import com.ebay.ebayfriend.R;

import bean.PurchasedItem;
import util.AudioUtil;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class VoiceProcessFragment extends Fragment {
	/**
	 * step 4 final 1.audio or 2. text
	 */
	protected MediaRecorder mr;
	private String recordFileName;
	private AudioUtil au;
	PurchasedItem pi;
	Button recordButton, playButton;
	private String recordName = null;
	private CustomToast ct;


	public VoiceProcessFragment() {
		mr = new MediaRecorder();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle b = getArguments();
		pi = (PurchasedItem) b.getSerializable("purchasedItem");
		recordName = getActivity().getFilesDir().toString() + File.separator
				+ "MyRecord";
		

		ct = new CustomToast(getActivity(),"Recording");
		au = new AudioUtil();
		View view = inflater.inflate(R.layout.process_voice, container, false);
		TextView windowTitleView = (TextView) getActivity().findViewById(
				R.id.post_title);
		windowTitleView.setText("Record Voice");
		recordButton = (Button) view.findViewById(R.id.process_record_button);
		recordButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					au.recordVoice(recordName);
					ct.showToast(99999999);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					au.storeVoice();
					ct.stopToast();
				}
				return false;
			}

		});
		playButton = (Button) view.findViewById(R.id.process_play_button);
		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				au.playVoice();

			}

		});
		return view;

	}

	public void playRecord() {

	}

	public String getRecordStyle1() {
		return null;
	}

	public String getRecordStyle2() {
		return null;
	}

	public String getRecordStyle3() {
		return null;
	}
	
	
}
