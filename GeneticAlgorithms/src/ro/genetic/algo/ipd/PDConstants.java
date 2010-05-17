package ro.genetic.algo.ipd;

public final class PDConstants {
	/**
	 * Genetic algorithm iterations.
	 */
	public static final int IPD_ALGO_ITER = 5000;
	
	/**
	 * The Iterative Prisoner Dilemma game length.
	 * 100 means each 50 iterations(2 players)
	 */
	public static final int IPD_GAME_LEN = 200;
	
	/**
	 * The population length at the beginning of algo.
	 */
	public static final int IPD_START_POP_LEN = 50;
	
	/**
	 * Start mutation probability in a generation for an individual.
	 */
	public final static double START_MUTATION_PROBAB = 1.0 / 300.0;
	
	/**
	 * Decrease mutation probability with this amount as generations
	 * advance.
	 */
	public final static double MUTATION_PROBAB_DEC = 1.0 / 10000.0;
	
	/**
	 * Number of individuals selected for crossover by the selection
	 * operator.
	 */
	public final static int SELECT_LEN = 10;
	
	public final static int[][] IPD_COST_MATRIX = {
	    //d  c
	/*d*/{1, 0},
	/*c*/{4, 3}
	//d = defect
	//c = cooperate
	};
}
