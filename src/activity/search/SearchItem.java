package activity.search;

public class SearchItem {
	private String name;
	private String portrait;
	private boolean ifFollow;
	
	public SearchItem(String name, String portrait, boolean ifFollow) {
		this.name = name;
		this.portrait = portrait;
		this.ifFollow = ifFollow;
	}
	
	public String getName() {
		return name;
	}
	public String getPortrait() {
		return portrait;
	}
	public boolean isFollow() {
		return ifFollow;
	}
	
	public void setIfFollow(boolean ifFollow){
		this.ifFollow = ifFollow;
	}
}
