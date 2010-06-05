package ro.ranking.benchmarking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.lucene.benchmark.quality.QualityBenchmark;
import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.benchmark.quality.QualityStats;
import org.apache.lucene.benchmark.quality.trec.TrecTopicsReader;
import org.apache.lucene.benchmark.quality.utils.SubmissionReport;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import ro.ranking.benchmarking.AggregatorQualityBenchmark.AggregatorSubmissionReport;

public class QueryDriver {
	
	private static void showUsageAndExit() {
		System.err
				.println("Usage: QueryDriver <topicsFile> <qrelsFile> "
						+ "<indexDir> <techiqueList> <aggregation> <resultsFile> [querySpec] [maxResults]" );
		System.err.println("topicsFile: input file containing queries");
		System.err
				.println("qrelsFile: input file containing relevance judgements");
		System.err.println("indexDir: index directory");
		System.err
				.println("techiqueList: the ranking technique(s) to be tested");
		System.err
				.println("aggregation: aggregation method used for multiple ranking techniques");
		System.err
		.println("submissionFile: output for trec_eval");
		System.err.println("querySpec: string composed of fields to use in query consisting of T=title,D=description,N=narrative");
		System.err.println("maxResults: maximum number of results a query can produce");
		System.exit(1);
	}

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 6 || args.length > 8) {
			showUsageAndExit();
		}

		File topicsFile = new File(args[0]);
		//File qrelsFile = new File(args[1]);
	
		
		File defaultIndexDir = new File(args[2]);
		
		String[] techniques = args[3].split(",");
		
		//TODO: this is needed because just cluster pruning uses another index
		//TODO: should make all use same index, somehow - this will simplify matters
		//TODO: cluster pruning must be revised
		Directory dir = FSDirectory.open(defaultIndexDir);
		Searcher searcher;
		QualityQueryParser[] qqParsers = new QualityQueryParser[techniques.length];
		
		String fieldSpec = args.length >= 7 ? args[6] : "T";
		Set<String> fieldSet = new HashSet<String>();
	    if (fieldSpec.indexOf('T') >= 0) fieldSet.add("title");
	    if (fieldSpec.indexOf('D') >= 0) fieldSet.add("description");
	    if (fieldSpec.indexOf('N') >= 0) fieldSet.add("narrative");
		int maxResults = args.length == 8? Integer.parseInt(args[7]) : 500;
		
		for (int i = 0; i < techniques.length; i++) {
			RankingTechnique rankingTechnique = ((Class<? extends RankingTechnique>) Class
					.forName("ro.ranking.technique." + techniques[i] + ".RankingTechniqueImpl"))
					.newInstance();
			
			// default to title & desc
			rankingTechnique.prepare(dir, fieldSet.toArray(new String[0]), "body");
			
			//TODO: this is needed for cluster pruning, FIXME!!!
			//all the searches will be done on the dir that is set by the last method that changes it
			//ie. cp
			if (rankingTechnique.getTestDirectory() != dir) {
				dir.close();
				dir = rankingTechnique.getTestDirectory();
			}
			
			// get the parser
			qqParsers[i] = rankingTechnique.getQualityQueryParser();

		}
		searcher = new IndexSearcher(dir, true);
		
		String docNameField = "docname";

		//PrintWriter logger = new PrintWriter(System.out, true);

		// use trec utilities to read trec topics into quality queries
		TrecTopicsReader qReader = new TrecTopicsReader();
		BufferedReader topicsReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(topicsFile), "UTF-8"));
		QualityQuery[] qqs = qReader.readQueries(topicsReader);

		// prepare judge, with trec utilities that read from a QRels file
//		BufferedReader qrelsReader = new BufferedReader(
//				new FileReader(qrelsFile));
//		Judge judge = new TrecJudge(qrelsReader);

		// validate topics & judgments match each other
//		judge.validateData(qqs, logger);
		
		PrintWriter submissionPW = new PrintWriter(args[5]);
		
		
		// run the benchmark
//		if (techniques.length == 1) {
//			SubmissionReport submitLog = new SubmissionReport(submissionPW, "lucene");
//			QualityBenchmark qrun = new QualityBenchmark(qqs, qqParsers[0], searcher, docNameField);
//			qrun.setMaxResults(maxResults);
//			qrun.execute(null, submitLog, null);
//		} else {
		
		// bad performance because sim was formatted with , instead of .
		// see SubmissionReport -> nf.format(sd[i].score)
		// still diff between results with lucene scores and len-docnum
		// i guess because of the fact that with lucene scoring some docs have the same
		// score and trec_eval sorts them by docno??
		AggregatorSubmissionReport submitLog = new AggregatorSubmissionReport(
				submissionPW, args[3]);
		Aggregator aggregator = null;
		if (techniques.length > 1) {
			aggregator = ((Class<? extends Aggregator>) Class
					.forName("ro.ranking.aggregator." + args[4]
							+ ".AggregatorImpl")).newInstance();
		}
		AggregatorQualityBenchmark qrun = new AggregatorQualityBenchmark(qqs,
				qqParsers, searcher, docNameField);
		qrun.setMaxResults(maxResults);
		qrun.execute(null, null, submitLog, aggregator);
//		}
		
		
		// print an avarage sum of the results
//		QualityStats avg = QualityStats.average(stats);
//		avg.log("SUMMARY", 2, logger, "  ");
//
//		// write summary to file
//		avg.log("SUMMARY", 2, fileLogger, "  ");
		
		//close stuff
		topicsReader.close();
		submissionPW.close();
	}

}
