package ro.calin.benchmark;

import java.io.BufferedReader;
import java.io.File;
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
	 * TODO: make the QualityQueryParser a parameter and load it dynamically
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 5) {
			System.err
					.println("Usage: QueryDriver <topicsFile> <qrelsFile> <submissionFile> <indexDir> <qqpImpl>");
			System.err.println("topicsFile: input file containing queries");
			System.err
					.println("qrelsFile: input file containing relevance judgements");
			System.err
					.println("submissionFile: output submission file for trec_eval");
			System.err.println("indexDir: index directory");
			System.err
					.println("qqpImpl: quality query parser implementation");
//			System.err
//			.println("querySpec: string composed of fields to use in query consisting of T=title,D=description,N=narrative:");
			System.exit(1);
		}

		File topicsFile = new File(args[0]);
		File qrelsFile = new File(args[1]);
		SubmissionReport submitLog = new SubmissionReport(new PrintWriter(
				args[2]), "lucene");
		FSDirectory dir = FSDirectory.open(new File(args[3]));
//		String fieldSpec = args.length == 5 ? args[4] : "T"; 
		String qqpImpl = args[4];
		
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

		//default to title & desc
		Set<String> fieldSet = new HashSet<String>();
		//if (fieldSpec.indexOf('T') >= 0)
			fieldSet.add("title");
		//if (fieldSpec.indexOf('D') >= 0)
			fieldSet.add("description");
//		if (fieldSpec.indexOf('N') >= 0)
//			fieldSet.add("narrative");

		
		Constructor<?> qqpConstr = ((Class<? extends QualityQueryParser>)Class.forName(qqpImpl)).getConstructors()[0];
		QualityQueryParser qqParser;
		if(qqpConstr.getParameterTypes().length == 3) {
			qqParser = (QualityQueryParser) qqpConstr.newInstance(fieldSet.toArray(new String[0]), "body", dir);
		} else {
			qqParser = (QualityQueryParser) qqpConstr.newInstance(fieldSet.toArray(new String[0]), "body");
		}
		
		// run the benchmark
		QualityBenchmark qrun = new QualityBenchmark(qqs, qqParser, searcher,
				docNameField);
		qrun.setMaxResults(maxResults);
		QualityStats stats[] = qrun.execute(judge, submitLog, logger);

		// print an avarage sum of the results
		QualityStats avg = QualityStats.average(stats);
		avg.log("SUMMARY", 2, logger, "  ");
	}

}
