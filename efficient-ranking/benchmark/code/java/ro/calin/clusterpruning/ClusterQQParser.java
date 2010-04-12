package ro.calin.clusterpruning;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

public class ClusterQQParser implements QualityQueryParser {
	private String qqNames[];
	private String indexField;
	ThreadLocal<ClusterQueryParser> queryParser = new ThreadLocal<ClusterQueryParser>();

	public ClusterQQParser(String[] qqNames, String indexField) {
		this.qqNames = qqNames;
		this.indexField = indexField;
	}
	
	private static class ClusterQueryParser extends QueryParser {
		public ClusterQueryParser (Version matchVersion, String f, Analyzer a) {
			super(matchVersion, f, a);
		}
		
		@Override
		protected BooleanQuery newBooleanQuery(boolean disableCoord) {
			return new ClusterQuery();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.lucene.benchmark.quality.QualityQueryParser#parse(org.apache
	 * .lucene.benchmark.quality.QualityQuery)
	 */
	public Query parse(QualityQuery qq) throws ParseException {
		ClusterQueryParser qp = queryParser.get();
		if (qp == null) {
			qp = new ClusterQueryParser(Version.LUCENE_CURRENT, indexField,
					new StandardAnalyzer(Version.LUCENE_CURRENT));
			queryParser.set(qp);
		}
		
		BooleanQuery bq = new BooleanQuery();
		for (int i = 0; i < qqNames.length; i++)
			bq.add(qp.parse(QueryParser.escape(qq.getValue(qqNames[i]))),
					BooleanClause.Occur.SHOULD);

		return bq;
	}
}
