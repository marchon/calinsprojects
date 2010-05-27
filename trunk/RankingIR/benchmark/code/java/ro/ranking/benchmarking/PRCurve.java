package ro.ranking.benchmarking;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import ro.ranking.benchmarking.chart.PRChart;
import ro.ranking.benchmarking.chart.PRChartInfo;

public class PRCurve {
	public static void main(String[] args) throws IOException {
		//check args
		if(args.length != 2) {
			System.err.println("Usage: PRCuvrve <res_dir> <corpus>");
			System.exit(-1);
		}
		File resultDir = new File(args[0]);
		final String corpus = args[1];
		
		File[] results = resultDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.contains(corpus) && name.endsWith(".txt");
			}
		});
		
		PRChartInfo info = new PRChartInfo(corpus + " corpus");
		Map<String, double[]> precs = new HashMap<String, double[]>();
		
		for (int i = 0; i < results.length; i++) {
			String name = results[i].getName();
			String method = name.substring(0, name.indexOf('@'));
			
			double[] pvals = new double[11];
			
			Scanner sc = new Scanner(results[i]);
			
			while(!sc.next().contains("ircl_prn.0.00"));
			
			for (int j = 0; j < pvals.length; j++) {
				String line = sc.nextLine();
				pvals[j] = Double.parseDouble(line.substring(line.length() - 6,
						line.length()));
			}
			
			sc.close();
			
			precs.put(method, pvals);
		}
		
		info.setPinterps(precs);
		
		PRChart chart = new PRChart(info);
		chart.writeToPNGFile(new File(resultDir, corpus + ".png"));
	}
}
