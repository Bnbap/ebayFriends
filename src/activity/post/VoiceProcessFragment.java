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
	private Toast toast;
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
		

		ct = new CustomToast(getActivity());
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

	@SuppressLint("ShowToast")
	public class CustomToast {
		private Context context;
		private Handler handler = null;
		private Runnable toastThread = new Runnable() {
			public void run() {
				toast.setText("Recording");
				toast.show();
				handler.postDelayed(toastThread, 3300);
			}
		};
		public CustomToast(Context context) {
			this.context = context;
			handler = new Handler(this.context.getMainLooper());
			ProgressBar pb = new ProgressBar(getActivity(), null,
					android.R.attr.progressBarStyleInverse);
			pb.setMax(100);
			pb.setProgress(1);
			pb.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));

			toast = Toast.makeText(getActivity(), "Recording", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			LinearLayout toastView = (LinearLayout) toast.getView();
			toastView.setGravity(Gravity.CENTER);
			toastView.addView(pb,1);
		}
		public void setText(String text) {
			toast.setText(text);
		}
		public void showToast(final long length) {
			handler.post(toastThread);
			Thread timeThread = new Thread() {
				public void run() {
					try {
						Thread.sleep(length);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					CustomToast.this.stopToast();
				}
			};
			timeThread.start();
		}
		public void stopToast() {
			// 删除Handler队列中的仍处理等待的消息元素删除
			handler.removeCallbacks(toastThread);
			// 撤掉仍在显示的Toast
			toast.cancel();
		}
	}
	
	
}
