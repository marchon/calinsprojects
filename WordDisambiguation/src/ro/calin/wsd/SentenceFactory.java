package ro.calin.wsd;

public interface SentenceFactory {
	Sentence getNextSentence();
	void close();
}
