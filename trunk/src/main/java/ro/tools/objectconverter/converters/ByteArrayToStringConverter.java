package ro.tools.objectconverter.converters;

import ro.tools.objectconverter.Converter;

/**
 * Converts a byte[] to a String.
 * 
 */
public class ByteArrayToStringConverter implements Converter {

    @Override
    public Object convert(Object source) {
        if(source instanceof byte[]) {
            return new String((byte[])source);
        }
        
        return null;
    }

}
