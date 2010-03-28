package ro.calin.ranking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.lucene.benchmark.quality.Judge;
import org.apache.lucene.benchmark.quality.QualityBenchmark;
import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.benchmark.quality.QualityStats;
import org.apache.lucene.benchmark.quality.trec.TrecJudge;
import org.apache.lucene.benchmark.quality.trec.TrecTopicsReader;
import org.apache.lucene.benchmark.quality.utils.SubmissionReport;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.SegmentInfo;
import org.apache.lucene.index.SegmentInfos;
import org.apache.lucene.index.SegmentReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.FSDirectory;

public class AXQueryDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 4 || args.length > 5) {
			System.err
					.println("Usage: QueryDriver <topicsFile> <qrelsFile> <submissionFile> <indexDir> [querySpec]");
			System.err.println("topicsFile: input file containing queries");
			System.err
					.println("qrelsFile: input file containing relevance judgements");
			System.err
					.println("submissionFile: output submission file for trec_eval");
			System.err.println("indexDir: index directory");
			System.err
					.println("querySpec: string composed of fields to use in query consisting of T=title,D=description,N=narrative:");
			System.err
					.println("\texample: TD (query on Title + Description). The default is T (title only)");
			System.exit(1);
		}

		File topicsFile = new File(args[0]);
		File qrelsFile = new File(args[1]);
		SubmissionReport submitLog = new SubmissionReport(new PrintWriter(
				args[2]), "lucene");
		FSDirectory dir = FSDirectory.open(new File(args[3]));
		String fieldSpec = args.length == 5 ? args[4] : "T"; // default to
																// Title-only if
																// not
																// specified.
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

		Set<String> fieldSet = new HashSet<String>();
		if (fieldSpec.indexOf('T') >= 0)
			fieldSet.add("title");
		if (fieldSpec.indexOf('D') >= 0)
			fieldSet.add("description");
		if (fieldSpec.indexOf('N') >= 0)
			fieldSet.add("narrative");

		float avdl = 0;
		float sumT = 0.0f; 
	    float numT = 0.0f;
		
		//load index, calc avdl
		SegmentInfos segInfo = new SegmentInfos();
		segInfo.read(dir);
		int numSegments = segInfo.size();
	
		for (int i = 0; i < numSegments; i++) {
			SegmentInfo info = segInfo.info(i);
			SegmentReader reader = null;
			reader = SegmentReader.get(false, info, 1);
			Collection fieldNames = reader
					.getFieldNames(IndexReader.FieldOption.ALL);
			Iterator it = fieldNames.iterator();
			byte[] b = new byte[reader.maxDoc()];
			while (it.hasNext()) {
				String fieldName = (String) it.next();
				reader.norms(fieldName, b, 0);
				float sum = 0.0f;
				for (int j = 0; j < b.length; j++) {
					float dl = 1.0f / Similarity.decodeNorm(b[j])
							/ Similarity.decodeNorm(b[j]);
					sum += dl;
				}
				
				sumT += sum;
				numT += b.length;
			}
			reader.close();
		}

		
		if(sumT == 0 || numT == 0) {
			System.err.println("Error calculating average document lenght!!!");
			System.exit(1);
		}
		
		avdl = sumT / numT; 
		
		// set the parsing of quality queries into Lucene queries.
		QualityQueryParser qqParser = new AXQQParser(fieldSet
				.toArray(new String[0]), "body", avdl);

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
