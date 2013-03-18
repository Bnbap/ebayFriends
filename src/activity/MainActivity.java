package activity;

import layout.MenuLayout;
import layout.MenuLayout.OnScrollListener;
import activity.item.ItemDetailActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ebay.ebayfriend.R;

public class MainActivity extends Activity implements OnTouchListener,
		GestureDetector.OnGestureListener, OnItemClickListener {
	private boolean hasMeasured = false;
	private LinearLayout contentLayout;
	private LinearLayout menuLayout;
	private ImageView iv_set;
	private ListView lv_set;

	private int MAX_WIDTH = 0;
	private final static int SPEED = 30;

	private final static int sleep_time = 5;

	private GestureDetector mGestureDetector;
	private boolean isScrolling = false;
	private float mScrollX;
	private int window_width;

	private String TAG = "jj";

	private View view = null;

	private String title[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"7", "8", "9", "7", "8", "9", "7", "8", "9" };

	private MenuLayout mylaout;

	/***
	 */

	// change to Item Details activity
	void displayItemDetails(View view) {
		Intent intent = new Intent(this, ItemDetailActivity.class);
		startActivity(intent);
	}

	void InitView() {
		contentLayout = (LinearLayout) findViewById(R.id.layout_content);
		menuLayout = (LinearLayout) findViewById(R.id.layout_menu);
		iv_set = (ImageView) findViewById(R.id.iv_set);
		lv_set = (ListView) findViewById(R.id.lv_set);
		mylaout = (MenuLayout) findViewById(R.id.mylaout);
		lv_set.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
				R.id.tv_item, title));
		/***
		 */
		mylaout.setOnScrollListener(new OnScrollListener() {
			@Override
			public void doScroll(float distanceX) {
				doScrolling(distanceX);
			}

			@Override
			public void doLoosen() {
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
						.getLayoutParams();
				Log.e("doloosen","rightMargin="+layoutParams.rightMargin+",  -MAX_WIDTH / 2="+ (-MAX_WIDTH / 2));
//				Log.e("jj", "layoutParams.rightMargin="
//						+ layoutParams.rightMargin);
				if (layoutParams.rightMargin < -MAX_WIDTH / 2) {
					new AsynMove().execute(-SPEED);
				} else {
					new AsynMove().execute(SPEED);
				}
			}
		});

		lv_set.setOnItemClickListener(this);

		menuLayout.setOnTouchListener(this);
		contentLayout.setOnTouchListener(this);
	//	iv_set.setOnTouchListener(this);
		iv_set.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				displayItemDetails(v);
			}
		});
		mGestureDetector = new GestureDetector(this);
		mGestureDetector.setIsLongpressEnabled(false);
		getMAX_WIDTH();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		InitView();

	}

	/***
	 */
	void doScrolling(float distanceX) {
		isScrolling = true;
		mScrollX = distanceX;
//		Log.e(TAG, "mScrollX=" + mScrollX + ",distanceX= " + distanceX);
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
				.getLayoutParams();
		RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) menuLayout
				.getLayoutParams();
		layoutParams.rightMargin += mScrollX;
		layoutParams_1.rightMargin = window_width + layoutParams.rightMargin;
		if (layoutParams.rightMargin <= -MAX_WIDTH) {
			isScrolling = false;
			layoutParams.rightMargin = -MAX_WIDTH;
			layoutParams_1.rightMargin = window_width - MAX_WIDTH;

		} else if (layoutParams.rightMargin >= 0) {
			isScrolling = false;
			layoutParams.rightMargin = 0;
			layoutParams_1.rightMargin = window_width;
		}
