package ro.tools.objectconverter;

/**
 * Exception thrown when conversion failed.
 * This is a runtime exception, should not be handled,
 * instead, the annotations should be checked.
 * 
 */
public class ConverterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConverterException(final String msg) {
        super(msg);
    }

    public ConverterException(final Throwable cause) {
        super(cause);
    }

    public ConverterException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
