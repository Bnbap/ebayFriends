package activity.newsfeed;

import java.io.IOException;
import java.util.Map;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.BaseAdapter;

public class SafeMediaPlayer extends MediaPlayer {
	public static final int STOP = 0;
	public static final int PLAYING = 1;
	public static final int PREPARE = 2;
	public static final int PAUSED = 3;
	
	private Map<Integer, PlayState> playStateMap;
	int usePosition = -1;
	PlayState playerState = new PlayState("");
	
	BaseAdapter adapter;
	
	public SafeMediaPlayer(Map<Integer, PlayState> playStateMap, BaseAdapter adapter) {
		this.playStateMap = playStateMap;
		this.adapter = adapter;
		this.setOnPreparedListener(new MediaPlayerPreparedListener(adapter));
		this.setOnCompletionListener(new MediaPlayerCompleteListener(adapter));
	}
	
	public void togglePlay(int clickItemIndex){
		PlayState clickItemPlayState = playStateMap.get(clickItemIndex);
		int clickState = clickItemPlayState.getState();
		
		if (usePosition != clickItemIndex){// Click different one
			// Last one not stop
			this.reset();
			if (usePosition != -1)
				playStateMap.get(usePosition).setState(STOP);
			// set Data source 
			try {
				this.setDataSource(playStateMap.get(clickItemIndex)
						.getAudioSource());
				Log.e("mediaPlayer", "set source: " + playStateMap.get(clickItemIndex)
						.getAudioSource());
			} catch (IllegalArgumentException e) {
				Log.e("MediaPlayer", "Illegal Argument");
			} catch (SecurityException e) {
				Log.e("MediaPlayer", "Security issue");
			} catch (IllegalStateException e) {
				Log.e("MediaPlayer", "Illegal State");
			} catch (IOException e) {
				Log.e("MediaPlayer", "IO issue");
			}
			this.prepareAsync();
			clickItemPlayState.setState(PREPARE);
			playerState.setState(PREPARE);
			usePosition = clickItemIndex;
		}
		else{// Click the same one
			switch (clickState){
			case PLAYING:
				this.pause();
				playerState.setState(PAUSED);
				clickItemPlayState.setState(PAUSED);
				break;
			case PAUSED:
				playerState.setState(PLAYING);
				clickItemPlayState.setState(PLAYING);
				break;
			case PREPARE:
				break;
			}
		}
	}
	
	private class MediaPlayerCompleteListener implements OnCompletionListener{
		private BaseAdapter adapter;

		public MediaPlayerCompleteListener(BaseAdapter adapter) {
			this.adapter = adapter;
		}
		@Override
		public void onCompletion(MediaPlayer mediaPlayer) {
			Log.e("mediaplayer", "complete");
			mediaPlayer.stop();
			mediaPlayer.reset();
			playStateMap.get(usePosition).setState(STOP);
			usePosition = -1;
			playerState.setState(STOP);
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
			Log.e("media", "prepared...");
			mp.start();
			playStateMap.get(usePosition).setState(PLAYING);
			playerState.setState(PLAYING);
			adapter.notifyDataSetChanged();
		}
	}
}
