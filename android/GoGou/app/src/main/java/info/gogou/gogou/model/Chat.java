package info.gogou.gogou.model;

import java.io.Serializable;

public class Chat extends Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String from;

	private String to;
	
	private String type;

    // Date and time of chat which has been posted
	private String postDate;

	private String content;

    private int layoutId;

	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

    public int getLayoutId() {
        return layoutId;
    }
    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ID: ")
                .append(getId())
                .append(" From: ")
                .append(from)
                .append(" To: ")
                .append(to)
                .append(" Type: ")
                .append(type)
                .append(" Post on: ")
                .append(postDate)
                .append("--")
                .append(content)
                .append(" Layout Id: ")
                .append(layoutId);
        return builder.toString();
    }
}
