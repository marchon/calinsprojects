package ro.genetic.algo.ipd;

import ro.genetic.algo.Chromosome;
import ro.genetic.algo.Individual;
import ro.genetic.algo.IndividualGenerator;
import static ro.genetic.algo.ipd.PDConstants.*;

public class PDIndividualGenerator implements IndividualGenerator {
	
	private byte[] generateRandomBinarySequence() {
		byte[] b = new byte[IPD_GAME_LEN];

		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) (Math.random() < 0.5 ? 0 : 1);
		}

		return b;
	}
	@Override
	public Individual[] generateIndividuals() {
		Individual[] individuals = new Individual[IPD_START_POP_LEN];
		
		for (int i = 0; i < individuals.length; i++) {
			individuals[i] = new PDIndividual();
			
			individuals[i].decode(new Chromosome(generateRandomBinarySequence()));
		}
		
		return individuals;
	}

}
