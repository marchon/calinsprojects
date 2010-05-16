package ro.ranking.technique.axiomatic;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import ro.ranking.benchmarking.AbstractQualityQueryParser;

public class AxiomaticQualityQueryParser extends AbstractQualityQueryParser {
	private float avdl;

	public AxiomaticQualityQueryParser(String[] qqNames, String indexField,
			float avdl) {
		super(qqNames, indexField);
		this.avdl = avdl;
	}

	class AxiomaticQueryParser extends QueryParser {
		public AxiomaticQueryParser(Version matchVersion, String f, Analyzer a,
				float avdl) {
			super(matchVersion, f, a);
		}

		@Override
		protected Query newTermQuery(Term term) {
			return new AxiomaticTermQuery(term, avdl, 0.35f, 0.5f);
		}

		@Override
		protected Query getBooleanQuery(List<BooleanClause> clauses)
				throws ParseException {
			// disable coord; 
			//TODO: see what this means
			return getBooleanQuery(clauses, true);
		}
	}

	@Override
	public QueryParser createQueryParser() {
		return new AxiomaticQueryParser(Version.LUCENE_CURRENT, indexField,
				new StandardAnalyzer(Version.LUCENE_CURRENT), avdl);
	}
}
