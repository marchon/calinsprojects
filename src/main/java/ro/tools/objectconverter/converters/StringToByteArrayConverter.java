package ro.tools.objectconverter.converters;

import ro.tools.objectconverter.Converter;

/**
 * Converts a String to byte[].
 * 
 */
public class StringToByteArrayConverter implements Converter {

    @Override
    public Object convert(Object source) {
        if(source instanceof String) {
            return ((String)source).getBytes();
        }
        
        return null;
    }

}
