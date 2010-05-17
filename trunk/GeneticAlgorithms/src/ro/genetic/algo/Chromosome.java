package ro.genetic.algo;

import java.util.Arrays;


public class Chromosome {
	private byte[] information;
	
	public Chromosome(String info) {
		byte[] bs = new byte[info.length()];
		for (int i = 0; i < info.length(); i++) {
			if(info.charAt(i) == '1')
				bs[i] =  1;
		}
		information = bs;
	}
	
	public Chromosome(Chromosome chromosome) {
		information = Arrays.copyOf(chromosome.information,
				chromosome.information.length);
	}

	public Chromosome(byte[] info) {
		information = info;
	}
	
	public byte[] getInformation() {
		return information;
	}

	public void mutate() {
		int n = (int)(Math.random() * information.length);
		information[n] = (byte) (1 - information[n]);
	}
	
	public void swapGenes(Chromosome mate) {
		int crossover = (int)(Math.random() * information.length);
		for (int i = crossover; i < information.length; i++) {
			byte temp = information[i];
			information[i] = mate.information[i];
			mate.information[i] = temp;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < information.length; i++) {
			sb.append(information[i]);
		}
		
		return sb.toString();
	}
}
