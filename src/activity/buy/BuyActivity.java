package activity.buy;

import java.util.ArrayList;
import java.util.HashMap;

import util.BuyUtil;
import util.PicUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	private TextView title;
	private ImageView buyBt;
	private PurchaseHandler purchaseHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		goodsId = b.getString("goodsId");
//		
//		goodsId = "5195ab587985f706f8f8abc9";
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);

		adapter = new GridViewAdapter();
		buyHandler = new BuyHandler();
		purchaseHandler = new PurchaseHandler();

		gv = (GridView) findViewById(R.id.buy_grid_view);
		gv.setAdapter(adapter);
		
		buyBt = (ImageView) findViewById(R.id.buy_buy);
		buyBt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Thread buyThread = new BuyThread(goodsId);
				buyThread.start();
				
			}
			
		});

		
		Thread thread = new LoadDataThread();
		thread.start();
//		try {
//			thread.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		

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
			int size = urlList.size();
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			float density = dm.density;
			int allWidth = (int) (310 * size * density);
			int itemWidth = (int) (300 * density);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					allWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
			gv.setLayoutParams(layoutParams);
			gv.setColumnWidth(itemWidth);
			gv.setHorizontalSpacing(10);
			gv.setStretchMode(GridView.NO_STRETCH);
			gv.setNumColumns(size);
			
			title = (TextView)BuyActivity.this.findViewById(R.id.buy_title);
			title.setText((String)dataList.get("name"));
			
			description = (TextView)BuyActivity.this.findViewById(R.id.description);
			description.setText((String)dataList.get("description"));
			
			gv.invalidate();
			title.invalidate();
			description.invalidate();
			adapter.notifyDataSetChanged();
			
			
		}
	}
	
	@SuppressLint("HandlerLeak")
	class PurchaseHandler extends Handler{
		public PurchaseHandler (){
			
		}
		public PurchaseHandler(Looper l){
			
		}
		@Override
		public void handleMessage(Message msg){
			Toast.makeText(getApplicationContext(), "Purchase Success", Toast.LENGTH_LONG).show();
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
			return urlList.size();
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
			ImageView iv = (ImageView) convertView.findViewById(R.id.buy_imageView);
			iv.setScaleType(ScaleType.CENTER_CROP);
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.ic_stub)
					.showImageForEmptyUri(R.drawable.ic_empty)
					.showImageOnFail(R.drawable.ic_error).cacheInMemory()
					.cacheOnDisc().displayer(new RoundedBitmapDisplayer(0))
					.build();
			String picUrl = urlList.get(position);
			ImageLoader.getInstance().displayImage(picUrl, iv, options,
					animateFirstListener);

			return convertView;
		}

	}
	class BuyThread extends Thread{
		private String goodsId;
		public BuyThread(String id){
			this.goodsId=id;
		}
		
		@Override
		public void run(){
			BuyUtil.buy(goodsId);
			Message msg = new Message();
			purchaseHandler.sendMessage(msg);
			
		}
	}

}
