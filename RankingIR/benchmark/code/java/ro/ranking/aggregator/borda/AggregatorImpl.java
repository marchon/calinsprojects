package ro.ranking.aggregator.borda;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ro.ranking.benchmarking.Aggregator;


public class AggregatorImpl implements Aggregator {
	private class DoubleAccumulator {
		double val = 0.0;
	}
	
	@Override
	public String[] aggregate(String[][] rankings) {
		Map<String, DoubleAccumulator> accumulatorMap = new HashMap<String, DoubleAccumulator>();
		
		for (int i = 0; i < rankings.length; i++) {
			for (int j = 0; j < rankings[i].length; j++) {
				DoubleAccumulator acc = accumulatorMap.get(rankings[i][j]);
				if(acc == null) {
					acc = new DoubleAccumulator();
					accumulatorMap.put(rankings[i][j], acc);
				}
				
				// pos 1 - 1.0
				// pos 2 - 0.5 = 1/2
				// pos n - 1/n
				acc.val += 1.0 / (double)(j + 1);
			}
		}
		
		List<Entry<String, DoubleAccumulator>> list = new LinkedList<Entry<String, DoubleAccumulator>>(
				accumulatorMap.entrySet());
		Collections.sort(list,
				new Comparator<Entry<String, DoubleAccumulator>>() {
					@Override
					public int compare(Entry<String, DoubleAccumulator> o1,
							Entry<String, DoubleAccumulator> o2) {
						//descending
						return (int) Math.signum(o2.getValue().val - o1.getValue().val);
					}
				});

		//calc max len
		int max = rankings[0].length;
		
		for (int i = 1; i < rankings.length; i++) {
			if(rankings[i].length > max)
				max = rankings[i].length;
		}
		
		int n = Math.min(max, list.size());
		
		String[] aggRank = new String[n];
		for (int i = 0; i < n; i++) {
			aggRank[i] = list.get(i).getKey();
		}
		return aggRank;
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.asList(new AggregatorImpl().aggregate(new String[][]{
				{"a", "b", "c", "d"}, 
				{"b", "a", "c", "e"}, 
				{"b", "c", "a"}
		})));
	}
}
