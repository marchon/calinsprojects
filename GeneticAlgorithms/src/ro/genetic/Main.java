package ro.genetic;

import ro.genetic.algo.World;
import ro.genetic.algo.ipd.PDAlgorithm;
import ro.genetic.algo.ipd.PDIndividualGenerator;

public class Main {
	public static void main(String[] args) {
		World world = new World(new PDIndividualGenerator(), new PDAlgorithm(),
				false);

		int i = 0;
		while (i++ < 100000) {
			world.nextGeneration();
			if(i == 1) {
				System.out.println(world.getBest());
			}
		}
		
		System.out.println(world.getBest());
	}
}
