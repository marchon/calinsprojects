package ro.calin.wsd;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Calin
 *
 * Assumes all sentences have the same head word.
 */
public class WordDisambiguation {
	/**
	 * c(word, sense) = weight
	 * 
	 * maps maps sense -> word -> weight
	 */
	private Map<String, Map<String, DoubleAcummulator>> learnedSenseWordWeights;
	private Map<String, DoubleAcummulator> learedSenseWeights;

	private static class DoubleAcummulator {
		private double value;

		private DoubleAcummulator() {
		}

		private void add(double d) {
			value += d;
		}

		private void normalize(double norm) {
			value = Math.log(value / norm);
		}

		@Override
		public String toString() {
			return Double.toString(value);
		}
	}

	public WordDisambiguation() {
		learnedSenseWordWeights = new HashMap<String, Map<String, DoubleAcummulator>>();
		learedSenseWeights = new HashMap<String, DoubleAcummulator>();
	}

	private DoubleAcummulator getOrCreate(Map<String, DoubleAcummulator> map,
			String key) {
		DoubleAcummulator da = map.get(key);
		if (da == null) {
			da = new DoubleAcummulator();
			map.put(key, da);
		}

		return da;
	}

	public void train(SentenceFactory sentenceFactory) {
		Sentence sentence;
		int occurences = 0;
		Map<String, DoubleAcummulator> wordSenseNorms = new HashMap<String, DoubleAcummulator>();

		while ((sentence = sentenceFactory.getNextSentence()) != null) {
			occurences++;

			Word[] contextWords = sentence.getHeadWordCotext();

			DoubleAcummulator senseWeight = getOrCreate(learedSenseWeights,
					sentence.getHeadWordSense());
			senseWeight.add(1);

			Map<String, DoubleAcummulator> wordWeights = learnedSenseWordWeights
					.get(sentence.getHeadWordSense());
			if (wordWeights == null) {
				wordWeights = new HashMap<String, DoubleAcummulator>();
				learnedSenseWordWeights.put(sentence.getHeadWordSense(),
						wordWeights);
			}
			for (Word word : contextWords) {
				DoubleAcummulator weight = getOrCreate(wordWeights, word
						.getValue());
				weight.add(1);
			}

			DoubleAcummulator norm = getOrCreate(wordSenseNorms, sentence
					.getHeadWordSense());
			norm.add(contextWords.length);
		}

		// normalize stuff

		// first the sense weights by number of "to be disambiguated" word
		// occurences
		for (DoubleAcummulator accum : learedSenseWeights.values()) {
			accum.normalize(occurences);
		}

		// second, context<->sense mapping by the sum of all
		for (String sense : learnedSenseWordWeights.keySet()) {
			Map<String, DoubleAcummulator> senseMap = learnedSenseWordWeights
					.get(sense);

			double senseNorm = wordSenseNorms.get(sense).value;

			for (DoubleAcummulator weight : senseMap.values()) {
				weight.normalize(senseNorm);
			}
		}
	}

	public double disambiguate(SentenceFactory sentenceFactory) {
		double correct = 0;
		double total = 0;

		Sentence sentence;
		while ((sentence = sentenceFactory.getNextSentence()) != null) {
			total++;

			double argmax = 0;
			String bestSense = null;

			Word[] words = sentence.getHeadWordCotext();

			if (words != null) {
				Set<String> senses = learedSenseWeights.keySet();
				for (String sense : senses) {
					double bayesProb = learedSenseWeights.get(sense).value;

					Map<String, DoubleAcummulator> wordWeights = learnedSenseWordWeights
							.get(sense);

					if (wordWeights != null) {
						for (Word word : words) {
							DoubleAcummulator da = wordWeights.get(word
									.getValue());
							if (da != null)
								bayesProb += da.value;
						}

						// log makes it negative
						if (bayesProb < argmax) {
							argmax = bayesProb;
							bestSense = sense;
						}
					}
				}

				if (sentence.getHeadWordSense().equals(bestSense)) {
					correct++;
				}
			}
		}

		if (total == 0)
			return -1;

		return correct / total;
	}

	@Override
	public String toString() {
		return learnedSenseWordWeights.toString() + "\n"
				+ learedSenseWeights.toString();
	}
}
