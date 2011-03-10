package ro.tools.objectconverter;

import java.beans.PropertyDescriptor;
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

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Copies property values from one object to another.
 * Can do the same for a list of objects.
 * 
 * TODO:
 *  <ul>
 *      <li>1. investigate potential usage of BeanUtils</li>
 *      <li>2. handle arrays and hash maps</li>
 *      <li>3. handle reverse mapping, in which the metadata is provided in the source field</li>  
 *      <li>4. add logging</li>
 *      <li>5. finish unit testing</li>
 *      <li>6. test with SecurityManager (getDeclaredFields() throws SecurityError)</li>
 *      <li>7. cache instances of converter implementations</li>
 *      <li>8. try to get source fields from parent interfaces (it supports just abstract)</li>
 *      <li>9. better support for collections(see notes)</li>
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
    private static boolean typesConvertable(Class<?> srcType, Class<?> destType) {
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
     *                      <li>2.5.1. if types are some kind of list, apply the algorithm from {@link ObjectConverter#convert(Collection, Collection, Class, String...)}}</li>
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
        for (PropertyDescriptor destPd : PropertyUtils.getPropertyDescriptors(source)) {
            
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
                //else if collection, convert recursively each member
                //TODO: handle arrays and hashmaps
                } else if(valueToBeConverted instanceof Collection && type.length > 0) {
                    Object destCollection = PropertyUtils.getProperty(destination, destPd.getName()); 
                    
                    if(destCollection == null) {
                        //1. try to instantiate
                        //2. if abstract or interface try HashSet if subclass of Set, or ArrayList otherwise
                        //TODO: can we do better?
                        try {
                            destCollection = destPd.getPropertyType().newInstance();
                        } catch (InstantiationException e) {
                            //revert to set or list(for now)
                            if(Set.class.isAssignableFrom(destPd.getPropertyType())) {
                                destCollection = new HashSet();
                            } else {
                                destCollection = new ArrayList();
                            }
                        }
                    }
                    //recursively convert each item in the collection
                    convert((Collection)valueToBeConverted, (Collection)destCollection, type[0], groups);
                    valueToBeConverted = destCollection;
                //else if not directly assignable, convert recursively
                } else if(!typesConvertable(srcPd.getPropertyType(), destPd.getPropertyType())){
                    Object destObj = PropertyUtils.getProperty(destination, destPd.getName());
                    if(destObj == null) {
                        //try to instantiate the field type, if we cannot, try the provided type, if exists
                        try {
                            destObj = destPd.getPropertyType().newInstance();
                        } catch (InstantiationException e) {
                            if(type.length > 0) {
                                destObj = type[0].newInstance();
                            } else {
                                throw new ConverterException("Cannot instantiate field " + 
                                        destPd.getName() + ". Please provide a type.");
                            }
                        }
                    }
                    //recursively convert properties from source to dest
                    convert(valueToBeConverted, destObj, groups);
                    valueToBeConverted = destObj;
                } else {/*nothing to be transformed*/}
                
                //copy the, now converted, value
                PropertyUtils.setProperty(destination, destPd.getName(), valueToBeConverted);
            } catch (Exception e) {
                throw new ConverterException("Error converting field " + destPd.getName(), e);
            } 
        }
    }
    
    /**
     * Applies method {@link ObjectConverter#convert(Object, Object, String...)} for each reference in srcCollection.
     * The destination object is instantiated and it is of type {@code destType}.
     * Assumes the destination collection is empty(clears it if not).
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
                destObj = destType.newInstance();
            } catch (Exception e) {
                throw new ConverterException("Error instantiating object of type " + destType.getName(), e);
            }
            convert(srcIt.next(), destObj, groups);
            destCollection.add(destObj);
        }
    }
}
