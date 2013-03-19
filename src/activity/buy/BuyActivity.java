package activity.buy;

import com.ebay.ebayfriend.R;
import com.ebay.ebayfriend.R.layout;
import com.ebay.ebayfriend.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class BuyActivity extends Activity {

	private LinearLayout ll = null;
	private LinearLayout gallery = null;
	private LayoutParams lp = null;
	private LayoutParams imglp = null;
	private TranslateAnimation rightAnimation;
	private TranslateAnimation leftAnimation;
	private TextView     pic_text = null;
	private TextView     description = null;
	
	private int num_pics = 3;
	private int pointer  = 0;
	private int res[]    = {
			R.drawable.b, R.drawable.a, R.drawable.c
	};
	private float down_x, up_x;
	
	private int screen_width = 720;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);

		ll = (LinearLayout) this.findViewById(R.id.test_layout);
		pic_text = (TextView) this.findViewById(R.id.pic_text);
		String init_num = "1/"+num_pics;
		pic_text.setText(init_num);
		
		description = (TextView) this.findViewById(R.id.description);
		description.setText("   " + description.getText());
		
		lp = new LinearLayout.LayoutParams(screen_width * num_pics, 450);
		lp.leftMargin = 0;
		lp.topMargin  = 0;
		
		gallery = new LinearLayout(this);
		gallery.setLayoutParams(lp);
		gallery.setOrientation(LinearLayout.HORIZONTAL);
		gallery.setBackgroundColor(Color.WHITE);
		
		imglp = new LinearLayout.LayoutParams(screen_width, LayoutParams.FILL_PARENT);
		imglp.leftMargin = 0;
		imglp.topMargin = 0;
		
		for(int i = 0; i < num_pics; i++){
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(imglp);
			iv.setImageResource(res[i]);
			gallery.addView(iv);
		}
		
		ll.addView(gallery);
		//ll.setBackgroundColor(Color.BLACK);
		ll.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//Log.d("touch event", "new touch event");
				String num = "";
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:{
					down_x = event.getX();
					break;
				}
				case MotionEvent.ACTION_OUTSIDE:
				case MotionEvent.ACTION_UP:{
					up_x = event.getX();
					if(up_x - down_x > 0){
						if(pointer > 0){
							rightAnimation = new TranslateAnimation(- pointer * screen_width, - (pointer - 1) * screen_width, 0, 0);
							rightAnimation.setDuration(500);
							rightAnimation.setFillAfter(true);
		
							// animate right
							gallery.startAnimation(rightAnimation);
							pointer --;
							num = pointer+1+"/"+num_pics;
							pic_text.setText(num);
						}
						
					}
					else if(up_x - down_x < 0){
						if(pointer < num_pics -1){
							leftAnimation = new TranslateAnimation(- pointer * screen_width, - (pointer + 1) * screen_width, 0, 0);
							leftAnimation.setDuration(500);
							leftAnimation.setFillAfter(true);
							
							// animate left
							gallery.startAnimation(leftAnimation);
							pointer ++;
							num = pointer+1+"/"+num_pics;
							pic_text.setText(num);
						}
					}
					break;
				}
				}
				return true;
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buy, menu);
		return true;
	}

}
