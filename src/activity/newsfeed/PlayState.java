package activity.newsfeed;

public class PlayState {
	public static int STOP = 0;
	public static int PLAYING = 1;
	public static int PREPARE = 2;
	public static int PAUSED = 3;
	
	private String audioSource;
	private int state;
	
	public PlayState(String audioSource) {
		this.audioSource = audioSource;
	}
	
	public void setState(int state){
		this.state = state;
	}

	public String getAudioSource() {
		return audioSource;
	}

	public int getState() {
		return state;
	}
}
