package activity.newsfeed;

public class NewsFeedItem {
	public static int STOP = 0;
	public static int PLAYING = 1;
	public static int PREPARE = 2;
	public static int PAUSED = 3;
	private String image;
	private String icon;
	private String name;
	private String voice;
	private int playState = STOP;
	
	public NewsFeedItem(String image, String icon, String name, String voice) {
		this.image = image;
		this.icon = icon;
		this.name = name;
		this.voice = voice;
	}

	public String getImage() {
		return image;
	}

	public String getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}
	
	public String getVoice(){
		return voice;
	}
	public int getPlayState(){
		return playState;
	}
	public void setImage(String image){
		this.image = image;
	}
	
	public void setPlayState(int state){
		this.playState = state;
	}
}
