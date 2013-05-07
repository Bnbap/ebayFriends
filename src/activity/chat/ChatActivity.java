package activity.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ebay.ebayfriend.R;

public class ChatActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	private String ipstring = null, nameString = null;
	Thread thread = null;
	Socket s = null;
	private InetSocketAddress isa = null;
	DataInputStream dis = null;
	DataOutputStream dos = null;
	private String reMsg = null;
	private String chatKey = "chatKey";
	private String toName = "zhanghang", fromName = "tangyu", ip = "10.0.2.2";
	private boolean isConnect = false;

	private Button mBtnSend;
	private TextView mBtnRcd;
	private Button mBtnBack;
	private EditText mEditTextContent;
	private RelativeLayout mBottom;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	private boolean isShosrt = false;
	private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding,
			voice_rcd_hint_tooshort;
	private ImageView img1, sc_img1;
	private SoundMeter mSensor;
	private View rcChat_popup;
	private LinearLayout del_re;
	private ImageView chatting_mode_btn, volume;
	private boolean btn_vocie = false;
	private int flag = 1;
	private Handler mHandler = new Handler();
	private String voiceName;
	private long startVoiceT, endVoiceT;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		connect();

		initData();
	}

	public void initView() {
		mListView = (ListView) findViewById(R.id.listview);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtnRcd = (TextView) findViewById(R.id.btn_rcd);
		mBtnSend.setOnClickListener(this);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBottom = (RelativeLayout) findViewById(R.id.btn_bottom);
		mBtnBack.setOnClickListener(this);
		chatting_mode_btn = (ImageView) this.findViewById(R.id.ivPopUp);
		volume = (ImageView) this.findViewById(R.id.volume);
		rcChat_popup = this.findViewById(R.id.rcChat_popup);
		img1 = (ImageView) this.findViewById(R.id.img1);
		sc_img1 = (ImageView) this.findViewById(R.id.sc_img1);
		del_re = (LinearLayout) this.findViewById(R.id.del_re);
		voice_rcd_hint_rcding = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_rcding);
		voice_rcd_hint_loading = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_loading);
		voice_rcd_hint_tooshort = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_tooshort);
		mSensor = new SoundMeter();
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);

		// 语音文字切换按钮
		chatting_mode_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (btn_vocie) {
					mBtnRcd.setVisibility(View.GONE);
					mBottom.setVisibility(View.VISIBLE);
					btn_vocie = false;
					chatting_mode_btn
							.setImageResource(R.drawable.chatting_setmode_msg_btn);

				} else {
					mBtnRcd.setVisibility(View.VISIBLE);
					mBottom.setVisibility(View.GONE);
					chatting_mode_btn
							.setImageResource(R.drawable.chatting_setmode_voice_btn);
					btn_vocie = true;
				}
			}
		});
		mBtnRcd.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// 按下语音录制按钮时返回false执行父类OnTouch
				return false;
			}
		});
	}

	private String[] msgArray = new String[] { "有人就有恩怨", "有恩怨就有江湖", "人就是江湖",
			"你怎么退出？ ", "生命中充满了巧合", "两条平行线也会有相交的一天。" };

	private String[] dataArray = new String[] { "2012-10-31 18:00",
			"2012-10-31 18:10", "2012-10-31 18:11", "2012-10-31 18:20",
			"2012-10-31 18:30", "2012-10-31 18:35" };
	private final static int COUNT = 6;

	public void initData() {
		for (int i = 0; i < COUNT; i++) {
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(dataArray[i]);
			if (i % 2 == 0) {
				entity.setName("白富美");
				entity.setMsgType(true);
			} else {
				entity.setName("高富帅");
				entity.setMsgType(false);
			}

			entity.setText(msgArray[i]);
			mDataArrays.add(entity);
		}

		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_send:
			send();
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}

	private void send() {
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0) {

			System.out.println(s);
			try {
				dos.writeUTF(chatKey + "name:" + toName + "end;" + contString);

			} catch (SocketTimeoutException e) {
				System.out.println("連接超時，服務器未開啟或IP錯誤");
				// Toast.makeText(SocketmsgActivity.this, "連接超時，服務器未開啟或IP錯誤",
				// Toast.LENGTH_SHORT).show();
				// Intent intent = new
				// Intent(SocketmsgActivity.this,IniActivity.class);
				// startActivity(intent);
				// SocketmsgActivity.this.finish();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("連接超時，服務器未開啟或IP錯誤");
				// Toast.makeText(SocketmsgActivity.this, "連接超時，服務器未開啟或IP錯誤",
				// Toast.LENGTH_SHORT).show();
				// Intent intent = new
				// Intent(SocketmsgActivity.this,IniActivity.class);
				// startActivity(intent);
				// SocketmsgActivity.this.finish();
				e.printStackTrace();
			}
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(getDate());
			entity.setName("高富帅");
			entity.setMsgType(false);
			entity.setText(contString);

			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();

			mEditTextContent.setText("");

			mListView.setSelection(mListView.getCount() - 1);
		}
	}

	private String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins);

		return sbBuffer.toString();
	}

	// 按下语音录制按钮时
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (!Environment.getExternalStorageDirectory().exists()) {
			Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
			return false;
		}

		if (btn_vocie) {
			System.out.println("1");
			int[] location = new int[2];
			mBtnRcd.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
			int btn_rc_Y = location[1];
			int btn_rc_X = location[0];
			int[] del_location = new int[2];
			del_re.getLocationInWindow(del_location);
			int del_Y = del_location[1];
			int del_x = del_location[0];
			if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
				if (!Environment.getExternalStorageDirectory().exists()) {
					Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
					return false;
				}
				System.out.println("2");
				if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {// 判断手势按下的位置是否是语音录制按钮的范围内
					System.out.println("3");
					mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
					rcChat_popup.setVisibility(View.VISIBLE);
					voice_rcd_hint_loading.setVisibility(View.VISIBLE);
					voice_rcd_hint_rcding.setVisibility(View.GONE);
					voice_rcd_hint_tooshort.setVisibility(View.GONE);
					mHandler.postDelayed(new Runnable() {
						public void run() {
							if (!isShosrt) {
								voice_rcd_hint_loading.setVisibility(View.GONE);
								voice_rcd_hint_rcding
										.setVisibility(View.VISIBLE);
							}
						}
					}, 300);
					img1.setVisibility(View.VISIBLE);
					del_re.setVisibility(View.GONE);
					startVoiceT = SystemClock.currentThreadTimeMillis();
					voiceName = startVoiceT + ".amr";
					start(voiceName);
					flag = 2;
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {// 松开手势时执行录制完成
				System.out.println("4");
				mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
				if (event.getY() >= del_Y
						&& event.getY() <= del_Y + del_re.getHeight()
						&& event.getX() >= del_x
						&& event.getX() <= del_x + del_re.getWidth()) {
					rcChat_popup.setVisibility(View.GONE);
					img1.setVisibility(View.VISIBLE);
					del_re.setVisibility(View.GONE);
					stop();
					flag = 1;
					File file = new File(
							android.os.Environment
									.getExternalStorageDirectory()
									+ "/"
									+ voiceName);
					if (file.exists()) {
						file.delete();
					}
				} else {

					voice_rcd_hint_rcding.setVisibility(View.GONE);
					stop();
					endVoiceT = SystemClock.currentThreadTimeMillis();
					flag = 1;
					int time = (int) ((endVoiceT - startVoiceT) / 1000);
					if (time < 1) {
						isShosrt = true;
						voice_rcd_hint_loading.setVisibility(View.GONE);
						voice_rcd_hint_rcding.setVisibility(View.GONE);
						voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
						mHandler.postDelayed(new Runnable() {
							public void run() {
								voice_rcd_hint_tooshort
										.setVisibility(View.GONE);
								rcChat_popup.setVisibility(View.GONE);
								isShosrt = false;
							}
						}, 500);
						return false;
					}
					ChatMsgEntity entity = new ChatMsgEntity();
					entity.setDate(getDate());
					entity.setName("高富帅");
					entity.setMsgType(false);
					entity.setTime(time + "\"");
					entity.setText(voiceName);
					mDataArrays.add(entity);
					mAdapter.notifyDataSetChanged();
					mListView.setSelection(mListView.getCount() - 1);
					rcChat_popup.setVisibility(View.GONE);

				}
			}
			if (event.getY() < btn_rc_Y) {// 手势按下的位置不在语音录制按钮的范围内
				System.out.println("5");
				Animation mLitteAnimation = AnimationUtils.loadAnimation(this,
						R.anim.cancel_rc);
				Animation mBigAnimation = AnimationUtils.loadAnimation(this,
						R.anim.cancel_rc2);
				img1.setVisibility(View.GONE);
				del_re.setVisibility(View.VISIBLE);
				del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg);
				if (event.getY() >= del_Y
						&& event.getY() <= del_Y + del_re.getHeight()
						&& event.getX() >= del_x
						&& event.getX() <= del_x + del_re.getWidth()) {
					del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg_focused);
					sc_img1.startAnimation(mLitteAnimation);
					sc_img1.startAnimation(mBigAnimation);
				}
			} else {

				img1.setVisibility(View.VISIBLE);
				del_re.setVisibility(View.GONE);
				del_re.setBackgroundResource(0);
			}
		}
		return super.onTouchEvent(event);
	}

	private static final int POLL_INTERVAL = 300;

	private Runnable mSleepTask = new Runnable() {
		public void run() {
			stop();
		}
	};
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);

		}
	};

	private void start(String name) {
		mSensor.start(name);
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private void stop() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
		volume.setImageResource(R.drawable.amp1);
	}

	private void updateDisplay(double signalEMA) {

		switch ((int) signalEMA) {
		case 0:
		case 1:
			volume.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			volume.setImageResource(R.drawable.amp2);

			break;
		case 4:
		case 5:
			volume.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			volume.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			volume.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			volume.setImageResource(R.drawable.amp6);
			break;
		default:
			volume.setImageResource(R.drawable.amp7);
			break;
		}
	}

	public void head_xiaohei(View v) { // 标题栏 返回按钮

	}

	private Runnable doThread = new Runnable() {
		public void run() {
			System.out.println("running!");
			ReceiveMsg();
		}
	};

	public void connect() {
		thread = new Thread(null, connThread, "connect");
		thread.start();
	}

	private Runnable connThread = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				s = new Socket();
				isa = new InetSocketAddress(ip, 1235);
				s.connect(isa, 5000);

				if (s.isConnected()) {
					dos = new DataOutputStream(s.getOutputStream());
					dis = new DataInputStream(s.getInputStream());
					dos.writeUTF(chatKey + "online:" + fromName);
					/**
					 * 这里是关键，我在此耗时8h+ 原因是 子线程不能直接更新UI 为此，我们需要通过Handler物件，通知主线程Ui
					 * Thread来更新界面。
					 * 
					 */
					thread = new Thread(null, doThread, "Message");
					thread.start();
					System.out.println("connect");
					isConnect = true;
				}
			} catch (UnknownHostException e) {
				System.out.println("連接失敗");
				// Toast.makeText(SocketmsgActivity.this, "連接失敗",
				// Toast.LENGTH_SHORT).show();
				// Intent intent = new
				// Intent(SocketmsgActivity.this,IniActivity.class);
				// startActivity(intent);
				// SocketmsgActivity.this.finish();
				finish();
				e.printStackTrace();
			} catch (SocketTimeoutException e) {
				System.out.println("連接超時，服務器未開啟或IP錯誤");
				// Toast.makeText(SocketmsgActivity.this, "連接超時，服務器未開啟或IP錯誤",
				// Toast.LENGTH_SHORT).show();
				// Intent intent = new
				// Intent(SocketmsgActivity.this,IniActivity.class);
				// startActivity(intent);
				// SocketmsgActivity.this.finish();
				finish();
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("連接失敗");
				e.printStackTrace();
			}
		}
	};

	public void disConnect() {
		if (dos != null) {
			try {

				dos.writeUTF(chatKey + "offline:" + fromName);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 线程监视Server信息
	 */
	private void ReceiveMsg() {
		if (isConnect) {
			try {
				while ((reMsg = dis.readUTF()) != null) {
					System.out.println(reMsg);
					if (reMsg != null) {

						try {
							android.os.Message msgMessage = new android.os.Message();
							msgMessage.what = 0x1981;
							handler.sendMessage(msgMessage);
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			} catch (SocketException e) {
				// TODO: handle exception
				System.out.println("exit!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 通过handler更新UI
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x1981:

				ChatMsgEntity entity = new ChatMsgEntity();
				entity.setDate(getDate());
				entity.setName("白富美");
				entity.setMsgType(true);
				entity.setText(reMsg);

				mDataArrays.add(entity);
				mAdapter.notifyDataSetChanged();

				mEditTextContent.setText("");

				mListView.setSelection(mListView.getCount() - 1);

				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		disConnect();
		// System.exit(0);
	}
}