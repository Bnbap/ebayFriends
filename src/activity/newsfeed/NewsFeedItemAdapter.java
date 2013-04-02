package activity.newsfeed;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebay.ebayfriend.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class NewsFeedItemAdapter extends BaseAdapter {

	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private List<NewsFeedItem> newsFeedList = null;
	private Activity activity;
	private ImageLoader imageLoader;
	// play music para
	static final String AUDIO_PATH = "http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3";
	private MediaPlayer mediaPlayer;
	private int mediaFileLengthInMilliseconds;

	public NewsFeedItemAdapter(Activity activity, List<NewsFeedItem> newsFeedList, ImageLoader imageLoader) {
		this.newsFeedList = newsFeedList;
		this.activity = activity;
		this.imageLoader = imageLoader;

		// init play config
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
			}
		});
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
				if (!mediaPlayer.isPlaying()) {
					mediaPlayer.start();
				} else {
					mediaPlayer.pause();
				}
			}
		});
	}

	private class ViewHolder {
		public TextView name;
		public ImageView image;
		public ImageView icon;
		public Button playButton;
	}

	public List<NewsFeedItem> getItemList() {
		return newsFeedList;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		NewsFeedItem currentNewsFeed = newsFeedList.get(position);
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			view = activity.getLayoutInflater().inflate(R.layout.newsfeeditem, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.image = (ImageView) view.findViewById(R.id.image);
			holder.icon = (ImageView) view.findViewById(R.id.icon);
			holder.playButton = (Button) view.findViewById(R.id.play);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.name.setText(currentNewsFeed.getName());

		// set play config
		holder.playButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mediaPlayer.setDataSource(AUDIO_PATH);
					mediaPlayer.prepareAsync();
					holder.playButton.setText("||");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_error).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(20)).build();
		imageLoader.displayImage(currentNewsFeed.getImage(), holder.image, options, animateFirstListener);
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_error).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(500)).build();
		imageLoader.displayImage(currentNewsFeed.getIcon(), holder.icon, options, animateFirstListener);

		return view;
	}

	@Override
	public int getCount() {
		return newsFeedList.size();
	}

	@Override
	public Object getItem(int position) {
		return newsFeedList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
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

}
