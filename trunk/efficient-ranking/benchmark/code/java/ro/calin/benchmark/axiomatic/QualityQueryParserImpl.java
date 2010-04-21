package ro.calin.benchmark.axiomatic;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

public class QualityQueryParserImpl implements QualityQueryParser {
	private String qqNames[];
	private String indexField;
	ThreadLocal<AXQueryParser> queryParser = new ThreadLocal<AXQueryParser>();

	private float avdl;

	public QualityQueryParserImpl(String[] qqNames, String indexField, float avdl) {
		this.qqNames = qqNames;
		this.indexField = indexField;
		this.avdl = avdl;
	}
	
	static class AXQueryParser extends QueryParser {
		private float avdl;
		public AXQueryParser(Version matchVersion, String f, Analyzer a, float avdl) {
			super(matchVersion, f, a);
			this.avdl = avdl;
		}
		
		@Override
		protected Query newTermQuery(Term term) {
			return new AXTermQuery(term, avdl, 0.35f, 0.5f);
		}
		
		@Override
		protected Query getBooleanQuery(List<BooleanClause> clauses)
				throws ParseException {
			//disable coord; TODO: see what this means
			return getBooleanQuery(clauses, true);
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
		AXQueryParser qp = queryParser.get();
		if (qp == null) {
			qp = new AXQueryParser(Version.LUCENE_CURRENT, indexField,
					new StandardAnalyzer(Version.LUCENE_CURRENT), avdl);
			queryParser.set(qp);
		}
		
		
		BooleanQuery bq = new BooleanQuery();
		for (int i = 0; i < qqNames.length; i++)
			bq.add(qp.parse(QueryParser.escape(qq.getValue(qqNames[i]))),
					BooleanClause.Occur.SHOULD);

		return bq;
	}
}
