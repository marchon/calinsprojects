package ro.calin.clusterpruning;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Weight;
import org.apache.lucene.search.BooleanClause.Occur;

/**
 * @author Calin
 *
 */
public class ClusterQuery extends BooleanQuery {
	
	private boolean gotCluster = false;
	
	@Override
	public Weight createWeight(Searcher searcher) throws IOException {
		//do some lucene magic here :D
		//first query documents marked with L using a boolean query equivalent to this
		//then query for documents marked with that cluster
		
		if(!gotCluster) {
			gotCluster = true;
			BooleanQuery bq = new BooleanQuery();
			bq.add(new TermQuery(new Term("label", "L")), Occur.MUST);
			//add current query clauses
			//or do bq.add(this, MUST)???
			for(BooleanClause bc : this) {
				bq.add(bc);
			}
			TopDocs td = searcher.search(bq, 1);
			
			if(td.scoreDocs.length > 0) {
				Document leader = searcher.doc(td.scoreDocs[0].doc);
				this.add(new TermQuery(new Term("cluster", leader.get("cluster"))), Occur.MUST);
			}
		}
		
		return super.createWeight(searcher);
	}
	
//	public static void main(String[] args) throws IOException, ParseException {
//		FSDirectory dir = FSDirectory.open(new File("collections/ohsumed/index_clust"));
//		IndexSearcher is = new IndexSearcher(dir);
//		
//		String query = "60 year old menopausal woman without hormone replacement therapy";
//		
//		
//		QueryParser qp = new QueryParser(Version.LUCENE_31, "body", new StandardAnalyzer(Version.LUCENE_31));
//		
//		Query qu = qp.parse(query);
//		
//		long start = System.currentTimeMillis();
//		
//		BooleanQuery firstWrapper = new BooleanQuery();
//		firstWrapper.add(new TermQuery(new Term("label", "L")), Occur.MUST);
//		firstWrapper.add(qu, Occur.MUST);
//		
//		TopDocs td = is.search(firstWrapper, 1);
//		if(td.scoreDocs.length > 0) {
//			Document leader = is.doc(td.scoreDocs[0].doc);
//			
//			System.out.println(leader.get("cluster"));
//			BooleanQuery secondWrapper = new BooleanQuery();
//			secondWrapper.add(qu, Occur.MUST);
//			secondWrapper.add(new TermQuery(new Term("cluster", leader.get("cluster"))), Occur.MUST);
//			
//			td = is.search(secondWrapper, 200);
//			System.out.println(Arrays.asList(td.scoreDocs));
//		}
//		
//		System.out.println("Cluster query took: " + ((System.currentTimeMillis() - start)) + " ms.");
//		
//		start = System.currentTimeMillis();
//		td = is.search(qu, 200);
//		System.out.println(Arrays.asList(td.scoreDocs));
//		System.out.println("Normal query took: " + ((System.currentTimeMillis() - start)) + " ms.");
//		
//		is.close();
//		dir.close();
//	}
}
