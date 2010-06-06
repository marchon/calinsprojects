package ro.ranking.technique.bm25;

import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import ro.ranking.benchmarking.AbstractQualityQueryParser;
import ro.ranking.benchmarking.Utils;

public class BM25QualityQueryParser extends AbstractQualityQueryParser {
	public BM25QualityQueryParser(String[] qqNames, String indexField) {
		super(qqNames, indexField);
	}

	@Override
	public QueryParser createQueryParser() {
		return new QueryParser(Version.LUCENE_CURRENT, indexField,
				Utils.getDefaultAnalyzer());
	}

	public Query parse(QualityQuery qq) throws ParseException {
		// not used
		QueryParser qp = queryParserThreadLocal.get();
		if (qp == null) {
			qp = createQueryParser();
			queryParserThreadLocal.set(qp);
		}

		StringBuilder qsb = new StringBuilder();

		for (int i = 0; i < qqNames.length; i++) {
			String query = qq.getValue(qqNames[i]);
			if (query != null && !query.trim().equals("")) {
				qsb.append(QueryParser.escape(query));
				if (i < qqNames.length - 1)
					qsb.append(" ");
			}
		}
		
		Query q = new BM25BooleanQuery(qsb.toString(), qp);
		
		return q;
	}
}
