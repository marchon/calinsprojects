package ro.calin.benchmark.clusterpruning;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

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
 * 		2. mark each doc in LD with 'L'
 * 		3. for each doc in index that is not in LD
 * 			3.1. find most similar leader L
 * 			3.2. mark doc with the id of L
 * 
 * NOTE that another lucene index is created
 * 
 * @author calin
 */
public class ClusterIndexProcessing {
	

	/**
	 * @param args
	 * @throws IOException 
	 */
	

}
