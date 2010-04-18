package ro.calin.wsd;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import ro.calin.thirdparty.Stemmer;

public class XMLSentenceFactory implements SentenceFactory {

	XMLInputFactory xmlInputFactory = (XMLInputFactory) XMLInputFactory
			.newInstance();
	XMLStreamReader xmlStreamReader = null;
	private SentenceFactoryFilter filter;

	private int sentenceId = 0;

	public XMLSentenceFactory(Reader xmlReader, SentenceFactoryFilter filter)
			throws XMLStreamException {
		this.xmlStreamReader = (XMLStreamReader) xmlInputFactory
				.createXMLStreamReader(xmlReader);
		this.filter = filter;

		if (!readUntilNextInstance()) {
			close();
		}

	}

	private int readUntilTypeAndNode(String nodeName, int... types) {
		try {
			int event;
			do {
				event = xmlStreamReader.next();
				for (int type : types) {
					if (event == type) {
						if (nodeName == null
								|| nodeName.equals(xmlStreamReader
										.getLocalName())) {
							return event;
						}
					} else if (event == XMLStreamConstants.END_DOCUMENT) {
						return 0;
					}
				}
			} while (true);
		} catch (XMLStreamException e) {
			e.printStackTrace(System.err);
			return 0;
		}
	}

	private boolean readUntilNode(String nodeName) {
		return 0 != readUntilTypeAndNode(nodeName,
				XMLStreamConstants.START_ELEMENT);
	}

	private boolean readUntilNextInstance() {
		return readUntilNode("instance");
	}

	private String getSense() throws XMLStreamException {
		if (!readUntilNode("answer"))
			throw new XMLStreamException("Illegal format.");

		return xmlStreamReader.getAttributeValue(null, "senseid");
	}

	/**
	 * @param word
	 * @return -1 - no more words; 0 - normal; 1 - head word
	 * @throws XMLStreamException
	 *             TODO: has issues; FIXME
	 */
	private int getNextWord(Word word) throws XMLStreamException {
		int res = 0;
		int event = readUntilTypeAndNode(null,
				XMLStreamConstants.START_ELEMENT,
				XMLStreamConstants.END_ELEMENT, XMLStreamConstants.CHARACTERS);

		if (event == XMLStreamConstants.START_ELEMENT) { // head
			res = 1;
			event = xmlStreamReader.next();
		} else if (event == XMLStreamConstants.END_ELEMENT) { // end context
			return -1;
		}

		if (event != XMLStreamConstants.CHARACTERS) {
			readUntilTypeAndNode(null, XMLStreamConstants.CHARACTERS);
			// throw new XMLStreamException("Illegal format.");
		}

		// stem it
		word.setValue(new Stemmer(xmlStreamReader.getText().trim()).stem()
				.toString());
		readUntilTypeAndNode("p", XMLStreamConstants.START_ELEMENT);
		word.setRole(xmlStreamReader.getAttributeValue(null, "s"));
		readUntilTypeAndNode("p", XMLStreamConstants.END_ELEMENT);
		// xmlStreamReader.next(); // end p

		if (res == 1) { // end head
			readUntilTypeAndNode("head", XMLStreamConstants.END_ELEMENT);
			// xmlStreamReader.next();
		}

		return res;
	}

	@Override
	public Sentence getNextSentence() {
		if (xmlStreamReader == null)
			return null;

		// filter sentences
		while (!filter.approveId(++sentenceId)) {
			if (!readUntilNextInstance()) {
				close();
				return null;
			}
		}

		try {
			Sentence sentence = null;
			do {
				List<Word> words = new ArrayList<Word>();
				Word headWord = null;
				String headWordSense = null;

				// construct the sentence
				headWordSense = getSense();

				if (!readUntilNode("context")) {
					close();
					return null;
				}

				Word word = new Word();
				int res;
				while ((res = getNextWord(word)) != -1) {
					words.add(word);

					if (res == 1) {
						headWord = word;
					}

					word = new Word();
				}

				if (!readUntilNextInstance()) {
					close();
				}

				sentence = new Sentence(words.toArray(new Word[words.size()]),
						headWord, headWordSense);
			} while (!filter.approveSentence(sentence));
			return sentence;
		} catch (XMLStreamException e) {
			close(e);
		}

		return null;
	}

	private void close(Exception e) {
		e.printStackTrace(System.err);
		close();
	}

	@Override
	public void close() {
		if (xmlStreamReader != null) {
			try {
				xmlStreamReader.close();
			} catch (XMLStreamException e) {
				e.printStackTrace(System.err);
			}
			xmlStreamReader = null;
		}
	}
}
