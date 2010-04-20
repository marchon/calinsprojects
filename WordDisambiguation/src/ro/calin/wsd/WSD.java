package ro.calin.wsd;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLStreamException;

public class WSD {
	private static final List<String> validRoles = Arrays
			.asList(new String[] { "FW", "NN", "NNP", "NNPS", "NNS", "CD",
					"MO", "VB", "VBD", "VBG", "VBN", "VBP", "VBZ", "RB", "RBR",
					"RBS", "RP", "JJ", "JJR", "JJS" });

	private static class SentenceFactoryFilterImpl implements
			SentenceFactoryFilter {
		private int mod;
		private int id;
		private boolean allButThis;

		public SentenceFactoryFilterImpl(int mod, int id, boolean allButThis) {
			this.mod = mod;
			this.id = id;
			this.allButThis = allButThis;
		}

		@Override
		public boolean approveId(int id) {
			return allButThis ? this.id != (id % mod) : this.id == (id % mod);
		}

		@Override
		public boolean approveSentence(Sentence sentence) {
			return true;
		}

		@Override
		public boolean approveWord(Word word) {
			return validRoles.contains(word.getRole());
		}
	}
	
	
	

	private static Object[][] tests = new Object[][] {
	/* {name, corpus, nb_itr, filter, window_len} */
		{ "hard", "corpus/hard.xml", 10, "ro.calin.wsd.WSD$SentenceFactoryFilterImpl", 5 },
		{ "line", "corpus/line.xml", 10, "ro.calin.wsd.WSD$SentenceFactoryFilterImpl", 5 },
		{ "interest", "corpus/interest.xml", 10, "ro.calin.wsd.WSD$SentenceFactoryFilterImpl", 5 },
		{ "serve", "corpus/serve.xml", 10, "ro.calin.wsd.WSD$SentenceFactoryFilterImpl", 5 }
	};
	
	
	

	public static void main(String[] args) throws FileNotFoundException,
			XMLStreamException, SecurityException, ClassNotFoundException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {

		for (int i = 0; i < tests.length; i++) {
			Object[] test = tests[i];
			System.out.println("Performing test: " + test[0] + " using corpus "
					+ test[1]);
			int itrs = (Integer) test[2];
			int wlen = (Integer) test[4] / 2;

			Constructor<SentenceFactoryFilter> ctr = (Constructor<SentenceFactoryFilter>) Class
					.forName(test[3].toString()).getConstructors()[0];

			//train and test itrs times
			double avgFairness = 0;
			for (int itr = 0; itr < itrs; itr++) {
				XMLSentenceFactory trainFactory = new XMLSentenceFactory(
						new FileReader((String) test[1]), ctr.newInstance(itrs, itr,
								true), wlen, wlen);
				XMLSentenceFactory testFactory = new XMLSentenceFactory(
						new FileReader((String) test[1]), ctr.newInstance(itrs, itr,
								false), wlen, wlen);
				
				WordDisambiguation wd = new WordDisambiguation();
				wd.train(trainFactory);
				double fairness = wd.disambiguate(testFactory);
				System.out.println("Fairness at " + (itr + 1) + ": " + fairness);
				avgFairness += fairness;
			}
			
			System.out.println("Average farirness: " + avgFairness / itrs);
		}
	}
}
