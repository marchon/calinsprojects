package ro.genetic.algo.ipd;

import ro.genetic.algo.Chromosome;
import ro.genetic.algo.Individual;
import static ro.genetic.algo.ipd.PDConstants.*;

/**
 * @author Calin
 *
 * We don't need to encode/decode since the individuals
 * are sequences of 0 and 1's
 */
public class PDIndividual implements Individual {
	private Chromosome chromosome;
	
	/**
	 * cache evaluation result until chromosome is changed
	 */
	private int score = -1;
	
	@Override
	public void decode(Chromosome chromosome) {
		this.chromosome = chromosome;
		score = -1;
	}

	@Override
	public Chromosome encode() {
		return chromosome;
	}
	
	private int eval() {
		if(chromosome == null)
			return 0;
		
		int s1 = 0;
		int s2 = 0;

		// merged sequence of the 2 player actions
		// assumed to be even length
		byte[] pdSequence = chromosome.getInformation();

		for (int i = 0; i < pdSequence.length; i += 2) {
			s1 += IPD_SCORE_MATRIX[pdSequence[i]][pdSequence[i + 1]];
			s2 += IPD_SCORE_MATRIX[pdSequence[i + 1]][pdSequence[i]];
		}

		return s1 + s2;
	}

	@Override
	public int evaluate() {
		if(score == -1)
			score = eval();
		
		return score;
	}
	
	@Override
	public String toString() {
		byte[] pdSequence = chromosome.getInformation();
		StringBuilder p1 = new StringBuilder();
		StringBuilder p2 = new StringBuilder();
		String[] ops = {"d", "c"};
		for (int i = 0; i < pdSequence.length; i+=2) {
			p1.append(ops[pdSequence[i]]);
			p2.append(ops[pdSequence[i + 1]]);
		}
		
		return "{score=" + evaluate() + "\np1: " + p1.toString() + "\np2: "
				+ p2.toString() + "}";
	}
}
