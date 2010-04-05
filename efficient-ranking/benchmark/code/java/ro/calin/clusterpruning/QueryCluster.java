package ro.calin.clusterpruning;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class QueryCluster /*extends Query*/{

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		FSDirectory dir = FSDirectory.open(new File("collections/ohsumed/index_clust"));
		IndexSearcher is = new IndexSearcher(dir);
		
		String query = "60 year old menopausal woman without hormone replacement therapy";
		
		
		QueryParser qp = new QueryParser(Version.LUCENE_31, "body", new StandardAnalyzer(Version.LUCENE_31));
		
		Query qu = qp.parse(query);
		
		long start = System.currentTimeMillis();
		
		BooleanQuery firstWrapper = new BooleanQuery();
		firstWrapper.add(new TermQuery(new Term("label", "L")), Occur.MUST);
		firstWrapper.add(qu, Occur.MUST);
		
		TopDocs td = is.search(firstWrapper, 1);
		if(td.scoreDocs.length > 0) {
			//TODO: do not rely on lucene internal ids, use labels
			//TODO: when doing second query, take into cosideration the leader also(by docid), should be:
			// (label: id or docid: id) AND qu
			String id = Integer.toString(td.scoreDocs[0].doc);
			
			BooleanQuery secondWrapper = new BooleanQuery();
			secondWrapper.add(new TermQuery(new Term("label", id)), Occur.MUST);
			secondWrapper.add(qu, Occur.MUST);
			
			td = is.search(secondWrapper, 200);
			System.out.println(Arrays.asList(td.scoreDocs));
		}
		
		System.out.println("Cluster query took: " + ((System.currentTimeMillis() - start)) + " ms.");

		start = System.currentTimeMillis();
		td = is.search(qu, 200);
		System.out.println(Arrays.asList(td.scoreDocs));
		System.out.println("Normal query took: " + ((System.currentTimeMillis() - start)) + " ms.");
		
		//TODO: when creating query for benchmark do first query in constructor...or something
		is.close();
		dir.close();
	}

}
