package ro.ranking.technique.f2exp;

import java.io.IOException;

import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.index.SegmentInfos;
import org.apache.lucene.store.Directory;

import ro.ranking.benchmarking.RankingTechnique;
import ro.ranking.benchmarking.Utils;

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
	public void prepare(Directory dir, String[] qqNames, String indexField)
			throws IOException {
		this.dir = dir;
		this.qqNames = qqNames;
		this.indexField = indexField;

		// load index, calc avdl
		SegmentInfos segInfo = new SegmentInfos();
		segInfo.read(dir);

		try {
			this.avdl = Utils.getAvgDocLen(segInfo);
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