//		Log.v(TAG, "content.rightMargin=" + layoutParams.rightMargin
//				+ ",menu.rightMargin =" + layoutParams_1.rightMargin);

		contentLayout.setLayoutParams(layoutParams);
		menuLayout.setLayoutParams(layoutParams_1);
	}

	/***
	 */
	void getMAX_WIDTH() {
		ViewTreeObserver viewTreeObserver = contentLayout.getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					window_width = getWindowManager().getDefaultDisplay()
							.getWidth();
					MAX_WIDTH = menuLayout.getWidth();
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
							.getLayoutParams();
					RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) menuLayout
							.getLayoutParams();
					ViewGroup.LayoutParams layoutParams_2 = mylaout
							.getLayoutParams();
					layoutParams.width = window_width;
					contentLayout.setLayoutParams(layoutParams);

					layoutParams_1.rightMargin = window_width;
					menuLayout.setLayoutParams(layoutParams_1);
					layoutParams_2.width = MAX_WIDTH;
					mylaout.setLayoutParams(layoutParams_2);

//					Log.v(TAG, "MAX_WIDTH=" + MAX_WIDTH + "width="
//							+ window_width);
					hasMeasured = true;
				}
				return true;
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
					.getLayoutParams();
			if (layoutParams.rightMargin < -MAX_WIDTH/2) {
				new AsynMove().execute(-SPEED);
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		view = v;

		if (MotionEvent.ACTION_UP == event.getAction() && isScrolling == true) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
					.getLayoutParams();
			if (layoutParams.rightMargin > -MAX_WIDTH / 2) {
				Log.e("jj", "speed");
				new AsynMove().execute(SPEED);
			} else {
				Log.e("jj", "-speed");
				new AsynMove().execute(-SPEED);
			}
		}

		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {

		int position = lv_set.pointToPosition((int) e.getX(), (int) e.getY());
		if (position != ListView.INVALID_POSITION) {
			View child = lv_set.getChildAt(position
					- lv_set.getFirstVisiblePosition());
			if (child != null)
				child.setPressed(true);
		}

		mScrollX = 0;
		isScrolling = false;
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	/***
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if (view != null && view == iv_set) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
					.getLayoutParams();
			Log.e("jj","rightMargin="+layoutParams.rightMargin+", -Max_width/2="+ -MAX_WIDTH/2 );
			if (layoutParams.rightMargin < -MAX_WIDTH/2) {
				new AsynMove().execute(-SPEED);
				lv_set.setSelection(0);
			} else {
				new AsynMove().execute(SPEED);
			}
		} else if (view != null && view == contentLayout) {
			RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) contentLayout
					.getLayoutParams();
			if (layoutParams.rightMargin >= -MAX_WIDTH/2) {
				new AsynMove().execute(SPEED);
			}
		}

		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		doScrolling(distanceX);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	class AsynMove extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			Log.e("jj", "AsynMove");
			int times = 0;
			if (MAX_WIDTH % Math.abs(params[0]) == 0)
				times = MAX_WIDTH / Math.abs(params[0]);
			else
				times = MAX_WIDTH / Math.abs(params[0]) + 1;

			for (int i = 0; i < times; i++) {
				publishProgress(params[0]);
				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		/**
		 * update UI
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
					.getLayoutParams();
			RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) menuLayout
					.getLayoutParams();
			if (values[0] > 0) {
				layoutParams.rightMargin = Math.min(layoutParams.rightMargin
						+ values[0], 0);
				layoutParams_1.rightMargin = Math.min(
						layoutParams_1.rightMargin + values[0], window_width);
//				Log.v(TAG, "content.rightMargin" + layoutParams.rightMargin
//						+ ",menu_rightMargin" + layoutParams_1.rightMargin);
			} else {
				layoutParams.rightMargin = Math.max(layoutParams.rightMargin
						+ values[0], -MAX_WIDTH);
				layoutParams_1.rightMargin = Math.max(
						layoutParams_1.rightMargin + values[0], window_width
								- MAX_WIDTH);
//				Log.v(TAG, "content.rightMargin" + layoutParams.rightMargin
//						+ ",menu_rightMargin" + layoutParams_1.rightMargin);
			}
			menuLayout.setLayoutParams(layoutParams_1);
			contentLayout.setLayoutParams(layoutParams);

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentLayout
				.getLayoutParams();
		if (layoutParams.rightMargin == -MAX_WIDTH)
			Toast.makeText(MainActivity.this, title[position], 1).show();
	}
}