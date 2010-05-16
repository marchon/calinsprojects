package ro.ranking.technique.axiomatic;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.SegmentInfo;
import org.apache.lucene.index.SegmentInfos;
import org.apache.lucene.index.SegmentReader;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.Directory;

import ro.ranking.benchmarking.RankingTechnique;

public class RankingTechniqueImpl implements RankingTechnique {
	private String[] qqNames;
	private String indexField;
	private float avdl;
	
	private Directory dir;
	
	@Override
	public QualityQueryParser getQualityQueryParser() {
		return new AxiomaticQualityQueryParser(qqNames, indexField, avdl);
	}

	@Override
	public void prepare(Directory dir, String[] qqNames, String indexField) throws IOException {
		this.dir = dir;
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
			Collection<String> fieldNames = reader
					.getFieldNames(IndexReader.FieldOption.ALL);
			Iterator<String> it = fieldNames.iterator();
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
	
	@Override
	public Directory getTestDirectory() {
		return dir;
	}
}
