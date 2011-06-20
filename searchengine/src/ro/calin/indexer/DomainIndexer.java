package ro.calin.indexer;

import static ro.calin.Constants.FIELD_CONTENT;
import static ro.calin.Constants.FIELD_TITLE;
import static ro.calin.Constants.FIELD_URL;
import static ro.calin.Constants.INDEX_DIRECTORY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.LockObtainFailedException;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.FrameTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;

import ro.calin.LogHelper;
import ro.calin.Utils;
import ro.calin.crowler.HypertextCrawler;
import ro.calin.crowler.HypertextCrawlerProcessor;
import ro.calin.crowler.HypertextDocumentParser;
import ro.calin.crowler.HypertextDocumentParserException;
import ro.calin.crowler.ParsedHypertextDocument;

/**
 * Indexeaza continutul unor domenii.
 * 
 * @author Calin
 */
public class DomainIndexer {
	static LogHelper log = LogHelper.getInstance(DomainIndexer.class);

	public static final String PARSABLE_CONTENT_TYPE = "text/.*";
	/**
	 * Expresie regulata folosita pentru a depista link-urile dupa parsarea
	 * continutului.
	 */
	private static final String LINK_REGEX = "<[^>]*>";

	/**
	 * Expresie regulata folosita pentru a elimina spatiile nenecesare.
	 */
	private static final String EMPTY_REGEX = "\\s+";

	/**
	 * Pattern-ul folosit pentru a extrage link-urile.
	 */
	private static final Pattern pattern = Pattern.compile(LINK_REGEX);

	/**
	 * Filtrare link-uri care contin numele documentului in url dupa extensii
	 * nesuportate.
	 */
	private static final String[] unsuportedExtensions = { ".jpg", ".jpeg", ".jpe", ".jif", ".jfif", ".jfi", ".gif",
			".png", ".tiff", ".tif", ".pdf", ".ps", ".doc", ".rtf", ".xls", ".zip", ".rar" };

	/**
	 * Clasa interioara folosita la parsat documente html.
	 * 
	 * @author Calin
	 */
	private class WebPageParser implements HypertextDocumentParser {

		/**
		 * Tine lista de link-uri vizitate in aceasta sesiune.
		 */
		private HashSet<String> visitedLinks = new HashSet<String>();

		@Override
		public ParsedWebPage parse(String url, boolean parseLinks) throws HypertextDocumentParserException {
			try {

				Parser htmlParser = new Parser(url);

				String contentType = htmlParser.getConnection().getContentType();

				if (contentType == null || !contentType.matches(PARSABLE_CONTENT_TYPE))
					throw new HypertextDocumentParserException("Unsuported content type.");

				ParsedWebPage page = new ParsedWebPage();

				page.setContentType(contentType);

				page.setUrl(url);

				List<String> links = new ArrayList<String>();

				// get the title
				try {
					NodeList ttls = htmlParser.extractAllNodesThatMatch(new NodeClassFilter(TitleTag.class));
					page.setTitle(((TitleTag) ttls.elementAt(0)).getTitle());
				} catch (Exception e) {
					page.setTitle(page.getTitle());
				} finally {
					htmlParser.reset();
				}

				// get the content and the links
				StringBean stringBean = new StringBean();
				stringBean.setLinks(true);

				htmlParser.visitAllNodesWith(stringBean);

				String htmlString = stringBean.getStrings();

				// parse the content and get string and the links
				Matcher matcher = pattern.matcher(htmlString);

				page.setStringContent(matcher.replaceAll(" ").replaceAll(EMPTY_REGEX, " "));

				if (parseLinks) {

					// extract the url from frames, if any
					try {
						NodeList frms = htmlParser.extractAllNodesThatMatch(new NodeClassFilter(FrameTag.class));

						for (int i = 0; i < frms.size(); i++) {
							String link = ((FrameTag) frms.elementAt(0)).getFrameLocation();

							if (isValidLink(link))
								links.add(link);
						}
					} catch (Exception e) {
					} finally {
						htmlParser.reset();
					}

					matcher.reset();

					while (matcher.find()) {
						String match = matcher.group();
						// the form is <http://...>, so we remove the < and >
						String link = match.substring(1, match.length() - 1);

						if (isValidLink(link))
							links.add(link);
					}

					// add the links to parsed document
					page.setLinks(links);
				}

				return page;

			} catch (Exception e) {
				throw new HypertextDocumentParserException(e);
			}
		}

