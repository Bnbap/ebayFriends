package activity.newsfeed;

public class NewsFeedItem {
	public static int STOP = 0;
	public static int PLAYING = 1;
	public static int PREPARE = 2;
	private String image;
	private String icon;
	private String name;
	private int playState = STOP;
	
	public NewsFeedItem(String image, String icon, String name) {
		this.image = image;
		this.icon = icon;
		this.name = name;
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
