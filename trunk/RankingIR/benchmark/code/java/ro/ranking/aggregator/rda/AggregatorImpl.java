package ro.ranking.aggregator.rda;

import java.util.Arrays;
import java.util.LinkedHashMap;

import ro.ranking.benchmarking.Aggregator;


public class AggregatorImpl implements Aggregator {
	
	private String[] buildUniverse(String[][] rankings, LinkedHashMap<String, Integer> univMap) {
		//link hash map to preserve order
		int maxId = 0;
		for (int i = 0; i < rankings.length; i++) {
			String[] ranking = rankings[i];
			
			for (int j = 0; j < ranking.length; j++) {
				String obj = ranking[j].trim();
				
				Integer id = univMap.get(obj);
				if(id == null) {
					id = maxId++;
					univMap.put(obj, id);
				}
			}
		}
		
		return univMap.keySet().toArray(new String[univMap.keySet().size()]);
	}
	
	private int[] sigmaRanking(int[] objIds, int n) {
		int[] sigma = new int[n];
		
		Arrays.fill(sigma, n);
		
		for (int i = 0; i < objIds.length; i++) {
			sigma[objIds[i]] = i;
		}
		
		return sigma;
	}
	
	private int[][] buildSigmaRankings(String[][] rankings, LinkedHashMap<String, Integer> univMap) {
		//each ranking list will be indexed by object id for seqential access
		//each ranking will have the length of the universe, 
		//if an object is not in this ranking, ranking[obj] = n(where n is len of universe)
		int[][] result = new int[rankings.length][univMap.size()];
		
		int n = univMap.size();
		
		for (int i = 0; i < result.length; i++) {
			String[] ranking = rankings[i];
			
			//init ranking
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = n;
			}
			
			for (int j = 0; j < ranking.length; j++) {
				result[i][univMap.get(ranking[j])] = j;
			}
		}
		
		return result;
	}
	
	private int ord(int[] sigma, int x) {
		//sigma are intotdeuna marimea universului
		//pentru ca este o mapare a id-ului unui obiect
		//la pozitia 'in ierarhie
		//daca ierarhia nu con'tine un obiect i,
		//sigma[i] = n
		
		//ord(x) este definit ca |len(sigma) - sigma(x)|
		//TODO: tine lungimea ierarhiei separat
		int ret = sigma.length - sigma[x] - 1;
		ret = ret < 0? 0 : ret;
		return ret;
	}
	
	private int delta(int[] sigma, int[] tau) {
		int s = 0;
		for (int x = 0; x < sigma.length; x++) {
			s += Math.abs(ord(sigma, x) - ord(tau, x));
		}
		return s;
	}
	
	private int delta(int[] sigma, int[][] Tau) {
		int s = 0;
		
		for (int i = 0; i < Tau.length; i++) {
			s += delta(sigma, Tau[i]);
		}
		
		return s;
	}
	
	private int[] taggregate(int[][] sigmaRankings, int t) {
		int n = sigmaRankings[0].length;
		double[][] D = new double[n][n];

		// calculate d
		for (int k = 0; k < n; k++) {
			for (int j = 0; j < n; j++) {
				int sum = 0;
				for (int i = 0; i < sigmaRankings.length; i++) {
					int ord = ord(sigmaRankings[i], k);
					if (j < t)
						ord = j - ord;
					sum += Math.abs(ord);
				}
				D[j][k] = sum;
			}
		}

		int[] tAggregation = HungarianAlgorithm.hgAlgorithm(D, "min");

		return reverse(Arrays.copyOf(tAggregation, t));
	}
	
	private int[] reverse(int[] arr) {
		for (int i = 0; i < arr.length / 2; i++) {
			int j = arr.length - i - 1;
			int temp = arr[i];
			arr[i] = arr[j];
			arr[j] = temp;
		}
		
		return arr;
	}
	
	@Override
	public String[] aggregate(String[][] rankings) {
		LinkedHashMap<String, Integer> universeMap = new LinkedHashMap<String, Integer>();
		String[] universe = buildUniverse(rankings, universeMap);
		
//		System.out.println(Arrays.toString(universe));
		
		int[][] sigmaRankings = buildSigmaRankings(rankings, universeMap);
		

		int max = rankings[0].length;

		for (int i = 1; i < rankings.length; i++) {
			if (rankings[i].length > max)
				max = rankings[i].length;
		}

		int t = Math.min(max, universe.length);
		
		int[] best = taggregate(sigmaRankings, t);
//		int bestDist = Integer.MAX_VALUE;
//		for (int t = 1; t <= universe.length; t++) {
//			int[] aggreg = taggregate(sigmaRankings, t);
//			
//			String[] result = new String[aggreg.length];
//			for (int i = 0; i < result.length; i++) {
//				result[i] = universe[aggreg[i]];
//			}
//			
//			int dist = delta(sigmaRanking(aggreg, universe.length), sigmaRankings);
//			
//			System.out.println(t + "-aggreg: (" + Arrays.toString(result) + ", " + dist + ")");
//			
//			if(dist <= bestDist) {
//				bestDist = dist;
//				best = aggreg;
//			}
//		}
		
//		for (int i = 0; i < best.length - 1; i++) {
//			if(best[i] == best[i+1]) {
//				for (int j = 0; j < rankings.length; j++) {
//					System.out.println(Arrays.toString(rankings[j]));
//				}
//				System.out.println();
//				break;
//			}
//		}
		
		String[] result = new String[best.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = universe[best[i]];
		}
		
		System.out.println(Arrays.toString(result));
		
		return result;
	}
	
	public static void main(String[] args) {
		AggregatorImpl rda = new AggregatorImpl();

		String[][] rankings = new String[][]{
				{"1", "2", "3"},
				{"3", "4"},
				{"1", "3", "2", "4"}
//				{"b", "a", "c", "e"},
//				{"d", "e", "c", "a"},
//				{"a", "b", "e", "c"},
//				{"b", "a", "d", "e"}
		};

		System.out.println(Arrays.toString(rda.aggregate(rankings)));
	}
}
