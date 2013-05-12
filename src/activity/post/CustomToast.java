package activity.post;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class CustomToast {
	private Context context;
	private Handler handler = null;
	private Toast toast;
	private String text;
	private Runnable toastThread = new Runnable() {
		public void run() {
			toast.setText(text);
			toast.show();
			handler.postDelayed(toastThread, 3300);
		}
	};
	public CustomToast(Context context,String text) {
		this.context = context;
		this.text = text;
		handler = new Handler(this.context.getMainLooper());
		ProgressBar pb = new ProgressBar(context, null,
				android.R.attr.progressBarStyleInverse);
		pb.setMax(100);
		pb.setProgress(1);
		pb.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		toast = Toast.makeText(context, "Recording", Toast.LENGTH_LONG);
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