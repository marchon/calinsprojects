package ro.ranking.technique.lucene;

import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;

import ro.ranking.benchmarking.AbstractQualityQueryParser;
import ro.ranking.benchmarking.Utils;

public class LuceneQualityQueryParser extends AbstractQualityQueryParser {

	public LuceneQualityQueryParser(String[] qqNames, String indexField) {
		super(qqNames, indexField);
	}

	@Override
	public QueryParser createQueryParser() {
		return new QueryParser(Version.LUCENE_CURRENT, indexField,
				Utils.getDefaultAnalyzer());
	}

}
