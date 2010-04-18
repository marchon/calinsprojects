package ro.calin.wsd;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WordDisambiguation {
	/**
	 * c(word, sense) = weight
	 */
	private Map<String, Map<String, DoubleAcummulator>> learnedWordSenseWeights;
	private Map<String, DoubleAcummulator> learedSenseWeights;
	
	private static class DoubleAcummulator {
		private double value;
		private DoubleAcummulator(){
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
		learnedWordSenseWeights = new HashMap<String, Map<String,DoubleAcummulator>>();
		learedSenseWeights = new HashMap<String, DoubleAcummulator>();
	}
	
	private DoubleAcummulator getOrCreate(Map<String, DoubleAcummulator> map, String key) {
		DoubleAcummulator da = map.get(key);
		if(da == null) {
			da = new DoubleAcummulator();
			map.put(key, da);
		}
		
		return da;
	}
	
	public void learn(SentenceFactory sentenceFactory) {
		Sentence sentence;
		int occurences = 0;
		Map<String, DoubleAcummulator> wordSenseNorms = new HashMap<String, DoubleAcummulator>();
		
		while((sentence = sentenceFactory.getNextSentence()) != null) {
			occurences++;
			
			Word[] contextWords = sentence.getHeadWordCotext();
			
			DoubleAcummulator senseWeight = getOrCreate(learedSenseWeights, sentence.getHeadWordSense());
			senseWeight.add(1);
			
			for (Word word : contextWords) {
				Map<String, DoubleAcummulator> senseWeights = learnedWordSenseWeights.get(word);
				if(senseWeights == null) {
					senseWeights = new HashMap<String, DoubleAcummulator>();
					learnedWordSenseWeights.put(word.getValue(), senseWeights);
				}
				
				DoubleAcummulator weight = getOrCreate(senseWeights, sentence.getHeadWordSense());
				weight.add(1);
				
				DoubleAcummulator norm = getOrCreate(wordSenseNorms, sentence.getHeadWordSense());
				norm.add(1);
			}
		}
		
		//normalize stuff
		
		//first the sense weights by number of "to be disambiguated" word occurences
		for (DoubleAcummulator accum : learedSenseWeights.values()) {
			accum.normalize(occurences);
		}
		
		System.out.println(wordSenseNorms);
		//second, context<->sense mapping by the sum of all
		for (Map<String, DoubleAcummulator> senseWeights : learnedWordSenseWeights.values()) {
			Set<String> senses = senseWeights.keySet();
			
			for (String sense : senses) {
				senseWeights.get(sense).normalize(wordSenseNorms.get(sense).value);
			}
		}
	}
	
	public double disambiguate(SentenceFactory sentenceFactory) {
		double correct = 0;
		double total = 0;
		
		Sentence sentence;
		while((sentence = sentenceFactory.getNextSentence()) != null) {
			total++;
			
			double argmax = 0;
			String currentSense = null;
			
			Word[] words = sentence.getHeadWordCotext();
			
			Set<String> senses = learedSenseWeights.keySet();
			
			
			for (String sense : senses) {
				double bayesProb = learedSenseWeights.get(sense).value;
				
				for (Word word : words) {
					Map<String, DoubleAcummulator> tmp = learnedWordSenseWeights.get(word);
					if(tmp != null) {
						DoubleAcummulator da = tmp.get(sense);
						if(da != null)
							bayesProb += da.value;
					}
				}
				
				if(bayesProb > argmax) {
					argmax = bayesProb;
					currentSense = sense;
				}
			}
			
			if(sentence.getHeadWordSense().equals(currentSense)) {
				correct++;
			}
		}
		
		if(total == 0)
			return -1;
		
		return correct / total;
	}
	
	@Override
	public String toString() {
		return learnedWordSenseWeights.toString();
	}
}
