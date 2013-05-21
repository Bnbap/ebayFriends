package activity;

import notification.client.NotificationAccess;
import activity.newsfeed.NewsFeedFragment;
import activity.post.PostActivity;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Interpolator;
import android.widget.ImageButton;

import com.ebay.ebayfriend.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

public class MainActivity extends SlidingActivity {
	private Fragment mContent;
	private Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		NotificationAccess.getInstance().setCallBackActivity(this);
		NotificationAccess.getInstance().startNotificationService();
		activity = this;
		// set the Above View
		if (savedInstanceState != null)
			mContent = getFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new NewsFeedFragment();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// set the Above View
		setContentView(R.layout.content_frame);
		getFragmentManager().beginTransaction()
				.replace(R.id.content, mContent).commit();

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MenuFragment()).commit();

		// set post listener
		ImageButton postButton = (ImageButton) findViewById(R.id.mainview_post_bt);
		postButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, PostActivity.class);
				startActivity(intent);
			}
		});
		// customize the SlidingMenu
		final SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setBehindScrollScale(0.0f);
		sm.setBehindCanvasTransformer(new CanvasTransformer() {
			private Interpolator interp = new Interpolator() {
				@Override
				public float getInterpolation(float t) {
					t -= 1.0f;
					return t * t * t + 1.0f;
				}		
			};
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.translate(0, canvas.getHeight()*(1-interp.getInterpolation(percentOpen)));
			}
			});
		// back button listener
		ImageButton backButton = (ImageButton) findViewById(R.id.iv_set);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sm.showMenu();
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getFragmentManager().putFragment(outState, "mContent", mContent);
	}

	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getFragmentManager().beginTransaction()
				.replace(R.id.content, fragment).commit();
		getSlidingMenu().showContent();
	}
}
