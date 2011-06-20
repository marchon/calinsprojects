package ro.calin.indexer;

import java.util.List;

import ro.calin.crowler.ParsedHypertextDocument;

public class ParsedWebPage implements ParsedHypertextDocument {
	/**
	 * Url-ul paginii.
	 */
	private String url;
	/**
	 * Titlul paginii.
	 */
	private String title;
	/**
	 * Textul continut in pagina.
	 */
	private String stringContent;

	/**
	 * Tipul continutului. Ar trebui sa inceapa cu text/...
	 */
	private String contentType;

	/**
	 * Link-urile continute in pagina.
	 */
	private List<String> links = null;

	public List<String> getLinks() {
		return links;
	}

	public List<String> getValidParsedLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStringContent() {
		return stringContent;
	}

	public void setStringContent(String stringContent) {
		this.stringContent = stringContent;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
