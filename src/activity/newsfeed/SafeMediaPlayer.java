package activity.newsfeed;

import java.io.IOException;
import java.util.List;

import android.media.MediaPlayer;

public class SafeMediaPlayer extends MediaPlayer {
	public static int STOP = 0;
	public static int PLAYING = 1;
	public static int PREPARE = 2;
	public static int PAUSED = 3;
	
	private int currentItemIndex = -1;
	private List<NewsFeedItem> newsFeedList;
	private int currentState = STOP;
	
	public SafeMediaPlayer(List<NewsFeedItem> newFeedList) {
		this.newsFeedList = newFeedList;
	}
	public void togglePlay(int clickItemIndex) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		if (currentItemIndex != clickItemIndex){//Click the differnet one
			if (currentItemIndex != -1){//The last item has not stopped
				this.reset();
			}
			// set Datasource
			this.setDataSource(newsFeedList.get(clickItemIndex).getVoice());
			this.prepareAsync();
			currentItemIndex = clickItemIndex;
		}
		else{// Click the same item
			if(this.isPlaying()){
				this.pause();
				currentState = SafeMediaPlayer.PAUSED;
			}
			else if(currentState == NewsFeedItem.PAUSED){
				this.start();
				currentState = SafeMediaPlayer.PLAYING;
			}
		}
	}
}
