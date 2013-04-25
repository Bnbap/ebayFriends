package activity.post;

import util.PicUtil;
import android.app.Fragment;
import android.graphics.Bitmap;

public class PicPoccessFragment extends Fragment {

	protected Bitmap originBitmap,thumbPic;
	private PicUtil picUtil;
	public PicPoccessFragment(){
		
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
