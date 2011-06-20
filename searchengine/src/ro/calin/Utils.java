package ro.calin;

import static ro.calin.Constants.ANALYZER;
import static ro.calin.Constants.INDEX_DIRECTORY;
import static ro.calin.Constants.INDEX_READER;
import static ro.calin.Constants.INDEX_SEARCHER;
import static ro.calin.Constants.STATE_CONTAINER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;

import ro.calin.snowball.SnowballAnalyzer;

public final class Utils {
	static LogHelper log = LogHelper.getInstance(Utils.class);

	public static Map<String, Object> getStateMap(ServletContext sc) {
		Map<String, Object> smap = (Map<String, Object>) sc.getAttribute(STATE_CONTAINER);

		if (smap == null) {
			smap = new HashMap<String, Object>();
			sc.setAttribute(STATE_CONTAINER, smap);
		}

		return smap;
	}

	public static IndexSearcher getSearcher(Map<String, Object> smap) {
		IndexSearcher searcher = (IndexSearcher) smap.get(INDEX_SEARCHER);

		if (searcher == null) {
			reopenSearcher(smap);
			searcher = (IndexSearcher) smap.get(INDEX_SEARCHER);
		}

		return searcher;
	}

	public static void reopenSearcher(Map<String, Object> smap) {
		closeSearcher(smap);

		try {
			IndexSearcher searcher = new IndexSearcher(INDEX_DIRECTORY);

			smap.put(INDEX_SEARCHER, searcher);
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.fatal("Open searcher exception ", e);
		}
	}

	public static void closeSearcher(Map<String, Object> smap) {
		if (smap.get(INDEX_SEARCHER) != null) {
			IndexSearcher searcher = (IndexSearcher) smap.get(INDEX_SEARCHER);
			try {
				searcher.close();
			} catch (IOException e) {
				if (log.isDebugEnabled())
					log.error("Close searcher exception ", e);
			}
		}
	}

	public static IndexReader getReader(Map<String, Object> smap) {
		IndexReader reader = (IndexReader) smap.get(INDEX_READER);

		if (reader == null) {
			reopenReader(smap);
			reader = (IndexReader) smap.get(INDEX_READER);
		}

		return reader;
	}

	public static synchronized void reopenReader(Map<String, Object> smap) {
		closeReader(smap);

		try {
			IndexReader reader = IndexReader.open(INDEX_DIRECTORY);

			smap.put(INDEX_READER, reader);
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.fatal("Open reader exception ", e);
		}
	}

	public static synchronized void closeReader(Map<String, Object> smap) {
		if (smap.get(INDEX_READER) != null) {
			IndexReader reader = (IndexReader) smap.get(INDEX_READER);
			try {
				reader.close();
			} catch (IOException e) {
				if (log.isDebugEnabled())
					log.error("Close reader exception ", e);
			}
		}
	}

	public static Analyzer getAnalyzer(Map<String, Object> smap) {
		Analyzer an = (Analyzer) smap.get(ANALYZER);

		if (an == null) {
			// TODO: instantiate custom analyzer here
			an = new SnowballAnalyzer("Romanian");

			smap.put(ANALYZER, an);
		}

		return an;
	}

	public static String formatJSONString(String s) {
		return s.replaceAll("\\s+", " ").replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
	}
}
