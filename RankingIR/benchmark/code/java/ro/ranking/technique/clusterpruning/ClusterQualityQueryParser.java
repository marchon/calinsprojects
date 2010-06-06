package ro.ranking.technique.clusterpruning;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.util.Version;

import ro.ranking.benchmarking.AbstractQualityQueryParser;
import ro.ranking.benchmarking.Utils;

public class ClusterQualityQueryParser extends AbstractQualityQueryParser {
	public ClusterQualityQueryParser(String[] qqNames, String indexField) {
		super(qqNames, indexField);
	}

	private static class ClusterQueryParser extends QueryParser {
		public ClusterQueryParser(Version matchVersion, String f, Analyzer a) {
			super(matchVersion, f, a);
		}

		@Override
		protected BooleanQuery newBooleanQuery(boolean disableCoord) {
			return new ClusterQuery();
		}
	}

	@Override
	public QueryParser createQueryParser() {
		return new ClusterQueryParser(Version.LUCENE_CURRENT, indexField,
				Utils.getDefaultAnalyzer());
	}
}