		/**
		 * Verifica validitatea unui link mai intai filtrand dupa extensie, daca
		 * exista si apoi asigurandu-se ca face parte din lista de domenii ce
		 * trebuie indexata.
		 * 
		 * @param link
		 * @return
		 */
		synchronized private boolean isValidLink(String link) {
			//TODO: filter links to portions of the same page
			// http://([\\w\\d]+\\.)+(ro|com|org|net...etc)[^#]*
			
			boolean isFromADValidDomain = false;

			for (String filter : domainFilters) {
				if (link.matches(filter)) {
					isFromADValidDomain = true;
					break;
				}
			}

			if (!isFromADValidDomain)
				return false;

			for (String unsuportedExt : unsuportedExtensions) {
				if (link.endsWith(unsuportedExt))
					return false;
			}

			if (visitedLinks.contains(link))
				return false;

			visitedLinks.add(link);

			return true;
		}
	}

	/**
	 * Clasa insarcinata cu indexarea continutului unui document parsat si
	 * redeschiderea indexului la terminarea job-ului de indexare.
	 * 
	 * @author Calin
	 */
	private class ParsedWebPageProcessor implements HypertextCrawlerProcessor {
		@Override
		synchronized public void handleParsedDocument(ParsedHypertextDocument pdoc) {
			if (pdoc instanceof ParsedWebPage) {
				ParsedWebPage ppage = (ParsedWebPage) pdoc;

				Document doc = new Document();

				doc.add(new Field(FIELD_URL, ppage.getUrl(), Field.Store.YES, Field.Index.NO));
				doc.add(new Field(FIELD_TITLE, ppage.getTitle(), Field.Store.YES, Field.Index.NO));
				doc.add(new Field(FIELD_CONTENT, ppage.getStringContent(), Field.Store.YES, Field.Index.ANALYZED));

				try {
					writer.addDocument(doc);
				} catch (Exception e) {
					if (log.isDebugEnabled())
						log.fatal("Index update error: ", e);
				}
			}
		}

		@Override
		synchronized public void handleFinishedJob() {
			try {
				writer.optimize();
				writer.close();
				writer = null;

				Utils.reopenSearcher(applicationState);
				Utils.reopenReader(applicationState);

				running = false;
			} catch (Exception e) {
				if (log.isDebugEnabled())
					log.fatal("Index optimize/close error: ", e);
			}
		}
	}

	/**
	 * Index writer
	 */
	private IndexWriter writer = null;

	/**
	 * Tabel asociativ ce contine informatii despre aplicatie, si obiecte
	 * folosite la interogarea indexului.
	 */
	private Map<String, Object> applicationState = null;

	/**
	 * Lista de domenii.
	 */
	private String[] domainFilters = null;

	/**
	 * Flag care indica daca se indexeaza in momentul de fata.
	 */
	private boolean running = false;

	public boolean isRunning() {
		return running;
	}

	/**
	 * Incepe indexarea listei de domenii.
	 * 
	 * @param state
	 * @param domains
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 */
	public void start(Map<String, Object> state, String... domains) throws CorruptIndexException,
			LockObtainFailedException, IOException, IllegalArgumentException, IllegalStateException {

		if (state == null || domains == null || domains.length == 0) {
			throw new IllegalArgumentException("Null args or empty domain list.");
		}

		if (running) {
			throw new IllegalStateException("Indexer is running.");
		}

		applicationState = state;

		domainFilters = new String[domains.length];
		for (int i = 0; i < domains.length; i++) {
			domainFilters[i] = "http://(www.)?" + domains[i] + ".*";
		}

		writer = new IndexWriter(INDEX_DIRECTORY, Utils.getAnalyzer(applicationState), new IndexWriter.MaxFieldLength(
				1000000));

		String[] fullDomains = new String[domains.length];

		for (int i = 0; i < domains.length; i++) {
			fullDomains[i] = "http://" + domains[i];
		}

		HypertextCrawler crawler = new HypertextCrawler(new ParsedWebPageProcessor(), new WebPageParser(),
				20/* threads */, 3/* depth */, fullDomains);

		new Thread(crawler).start();

		running = true;
	}

	public void stop() throws Exception {

	}

	private DomainIndexer() {
	}

	/**
	 * Instanta singleton.
	 */
	static DomainIndexer instance = null;

	/**
	 * Intoarce instanta singleton.
	 * 
	 * @return
	 */
	public static DomainIndexer getSingleton() {
		if (instance == null) {
			instance = new DomainIndexer();
		}

		return instance;
	}
}
