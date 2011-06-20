package ro.calin.crowler;

public class HypertextDocumentParserException extends Exception{
	private static final long serialVersionUID = 6847519761987478133L;

	public HypertextDocumentParserException() {
	}
	
	public HypertextDocumentParserException(String text) {
		super(text);
	}
	
	public HypertextDocumentParserException(Throwable cause) {
		super(cause);
	}
}
