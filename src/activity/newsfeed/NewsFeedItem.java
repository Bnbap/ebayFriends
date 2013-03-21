package activity.newsfeed;

public class NewsFeedItem {
	private String image;
	private String icon;
	private String name;
	
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
}
