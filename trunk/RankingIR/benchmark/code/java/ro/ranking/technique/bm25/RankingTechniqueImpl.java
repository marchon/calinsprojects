package ro.ranking.technique.bm25;

import java.io.IOException;

import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.index.SegmentInfos;
import org.apache.lucene.store.Directory;

import ro.ranking.benchmarking.RankingTechnique;
import ro.ranking.benchmarking.Utils;

/**
 * @author Calin
 * 
 */
public class RankingTechniqueImpl implements RankingTechnique {
	private String[] qqNames;
	private String indexField;
	private Directory dir;

	@Override
	public QualityQueryParser getQualityQueryParser() {
		return new BM25QualityQueryParser(qqNames, indexField);
	}

	@Override
	public void prepare(Directory dir, String[] qqNames, String indexField)
			throws IOException {
		this.qqNames = qqNames;
		this.indexField = indexField;
		this.dir = dir;

		// load index, calc avdl
		SegmentInfos segInfo = new SegmentInfos();
		segInfo.read(dir);

		try {
			BM25Parameters.setAverageLength(indexField, Utils
					.getAvgDocLen(segInfo));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public Directory getTestDirectory() {
		return dir;
	}
}
