package activity.newsfeed;

public class ReplyItem {
	private String icon;
	private String replyName;
	private String replyContent;
	private String audioReplyURL;
	
	public ReplyItem(String replyName, String icon, String replyContent, String audioReplyURL) {
		this.replyName = replyName;
		this.icon = icon;
		this.replyContent = replyContent;
		this.audioReplyURL = audioReplyURL;
	}

	public String getIcon() {
		return icon;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public String getAudioReplyURL() {
		return audioReplyURL;
	}
	
	public String getReplyName() {
		return replyName;
	}
}
