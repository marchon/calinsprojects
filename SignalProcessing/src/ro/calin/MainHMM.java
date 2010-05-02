package ro.calin;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

import ro.calin.hmm.DiscreteHMM;
import ro.calin.hmm.IllegalProbabilityDistributionException;

/**
 * @author Calin
 * 
 */
public class MainHMM {

	/**
	 * @param args
	 * @throws IllegalProbabilityDistributionException
	 */
	public static void main(String[] args)
			throws IllegalProbabilityDistributionException {
		// force parse doubles in format dd.dd not dd,dd
		Locale.setDefault(Locale.ENGLISH);

		Scanner sc = new Scanner(MainHMM.class.getResourceAsStream("/res/dhmm.txt"));

		int hmmNum = sc.nextInt();
		DiscreteHMM[] dhmms = new DiscreteHMM[hmmNum];

		for (int i = 0; i < dhmms.length; i++) {
			dhmms[i] = new DiscreteHMM(sc);
		}

		// pick a random hmm to generate sequence from
		int rhmm = (int) Math.round(Math.random() * (hmmNum - 1));
		int[] seq = dhmms[rhmm].generateObservations(10);

		System.out.println("Generated sequence from hmm" + (rhmm + 1) +" is: "
				+ Arrays.asList(dhmms[rhmm].getSymbolSequence(seq)));

		//calculate forward probability for each hmm
		for (int i = 0; i < dhmms.length; i++) {
			double prob = dhmms[i].getForwordProbability(seq);
			System.out.println("Forward probability for hmm" + (i + 1) + ": " + prob);
		}
		
		sc.close();
	}

}
