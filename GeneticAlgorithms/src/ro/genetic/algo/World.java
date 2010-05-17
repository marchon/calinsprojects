package ro.genetic.algo;

public class World {
	private Individual[] population;
	private GeneticAlgorithm algo;
	private Individual best;
	private int generation = 0;
	private boolean verbose;

	public World(IndividualGenerator generator, GeneticAlgorithm algo, boolean verbose) {
		this.population = generator.generateIndividuals();
		this.algo = algo;
		this.verbose = verbose;
	}

	public Individual[] getIndividuals() {
		return population;
	}

	public Individual getBest() {
		return best;
	}
	
	public void nextGeneration() {
		generation++;
		
		Individual[] parents = algo.select(population);
		best = parents[0];
		if(verbose) {
			System.out.println("Best individual in generation " + generation + ":");
			System.out.println(parents[0]);
		}
		
		Individual[] offsprings = algo.crossover(parents);
		algo.mutate(offsprings);
		population = algo.replace(population, offsprings);
	}
}
