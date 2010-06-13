package ro.ranking.benchmarking;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.benchmark.quality.Judge;
import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.benchmark.quality.QualityStats;
import org.apache.lucene.benchmark.quality.utils.DocNameExtractor;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;

public class AggregatorQualityBenchmark {

	public static class AggregatorSubmissionReport {
		private NumberFormat nf;
		  private PrintWriter logger;
		  private String name;
		  
		  /**
		   * Constructor for SubmissionReport.
		   * @param logger if null, no submission data is created. 
		   * @param name name of this run.
		   */
		  public AggregatorSubmissionReport (PrintWriter logger, String name) {
		    this.logger = logger;
		    this.name = name;
		    nf = NumberFormat.getInstance();
		    nf.setMaximumFractionDigits(4);
		    nf.setMinimumFractionDigits(4);
		  }
		  
		  /**
		   * Report a search result for a certain quality query.
		   * @param qq quality query for which the results are reported.
		   * @param td search results for the query.
		   * @param docNameField stored field used for fetching the result doc name.  
		   * @param searcher index access for fetching doc name.
		   * @throws IOException in case of a problem.
		   */
		  public void report(QualityQuery qq, String[] docNames, String docNameField, Searcher searcher) throws IOException {
		    if (logger==null) {
		      return;
		    }
		    String sep = " \t ";
		    for (int i=0; i< docNames.length; i++) {
		      String docName = docNames[i];
		      logger.println(
		          qq.getQueryID()       + sep +
		          "Q0"                   + sep +
		          format(docName,20)    + sep +
		          format(""+i,7)        + sep +
		          (docNames.length - i) + sep +
		          name
		          );
		    }
		  }

		  public void flush() {
		    if (logger!=null) {
		      logger.flush();
		    }
		  }
		  
		  private static String padd = "                                    ";
		  private String format(String s, int minLen) {
		    s = (s==null ? "" : s);
		    int n = Math.max(minLen,s.length());
		    return (s+padd).substring(0,n);
		  }
	}
	
	/** Quality Queries that this quality benchmark would execute. */
	protected QualityQuery qualityQueries[];

	/** Parser for turning QualityQueries into Lucene Queries. */
	protected QualityQueryParser[] qqParsers;

	/** Index to be searched. */
	protected Searcher searcher;

	/**
	 * index field to extract doc name for each search result; used for judging
	 * the results.
	 */
	protected String docNameField;

	/**
	 * maximal number of queries that this quality benchmark runs. Default:
	 * maxint. Useful for debugging.
	 */
	private int maxQueries = Integer.MAX_VALUE;

	/** maximal number of results to collect for each query. Default: 1000. */
	private int maxResults = 1000;

	/**
	 * Create a QualityBenchmark.
	 * 
	 * @param qqs
	 *            quality queries to run.
	 * @param qqParser
	 *            parser for turning QualityQueries into Lucene Queries.
	 * @param searcher
	 *            index to be searched.
	 * @param docNameField
	 *            name of field containing the document name. This allows to
	 *            extract the doc name for search results, and is important for
	 *            judging the results.
	 */
	public AggregatorQualityBenchmark(QualityQuery qqs[],
			QualityQueryParser[] qqParsers, Searcher searcher,
			String docNameField) {
		this.qualityQueries = qqs;
		this.qqParsers = qqParsers;
		this.searcher = searcher;
		this.docNameField = docNameField;
	}

		
	/**
	 * Run the quality benchmark.
	 * 
	 * @param judge
	 *            the judge that can tell if a certain result doc is relevant
	 *            for a certain quality query. If null, no judgements would be
	 *            made. Usually null for a submission run.
	 * @param submitRep
	 *            submission report is created if non null.
	 * @param qualityLog
	 *            If not null, quality run data would be printed for each query.
	 * @return QualityStats of each quality query that was executed.
	 * @throws Exception
	 *             if quality benchmark failed to run.
	 */
	public long execute(Judge judge, PrintWriter qualityLog,
			AggregatorSubmissionReport submitRep, Aggregator aggregator) throws Exception {
		int nQueries = Math.min(maxQueries, qualityQueries.length);
		QualityStats stats[] = new QualityStats[nQueries];
		
		long avgSearchTime = 0;
		
		for (int i = 0; i < nQueries; i++) {
			QualityQuery qq = qualityQueries[i];
			// generate query
			Query[] qs = new Query[qqParsers.length];
			int j = 0;

			List<String[]> rankings = new ArrayList<String[]>();
			long totalSearchTime = 0;
			DocNameExtractor xt = new DocNameExtractor(docNameField);
			
			for (int k = 0; k < qqParsers.length; k++) {
				QualityQueryParser qqParser = qqParsers[k];
				Query q = qqParser.parse(qq);

				long t1 = System.currentTimeMillis();
				TopDocs td = searcher.search(q, null, maxResults);
				long searchTime = System.currentTimeMillis() - t1;

				String[] docNames = new String[td.scoreDocs.length];
				for (int l = 0; l < docNames.length; l++) {
					docNames[l] = xt.docName(searcher, td.scoreDocs[l].doc);
				}

				rankings.add(docNames);

				totalSearchTime += searchTime;

				qs[j++] = q;
			}
			
			// aggregate rankings
			String[] finalRanking;

			if (rankings.size() == 1 || aggregator == null) {
				finalRanking = rankings.get(0);
			} else {
				long t1 = System.currentTimeMillis();
				finalRanking = aggregator.aggregate(rankings
						.toArray(new String[rankings.size()][]));
				
				totalSearchTime += (System.currentTimeMillis() - t1);
			}
			// most likely we either submit or judge, but check both
			if (judge != null) {
				stats[i] = analyzeQueryResults(qq, qs, finalRanking,
						judge, qualityLog, totalSearchTime);
			}

			if (submitRep != null) {
				submitRep.report(qq, finalRanking, docNameField, searcher);
			}
			
			avgSearchTime += totalSearchTime;
		}
		if (submitRep != null) {
			submitRep.flush();
		}

		avgSearchTime /= nQueries;
		
		return avgSearchTime;
	}

	/* Analyze/judge results for a single quality query; optionally log them. */
	private QualityStats analyzeQueryResults(QualityQuery qq, Query[] qs,
			String[] docNames, /* long[] docNameExtractTimes, */Judge judge,
			PrintWriter logger, long searchTime) throws IOException {
		QualityStats stts = new QualityStats(judge.maxRecall(qq), searchTime);

		for (int i = 0; i < docNames.length; i++) {
			String docName = docNames[i];

			boolean isRelevant = judge.isRelevant(docName, qq);
			stts.addResult(i + 1, isRelevant, /* docNameExtractTimes[i] */0);
		}
		if (logger != null) {
			for (Query q : qs) {
				logger.println(qq.getQueryID() + "  -  " + q);
			}

			stts.log(qq.getQueryID() + " Stats:", 1, logger, "  ");
		}
		return stts;
	}

	/**
	 * @return the maximum number of quality queries to run. Useful at
	 *         debugging.
	 */
	public int getMaxQueries() {
		return maxQueries;
	}

	/**
	 * Set the maximum number of quality queries to run. Useful at debugging.
	 */
	public void setMaxQueries(int maxQueries) {
		this.maxQueries = maxQueries;
	}

	/**
	 * @return the maximum number of results to collect for each quality query.
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * set the maximum number of results to collect for each quality query.
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

}
