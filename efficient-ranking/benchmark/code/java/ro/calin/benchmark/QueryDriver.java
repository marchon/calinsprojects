package ro.calin.benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

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
import org.apache.lucene.store.FSDirectory;

public class QueryDriver {

	/**
	 * @param args
	 * 
	 * 
	 *            TODO: make the QualityQueryParser a parameter and load it
	 *            dynamically
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 6) {
			System.err
					.println("Usage: QueryDriver <topicsFile> <qrelsFile> <submissionFile> <avgResFile> <indexDir> <qqpImpl>");
			System.err.println("topicsFile: input file containing queries");
			System.err
					.println("qrelsFile: input file containing relevance judgements");
			System.err
					.println("submissionFile: output submission file for trec_eval");
			System.err
					.println("avgResFile: output benchmark measures(average)");
			System.err.println("indexDir: index directory");
			System.err.println("qqpImpl: quality query parser implementation");

			System.exit(1);
		}

		File topicsFile = new File(args[0]);
		File qrelsFile = new File(args[1]);
		SubmissionReport submitLog = new SubmissionReport(new PrintWriter(
				args[2]), "lucene");
		FSDirectory dir = FSDirectory.open(new File(args[4]));
		String qqpImpl = args[5];

		Searcher searcher = new IndexSearcher(dir, true);

		int maxResults = 1000;
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

		// default to title & desc
		String[] flds = {"title", "description"};

		Constructor<?> qqpConstr = null;
		try {
			qqpConstr = ((Class<? extends QualityQueryParser>) Class
					.forName(qqpImpl)).getConstructor(String[].class,
					String.class);
		} catch (NoSuchMethodException e) {
			try {
				qqpConstr = ((Class<? extends QualityQueryParser>) Class
						.forName(qqpImpl)).getConstructor(String[].class,
						String.class, FSDirectory.class);
			} catch (NoSuchMethodException e2) {
				System.err.println("No appropriate constructor!");
				System.exit(1);
			}
		}
		QualityQueryParser qqParser;
		if (qqpConstr.getParameterTypes().length == 3) {
			qqParser = (QualityQueryParser) qqpConstr.newInstance(flds, "body", dir);
		} else {
			qqParser = (QualityQueryParser) qqpConstr.newInstance(flds, "body");
		}

		// run the benchmark
		QualityBenchmark qrun = new QualityBenchmark(qqs, qqParser, searcher,
				docNameField);
		qrun.setMaxResults(maxResults);
		QualityStats stats[] = qrun.execute(judge, submitLog, logger);

		// print an avarage sum of the results
		QualityStats avg = QualityStats.average(stats);
		avg.log("SUMMARY", 2, logger, "  ");
		
		//write summary to file
		PrintWriter fileLogger = new PrintWriter(new FileOutputStream(args[3]), true);
		avg.log("SUMMARY", 2,fileLogger , "  ");
		fileLogger.close();
	}

}
