package ro.genetic.algo.ipd;

import java.util.Arrays;
import java.util.Comparator;

import ro.genetic.algo.Chromosome;
import ro.genetic.algo.GeneticAlgorithm;
import ro.genetic.algo.Individual;

public class PDAlgorithm implements GeneticAlgorithm {
	private final static double MUTATION_PROBAB = 1.0 / 2000.0;
	private final static int SELECT_NUM = 6;

	@Override
	public Individual[] crossover(Individual[] parents) {
		// the parents are replaced
		// TODO: use another replacement method
		for (int i = 0; i < parents.length; i += 2) {
			Chromosome m = parents[i].encode();
			Chromosome f = parents[i + 1].encode();

			m.swapGenes(f);

			parents[i].decode(m);
			parents[i + 1].decode(f);
		}

		return parents;
	}

	@Override
	public void mutate(Individual[] offsprings) {
		if (Math.random() < MUTATION_PROBAB) {
			int n = (int) (Math.random() * offsprings.length);
			offsprings[n].encode().mutate();
		}
	}

	@Override
	public void replace(Individual[] population, Individual[] offsprings) {
		// population already contains offsprings because
		// I overwritten parents values in crossover
		// TODO: use another replacement method
	}

	@Override
	public Individual[] select(Individual[] population) {
		Arrays.sort(population, new Comparator<Individual>() {
			@Override
			public int compare(Individual o1, Individual o2) {
				return o2.evaluate() - o1.evaluate();
			}
		});

//		System.out.println(Arrays.deepToString(population));
		return Arrays
				.copyOf(population,
						population.length < SELECT_NUM ? population.length
								: SELECT_NUM);
	}
}
