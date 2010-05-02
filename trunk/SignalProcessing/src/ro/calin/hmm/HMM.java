package ro.calin.hmm;

import java.util.Scanner;

/**
 * @author Calin
 *
 */
public interface HMM {
	HMM load(Scanner source) throws IllegalProbabilityDistributionException;
	String[] getSymbolSequence(double[] seq);
	String[] getStateSequence(int[] seq);
	double[] generateObservations(int seqLen);
	double getForwordProbability(double[] observations);
	int[] getViterbiStateSequence(double[] observations);
}
