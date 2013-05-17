package activity.newsfeed;

import java.io.Serializable;

public class NewsFeedItem implements Serializable{
	private static final long serialVersionUID = 1L;
	public static int STOP = 0;
	public static int PLAYING = 1;
	public static int PREPARE = 2;
	public static int PAUSED = 3;
	private String image;
	private String icon;
	private String name;
	private String voice;
	private String comments;
	private String goodURL;
	private int playState = STOP;
	
	public NewsFeedItem(String image, String icon, String name, String voice, String comments, String goodURL) {
		this.image = image;
		this.icon = icon;
		this.name = name;
		this.voice = voice;
		this.comments = comments;
		this.goodURL = goodURL;
	}

	public String getGoodURL(){
		return goodURL;
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
	
	public String getComments(){
		return comments;
	}
	public void setImage(String image){
		this.image = image;
	}
	
	public void setPlayState(int state){
		this.playState = state;
	}
}
