package ro.calin.hmm;

import java.util.Scanner;

/**
 * @author Calin
 * 
 * INPUT:
 * 
 * {@link #states}
 * n
 * s1, ...., sn
 * 
 * {@link #A}
 * a11, ...., a1n
 * ..............
 * an1, ...., ann
 * 
 * {@link #symbols}
 * m
 * o1, ...., om
 * 
 * {@link #B}
 * b11, ...., b1m
 * ..............
 * bn1, ...., bnm
 *
 * {@link #PI}
 * p1, ...., pn
 */
public class DiscreteHMM extends AbstractHMM {
	
	protected String[] symbols;
	private double[][] B; //output probability matrix
	
	@Override
	protected void loadSymbolDistribution(Scanner source) 
		throws IllegalProbabilityDistributionException {
		
		int n = states.length;
		
		int m = source.nextInt();
		symbols = new String[m];
		for (int i = 0; i < symbols.length; i++) {
			symbols[i] = source.next();
		}
		
		B = new double[n][m];
		readProbDistrMatrix(B, source);
	}

	@Override
	protected double getStateSymbProbab(int state, double symb) {
		return B[state][(int)symb];
	}
	
	@Override
	public double generateNextObservation(int state) {
		return genIndFromProbDistr(B[state]);
	}

	@Override
	public String[] getSymbolSequence(double[] seq) {
		String[] strSeq = new String[seq.length];
		
		for (int i = 0; i < strSeq.length; i++) {
			strSeq[i] = symbols[(int)seq[i]];
		}
		
		return strSeq;
	}
}
