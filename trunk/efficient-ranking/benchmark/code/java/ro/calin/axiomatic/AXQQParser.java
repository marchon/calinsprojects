package ro.calin.axiomatic;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.SegmentInfo;
import org.apache.lucene.index.SegmentInfos;
import org.apache.lucene.index.SegmentReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class AXQQParser implements QualityQueryParser {
	private String qqNames[];
	private String indexField;
	ThreadLocal<AXQueryParser> queryParser = new ThreadLocal<AXQueryParser>();

	private float avdl;

	public AXQQParser(String[] qqNames, String indexField, FSDirectory dir) throws IOException {
		this.qqNames = qqNames;
		this.indexField = indexField;
		
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
		
		this.avdl = sumT / numT; 
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
