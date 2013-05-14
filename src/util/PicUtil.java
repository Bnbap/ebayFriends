package util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.ebay.ebayfriend.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PicUtil {
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	public static DisplayImageOptions imageOption = null;
	public static DisplayImageOptions iconOption = null;
	public static AnimateFirstDisplayListener animateListener = null;

	public static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	public static DisplayImageOptions getMainImageOption() {
		if (imageOption == null) {
			imageOption = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.ic_stub)
					.showImageForEmptyUri(R.drawable.ic_empty)
					.showImageOnFail(R.drawable.ic_error).cacheInMemory()
					.cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
					.build();
		}
		return imageOption;
	}

	public static DisplayImageOptions getIconOption() {
		if (iconOption == null) {
			iconOption = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.ic_stub)
					.showImageForEmptyUri(R.drawable.ic_empty)
					.showImageOnFail(R.drawable.ic_error).cacheInMemory()
					.cacheOnDisc().displayer(new RoundedBitmapDisplayer(500))
					.build();
		}
		return iconOption;
	}

	public static AnimateFirstDisplayListener getAnimateListener() {
		if (animateListener == null) {
			animateListener = new AnimateFirstDisplayListener();
		}
		return animateListener;
	}
}
