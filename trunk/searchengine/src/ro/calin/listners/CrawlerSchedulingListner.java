package ro.calin.listners;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ro.calin.LogHelper;
import ro.calin.Utils;

/**
 * Application Lifecycle Listener implementation class CrawlerSchedulingListner
 * 
 */
public class CrawlerSchedulingListner implements ServletContextListener {
	static LogHelper log = LogHelper.getInstance(CrawlerSchedulingListner.class);

	/**
	 * Default constructor.
	 */
	public CrawlerSchedulingListner() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();

		Map<String, Object> state = Utils.getStateMap(sc);

		// store searcher and reader
		Utils.reopenSearcher(state);
		Utils.reopenReader(state);
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();

		Map<String, Object> state = Utils.getStateMap(sc);

		// close the searcher and reader
		Utils.closeSearcher(state);
		Utils.closeReader(state);
	}

}
