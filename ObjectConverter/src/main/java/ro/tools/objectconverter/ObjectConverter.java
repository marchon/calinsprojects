package ro.tools.objectconverter;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Copies property values from one object to another.
 * Can do the same for a list of objects.
 * 
 * TODO:
 *  <ul>
 *      <li>1. handle arrays and hash maps</li>
 *      <li>2. handle reverse mapping, in which the metadata is provided in the source field</li>  
 *      <li>3. add logging</li>
 *      <li>4. finish unit testing</li>
 *      <li>5. test with SecurityManager (getDeclaredFields() throws SecurityError)</li>
 *      <li>6. cache instances of converter implementations</li>
 *      <li>7. try to get source fields from parent interfaces (it supports just abstract)</li>
 *      <li>8. better support for collections(see notes)</li>
 *  </ul>
 */
public class ObjectConverter {
    private ObjectConverter() {
    }

    /**
     * Maps java primitive types to their wrapper counterparts.
     */
    private static Map<Class<?>, Class<?>> primitives2Wrappers = new HashMap<Class<?>, Class<?>>();

    static {
        primitives2Wrappers.put(boolean.class, Boolean.class);
        primitives2Wrappers.put(char.class, Character.class);
        primitives2Wrappers.put(byte.class, Byte.class);
        primitives2Wrappers.put(short.class, Short.class);
        primitives2Wrappers.put(int.class, Integer.class);
        primitives2Wrappers.put(long.class, Long.class);
        primitives2Wrappers.put(float.class, Float.class);
        primitives2Wrappers.put(double.class, Double.class);
        primitives2Wrappers.put(void.class, Void.class);
    }

    /**
     * Tests if an object pointed by a reference of type destType can be assigned to a reference of type srcType.
     * 
     * This is possible if destType is a superclass or through boxing/unboxing.
     * 
     * @param srcType
     * @param destType
     * @return
     */
    private static boolean areTypesConvertable(Class<?> srcType, Class<?> destType) {
    	System.out.println(srcType.getName() + " -> " + destType.getName());
    	
        if (destType.isAssignableFrom(srcType))
            return true;

        final Class<?> destWrapper = primitives2Wrappers.get(destType);
        final Class<?> srcWrapper = primitives2Wrappers.get(srcType);
        // handle primitive to wrapper mapping
        return (destWrapper != null && destWrapper.isAssignableFrom(srcType))
                || (srcWrapper != null && srcWrapper.isAssignableFrom(destType));
    }
    
