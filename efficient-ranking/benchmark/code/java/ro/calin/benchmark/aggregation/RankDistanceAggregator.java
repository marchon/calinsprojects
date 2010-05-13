package ro.calin.benchmark.aggregation;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import ro.calin.benchmark.aggregation.assignment.AssignmentAlgorithm;
import ro.calin.benchmark.aggregation.assignment.HungarianAlgorithm;


public class RankDistanceAggregator implements Aggregator {
	private String[] buildUniverse(String[][] rankings, LinkedHashMap<String, Integer> univMap) {
		//link hash map to preserve order
		int maxId = 0;
		for (int i = 0; i < rankings.length; i++) {
			String[] ranking = rankings[i];
			
			for (int j = 0; j < ranking.length; j++) {
				String obj = ranking[j];
				
				Integer id = univMap.get(obj);
				if(id == null) {
					id = maxId++;
					univMap.put(obj, id);
				}
			}
		}
		
		return univMap.keySet().toArray(new String[univMap.keySet().size()]);
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
		return sigma.length - sigma[x];
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
	
	@Override
	public String[] aggregate(String[][] rankings) {
		LinkedHashMap<String, Integer> universeMap = new LinkedHashMap<String, Integer>();
		String[] universe = buildUniverse(rankings, universeMap);
		int n = universe.length;
		
		int[][] sigmaRankings = buildSigmaRankings(rankings, universeMap);
		
		float[][] D = new float[n][n];
		float[][] D1 = new float[n][n];
		AssignmentAlgorithm resolver = new HungarianAlgorithm();
		int minDelta = Integer.MAX_VALUE;
		int[] aggreg = null;
		
		for (int t = 0; t < n; t++) {
			
			//calculate d
			for (int k = 0; k < n; k++) {
				for (int j = 0; j < n; j++) {
					int sum = 0;
					for (int i = 0; i < sigmaRankings.length; i++) {
						int ord = ord(sigmaRankings[i], k);
						if(j <= t) ord = j - ord;
						sum += Math.abs(ord);						
					}
					D[k][j] = sum;
					D1[k][j] = sum;
				}
			}
			
//			for (int i = 0; i < D.length; i++) {
//				for (int j = 0; j < D[i].length; j++) {
//					System.out.print(D[i][j]+" ");
//				}
//				System.out.println();
//			}
			//i1, i2,..., it
			//Arrays.copyOf(resolver.computeAssignments(D), t + 1);
			int[] tAggregation = resolver.computeAssignments(D);
			
			int delta = 0;
			for (int i = 0; i < tAggregation.length; i++) {
				delta += D1[tAggregation[i]][i];
			}
			
			System.out.println("delta=" + delta + ",min=" + minDelta);
			if(delta < minDelta) {
				minDelta = delta;
				aggreg = Arrays.copyOf(tAggregation, t + 1);
				
				for (int i = 0; i < aggreg.length; i++) {
					System.out.print(aggreg[i] + " ");
				}
				System.out.println();
			}
		}
		
		if(aggreg == null)
			return null;
		
		String[] result = new String[aggreg.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = universe[aggreg[i]];
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		RankDistanceAggregator rda = new RankDistanceAggregator();
//		LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		String[][] rankings = new String[][]{
				{"1", "2", "3"},
				{"3", "4"},
				{"1", "3", "2", "4"}
		};
//		System.out.println(Arrays.asList(rda.buildUniverse(rankings, map)));
//		System.out.println(map);
//		
//		int[][] mat = rda.buildSigmaRankings(rankings, map);
//		
//		for (int i = 0; i < mat.length; i++) {
//			for (int j = 0; j < mat[i].length; j++) {
//				System.out.print(mat[i][j]+" ");
//			}
//			System.out.println();
//		}
//		
//		for (int i = 0; i < mat[0].length; i++) {
//			System.out.println(rda.ord(mat[0], i));
//		}
		
		System.out.println(Arrays.asList(rda.aggregate(rankings)));
	}
}
