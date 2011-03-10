package ro.tools.objectconverter;

/**
 * An implementation of this interface converts an object to another object. 
 * All the responsibility is left the the implementer.
 * 
 */
public interface Converter {
    /**
     * @param source the object to be converted
     * @return converted object
     */
    Object convert(Object source);
}
