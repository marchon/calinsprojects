package ro.calin.crowler;


/**
 * Interfata ce trebuie implementata de un parser ce va fi pasat Crawler-ului.
 * Daca metoda contine operatii sensibile la multi-threading ar trebui sincronizata.
 * 
 * @author Calin
 */
public interface HypertextDocumentParser {
	/**
	 * Metoda ce intoarce un document parsat. Cand s-a ajuns la depth-ul maxim
	 * aceasta metoda va fi apelata cu al doilea parametru false.
	 * 
	 * @param connection
	 * @param parseLinks
	 * @return un document parsat.
	 * @throws HypertextDocumentParserException
	 */
	public ParsedHypertextDocument parse(String url, boolean parseLinks) throws HypertextDocumentParserException;
}
