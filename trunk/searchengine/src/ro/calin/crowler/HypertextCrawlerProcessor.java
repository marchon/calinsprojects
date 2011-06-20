package ro.calin.crowler;

/**
 * Interfata ce trebuie implementata si pasata crawler-ului inainte de a porni
 * procesul de crawl-ing pentru a specifica comportamentul acestuia.
 * Metoda va fi apelata de pe thread-uri diferite, asadar daca va contine
 * cod care nu este thread-safe, este necesara implementarea unui mecanism de sincronizare.
 * 
 * @author Calin
 * @version %I%,%G%
 */
public interface HypertextCrawlerProcessor {
	/**
	 * Metoda apelata dupa ce dupa ce continutul unui raspuns este parsat.
	 * 
	 * @param document	Documentul parsat
	 */
	public void handleParsedDocument(ParsedHypertextDocument document);
	public void handleFinishedJob();
}
