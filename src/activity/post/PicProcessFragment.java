package activity.post;

import com.ebay.ebayfriend.R;

import bean.PurchasedItem;
import util.PicUtil;
import activity.post.AttachItemFragment.GetItemListThread;
import activity.post.AttachItemFragment.ItemListHandler;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class PicProcessFragment extends Fragment {

	protected Bitmap originBitmap,thumbPic;
	private PicUtil picUtil;
	private PurchasedItem pi;
	public PicProcessFragment(){
		Bundle b = getArguments();
		pi = (PurchasedItem)b.getSerializable("purchasedItem");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.process_pic, container, false);
		TextView windowTitleView = (TextView) getActivity().findViewById(
				R.id.window_title);
		windowTitleView.setText("Post");

		lv = (ListView) view.findViewById(R.id.purchased_list);

		return view;
	}
	
	public void setBitmap(Bitmap bitmap){
		this.originBitmap=bitmap;
	}
	public Bitmap getThumbPic(){
		return this.thumbPic;
	}
	
	public Bitmap getOriginBitmap(){
		return this.originBitmap;
	}
	
	public Bitmap getBitmapStyle1(){
		return null;
	}
	
	public Bitmap getBitmapStyle2(){
		return null;
	}
	public Bitmap getBitmapStyle3(){
		return null;
	}
}
