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
 * {@link #symbols}
 * m
 * o1, ...., om
 * 
 * {@link #A}
 * a11, ...., a1n
 * ..............
 * an1, ...., ann
 * 
 * {@link #B}
 * b11, ...., b1m
 * ..............
 * bn1, ...., bnm
 *
 * {@link #PI}
 * p1, ...., pn
 */
public class DiscreteHMM implements HMM {
	private String[] states;
	private String[] symbols;
	
	private double[][] A; //transition probability matrix
	private double[][] B; //output probability matrix
	private double[] PI;  //initial state distribution vector
	
	public DiscreteHMM(Scanner source) 
		throws IllegalProbabilityDistributionException {
		
		int n = source.nextInt();
		states = new String[n];
		for (int i = 0; i < states.length; i++) {
			states[i] = source.next();
		}
		
		int m = source.nextInt();
		symbols = new String[m];
		for (int i = 0; i < symbols.length; i++) {
			symbols[i] = source.next();
		}
		
		A = new double[n][n];
		for (int i = 0; i < n; i++) {
			double sum = 0.0;
			for (int j = 0; j < n; j++) {
				double p = source.nextDouble();
				sum += p;
				A[i][j] = p;
			}
			
			if(Math.abs(sum - 1.0) > 0.01)
				throw new IllegalProbabilityDistributionException("Must add up to 1.");
		}
		
		B = new double[n][m];
		for (int i = 0; i < n; i++) {
			double sum = 0.0;
			for (int j = 0; j < m; j++) {
				double p = source.nextDouble();
				sum += p;
				B[i][j] = p;
			}
			
			if(Math.abs(sum - 1.0) > 0.01)
				throw new IllegalProbabilityDistributionException("Must add up to 1.");
		}
		
		PI = new double[n];
		double sum = 0.0;
		for (int i = 0; i < n; i++) {
			double p = source.nextDouble();
			sum += p;
			PI[i] = p;
		}
		
		if(Math.abs(sum - 1.0) > 0.01)
			throw new IllegalProbabilityDistributionException("Must add up to 1.");
	}

	private int genIndFromProbDistr(double[] distr) {
		double rand = Math.random();
	
		double from = 0;
		
		for (int i = 0; i < distr.length; i++) {
			double to = from + distr[i];
			
			if(from <= rand && rand < to)
				return i;
			
			from = to;
		}
		
		return -1;
	}
	
	@Override
	public int[] generateObservations(int seqLen) {
		int currentState = genIndFromProbDistr(PI); //generate init state
		
		int[] seq = new int[seqLen];
		
		for (int i = 0; i < seqLen; i++) {
			seq[i] = genIndFromProbDistr(B[currentState]); //generate obs for current state
			currentState = genIndFromProbDistr(A[currentState]); //generate next state
		}
		
		return seq;
	}
	
	@Override
	public String[] getSymbolSequence(int[] seq) {
		String[] strSeq = new String[seq.length];
		
		for (int i = 0; i < strSeq.length; i++) {
			strSeq[i] = symbols[seq[i]];
		}
		
		return strSeq;
	}

	@Override
	public double getForwordProbability(int[] observations) {
		//after iteration t alpha[i] represents the probability that
		//HMM is in state i and the generated sequence is o[0]o[1]...o[t]
		double[] alpha = new double[states.length];
		
		//init alpha
		for (int i = 0; i < alpha.length; i++) {
			alpha[i] = PI[i] * B[i][observations[0]];
		}
		
		for (int t = 1; t < observations.length; t++) { // time/iteration t
			for (int j = 0; j < alpha.length; j++) { // calculate for each state j
				double sum = 0.0;
				for (int i = 0; i < alpha.length; i++) { // for each state i
					sum += (alpha[i] * A[i][j]);		 // A[i][j] represents the 
				}										 // probability to get from i to j
				
				alpha[j] = sum * B[j][observations[t]];
			}
		}
		
		double forword = 0.0;
		for (int i = 0; i < alpha.length; i++) {
			forword += alpha[i];
		}
		
		return forword;
	}

	@Override
	public int[] getViterbiStateSequence(int[] observations) {
		// TODO Auto-generated method stub
		return null;
	}

}
