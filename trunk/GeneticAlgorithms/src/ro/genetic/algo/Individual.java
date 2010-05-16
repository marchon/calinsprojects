package ro.genetic.algo;

public interface Individual {
	Chromosome encode();
	void decode(Chromosome chromosome);
	int evaluate();
}
