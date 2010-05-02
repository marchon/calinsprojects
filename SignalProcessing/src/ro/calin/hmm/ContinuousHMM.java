package ro.calin.hmm;

import java.util.Scanner;

/**
 * @author Calin
 * 
 *         INPUT:
 * 
 *         {@link #states} n s1, ...., sn
 * 
 *         {@link #A} a11, ...., a1n .............. an1, ...., ann
 * 
 * 		   {@link #M} m
 * 
 *         {@link #bPk} b11, ...., b1m .............. bn1, ...., bnm
 * 
 *         {@link #bMean} b11, ...., b1m .............. bn1, ...., bnm
 * 
 *         {@link #bDisp} b11, ...., b1m .............. bn1, ...., bnm
 * 
 *         {@link #PI} p1, ...., pn
 */
public class ContinuousHMM extends AbstractHMM {

	private int M;
	private double[][] bPk;
	private double[][] bMean;
	private double[][] bDisp;

	private void readMatrix(double[][] matrix, Scanner sc) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = sc.nextDouble();
			}
		}
	}

	@Override
	protected void loadSymbolDistribution(Scanner source)
			throws IllegalProbabilityDistributionException {
		int n = states.length;
		
		M = source.nextInt();
		
		bPk = new double[n][M];
		readProbDistrMatrix(bPk, source);

		bMean = new double[n][M];
		readMatrix(bMean, source);

		bDisp = new double[n][M];
		readMatrix(bDisp, source);
	}
	
	/**
	 * http://www.protonfish.com/random.shtml
	 * ???
	 * hope this is ok
	 * 
	 * @param mean
	 * @param deviation
	 * @return
	 */
	private double gaussianRandom(double mean, double deviation) {
		//gen normal distr random number (-3, 3)
		double rand = rand() + rand() + rand();
		
		//multiply by deviation and add mean
		return rand * deviation + mean;
	}
	
	private double rand() {
		return (Math.random() * 2 - 1);
	}

	@Override
	public double generateNextObservation(int state) {
		int obsInd = genIndFromProbDistr(bPk[state]);
		return gaussianRandom(bMean[state][obsInd], bDisp[state][obsInd]);
	}

	/**
	 * http://en.wikipedia.org/wiki/Probability_density_function
	 * http://en.wikipedia.org/wiki/Gaussian_density
	 * 
	 * @param x
	 * @param mean
	 * @param dev
	 * @return
	 */
	private double pdf(double x, double mean, double dev) {
		double f, nr, exp;
		nr = 1 / (dev * Math.sqrt(2 * Math.PI));
		exp = ((-1) * (x - mean) * (x - mean)) / (2 * dev * dev);

		f = nr * Math.pow(Math.E, exp);
		return f;
	}

	@Override
	protected double getStateSymbProbab(int state, double symb) {
		double bx = 0.0, rep;
		int i;

		for (i = 0; i < M; i++) {
			rep = pdf(symb, bMean[state][i], bDisp[state][i]);
			bx += (bPk[state][i] * rep);
		}
		
		return bx;
	}

	@Override
	public String[] getSymbolSequence(double[] seq) {
		String[] res = new String[seq.length];
		for (int i = 0; i < seq.length; i++) {
			res[i] = String.valueOf(seq[i]);
		}

		return res;
	}
}
