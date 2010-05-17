package ro.genetic.algo.ipd;

import ro.genetic.algo.World;
import static ro.genetic.algo.ipd.PDConstants.*;

public class Main {
	public static void main(String[] args) {
		World world = new World(new PDIndividualGenerator(), new PDAlgorithm(),
				false);

		int i = 0;
		while (i++ < IPD_ALGO_ITER) {
			world.nextGeneration();
			if(i == 1) {
				System.out.println("FIRST:");
				System.out.println(world.getBest());
			}
		}
		
		System.out.println("LAST:");
		System.out.println(world.getBest());
	}
}
