package ro.calin.benchmark.aggregation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.benchmark.quality.Judge;
import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.benchmark.quality.QualityStats;
import org.apache.lucene.benchmark.quality.utils.DocNameExtractor;
import org.apache.lucene.benchmark.quality.utils.SubmissionReport;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;

public class AggregatorQualityBenchmark {

	/** Quality Queries that this quality benchmark would execute. */
	protected QualityQuery qualityQueries[];

	/** List of parsers for turning QualityQueries into Lucene Queries mapped to searchers */
	/** This is in case one of the "classifiers" is using another index(but the index should contain the same docs)*/
	protected Map<QualityQueryParser, Searcher> parserSearcherMap;

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
	public AggregatorQualityBenchmark(QualityQuery qqs[], Map<QualityQueryParser, Searcher> parserSearcherMap, String docNameField) {
		this.qualityQueries = qqs;
		this.parserSearcherMap = parserSearcherMap;
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
	public QualityStats[] execute(Judge judge, /*SubmissionReport submitRep,*/ Aggregator aggregator,
			PrintWriter qualityLog) throws Exception {
		int nQueries = Math.min(maxQueries, qualityQueries.length);
		QualityStats stats[] = new QualityStats[nQueries];
		for (int i = 0; i < nQueries; i++) {
			QualityQuery qq = qualityQueries[i];
			// generate query
			Query[] qs = new Query[parserSearcherMap.size()];
			int j = 0;
			
			List<String[]> rankings = new ArrayList<String[]>();
			long totalSearchTime = 0;
			DocNameExtractor xt = new DocNameExtractor(docNameField);
			for (Iterator<Entry<QualityQueryParser, Searcher>> iterator = parserSearcherMap
					.entrySet().iterator(); iterator.hasNext();) {
				Entry<QualityQueryParser, Searcher> entry = iterator.next();
				
				Query q = entry.getKey().parse(qq);
				
				long t1 = System.currentTimeMillis();
				TopDocs td = entry.getValue().search(q, null, maxResults);
				long searchTime = System.currentTimeMillis() - t1;
				
				String[] docNames = new String[td.scoreDocs.length];
				for (int k = 0; k < docNames.length; k++) {
					docNames[k] = xt.docName(entry.getValue(), td.scoreDocs[k].doc);
				}
				
				rankings.add(docNames);
				
				totalSearchTime += searchTime;
				
				qs[j++] = q;
			}
			
			//aggregate rankings
			String[] aggregatedRanking = aggregator.aggregate(rankings
					.toArray(new String[rankings.size()][]));
			
			// most likely we either submit or judge, but check both
			if (judge != null) {
				stats[i] = analyzeQueryResults(qq, qs, aggregatedRanking, judge, qualityLog,
						totalSearchTime);
			}
			
			//TODO: make this work
//			if (submitRep != null) {
//				submitRep.report(qq, td, docNameField, searcher);
//			}
		}
//		if (submitRep != null) {
//			submitRep.flush();
//		}
		return stats;
	}

	/* Analyze/judge results for a single quality query; optionally log them. */
	private QualityStats analyzeQueryResults(QualityQuery qq, Query[] qs,
			String[] docNames, /*long[] docNameExtractTimes,*/ Judge judge, PrintWriter logger, long searchTime)
			throws IOException {
		QualityStats stts = new QualityStats(judge.maxRecall(qq), searchTime);

		for (int i = 0; i < docNames.length; i++) {
			String docName = docNames[i];
			
			boolean isRelevant = judge.isRelevant(docName, qq);
			stts.addResult(i + 1, isRelevant, /*docNameExtractTimes[i]*/0);
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
