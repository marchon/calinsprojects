package ro.calin.benchmark.clusterpruning;

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
}
