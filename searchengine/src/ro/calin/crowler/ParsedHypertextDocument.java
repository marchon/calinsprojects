package ro.calin.crowler;

import java.util.List;

/**
 * Reprezinta un document parsat.
 * Crowler-ul extrage link-urile dintr-un document prin intermediul acestei metode.
 * Asadar trebuiesc formatate, validate, si eventual verificat ca nu au mai fost vizitate.
 * 
 * @author Calin
 */
public interface ParsedHypertextDocument {
	/**
	 * Metoda prin care Crawler-ul extrage link-urile dintr-un document.
	 * Poate intoarce null sau o lista vida daca nu exista link-uri.
	 * 
	 * @return lista de link-uri
	 */
	public List<String> getValidParsedLinks();
}
