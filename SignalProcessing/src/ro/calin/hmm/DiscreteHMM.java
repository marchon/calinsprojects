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
public class DiscreteHMM extends AbstractHMM {
	
	private double[][] B; //output probability matrix
	
	@Override
	protected void loadSymbolStateDistribution(Scanner source) 
		throws IllegalProbabilityDistributionException {
		
		int n = states.length;
		int m = symbols.length;
		
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
		
	}

	@Override
	protected double[] getSymbDistrForState(int state) {
		return B[state];
	}
}
