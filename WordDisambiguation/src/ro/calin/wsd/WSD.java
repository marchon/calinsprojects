package ro.calin.wsd;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLStreamException;

public class WSD {
	public static void main(String[] args) throws FileNotFoundException,
			XMLStreamException {

		final List<String> validRoles = Arrays.asList(new String[] { "FW",
				"NN", "NNP", "NNPS", "NNS", "CD", "MO", "VB", "VBD", "VBG",
				"VBN", "VBP", "VBZ", "RB", "RBR", "RBS", "RP", "JJ", "JJR",
				"JJS" });

		double avgFairness = 0;
		for (int i = 0; i < 10; i++) {
			final int itr = i;
			XMLSentenceFactory trainFactory = new XMLSentenceFactory(
					new FileReader("corpus/line1.xml"),
					new SentenceFactoryFilter() {
						@Override
						public boolean approveWord(Word word) {
							return validRoles.contains(word.getRole());
						}

						@Override
						public boolean approveSentence(Sentence sentence) {
							return true;
						}

						@Override
						public boolean approveId(int id) {
							return id % 10 != itr;
						}
					}, 6, 6);

			XMLSentenceFactory testFactory = new XMLSentenceFactory(
					new FileReader("corpus/line1.xml"),
					new SentenceFactoryFilter() {
						@Override
						public boolean approveWord(Word word) {
							return validRoles.contains(word.getRole());
						}

						@Override
						public boolean approveSentence(Sentence sentence) {
							return true;
						}

						@Override
						public boolean approveId(int id) {
							return id % 10 == itr;
						}
					}, 6, 6);

			WordDisambiguation wd = new WordDisambiguation();
			wd.train(trainFactory);
			double fairness = wd.disambiguate(testFactory);
			System.out.println("Fairness at " + i + ": " + fairness);
			avgFairness += fairness;
		}

		System.out.println("Average farirness: " + avgFairness);
	}
}
