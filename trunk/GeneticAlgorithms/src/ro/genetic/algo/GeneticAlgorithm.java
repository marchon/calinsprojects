package ro.genetic.algo;

public interface GeneticAlgorithm {
	/**
	 * @param individuals
	 * @return the fittest individuals that will be crossover
	 * 
	 * should return even number
	 */
	Individual[] select(Individual[] population);
	
	Individual[] crossover(Individual[] parents);
	
	void mutate(Individual[] offsprings);
	
	Individual[] replace(Individual[] population, Individual[] offsprings);
}
