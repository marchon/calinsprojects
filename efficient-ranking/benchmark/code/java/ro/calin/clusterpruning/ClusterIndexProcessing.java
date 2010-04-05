package ro.calin.clusterpruning;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.similar.MoreLikeThis;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Process an existing index to cluster documents.
 * The clustering will be done in the following way:
 * 		1. choose sqrt(n) doc as leaders in list LD
 * 		2. for each doc in LD mark the field 'type' to L
 * 		3. while unmarked documents exist do:
 * 			3.1. for each doc dl in LD
 * 				3.1.1. retrieve a list FD of m similar unmarked docs using MoreLikeThis
 * 				3.1.2. for each doc df in FD mark the field 'leader' with id(dl)
 * 
 * @author calin
 */
public class ClusterIndexProcessing {

//	private static Document getDoc(int id, IndexSearcher s) throws IOException {
//		TopDocs docs = s.search(new TermQuery(new Term("docid", "doc" + id)), 1);
//		
//		if(docs.scoreDocs.length == 0)
//			return null;
//		
//		return s.doc(docs.scoreDocs[0].doc);
//	}
	
	private static Query leaderQuery = new TermQuery(new Term("label", "L"));
	private static Term idTermTpl = new Term("docid");
	
//	private static String getLeader(String id, MoreLikeThis mlt,
//			IndexSearcher s) throws IOException {
//		TopDocs docs = s
//				.search(new TermQuery(new Term("docid", id)), 1);
//
//		if (docs.scoreDocs.length == 0)
//			return null;
//
//		Query simq = mlt.like(docs.scoreDocs[0].doc);
//		BooleanQuery leadq = new BooleanQuery();
//		leadq.add(simq, Occur.MUST);
//		leadq.add(leaderQuery, Occur.MUST);
//
//		docs = s.search(leadq, 1);
//		
//		if(docs.scoreDocs.length == 0)
//			return null;
//		
//		return s.doc(docs.scoreDocs[0].doc).get("docid");
//	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		FSDirectory srcDir = FSDirectory.open(new File(args[0]));
		FSDirectory destDir = FSDirectory.open(new File(args[1]));
		
		
		Analyzer stdAnalyzer = new StandardAnalyzer(Version.LUCENE_31);
		
		
		
		//optimize src index to have documents in order
//		IndexWriter iwSrc = new IndexWriter(srcDir, new IndexWriterConfig(
//				Version.LUCENE_31, stdAnalyzer));
//		iwSrc.optimize();
//		iwSrc.close();
		
		//src reader
		IndexReader irSrc = IndexReader.open(srcDir);
		
		//dest writer
		IndexWriter iwDest = new IndexWriter(destDir, new IndexWriterConfig(
				Version.LUCENE_31, stdAnalyzer));
		
		int numDocs = irSrc.numDocs();
		int numLeaders = (int) Math.sqrt(numDocs);
		Set<Integer> leaderIds = new TreeSet<Integer>();
		
		System.out.println("Gen random leaders: " + numLeaders);
		for (int i = 0; i < numLeaders; i++) {
			int leader;
			do {
				leader = (int) Math.round(Math.random() * numDocs);
				if(leader >= numDocs) leader = numDocs - 1;
				//unlikely: math.random() is less then 1(largest should be 0.99999...but round ads 0.5)
			} while(!leaderIds.add(leader));
		}		
		System.out.println("End gen random leaders.");
		
		
		System.out.println("Start copy and mark leaders...");
		for (int docId : leaderIds) {
			Document d = irSrc.document(docId);
			d.add(new Field("label", "L", Store.YES, Index.NOT_ANALYZED));
			iwDest.addDocument(d);
		}
		System.out.println("End copy and mark leaders.");
		
		iwDest.commit();
		IndexReader irDest = IndexReader.open(destDir);
		IndexSearcher sDest = new IndexSearcher(irDest);

		System.out.println("Start copy and mark each follower...");
		//more like this for similar docs
		MoreLikeThis mlt = new MoreLikeThis(irDest);
		mlt.setFieldNames(new String[]{"body"});
		mlt.setAnalyzer(stdAnalyzer);
		
		for (int docId = 0; docId < numDocs; docId++) {
			if(!leaderIds.contains(docId)) {//not a leader
				System.out.print(docId + " ");
				if(docId % 30 == 0) System.out.println();
				
				Document follower = irSrc.document(docId);
				
				//build sim query
				//TODO: check for another way...eventually with term vectors
				Query simq = mlt.like(new StringReader(follower.get("body")));
				BooleanQuery bq = new BooleanQuery();
				bq.add(leaderQuery, Occur.MUST);
				bq.add(simq, Occur.MUST);
				
				TopDocs td = sDest.search(bq, 1);
				//TODO: what to do when no doc is returned; investigate more like this
				
				if(td.scoreDocs.length > 0) {
					//TODO: use the score in some way??
					String leaderId = Integer.toString(td.scoreDocs[0].doc);
					follower.add(new Field("label", leaderId, Store.YES, Index.NOT_ANALYZED));
					iwDest.addDocument(follower);
				}
			}
		}		
		System.out.println("End copy and mark each follower.");

		sDest.close();
		irDest.clone();
		irSrc.close();
		iwDest.close();
		destDir.close();
		srcDir.close();
	}

}
