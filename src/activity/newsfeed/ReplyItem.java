package activity.newsfeed;

public class ReplyItem {
	private int icon;
	private String replyContent;
	private String audioReplyURL;
	
	public ReplyItem(int icon, String replyContent, String audioReplyURL) {
		this.icon = icon;
		this.replyContent = replyContent;
		this.audioReplyURL = audioReplyURL;
	}

	public int getIcon() {
		return icon;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public String getAudioReplyURL() {
		return audioReplyURL;
	}
	
}
