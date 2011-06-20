package ro.calin.json;

import static ro.calin.Constants.JSON_RESULT_CONTENT_SEQUENCE;
import static ro.calin.Constants.JSON_RESULT_TITLE;
import static ro.calin.Constants.JSON_RESULT_URL;

public class HitContainer implements JSONable {
	private String title;
	private String url;
	private String contentSequence;

	public HitContainer() {
	}

	public HitContainer(String t, String u, String c) {
		title = t;
		url = u;
		contentSequence = c;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContentSequence() {
		return contentSequence;
	}

	public void setContentSequence(String contentSequence) {
		this.contentSequence = contentSequence;
	}

	@Override
	public String toJSONString() {
		StringBuilder sb = new StringBuilder();

		sb.append('{');
		sb.append('"');
		sb.append(JSON_RESULT_TITLE);
		sb.append('"');
		sb.append(':');
		sb.append('"');
		sb.append(title);
		sb.append('"');

		sb.append(',');

		sb.append('"');
		sb.append(JSON_RESULT_URL);
		sb.append('"');
		sb.append(':');
		sb.append('"');
		sb.append(url);
		sb.append('"');

		sb.append(',');

		sb.append('"');
		sb.append(JSON_RESULT_CONTENT_SEQUENCE);
		sb.append('"');
		sb.append(':');
		sb.append('"');
		sb.append(contentSequence);
		sb.append('"');

		sb.append('}');

		return sb.toString();
	}
}
