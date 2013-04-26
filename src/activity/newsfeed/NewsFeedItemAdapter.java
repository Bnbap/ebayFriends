package activity.newsfeed;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ebay.ebayfriend.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

@SuppressLint("NewApi")
public class NewsFeedItemAdapter extends BaseAdapter {

	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private List<NewsFeedItem> newsFeedList = null;
	private Activity activity;
	private ImageLoader imageLoader;
	// play music para
	static final String AUDIO_PATH = "http://rainite.com/young4you.mp3";
	private MediaPlayer mediaPlayer;
	// Indicate the current mediaPlayer in the list position.
	private int preparePosition = -1;
	private ListView lv;
	private OnClickListener playListener;

	private class PlayButtonListener implements OnClickListener{
		BaseAdapter adapter;
		public PlayButtonListener(BaseAdapter adapter) {
			this.adapter = adapter;
		}
		
		@Override
		public void onClick(View v) {
			try {
				int currentPosition = lv.getPositionForView(v) - 1;
				NewsFeedItem currentItem = newsFeedList.get(currentPosition);
				if(currentPosition != preparePosition){//Different one
					if(preparePosition != -1){// last not stop
						mediaPlayer.reset();
						newsFeedList.get(preparePosition).setPlayState(NewsFeedItem.STOP);
					}
					mediaPlayer.setDataSource(newsFeedList.get(currentPosition).getVoice());
				    mediaPlayer.prepareAsync();
				    currentItem.setPlayState(NewsFeedItem.PREPARE);
				    preparePosition = currentPosition;
				}
				else{// The same one, prepared
					int currentState = currentItem.getPlayState();
					if(mediaPlayer.isPlaying()){
						mediaPlayer.pause();	
						currentItem.setPlayState(NewsFeedItem.PAUSED);
						adapter.notifyDataSetChanged();
					}
					else if(currentState == NewsFeedItem.PAUSED){
						mediaPlayer.start();
						currentItem.setPlayState(NewsFeedItem.PLAYING);
						adapter.notifyDataSetChanged();
					}	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	
	private class MediaPlayerCompleteListener implements OnCompletionListener{
		private BaseAdapter adapter;

		public MediaPlayerCompleteListener(BaseAdapter adapter) {
			this.adapter = adapter;
		}
		@Override
		public void onCompletion(MediaPlayer arg0) {
			newsFeedList.get(preparePosition).setPlayState(NewsFeedItem.STOP);
			preparePosition = -1;
			adapter.notifyDataSetChanged();
		}	
	}
	
	private class MediaPlayerPreparedListener implements OnPreparedListener{
		private BaseAdapter adapter;
		public MediaPlayerPreparedListener(BaseAdapter adapter) {
			this.adapter = adapter;
		}
		@Override
		public void onPrepared(MediaPlayer mp) {
			mp.start();
			newsFeedList.get(preparePosition).setPlayState(NewsFeedItem.PLAYING);
			adapter.notifyDataSetChanged();
		}
	}
	
	public NewsFeedItemAdapter(Activity activity,
		 final List<NewsFeedItem> newsFeedList, ImageLoader imageLoader, final ListView lv) {
		this.newsFeedList = newsFeedList;
		this.activity = activity;
		this.imageLoader = imageLoader;
		this.lv = lv;

		// init play config
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnCompletionListener(new MediaPlayerCompleteListener(this));
		mediaPlayer.setOnPreparedListener(new MediaPlayerPreparedListener(this));
		playListener = new PlayButtonListener(this);
	}

	private class ViewHolder {
		public TextView name;
		public ImageView image;
		public ImageView icon;
		public ImageButton playButton;
	}

	public List<NewsFeedItem> getItemList() {
		return newsFeedList;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		NewsFeedItem currentNewsFeed = newsFeedList.get(position);
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			view = activity.getLayoutInflater().inflate(R.layout.newsfeeditem,
					parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.image = (ImageView) view.findViewById(R.id.image);
			holder.icon = (ImageView) view.findViewById(R.id.icon);
			holder.playButton = (ImageButton) view.findViewById(R.id.play);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.name.setText(currentNewsFeed.getName());
		int playState = currentNewsFeed.getPlayState();
		if(playState == NewsFeedItem.PLAYING){
			holder.playButton.setImageResource(R.drawable.pausebutton);
		}
		else{
			holder.playButton.setImageResource(R.drawable.playbutton);
		}

		// set play config
		holder.playButton.setOnClickListener(playListener);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
				.build();
		imageLoader.displayImage(currentNewsFeed.getImage(), holder.image,
				options, animateFirstListener);
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(500))
				.build();
		imageLoader.displayImage(currentNewsFeed.getIcon(), holder.icon,
				options, animateFirstListener);

		return view;
	}
	
	public void updateList(List<NewsFeedItem> newsFeedList){
		this.newsFeedList = newsFeedList;
		this.notifyDataSetChanged();
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

	private static class AnimateFirstDisplayListener extends
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
}
