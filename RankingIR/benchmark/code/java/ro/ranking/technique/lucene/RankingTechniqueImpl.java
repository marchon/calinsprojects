package ro.ranking.technique.lucene;

import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.benchmark.quality.utils.SimpleQQParser;
import org.apache.lucene.store.Directory;

import ro.ranking.benchmarking.RankingTechnique;

public class RankingTechniqueImpl implements RankingTechnique {
	private String[] qqNames;
	private String indexField;
	private Directory dir;
	@Override
	public QualityQueryParser getQualityQueryParser() {
		return new LuceneQualityQueryParser(qqNames, indexField);
	}

	@Override
	public void prepare(Directory dir, String[] qqNames, String indexField) {
		this.qqNames = qqNames;
		this.indexField = indexField;
		this.dir = dir;
	}
	
	@Override
	public Directory getTestDirectory() {
		return dir;
	}
}
