package ro.ranking.benchmarking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.benchmark.quality.Judge;
import org.apache.lucene.benchmark.quality.QualityBenchmark;
import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.benchmark.quality.QualityStats;
import org.apache.lucene.benchmark.quality.trec.TrecJudge;
import org.apache.lucene.benchmark.quality.trec.TrecTopicsReader;
import org.apache.lucene.benchmark.quality.utils.SubmissionReport;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class QueryDriver {

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 6 || args.length > 7) {
			System.err
					.println("Usage: QueryDriver <topicsFile> <qrelsFile> <submissionFile> " +
							"<avgResFile> <indexDir> <techiqueList> [<aggregation>]");
			System.err.println("topicsFile: input file containing queries");
			System.err.println("qrelsFile: input file containing relevance judgements");
			System.err.println("submissionFile: output submission file for trec_eval");
			System.err.println("avgResFile: output benchmark measures(average)");
			System.err.println("indexDir: index directory");
			System.err.println("techiqueList: the ranking technique(s) to be tested");
			System.err.println("aggregation: aggregation method used for multiple ranking techniques");
			System.exit(1);
		}

		File topicsFile = new File(args[0]);
		File qrelsFile = new File(args[1]);
		SubmissionReport submitLog = new SubmissionReport(new PrintWriter(
				args[2]), "lucene");
		PrintWriter fileLogger = new PrintWriter(new FileOutputStream(args[3]),
				true);
		
		File defaultIndexDir = new File(args[4]);
		
		String[] techniques = args[5].split(",");
		
		if(techniques.length > 1 && args.length != 7) {
			System.err.println("Aggregator was not specified for multiple ranking methods.");
			System.exit(1);
		}
		
		//TODO: this is needed because just cluster pruning uses another index
		//TODO: should make all use same index, somehow - this will simplify matters
		//TODO: cluster pruning must be revised
		Map<QualityQueryParser, Searcher> parserSearcherMap = new HashMap<QualityQueryParser, Searcher>();

		for (int i = 0; i < techniques.length; i++) {
			RankingTechnique rankingTechnique = ((Class<? extends RankingTechnique>) Class
					.forName("ro.ranking.technique." + techniques[i] + ".RankingTechniqueImpl"))
					.newInstance();
			
			// default to title & desc
			Directory dir = FSDirectory.open(defaultIndexDir);
			rankingTechnique.prepare(dir, new String[] { "title", "description" },
					"body");
			
			// replace directory if needed(index is modified and stored in another place)
			// like in cluster pruning
			if (rankingTechnique.getTestDirectory() != dir) {
				dir.close();
				dir = rankingTechnique.getTestDirectory();
			}
			
			// get the parser
			QualityQueryParser qqParser = rankingTechnique.getQualityQueryParser();

			Searcher searcher = new IndexSearcher(dir, true);
			
			parserSearcherMap.put(qqParser, searcher);
		}

		int maxResults = 200;
		String docNameField = "docname";

		PrintWriter logger = new PrintWriter(System.out, true);

		// use trec utilities to read trec topics into quality queries
		TrecTopicsReader qReader = new TrecTopicsReader();
		QualityQuery qqs[] = qReader.readQueries(new BufferedReader(
				new FileReader(topicsFile)));

		// prepare judge, with trec utilities that read from a QRels file
		Judge judge = new TrecJudge(new BufferedReader(
				new FileReader(qrelsFile)));

		// validate topics & judgments match each other
		judge.validateData(qqs, logger);

		QualityStats[] stats;
		// run the benchmark
		if(parserSearcherMap.size() == 1) {
			Entry<QualityQueryParser, Searcher> ps = parserSearcherMap.entrySet().iterator().next();
			QualityBenchmark qrun = new QualityBenchmark(qqs, ps.getKey(), ps.getValue(),
					docNameField);
			qrun.setMaxResults(maxResults);
			stats = qrun.execute(judge, submitLog, logger);
		} else { 
			//aggregate results from multiple ranking techniques
			AggregatorQualityBenchmark qrun = new AggregatorQualityBenchmark(
					qqs, parserSearcherMap, docNameField);
			qrun.setMaxResults(maxResults);
			//no submission report
			
			Aggregator aggregator = ((Class<? extends Aggregator>) Class
					.forName("ro.ranking.aggregator." + args[6] + ".AggregatorImpl")).newInstance();
			
			stats = qrun.execute(judge, aggregator, logger);
		}
		
		// print an avarage sum of the results
		QualityStats avg = QualityStats.average(stats);
		avg.log("SUMMARY", 2, logger, "  ");

		// write summary to file
		avg.log("SUMMARY", 2, fileLogger, "  ");
		fileLogger.close();
	}

}
