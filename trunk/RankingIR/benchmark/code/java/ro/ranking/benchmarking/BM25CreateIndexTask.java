package ro.ranking.benchmarking;

import java.io.IOException;

import org.apache.lucene.benchmark.byTask.PerfRunData;
import org.apache.lucene.benchmark.byTask.tasks.CreateIndexTask;
import org.apache.lucene.benchmark.byTask.utils.Config;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.util.Version;

import ro.ranking.technique.bm25.BM25Similarity;

public class BM25CreateIndexTask extends CreateIndexTask {

	public BM25CreateIndexTask(PerfRunData runData) {
		super(runData);
	}

	@Override
	public int doLogic() throws IOException {
		PerfRunData runData = getRunData();
	    Config config = runData.getConfig();
	    
	    IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_31, runData.getAnalyzer())
        .setOpenMode(OpenMode.CREATE).setIndexDeletionPolicy(
            getIndexDeletionPolicy(config));
	    iwc.setSimilarity(new BM25Similarity());
	    
	    IndexWriter writer = new IndexWriter(runData.getDirectory(), iwc);
	    setIndexWriterConfig(writer, config);
	    runData.setIndexWriter(writer);
	    return 1;
	}
}
