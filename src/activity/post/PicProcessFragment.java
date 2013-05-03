package activity.post;

import bean.PurchasedItem;
import util.PicUtil;
import android.app.Fragment;
import android.graphics.Bitmap;

public class PicProcessFragment extends Fragment {

	protected Bitmap originBitmap,thumbPic;
	private PicUtil picUtil;
	private PurchasedItem pi;
	public PicProcessFragment(){
		
	}
	public void setPurchasedItem(PurchasedItem pi){
		this.pi=pi;
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
