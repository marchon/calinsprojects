package ro.calin.ubiquity;

import static ro.calin.ubiquity.Constants.*;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class UbiRepoServlet extends HttpServlet {
	private static Gson googleJson = new Gson();
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		int reqType = -1;
		PersistenceManager pm = null;

		try {
			reqType = Integer.parseInt(request.getParameter(REQ_TYPE_PARAM));

			if (reqType >= REQ_GETFEED && reqType < REQ_TOTAL) {
				response.setContentType(REQ_RESP_CONTENT_TYPES[reqType]);
			} else {
				response.setContentType(CONTENT_TYPE_JSON);
			}

			pm = PMF.get().getPersistenceManager();

			switch (reqType) {
			case REQ_GETFEED:
				serviceRequestGetFeed(request, response, pm);
				break;
			case REQ_SETFEED:
				serviceRequestSetFeed(request, response, pm);
				break;
			case REQ_GETFEEDJS:
				serviceRequestGetFeedJs(request, response, pm);
				break;
			case REQ_CREATEFEED:
				serviceRequestCreateFeed(request, response, pm);
				break;
			case REQ_DELETEFEED:
				serviceRequestDeleteFeed(request, response, pm);
				break;
			case REQ_LISTFEED:
				serviceRequestListFeed(request, response, pm);
				break;
			default:
				response.getWriter().printf(RESPONSE, ERROR, DETAILS_BAD_REQ_CODE, "''");
				break;
			}
		} catch (NumberFormatException e) {
			response.setContentType(CONTENT_TYPE_JSON);
			
			ResponseContent rc = new ResponseContent(ERROR, DETAILS_IVLD_REQ, null);
			response.getWriter().print(googleJson.toJson(rc));
		} catch (Exception e) {
			ResponseContent rc = new ResponseContent(ERROR, e.toString(), null);
			response.getWriter().print(googleJson.toJson(rc));
			e.printStackTrace();
		} finally {
			if (pm != null) {
				pm.close();
				pm = null;
			}
		}
	}

	private void serviceRequestListFeed(HttpServletRequest request, HttpServletResponse response, PersistenceManager pm)
			throws ServletException, IOException {
		List<Feed> feeds = (List<Feed>) pm.newQuery("select from " + Feed.class.getName()).execute();

		String[] feedNames = new String[feeds.size()];
		for (int i = 0; i < feedNames.length; i++) {
			feedNames[i] = feeds.get(i).getFeedName();
		}
		
		ResponseContent rc = new ResponseContent(SUCCESS, null, feedNames);
		response.getWriter().print(googleJson.toJson(rc));
	}

	/**
	 * @param request
	 * @param response
	 * @param pm
	 * @throws ServletException
	 * @throws IOException
	 */
	private void serviceRequestGetFeed(HttpServletRequest request, HttpServletResponse response, PersistenceManager pm)
			throws ServletException, IOException {
		String name = request.getParameter(REQ_FEEDNAME_PARAM);

		List<Feed> feeds = (List<Feed>) pm.newQuery(QUERY).execute(name);
		ResponseContent rc = new ResponseContent();
		
		if (!feeds.isEmpty()) {
			rc.setSuccess(SUCCESS);
			
			if (feeds.size() > 1) {
				rc.setDetails(DETAILS_MLTPL_FEED);
			}

			rc.setContent(feeds.get(0).getFeedContent().getValue());
		} else {
			rc.setSuccess(ERROR);
			rc.setDetails(DETAILS_NO_SUCH_FEED);
		}
		
		response.getWriter().print(googleJson.toJson(rc));
	}

	/**
	 * @param request
	 * @param response
	 * @param pm
	 * @throws ServletException
	 * @throws IOException
	 */
	private void serviceRequestSetFeed(HttpServletRequest request, HttpServletResponse response, PersistenceManager pm)
			throws ServletException, IOException {

		String name = request.getParameter(REQ_FEEDNAME_PARAM);
		String content = request.getParameter(REQ_FEEDCONTENT_PARAM);

		List<Feed> feeds = (List<Feed>) pm.newQuery(QUERY).execute(name);
		ResponseContent rc = new ResponseContent();
		
		if (!feeds.isEmpty()) {
			Feed feed = feeds.get(0);
			feed.setFeedContent(new Text(content));

			rc.setSuccess(SUCCESS);
		} else {
			rc.setSuccess(ERROR);
			rc.setDetails(DETAILS_NO_SUCH_FEED);
		}
		
		response.getWriter().print(googleJson.toJson(rc));
	}

	/**
	 * @param request
	 * @param response
	 * @param pm
	 * @throws ServletException
	 * @throws IOException
	 */
	private void serviceRequestGetFeedJs(HttpServletRequest request, HttpServletResponse response, PersistenceManager pm)
			throws ServletException, IOException {
		String name = request.getParameter(REQ_FEEDNAME_PARAM);

		List<Feed> feeds = (List<Feed>) pm.newQuery(QUERY).execute(name);

		if (!feeds.isEmpty()) {
			response.getWriter().write(feeds.get(0).getFeedContent().getValue());
		} else {
			// error, it should not get to this
		}
	}

	private void serviceRequestCreateFeed(HttpServletRequest request, HttpServletResponse response,
			PersistenceManager pm) throws ServletException, IOException {
		String name = request.getParameter(REQ_FEEDNAME_PARAM);

		List<Feed> feeds = (List<Feed>) pm.newQuery(QUERY).execute(name);
		ResponseContent rc = new ResponseContent();
		
		if (feeds.isEmpty()) {
			Feed feed = new Feed(name, new Text(""));
			pm.makePersistent(feed);

			rc.setSuccess(SUCCESS);
		} else {
			rc.setSuccess(ERROR);
			rc.setDetails(DETAILS_FEED_EXISTS);
		}

		response.getWriter().print(googleJson.toJson(rc));
	}

	/**
	 * @param request
	 * @param response
	 * @param pm2
	 * @throws ServletException
	 * @throws IOException
	 */
	private void serviceRequestDeleteFeed(HttpServletRequest request, HttpServletResponse response,
			PersistenceManager pm) throws ServletException, IOException {
		String name = request.getParameter(REQ_FEEDNAME_PARAM);

		List<Feed> feeds = (List<Feed>) pm.newQuery(QUERY).execute(name);
		ResponseContent rc = new ResponseContent();

		if (!feeds.isEmpty()) {
			pm.deletePersistent(feeds.get(0));

			rc.setSuccess(SUCCESS);
			
			if (feeds.size() > 1) {
				rc.setDetails(DETAILS_MLTPL_FEED);
			}
		} else {
			rc.setSuccess(ERROR);
			rc.setDetails(DETAILS_NO_SUCH_FEED);
		}
		
		response.getWriter().print(googleJson.toJson(rc));
	}
}
