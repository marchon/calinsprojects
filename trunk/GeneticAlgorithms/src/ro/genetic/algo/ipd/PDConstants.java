package ro.genetic.algo.ipd;

public final class PDConstants {
	/**
	 * Genetic algorithm iterations.
	 */
	public static final int IPD_ALGO_ITER = 200;
	
	/**
	 * The Iterative Prisoner Dilemma game length.
	 * 100 means each 50 iterations(2 players)
	 */
	public static final int IPD_GAME_LEN = 80;
	
	/**
	 * The population length at the beginning of algo.
	 */
	public static final int IPD_START_POP_LEN = 50;
	
	/**
	 * Start mutation probability in a generation for an individual.
	 */
	public final static double START_MUTATION_PROBAB = 1.0 / 100.0;
	
	/**
	 * Decrease mutation probability with this amount as generations
	 * advance.
	 */
	public final static double MUTATION_PROBAB_DEC = 0;//1.0 / 1000.0;
	
	/**
	 * Number of individuals selected for crossover by the selection
	 * operator.
	 * Must be even!
	 */
	public final static int SELECT_LEN = 24;
	
	/**
	 * T>R>P>S: 5>3>1>0
	 * 2*R > S+T: 2*3 > 5+0
	 */
	public final static int[][] IPD_SCORE_MATRIX = {
	    //d  c
	/*d*/{1, 5},
	/*c*/{0, 3}
	//d = defect
	//c = cooperate
	};
}