    /**
     * Instantiates a converter.
     * 
     * @param convertorClass
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private static Converter getConvertor(Class<? extends Converter> convertorClass) 
        throws InstantiationException, IllegalAccessException {
        return convertorClass.newInstance();
    }
    
    private static Convert getAnnotation(Class<?> clazz, String fieldName) throws SecurityException, NoSuchFieldException {
    	return clazz.getDeclaredField(fieldName).getAnnotation(Convert.class);
    }
    
    /**
     * Copies values of properties from one object to another.
     * It does this in the following way:
     * <ul>
     *      <li>1. loop over destination properties</li>
     *      <li>2. for each property in destination do:
     *          <ul>
     *              <li>2.1. skip if excluded or not in provided groups</li>
     *              <li>2.2. find corresponding property in source(see annotation {@link Convert} for mappings); skip if not found</li>
     *              <li>2.3. if there is a {@link Converter} specified (see {@link Convert} for details on specifying a converter), apply converter</li>
     *              <li>2.4. else, if types are compatible, just assign object referenced by src prop to dest prop</li>
     *              <li>2.5. else, if types are not compatible do:
     *                  <ul>
     *                      <li>2.5.1. if types are some kind of lists, apply this algorithm for each element</li>
     *                      <li>2.5.2. else, if references are of different types, apply this algorithm recursively on the objects</li>
     *                  </ul>
     *              </li>
     *          </ul>
     *      </li>
     * </ul>
     *  
     * @param source
     * @param destination
     * @param groups list of groups that will be taken in account (see {@link Convert})
     */
    @SuppressWarnings("unchecked")
    public static void convert(Object source, Object destination, String... groups) {
        if (source == null || destination == null) {
            throw new ConverterException("No null parameters.");
        }
        
        final List<String> groupList = Arrays.asList(groups);
        
        //loop through all destination properties
        //TODO: implement reverse: loop through all props of src if src is annotated
        //with reverse
        for (PropertyDescriptor destPd : PropertyUtils.getPropertyDescriptors(destination)) {
            
            //exclude inner classes back refs
            if(destPd.getName().equals("class")) continue;
            
            Convert metadata = null;
			try {
				metadata = getAnnotation(destination.getClass(), destPd.getName());
			} catch (Exception e) {
				//TODO: log it, an maybe handle Security exception
			}
            
            //defaults
            String mapping = "";
            Class<?> convertor = void.class; 
            String group = "";
            boolean exclude = false;
            Class<?>[] type = {};
            
            //get it from annotation, if it has one
            if(metadata != null) {
                mapping = metadata.mapping();
                convertor = metadata.convertor();
                
                //hack to fix annotation+generics bug(see Convert annotation)
                if(convertor != void.class && !(Converter.class.isAssignableFrom(convertor))) {
                    throw new ConverterException("This is not a convertor: " + metadata.convertor().getName());
                }
                
                group = metadata.group();
                exclude = metadata.exclude();
                type = metadata.type();
            }
            
            //skip if this field is excluded
            if(exclude) continue;
            
            //skip if the field is in a group not specified
            if(!"".equals(group) && !groupList.contains(group)) continue;
            
            //map to a prop with same name if not specified
            if("".equals(mapping)) mapping = destPd.getName();
            
            PropertyDescriptor srcPd = null;
			try {
				srcPd = PropertyUtils.getPropertyDescriptor(source, mapping);
			} catch (Exception e) {
				//TODO: don't care, just log
			}
            
            //no such property, log and skip, nothing to convert from
            if(srcPd == null) continue;
            
            try {
                Object valueToBeConverted = PropertyUtils.getProperty(source, srcPd.getName()); 
                
                //nothing to convert for this field
                if (valueToBeConverted == null) continue;
                
                //if a converter is provided, apply converter
                if(convertor != void.class) {
                    Converter convertorInstance = null;
                    convertorInstance = getConvertor((Class<? extends Converter>)convertor);

                    if(convertorInstance != null) {
                        valueToBeConverted = convertorInstance.convert(valueToBeConverted);
                    }
                //else if they cannot be assigned by default, go recursively, depending on type(Array, collection, map, normal object)
                } else if(valueToBeConverted.getClass().isArray() && type.length > 0) {
            		Object[] destArray = (Object[])getOrCreateObjectProperty(valueToBeConverted, destination, destPd, type);
                	convert((Object[])valueToBeConverted, (Object[])destArray, type[0], groups);
                	valueToBeConverted = destArray;
                } else if(valueToBeConverted instanceof Collection && type.length > 0) {
                	Collection destCollection = (Collection)getOrCreateObjectProperty(valueToBeConverted, destination, destPd, type);
                    convert((Collection)valueToBeConverted, (Collection)destCollection, type[0], groups);
                    valueToBeConverted = destCollection;
                } else if(valueToBeConverted instanceof Map && type.length > 1) {
                	Map destMap = (Map)getOrCreateObjectProperty(valueToBeConverted, destination, destPd, type);
                    convert((HashMap)valueToBeConverted, (HashMap)destMap, type[0], type[1], groups);
                    valueToBeConverted = destMap;
                } else if(!areTypesConvertable(srcPd.getPropertyType(), destPd.getPropertyType())) {
                	Object destObject = getOrCreateObjectProperty(valueToBeConverted, destination, destPd, type);
                	//this does not necessarily require a type
                    convert(valueToBeConverted, destObject, groups);
                    valueToBeConverted = destObject;
				}
                //else {nothing to be transformed}
                
                //copy the, now converted, value
                PropertyUtils.setProperty(destination, destPd.getName(), valueToBeConverted);
            } catch (Exception e) {
                throw new ConverterException("Error converting field " + destPd.getName(), e);
            } 
        }
    }
    
    /**
     * Helper. Get destination property or create the object if null.
     */
    private static Object getOrCreateObjectProperty(Object valueToBeConverted, Object destination, 
    		PropertyDescriptor destPd, Class<?>... type) 
    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
    	Object destObject = PropertyUtils.getProperty(destination, destPd.getName());
    	
