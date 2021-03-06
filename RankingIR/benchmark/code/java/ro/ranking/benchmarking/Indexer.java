package ro.ranking.benchmarking;

import java.util.Properties;

import org.apache.lucene.benchmark.byTask.PerfRunData;
import org.apache.lucene.benchmark.byTask.tasks.AddDocTask;
import org.apache.lucene.benchmark.byTask.tasks.CloseIndexTask;
import org.apache.lucene.benchmark.byTask.tasks.CreateIndexTask;
import org.apache.lucene.benchmark.byTask.tasks.PerfTask;
import org.apache.lucene.benchmark.byTask.tasks.RepSumByNameTask;
import org.apache.lucene.benchmark.byTask.tasks.ResetSystemEraseTask;
import org.apache.lucene.benchmark.byTask.tasks.TaskSequence;
import org.apache.lucene.benchmark.byTask.utils.Config;

public class Indexer {
	public static void main(String[] args) throws Exception {
		if(args.length != 2) {
			System.err.println("Usage: Indexer <workDir> <analyzer>");
			System.exit(1);
		}
		
	    Properties p = initProps(args[0], args[1]);
	    Config conf = new Config(p);
	    PerfRunData runData = new PerfRunData(conf);
	    
	    // 1. top sequence
	    TaskSequence top = new TaskSequence(runData,null,null,false); // top level, not parallel
	    
	    // 2. reset all
	    ResetSystemEraseTask reset = new ResetSystemEraseTask(runData);
	    top.addTask(reset);
	    
	    // 3. task to create the index
	    CreateIndexTask create = new CreateIndexTask(runData);
	    top.addTask(create);
	    
	    // 4. task to add all docs from corpus
	    TaskSequence seq1 = new TaskSequence(runData,"Seq",top,false);
	    seq1.setRepetitions(TaskSequence.REPEAT_EXHAUST);
	    seq1.setNoChildReport();
	    PerfTask addDoc = new AddDocTask(runData);
	    seq1.addTask(addDoc);
	    top.addTask(seq1);
	    
	    // 5. task to close the index
	    CloseIndexTask close = new CloseIndexTask(runData);
	    top.addTask(close);

	    // task to report
	    RepSumByNameTask rep = new RepSumByNameTask(runData);
	    top.addTask(rep);

	    // print algorithm
	    System.out.println(top.toString());
	    
	    // execute
		top.doLogic();
	  }

	  private static Properties initProps(String workingDir, String analyzer) {
	    Properties p = new Properties();
	    p.setProperty ( "work.dir"  				, workingDir );
	    p.setProperty ( "content.source"			, "org.apache.lucene.benchmark.byTask.feeds.TrecContentSource" );
	    p.setProperty ( "content.source.log.step" 	, "2500" );
	    p.setProperty ( "analyzer"            		, Utils.loadAnalyzerClass(analyzer).getName());
	    p.setProperty ( "directory"           		, "FSDirectory" );
	    p.setProperty ( "doc.tokenized"       		, "true" );
	    p.setProperty ( "doc.stored"          		, "true" );
	    //p.setProperty ( "doc.body.stored"			, "false");
	    p.setProperty ( "doc.term.vector"        	, "false" );
	    p.setProperty ( "content.source.forever"	, "false" );
	    p.setProperty ( "content.source.encoding"	, "UTF-8" );
	    p.setProperty ( "content.source.excludeIteration", "true" );
	    p.setProperty ( "index.name", "index_" + analyzer);
	    return p;
	  }
}
