package ro.calin.servlet;

import static ro.calin.Constants.FIELD_CONTENT;
import static ro.calin.Constants.FIELD_TITLE;
import static ro.calin.Constants.FIELD_URL;
import static ro.calin.Constants.MAX_NUMBER_OF_RESULTS;
import static ro.calin.Constants.REQUEST_PARAMETER_PAGE;
import static ro.calin.Constants.REQUEST_PARAMETER_QUERY;
import static ro.calin.Constants.RESULTS_PER_PAGE;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocCollector;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

import ro.calin.LogHelper;
import ro.calin.Utils;
import ro.calin.json.HitContainer;
import ro.calin.json.PagedJSONList;

/**
 * Servlet implementation class QueryHandler
 */
public class QueryHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static LogHelper log = LogHelper.getInstance(QueryHandler.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		String queryString = request.getParameter(REQUEST_PARAMETER_QUERY);

		int page = 1;

		try {
			page = Integer.parseInt(request.getParameter(REQUEST_PARAMETER_PAGE));
		} catch (NumberFormatException e) {
			// page = 1;
		}

		ServletContext sc = getServletContext();

		Map<String, Object> state = Utils.getStateMap(sc);

		IndexSearcher searcher = null;

		PagedJSONList<HitContainer> hits = new PagedJSONList<HitContainer>();

		if (queryString != null && (searcher = Utils.getSearcher(state)) != null) {

			Analyzer analyzer = Utils.getAnalyzer(state);

			QueryParser qp = new QueryParser(FIELD_CONTENT, analyzer);

			queryString = queryString.replaceAll("\\s+", " ");
			Query query;
			try {
				query = qp.parse(queryString);
				TopDocCollector collector = new TopDocCollector(MAX_NUMBER_OF_RESULTS);

				// TODO: vezi ce face mai exact rewrite
				query = query.rewrite(Utils.getReader(state));

				searcher.search(query, collector);

				SimpleHTMLFormatter formatter = new SimpleHTMLFormatter();
				Highlighter highlighter = new Highlighter(formatter, new QueryScorer(query));

				ScoreDoc[] scDocs = collector.topDocs().scoreDocs;
				int totalPages = scDocs.length / RESULTS_PER_PAGE + (scDocs.length % RESULTS_PER_PAGE == 0 ? 0 : 1);
				hits.setTotalPages(totalPages);

				int begin = RESULTS_PER_PAGE * (page - 1);
				int eend = begin + RESULTS_PER_PAGE;
				int end = eend < scDocs.length ? eend : scDocs.length;

				for (int i = begin; i < end; i++) {
					Document doc = searcher.doc(scDocs[i].doc);

					String content = doc.get(FIELD_CONTENT);
					TokenStream tokenStream = analyzer.tokenStream(FIELD_CONTENT, new StringReader(content));

					String summary = highlighter.getBestFragments(tokenStream, content, 3, "...");

					hits.add(new HitContainer(Utils.formatJSONString(doc.get(FIELD_TITLE)), Utils.formatJSONString(doc
							.get(FIELD_URL)), Utils.formatJSONString(summary)));
				}

				response.setContentType("text/json; charset=utf-8");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().println(hits);

			} catch (ParseException e) {
				if (log.isDebugEnabled())
					log.error("Error with query string '" + queryString + "'", e);
			}
		} else {
			if (log.isDebugEnabled())
				log.fatal("No query or no searcher on servlet context.");
		}
	}

}
