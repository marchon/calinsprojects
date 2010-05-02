package ro.calin.hmm;

import java.util.Scanner;

public abstract class AbstractHMM implements HMM {
	protected String[] states;
	protected String[] symbols;

	protected double[][] A; // transition probability matrix
	protected double[] PI; // initial state distribution vector

	protected abstract void loadSymbolStateDistribution(Scanner source)
			throws IllegalProbabilityDistributionException;
	protected abstract double[] getSymbDistrForState(int state);
	
	@Override
	public HMM load(Scanner source)
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

			if (Math.abs(sum - 1.0) > 0.01)
				throw new IllegalProbabilityDistributionException(
						"Must add up to 1.");
		}

		// load prob distrib for symbols
		loadSymbolStateDistribution(source);

		PI = new double[n];
		double sum = 0.0;
		for (int i = 0; i < n; i++) {
			double p = source.nextDouble();
			sum += p;
			PI[i] = p;
		}

		if (Math.abs(sum - 1.0) > 0.01)
			throw new IllegalProbabilityDistributionException(
					"Must add up to 1.");

		return this;
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
			seq[i] = genIndFromProbDistr(getSymbDistrForState(currentState)); //generate obs for current state
			currentState = genIndFromProbDistr(A[currentState]); //generate next state
		}
		
		return seq;
	}
	
	private String[] seq(int[] seq, String[] seqNames) {
		String[] strSeq = new String[seq.length];
		
		for (int i = 0; i < strSeq.length; i++) {
			strSeq[i] = seqNames[seq[i]];
		}
		
		return strSeq;
	}
	
	@Override
	public String[] getSymbolSequence(int[] seq) {
		return seq(seq, symbols);
	}
	
	@Override
	public String[] getStateSequence(int[] seq) {
		return seq(seq, states);
	}

	@Override
	public double getForwordProbability(int[] observations) {
		//after iteration t alpha[i] represents the probability that
		//HMM is in state i and the generated sequence is o[0]o[1]...o[t]
		double[] alpha = new double[states.length];
		
		//init alpha
		for (int i = 0; i < alpha.length; i++) {
			alpha[i] = PI[i] * getSymbDistrForState(i)[observations[0]];
		}
		
		for (int t = 1; t < observations.length; t++) { // time/iteration t
			for (int j = 0; j < alpha.length; j++) { // calculate for each state j
				double sum = 0.0;
				for (int i = 0; i < alpha.length; i++) { // for each state i
					sum += (alpha[i] * A[i][j]);		 // A[i][j] represents the 
				}										 // probability to get from i to j
				
				alpha[j] = sum * getSymbDistrForState(j)[observations[t]];
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
		int T = observations.length;
		int N = states.length;
		
		double[][] vt = new double[T][N];
		int[][] bt = new int[T][N];
		
		//init
		for (int i = 0; i < N; i++) {
			vt[0][i] = PI[i] * getSymbDistrForState(i)[observations[0]];
			bt[0][i] = 0;
		}
		
		//induction
		for (int t = 1; t < T; t++) {
			for (int j = 0; j < N; j++) {
				double max = vt[t - 1][0] * A[0][j];
				int argmax = 0;
				
				for (int i = 1; i < N; i++) {
					double val = vt[t - 1][i] * A[i][j];
					
					if(val > max) {
						max = val;
						argmax = i;
					}
				}
				
				vt[t][j] = max * getSymbDistrForState(j)[observations[t]];
				bt[t][j] = argmax;
			}
		}
		
		//ending
		int st = 0;
		
		for(int i = 1; i < N; i++) {
			if(vt[T - 1][i] > vt[T - 1][st]) {
				st = i;
			}
		}
		
		//backtracking
		int[] stateSeq = new int[T];
		stateSeq[T - 1] = st;
		
		for(int t = T - 2; t >= 0; t--) {
			stateSeq[t] = bt[t + 1][stateSeq[t + 1]];
		}
		
		return stateSeq;
	}

}
