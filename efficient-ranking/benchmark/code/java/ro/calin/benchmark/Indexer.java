package ro.calin.benchmark;

import java.util.Properties;

import org.apache.lucene.benchmark.byTask.PerfRunData;
import org.apache.lucene.benchmark.byTask.tasks.AddDocTask;
import org.apache.lucene.benchmark.byTask.tasks.CloseIndexTask;
import org.apache.lucene.benchmark.byTask.tasks.CreateIndexTask;
import org.apache.lucene.benchmark.byTask.tasks.RepSumByNameTask;
import org.apache.lucene.benchmark.byTask.tasks.ResetSystemEraseTask;
import org.apache.lucene.benchmark.byTask.tasks.TaskSequence;
import org.apache.lucene.benchmark.byTask.utils.Config;

public class Indexer {
	public static void main(String[] args) throws Exception {
		if(args.length != 1) {
			System.err.println("Usage: IndexCorpus <workDir>");
			System.exit(1);
		}
		
	    Properties p = initProps(args[0]);
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
	    seq1.addTask(new AddDocTask(runData));
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

	  // Sample programmatic settings. Could also read from file.
	  private static Properties initProps(String workingDir) {
	    Properties p = new Properties();
	    p.setProperty ( "work.dir"  				, workingDir );
	    p.setProperty ( "content.source"			, "org.apache.lucene.benchmark.byTask.feeds.TrecContentSource" );
	    p.setProperty ( "content.source.log.step" 	, "2500" );
	    p.setProperty ( "analyzer"            		, "org.apache.lucene.analysis.standard.StandardAnalyzer" );
	    p.setProperty ( "directory"           		, "FSDirectory" );
	    p.setProperty ( "doc.tokenized"       		, "true" );
	    p.setProperty ( "doc.stored"          		, "true" );  //try not to store, store just the term vectors
	    p.setProperty ( "doc.term.vector"        	, "false" ); //try true
	    p.setProperty ( "content.source.forever"	, "false" );
	    p.setProperty ( "content.source.encoding"	, "UTF-8" );
	    p.setProperty ( "content.source.excludeIteration", "false" );
	    return p;
	  }
}
