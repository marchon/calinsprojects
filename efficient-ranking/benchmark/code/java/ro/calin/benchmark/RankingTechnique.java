package ro.calin.benchmark;

import java.io.IOException;

import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.store.Directory;

public interface RankingTechnique {
	public void prepare(Directory dir, String[] qqNames, String indexField) throws IOException;
	public QualityQueryParser getQualityQueryParser();
	public Directory getTestDirectory();
}