    	//try to create, if null, depending on specified type
    	if(destObject == null) {
    		try {
            	destObject = destPd.getPropertyType().newInstance();
            } catch (InstantiationException e) {
            	//didn't work(interface, abstract, etc.), try workarounds
            	
            	//don't bother if type is not specified in annotation
            	if(type.length > 0) {
                	if(valueToBeConverted.getClass().isArray()) {
                		destObject = Array.newInstance(type[0], Array.getLength(valueToBeConverted));
                	} else if(valueToBeConverted instanceof Collection) {
                        //if abstract or interface try HashSet if subclass of Set, or ArrayList otherwise
                        //TODO: can we do better?
                        if(Set.class.isAssignableFrom(destPd.getPropertyType())) {
                        	destObject = new HashSet();
                        } else {
                        	destObject = new ArrayList();
                        }
                	} else if(valueToBeConverted instanceof Map && type.length > 1) {
                		//try HashMap
                    	destObject = new HashMap();
                	} else {
                    	destObject = type[0].newInstance();
                	}
            	}
            }
            
            if(destObject == null) {
        		throw new ConverterException("Cannot instantiate field " + 
                        destPd.getName() + ". Please provide a type.");
        	}
    	}
    	
    	return destObject;
	}

	/**
     * Applies method {@link ObjectConverter#convert(Object, Object, String...)} 
     * for each reference in srcCollection and a new object.
     * The destination object is instantiated and it is of type {@code destType}.
     * Assumes the destination collection is empty(clears it if not).
     * TODO: handle simple types??? just copy references?
     * 
     * @param srcCollection
     * @param destCollection
     * @param destType
     * @param groups
     */
    public static void convert(Collection<Object> srcCollection, Collection<Object> destCollection, Class<?> destType,
            String... groups) {
        if (destCollection.size() > 0)
            destCollection.clear();

        final Iterator<Object> srcIt = srcCollection.iterator();
        while (srcIt.hasNext()) {
            Object destObj;
            try {
            	//TODO, maybe if it is null, reference should be copied
                destObj = destType.newInstance();
            } catch (Exception e) {
                throw new ConverterException("Error instantiating object of type " + destType.getName(), e);
            }
            convert(srcIt.next(), destObj, groups);
            destCollection.add(destObj);
        }
    }
    
    /**
     * Applies method {@link ObjectConverter#convert(Object, Object, String...)} 
     * for each reference in srcArray and a new object.
     * The destination object is instantiated and it is of type {@code destType}.
     * Assumes the destination array is of equal size with source array.
     * TODO: handle simple types???
     * 
     * @param srcArray
     * @param destArray
     * @param destType
     * @param groups
     */
    public static void convert(Object[] srcArray, Object[] destArray, Class<?> destType,
            String... groups) {
        if (srcArray.length != destArray.length) 
        	throw new ConverterException("Arrays of unequal size!!!");

        for (int i = 0; i < srcArray.length; i++) {
            Object destObj;
            try {
                destObj = destType.newInstance();
            } catch (Exception e) {
                throw new ConverterException("Error instantiating object of type " + destType.getName(), e);
            }
            convert(srcArray[i], destObj, groups);
            destArray[i] = destObj;
        }
    }
    
    /**
     * Applies method {@link ObjectConverter#convert(Object, Object, String...)} 
     * for each key-value pair in srcArray and a newly created pair of objects.
     * The destination key-value pair objects are instantiated and they are of type
     * {@code destKeyType} and {@code destValueType}.
     * Assumes the destination map is empty(clears it if not).
     * 
     * TODO: handle simple types???
     * 
     * @param srcMap
     * @param destMap
     * @param destKeyType
     * @param destValueType
     * @param groups
     */
    public static void convert(Map<Object, Object> srcMap, Map<Object, Object> destMap, 
    		Class<?> destKeyType, Class<?> destValueType, String... groups) {
        if (destMap.size() > 0)
        	destMap.clear();

        final Iterator<Entry<Object, Object>> srcIt = srcMap.entrySet().iterator();
        while (srcIt.hasNext()) {
            Object destKey;
            Object destValue;
            try {
            	destKey = destKeyType.newInstance();
            	destValue = destValueType.newInstance();
            } catch (Exception e) {
                throw new ConverterException("Error instantiating key or value: ", e);
            }
            convert(srcIt.next().getKey(), destKey, groups);
            convert(srcIt.next().getValue(), destValue, groups);
            
            destMap.put(destKey, destValue);
        }
    }
}
