package ro.calin.ubiquity;

public class ResponseContent {
	private boolean success;
	private String details;
	private Object content;

	public ResponseContent() {
		this(false, null, null);
	}

	public ResponseContent(boolean s, String d, Object c) {
		success = s;
		details = d;
		content = c;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

}
