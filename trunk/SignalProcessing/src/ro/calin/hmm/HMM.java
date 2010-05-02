package ro.calin.hmm;

/**
 * @author Calin
 *
 */
public interface HMM {
	String[] getSymbolSequence(int[] seq);
	int[] generateObservations(int seqLen);
	double getForwordProbability(int[] observations);
	int[] getViterbiStateSequence(int[] observations);
}
