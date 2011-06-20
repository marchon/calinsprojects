package ro.calin.ubiquity;

public class Constants {
	public final static int REQ_GETFEED 	= 0;
	public final static int REQ_SETFEED 	= 1;
	public final static int REQ_GETFEEDJS 	= 2;
	public final static int REQ_CREATEFEED 	= 3;
	public final static int REQ_DELETEFEED 	= 4;
	public final static int REQ_LISTFEED 	= 5;
	public final static int REQ_TOTAL 		= 6;

	public final static String REQ_TYPE_PARAM 			= "req";

	public final static String REQ_FEEDNAME_PARAM 		= "feed";
	public final static String REQ_FEEDCONTENT_PARAM 	= "cnt";
	
	public final static String CONTENT_TYPE_JSON 	= "application/json";
	public final static String CONTENT_TYPE_JS 		= "application/x-javascript";
	
	public final static String[] REQ_RESP_CONTENT_TYPES = {
		CONTENT_TYPE_JSON,
		CONTENT_TYPE_JSON,
		CONTENT_TYPE_JS,
		CONTENT_TYPE_JSON,
		CONTENT_TYPE_JSON,
		CONTENT_TYPE_JSON
	};
	
	public static final String RESPONSE = "{\"success\": %s, \"details\": %s, \"content\": %s}";

	public static final String QUERY = "select from " + Feed.class.getName()
			+ " where feedName == feedNameParam parameters String feedNameParam";
	
	public static final boolean SUCCESS = true;
	public static final boolean ERROR 	= false;
	
	public static final String DETAILS_BAD_REQ_CODE 	= "brqc";
	public static final String DETAILS_IVLD_REQ 		= "irq";
	public static final String DETAILS_MLTPL_FEED 		= "mtpl";
	public static final String DETAILS_NO_SUCH_FEED 	= "nsfe";
	public static final String DETAILS_FEED_EXISTS		= "fae";
}
