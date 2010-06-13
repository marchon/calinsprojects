package ro.ranking.benchmarking;

import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;


public abstract class AbstractQualityQueryParser implements QualityQueryParser {
	protected String qqNames[];
	protected String indexField;
	protected ThreadLocal<QueryParser> queryParserThreadLocal = new ThreadLocal<QueryParser>();

	public AbstractQualityQueryParser(String[] qqNames, String indexField) {
		this.qqNames = qqNames;
		this.indexField = indexField;
	}
	
	public abstract QueryParser createQueryParser();
	
	@Override
	public Query parse(QualityQuery qq) throws ParseException {
		QueryParser qp = queryParserThreadLocal.get();
		if (qp == null) {
			qp = createQueryParser();
			queryParserThreadLocal.set(qp);
		}
		
		BooleanQuery bq = new BooleanQuery();
		for (int i = 0; i < qqNames.length; i++) {
			String query = qq.getValue(qqNames[i]);
			if (query != null && !query.trim().equals("")) {
				bq.add(qp.parse(QueryParser.escape(query)),
						BooleanClause.Occur.SHOULD);
			}
		}

		return bq;
	}

}
