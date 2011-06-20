package ro.calin;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Project: ITSO PiggyBank
 * 
 * This class provides a log and trace facility to the PiggyBank application. It
 * is implemented as a wrapper around the Jakarta Log4J logging facility, to
 * enable the underlying logging framework to be changed without rewriting
 * application code.
 * 
 * @author markus.meser@ch.ibm.com
 */
public class LogHelper {

    private static final String LOG_CONFIG_FILE = "cfg/log4j.xml";
    // Static variables
    private static boolean initialized = false;

    // Instance variables
    private Category category = null;

    /**
     * Creates a new LogHelper instance for a component
     */
    private LogHelper(Class<?> component) {
        super();

        // Initialize the logging system if required
        if (!initialized) {
            init();
        }

        // Register this component as a Log4J category
        category = Logger.getLogger(component);

    }

    /**
     * Creates a new LogHelper instance for a component.
     */
    public static synchronized LogHelper getInstance(Class<?> component) {
        return new LogHelper(component);
    }

    /**
     * Logs a debug message
     * 
     * @param o
     *            java.lang.Object The message to be written to the log
     */
    public void debug(Object o) {
        category.debug(o);
    }

   
    /**
     * Logs a debug message including stack trace from an exception
     * 
     * @param o
     *            java.lang.Object The message to be written to the log
     * @param t
     *            java.lang.Throwable The exception
     */
    public void debug(Object o, Throwable t) {
        category.debug(o, t);
    }

    /**
     * Logs an error message
     * 
     * @param o
     *            java.lang.Object The message to be written to the log
     */
    public void error(Object o) {
        category.error(o);
    }

    /**
     * Logs an error message including stack trace from an exception
     * 
     * @param o
     *            java.lang.Object The message to be written to the log
     * @param t
     *            java.lang.Throwable The exception
     */
    public void error(Object o, Throwable t) {
        category.error(o, t);
    }

    /**
     * Logs an informational message
     * 
     * @param o
     *            java.lang.Object The message to be written to the log
     */
    public void info(Object o) {
        category.info(o);
    }

    /**
     * Logs an informational message including stack trace from an exception
     * 
     * @param o
     *            java.lang.Object The message to be written to the log
     * @param t
     *            java.lang.Throwable The exception
     */
    public void info(Object o, Throwable t) {
        category.info(o, t);
    }

    /**
     * Initialize the underlying logging system that this class wraps.
     * 
     * @param loggingConfigFile
     *            an <code>URL</code> specifying the logging configuration
     *            file.
     */
    private synchronized void init() {
        // Safeguard against possible race condition
        if (initialized) {
            return;
        }

        // Use a Log4J DOMConfigurator to load logging information from
        // a xml file. configureAndWatch() will start a thread to
        // check the properties file every 60 seconds to see if it has changed,
        // and reload the configuration if necessary.
        //DOMConfigurator.configureAndWatch(log4ConfigFile, 60000);
        DOMConfigurator.configure(Loader.getResource(LOG_CONFIG_FILE));

        // We have now initialized the logging system successfully
        initialized = true;
    }

    /**
     * Returns true if debug level logging is enabled for this component
     * 
     * @return boolean
     */
    public boolean isDebugEnabled() {
        return category.isDebugEnabled();
    }

    /**
     * Returns true if info level logging is enabled for this component
     * 
     * @return boolean
     */
    public boolean isInfoEnabled() {
        return category.isInfoEnabled();
    }

    /**
     * Logs a warning message
     * 
     * @param o
     *            java.lang.Object The message to be written to the log
     */
    public void warn(Object o) {
        category.warn(o);
    }

    /**
     * Logs a warning message including stack trace from an exception
     * 
     * @param o
     *            java.lang.Object The message to be written to the log
     * @param t
     *            java.lang.Throwable The exception
     */
    public void warn(Object o, Throwable t) {
        category.warn(o, t);
    }

    /**
     * Logs a fatal message
     * 
     * @param o
     *            java.lang.Object The message to be written to the log
     */
    public void fatal(Object o) {
        category.fatal(o);
    }

    /**
     * Logs a fatal message including stack trace from an exception
     * 
     * @param o
     *            java.lang.Object The message to be written to the log
     * @param t
     *            java.lang.Throwable The exception
     */
    public void fatal(Object o, Throwable t) {
        category.fatal(o, t);
    }
}
