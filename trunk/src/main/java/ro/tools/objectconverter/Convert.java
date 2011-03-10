package ro.tools.objectconverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation that can be used to specify to {@link ObjectConverter} how
 * to handle conversion of objects.
 * 
 */
@Retention(RetentionPolicy.RUNTIME) public @interface Convert {
    
    /**
     * The name of the field in source object from which to convert.
     * If not specified, it will try to convert from a field with 
     * the same name as the one annotated.
     */
    String mapping() default "";
    
    //not using generics: Class<? extends Convertor> because of bug
    //http://netbeans.org/bugzilla/show_bug.cgi?id=193952#c4
    //"-proc:none is a fine workaround if no annotation processing is needed."
    //TODO: what is 'annotation processing'. Seems openjpa requires it.
    /**
     * An implementation of {@link Converter} which will be used to transform
     * the object referenced by the property of source object to an object that can be
     * assigned to the property annotated. 
     */
    Class<?> convertor() default void.class; 
    
    /**
     * A string which identifies a group. Properties can be grouped, and whole groups
     * can be ignored at conversion. See {@link ObjectConverter#convert(Object, Object, String...)}
     * last parameter.
     * If no group is specified, {@link ObjectConverter} will always 
     * try to convert to the field annotated.
     */
    String group() default "";
    
    /**
     * If true, the annotated field will be excluded from conversion.
     * By default it is false.
     */
    boolean exclude() default false;
    
    
    /**
     * Specifies type to instantiate for this field.
     * It will be used if the field is of a type that cannot be instantiated
     * or if the field is a collection/map/array, in this case representing the 
     * type of an element.
     * 
     * For map you must specify two types.
     * 
     * Eg.:
     * <br/>{@code @Convert(type=Date.class) }
     * <br/>{@code private List<Date> dates; } 
     * 
     * <br/>{@code @Convert(type={String.class, Date.class}) }
     * <br/>{@code private Map<String, Date> dates; } 
     * 
     */
    Class<?>[] type() default {};
}
