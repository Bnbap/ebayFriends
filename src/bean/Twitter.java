package bean;

import java.util.Date;

public class Twitter {

	private long id;
	private Photo4Post photo;
	private Voice4Post voice;
	private String text;
	private Date postDate;
	private User postUser;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Photo4Post getPhoto() {
		return photo;
	}
	public void setPhoto(Photo4Post photo) {
		this.photo = photo;
	}
	public Voice4Post getVoice() {
		return voice;
	}
	public void setVoice(Voice4Post voice) {
		this.voice = voice;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getPostDate() {
		return postDate;
	}
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	public User getPostUser() {
		return postUser;
	}
	public void setPostUser(User postUser) {
		this.postUser = postUser;
	}
	
	
	
}
