package layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/***
 * 
 */
public class MenuLayout extends LinearLayout {
	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;

	private boolean isLock = false;

	public OnScrollListener onScrollListener;

	private boolean b;

	public MenuLayout(Context context) {
		super(context);
	}

	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	public MenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new MySimpleGesture());

	}

	/***
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		b = mGestureDetector.onTouchEvent(ev);
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			onScrollListener.doLoosen();
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		super.onInterceptTouchEvent(ev);
		return b;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		isLock = false;
		return super.onTouchEvent(event);
	}

	class MySimpleGesture extends SimpleOnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			isLock = true;
			return super.onDown(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (!isLock)
				onScrollListener.doScroll(distanceX);

			if (Math.abs(distanceY) > Math.abs(distanceX)) {
				return false;
			} else {
				return true;
			}

		}
	}

	public interface OnScrollListener {
		void doScroll(float distanceX);

		void doLoosen();
	}

}
