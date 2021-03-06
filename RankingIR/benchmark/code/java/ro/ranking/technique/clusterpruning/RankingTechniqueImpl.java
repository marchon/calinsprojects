package ro.ranking.technique.clusterpruning;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.similar.MoreLikeThis;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import ro.ranking.benchmarking.RankingTechnique;

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
		return new ClusterQualityQueryParser(qqNames, indexField);
	}

	@Override
	public void prepare(Directory dir, String[] qqNames, String indexField) throws IOException {
		this.qqNames = qqNames;
		this.indexField = indexField;

		// build new cluster index
		
		boolean buildCluster = true;
		if(dir instanceof FSDirectory) {
			FSDirectory fsdir = (FSDirectory)dir;
			File f = fsdir.getDirectory();
			String clustIndName = f.getName() + "_clust";
			
			File newIndexFile = new File(f.getParent(), clustIndName);

			if(newIndexFile.exists()) {
				buildCluster = false;
				System.out.println("Cluster already exists, skipping build...");
			} 
			
			this.dir = FSDirectory.open(newIndexFile.getAbsoluteFile());
		} else if(dir instanceof RAMDirectory) {
			this.dir = new RAMDirectory();
		} else {
			throw new IOException("This directory type is not suported: " + dir.getClass().getName());
		}
		
		if(buildCluster) {
			System.out.println("Creating cluster index...");
			createClusterIndex(dir, this.dir);
		}
	}

	@Override
	public Directory getTestDirectory() {
		return dir;
	}

	
	/**
	 * 
	 * Process an existing index to cluster documents.
	 * The clustering will be done in the following way:
	 * 		1. choose sqrt(n) doc as leaders in list LD
	 * 		2. mark each doc in LD with 'L'
	 * 		3. for each doc in index that is not in LD
	 * 			3.1. find most similar leader L
	 * 			3.2. mark doc with the id of L
	 * 
	 * NOTE that another lucene index is created
	 * 
	 * 
	 * @param srcDir
	 * @param destDir
	 * @throws IOException
	 */
	private void createClusterIndex(Directory srcDir, Directory destDir)
			throws IOException {

		Analyzer stdAnalyzer = new StandardAnalyzer(Version.LUCENE_31);

		// src reader
		IndexReader irSrc = IndexReader.open(srcDir);

		// dest writer
		IndexWriter iwDest = new IndexWriter(destDir, new IndexWriterConfig(
				Version.LUCENE_31, stdAnalyzer));

		int numDocs = irSrc.numDocs();
		int numLeaders = (int) Math.sqrt(numDocs);
		Set<Integer> leaderIds = new HashSet<Integer>();

		System.out.println("Gen random leaders: " + numLeaders);
		for (int i = 0; i < numLeaders; i++) {
			int leader;
			do {
				leader = (int) Math.round(Math.random() * numDocs);
				if (leader >= numDocs)
					leader = numDocs - 1;
				// unlikely: math.random() is less then 1(largest should be
				// 0.99999...but round ads 0.5)
			} while (!leaderIds.add(leader));
		}
		System.out.println("End gen random leaders.");

		System.out.println("Start copy and mark leaders...");
		for (int docId : leaderIds) {
			Document d = irSrc.document(docId);
			d.add(new Field("label", "L", Store.YES, Index.NOT_ANALYZED));
			d.add(new Field("cluster", d.get("docid"), Store.YES,
					Index.NOT_ANALYZED));
			iwDest.addDocument(d);
		}
		System.out.println("End copy and mark leaders.");

		iwDest.commit();
		IndexReader irDest = IndexReader.open(destDir);
		IndexSearcher sDest = new IndexSearcher(irDest);

		System.out.println("Start copy and mark each follower...");
		// more like this for similar docs
		MoreLikeThis mlt = new MoreLikeThis(irDest);
		mlt.setFieldNames(new String[] { "body" });
		mlt.setAnalyzer(stdAnalyzer);
		mlt.setMinDocFreq(2);
		mlt.setMinTermFreq(0); // 0 means not to check, same effect as 1
		mlt.setMinWordLen(3);

		Set<Integer> notClust = new HashSet<Integer>();

		Query leaderQuery = new TermQuery(new Term("label", "L"));

		for (int docId = 0; docId < numDocs; docId++) {
			if (!leaderIds.contains(docId)) {// not a leader
				if (docId % 1000 == 0)
					System.out.println(docId);

				Document follower = irSrc.document(docId);

				// build sim query
				Query simq = mlt.like(new StringReader(follower.get("body")));
				BooleanQuery bq = new BooleanQuery();
				bq.add(leaderQuery, Occur.MUST);
				bq.add(simq, Occur.MUST);

				TopDocs td = sDest.search(bq, 1);

				if (td.scoreDocs.length > 0) {
					Document leader = sDest.doc(td.scoreDocs[0].doc);

					follower.add(new Field("cluster", leader.get("docid"),
							Store.YES, Index.NOT_ANALYZED));
					iwDest.addDocument(follower);
				} else {
					notClust.add(docId);
				}
			}
		}
		System.out.println("End copy and mark each follower.");

		System.out.println(notClust.size() + " docs could not be clustered.");
		System.out.println(notClust);

		sDest.close();
		irDest.clone();
		irSrc.close();
		iwDest.close();
	}
}
