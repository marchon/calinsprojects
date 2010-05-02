package ro.calin;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

import ro.calin.hmm.DiscreteHMM;
import ro.calin.hmm.HMM;
import ro.calin.hmm.IllegalProbabilityDistributionException;

/**
 * @author Calin
 * 
 */
public class MainHMM {
	
	public static void testHMM(Class<? extends HMM> type, InputStream is) throws IllegalProbabilityDistributionException, InstantiationException, IllegalAccessException {
		Scanner sc = new Scanner(is);

		int hmmNum = sc.nextInt();
		HMM[] dhmms = new HMM[hmmNum];

		for (int i = 0; i < dhmms.length; i++) {
			dhmms[i] = type.newInstance().load(sc);
		}

		// pick a random hmm to generate sequence from
		int rhmm = (int) Math.round(Math.random() * (hmmNum - 1));
		int[] seq = dhmms[rhmm].generateObservations(15);

		System.out.println("Generated observation sequence from HMM " + (rhmm + 1) + " is: "
				+ Arrays.asList(dhmms[rhmm].getSymbolSequence(seq)));
		System.out.println("------------------------------------------------");
		System.out.println();

		// calculate forward probability for each hmm
		for (int i = 0; i < dhmms.length; i++) {
			double prob = dhmms[i].getForwordProbability(seq);
			int[] stateSeq = dhmms[i].getViterbiStateSequence(seq);
			
			System.out.println("HMM " + (i + 1));
			System.out.println("Forward probability: "
					+ prob);
			System.out.println("Most probable state sequence: "
					+ Arrays.asList(dhmms[i].getStateSequence(stateSeq)));
			System.out.println("------------------------------------------------");
		}

		sc.close();
	}

	/**
	 * @param args
	 * @throws IllegalProbabilityDistributionException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args)
			throws IllegalProbabilityDistributionException, InstantiationException, IllegalAccessException {
		// force parse doubles in format dd.dd not dd,dd
		Locale.setDefault(Locale.ENGLISH);

		testHMM(DiscreteHMM.class, MainHMM.class.getResourceAsStream("/res/dhmm.txt"));
	}

}
