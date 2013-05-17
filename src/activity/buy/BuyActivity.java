package activity.buy;

import java.util.ArrayList;
import java.util.HashMap;

import util.BuyUtil;
import util.PicUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebay.ebayfriend.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class BuyActivity extends Activity {

	private ImageLoadingListener animateFirstListener = new PicUtil.AnimateFirstDisplayListener();
	private GridView gv;
	private TextView description = null;
	private HashMap<String, Object> dataList = new HashMap<String, Object>();
	private ArrayList<String> urlList = new ArrayList<String>();

	private BuyHandler buyHandler;
	private String goodsId;
	private GridViewAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		
//		Intent intent = getIntent();
//		Bundle b = intent.getExtras();
//		goodsId = b.getString("goodsId");
		
		goodsId = "5194e0a57985f72200acda41";
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);
		description = (TextView) this.findViewById(R.id.description);
		description.setText("   " + description.getText());

		adapter = new GridViewAdapter();
		buyHandler = new BuyHandler();

		gv = (GridView) findViewById(R.id.buy_grid_view);
		gv.setAdapter(adapter);

		
		Thread thread = new LoadDataThread();
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int size = urlList.size();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int allWidth = (int) (110 * size * density);
		int itemWidth = (int) (100 * density);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				allWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		gv.setLayoutParams(layoutParams);
		gv.setColumnWidth(itemWidth);
		gv.setHorizontalSpacing(10);
		gv.setStretchMode(GridView.NO_STRETCH);
		gv.setNumColumns(size);

		// ll.setBackgroundColor(Color.BLACK);
	}
	class LoadDataThread extends Thread{
		@Override
		public void run(){
			dataList = BuyUtil.getGoodsInfo(goodsId);

			urlList = (ArrayList<String>) dataList.get("pictures");
			
			buyHandler.sendMessage(new Message());
			
		}
	}
	class BuyHandler extends Handler{
		public BuyHandler(){
			
		}
		public BuyHandler(Looper l){
			
		}
		@Override
		public void handleMessage(Message msg){
			gv.invalidate();
			adapter.notifyDataSetChanged();
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.buy, menu);
		return true;
	}

	class GridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public String getItem(int position) {
			return urlList.get(position - 1);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.buy_grid_view,
					null);
			ImageView iv = (ImageView) findViewById(R.id.buy_imageView);
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.ic_stub)
					.showImageForEmptyUri(R.drawable.ic_empty)
					.showImageOnFail(R.drawable.ic_error).cacheInMemory()
					.cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
					.build();
			String picUrl = urlList.get(position);
			ImageLoader.getInstance().displayImage(picUrl, iv, options,
					animateFirstListener);

			return convertView;
		}

	}

}
