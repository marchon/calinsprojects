package ro.genetic.algo.ipd;

import java.util.Arrays;
import java.util.Comparator;

import ro.genetic.algo.Chromosome;
import ro.genetic.algo.GeneticAlgorithm;
import ro.genetic.algo.Individual;
import static ro.genetic.algo.ipd.PDConstants.*;

public class PDAlgorithm implements GeneticAlgorithm {
	private double mp = START_MUTATION_PROBAB;

	@Override
	public Individual[] crossover(Individual[] parents) {
		//length must be even, so truncate one if needed
		int len = parents.length - (parents.length % 2);
		
		Individual[] offsprings = new Individual[len];
		
		//for each to two adjacent individuals,
		//duplicate genetic info, exchange,
		//and create two new individuals
		for (int i = 0; i < len; i += 2) {
			Chromosome m = new Chromosome(parents[i].encode());
			Chromosome f = new Chromosome(parents[i + 1].encode());

			m.crossover(f);

			offsprings[i] = new PDIndividual();
			offsprings[i].decode(m);
			offsprings[i + 1] = new PDIndividual();
			offsprings[i + 1].decode(f);
		}

		return offsprings;
	}

	@Override
	public void mutate(Individual[] offsprings) {
		boolean dec = false;
		for (int i = 0; i < offsprings.length; i++) {
			Chromosome oc = offsprings[i].encode();
			dec = oc.mutate(mp);
			offsprings[i].decode(oc);
			dec = true;
		}
		
		//decrese mutation probability as the algorithm advances
		//tend to stability
		if(dec)
			mp -= MUTATION_PROBAB_DEC;
	}

	@Override
	public Individual[] replace(Individual[] population, Individual[] offsprings) {
		//replace low scoring individuals from the population
		//population is in descending score order(from select)
		int ol = offsprings.length - 1;
		int pl = population.length - 1;
		
		for (int i = 0; i < offsprings.length; i++) {
			population[pl - i] = offsprings[ol - i];
		}
		
		//just return the same pool of individuals(with some of them replaced)
		return population;
	}

	@Override
	public Individual[] select(Individual[] population) {
		Arrays.sort(population, new Comparator<Individual>() {
			@Override
			public int compare(Individual o1, Individual o2) {
				return o2.evaluate() - o1.evaluate();
			}
		});

		return Arrays
				.copyOf(population,
						population.length < SELECT_LEN ? population.length
								: SELECT_LEN);
	}
}
