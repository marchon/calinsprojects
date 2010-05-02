package ro.calin.hmm;

import java.util.Scanner;

/**
 * @author Calin
 *
 */
public interface HMM {
	HMM load(Scanner source) throws IllegalProbabilityDistributionException;
	String[] getSymbolSequence(int[] seq);
	String[] getStateSequence(int[] seq);
	int[] generateObservations(int seqLen);
	double getForwordProbability(int[] observations);
	int[] getViterbiStateSequence(int[] observations);
}
