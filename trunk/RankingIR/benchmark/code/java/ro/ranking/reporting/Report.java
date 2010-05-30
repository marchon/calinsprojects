package ro.ranking.reporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Report {
	private static interface Reporter {
		void report(String reportName, Map<String, Map<String, String>> data,
				OutputStream os) throws IOException;
	}

	private static class PRCurveReporter implements Reporter {
		private static String[] prkeys = { "ircl_prn.0.00", "ircl_prn.0.10",
				"ircl_prn.0.20", "ircl_prn.0.30", "ircl_prn.0.40",
				"ircl_prn.0.50", "ircl_prn.0.60", "ircl_prn.0.70",
				"ircl_prn.0.80", "ircl_prn.0.90", "ircl_prn.1.00" };

		@Override
		public void report(String reportName,
				Map<String, Map<String, String>> data, OutputStream os)
				throws IOException {
			PRChartInfo info = new PRChartInfo(reportName);
			Map<String, double[]> precs = new HashMap<String, double[]>();

			for (String method : data.keySet()) {
				double[] pvals = new double[11];

				Map<String, String> res = data.get(method);

				for (int j = 0; j < pvals.length; j++) {
					pvals[j] = Double.parseDouble(res.get(prkeys[j]));
				}

				precs.put(method, pvals);
			}

			info.setPinterps(precs);
			PRChart chart = new PRChart(info);
			chart.writeToPNGFile(os);
		}
	}

	private static class HTMLReporter implements Reporter {
		private static Map<String, String> labels = new LinkedHashMap<String, String>();
		static {
			labels.put("num_q", "Nb. interogari");
			labels.put("num_ret", "Nb. ret");
			labels.put("num_rel", "num_rel");
			labels.put("num_rel_ret", "num_rel_ret");
			labels.put("map", "map");
			labels.put("gm_ap", "gm_ap");
			labels.put("R-prec", "R-prec");
			labels.put("bpref", "bpref");
			labels.put("recip_rank", "recip_rank");
			/*labels.put("ircl_prn.0.00", "ircl_prn.0.00");
			labels.put("ircl_prn.0.10", "ircl_prn.0.10");
			labels.put("ircl_prn.0.20", "ircl_prn.0.20");
			labels.put("ircl_prn.0.30", "ircl_prn.0.30");
			labels.put("ircl_prn.0.40", "ircl_prn.0.40");
			labels.put("ircl_prn.0.50", "ircl_prn.0.50");
			labels.put("ircl_prn.0.60", "ircl_prn.0.60");
			labels.put("ircl_prn.0.70", "ircl_prn.0.70");
			labels.put("ircl_prn.0.80", "ircl_prn.0.80");
			labels.put("ircl_prn.0.90", "ircl_prn.0.90");
			labels.put("ircl_prn.1.00", "ircl_prn.1.00");*/
			labels.put("P5", "P5");
			labels.put("P10", "P10");
			labels.put("P15", "P15");
			labels.put("P20", "P20");
			labels.put("P30", "P30");
			labels.put("P100", "P100");
			labels.put("P200", "P200");
			labels.put("P500", "P500");
			labels.put("P1000", "P1000");
		}

		@Override
		public void report(String reportName,
				Map<String, Map<String, String>> data, OutputStream os) throws IOException {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					Report.class.getResourceAsStream("template.html")));

			String line;
			
			while(!(line = br.readLine()).contains("$$$$")) {
				sb.append(line);
			}
			
			sb.append(reportName);
			
			while(!(line = br.readLine()).contains("$$$$")) {
				sb.append(line);
			}
			
			//build the table
			//header
			sb.append("<th scope='col'>Metode</th>");
			for (String measure : labels.keySet()) {
				sb.append("<th scope='col'>");
				sb.append(labels.get(measure));
				sb.append("</td>");
			}
			
			while(!(line = br.readLine()).contains("$$$$")) {
				sb.append(line);
			}
			
			for (String method : data.keySet()) {
				sb.append("<tr><td>");
				sb.append(method);
				sb.append("</td>");
				Map<String, String> res = data.get(method);
				for (String measure : labels.keySet()) {
					sb.append("<td>");
					sb.append(res.get(measure));
					sb.append("</td>");
				}
				
				sb.append("</tr>");
			}
			
			while((line = br.readLine()) != null) {
				sb.append(line);
			}

			br.close();
			PrintWriter pw = new PrintWriter(os);
			pw.write(sb.toString());
			pw.flush();
		}
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("Usage: Report <res_dir> <corpus>");
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

		Map<String, Map<String, String>> trecres = new LinkedHashMap <String, Map<String, String>>();

		for (int i = 0; i < results.length; i++) {
			String name = results[i].getName();
			String method = name.substring(0, name.indexOf('@'));

			// some order
			Map<String, String> trecmap = new HashMap<String, String>();
			trecres.put(method, trecmap);

			BufferedReader br = new BufferedReader(new FileReader(results[i]));
			String line;

			while ((line = br.readLine()) != null) {
				if (line.indexOf("all") != -1) {
					String[] keyval = line.split("all");
					trecmap.put(keyval[0].trim(), keyval[1].trim());
				}
			}

			br.close();
		}

		// make dir
		File corpusDir = new File(resultDir, corpus);
		if (corpusDir.exists() || corpusDir.mkdir()) {
			OutputStream os = new FileOutputStream(new File(corpusDir,
					"prcurve.png"));
			new PRCurveReporter().report(corpus, trecres, os);
			os.close();

			os = new FileOutputStream(new File(corpusDir, "results.html"));
			new HTMLReporter().report(corpus, trecres, os);
			os.close();
		}
	}
}
