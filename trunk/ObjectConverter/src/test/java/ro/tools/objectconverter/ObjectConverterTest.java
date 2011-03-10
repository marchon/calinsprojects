package ro.tools.objectconverter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Date;

import org.junit.Test;

import ro.tools.objectconverter.converters.ByteArrayToStringConverter;
import ro.tools.objectconverter.converters.StringToByteArrayConverter;

public class ObjectConverterTest {
    @Test(expected=ConverterException.class)
    public void testNullParameters() {
        ObjectConverter.convert(null, null);
    }
    
    @Test
    public void testBasicCase() {
        class A {
            public int a;
        }
        
        class B {
            public int a;
        }
        
        final A a = new A();
        a.a = 5;
        
        final B b = new B();
        b.a = 0;
        
        ObjectConverter.convert(a, b);
        
        assertEquals(a.a, b.a);
    }
    
    @Test
    public void testBasicCaseWithSettersAndGetters() {
        class A {
            private int a;
            private boolean b;

            public boolean isB() {
                return b;
            }

            public void setB(boolean b) {
                this.b = b;
            }

            public int getA() {
                return a;
            }

            public void setA(int a) {
                this.a = a;
            }
        }
        
        class B {
            private int a;
            private boolean b;

            public boolean isB() {
                return b;
            }

            public void setB(boolean b) {
                this.b = b;
            }

            public int getA() {
                return a;
            }

            public void setA(int a) {
                this.a = a;
            }
        }
        
        final A a = new A();
        a.a = 5;
        a.b = true;
        
        final B b = new B();
        b.a = 0;
        b.b = false;
        
        ObjectConverter.convert(a, b);
        
        assertEquals(a.getA(), b.getA());
        assertEquals(a.isB(), b.isB());
    }
    
    @Test
    public void testExcludeAndGrouping() {
        class A {
            public int a;
            public String b;
            public Date c;
            public Integer d;
        }
        
        class B {
            @Convert(exclude=true)
            public int a;
            
            public String b;
            
            @Convert(group="g1")
            public Date c;
            
            @Convert(group="g2")
            public Integer d;
        }
        
        final A a = new A();
        a.a = 5;
        a.b = "test";
        a.c = new Date();
        a.d = 10;
        
        final B b = new B();
        b.a = 0;
        b.b = "test";
        //b.c = null; default
        b.d = -5;
        
        //not g2
        ObjectConverter.convert(a, b, "g1");
        
        //should not be changed because excluded
        assertEquals(0, b.a);
        
        assertEquals(a.b, b.b);
        
        assertEquals(a.c, b.c);
        assertSame(a.c, b.c); //should reference the same object
        
        //should not be changed because group not specified
        assertEquals(-5, b.d.intValue());
    }

    @Test
    public void testMappingToDifferentFieldName() {
        class A {
            public int differentName;
        }
        
        class B {
            @Convert(mapping="differentName")
            public int a;
        }
        
        final A a = new A();
        a.differentName = 5;
        
        final B b = new B();
        b.a = 0;
        
        ObjectConverter.convert(a, b);
        
        assertEquals(a.differentName, b.a);
    }
    
    @Test
    public void testCopyFieldFromSuperclassDefinition() {
        abstract class SuperSuperA {
            public int a;
        }
        
        abstract class SuperA extends SuperSuperA {
        }
        class A extends SuperA {
        }
        
        class B {
            public int a;
        }
        
        final A a = new A();
        a.a = 5;
        
        final B b = new B();
        b.a = 0;
        
        ObjectConverter.convert(a, b);
        
        assertEquals(a.a, b.a);
    }
    
    @Test
    public void testNoSuchFieldFoundInClassHierarchy() {
        abstract class SuperSuperA {}
        abstract class SuperA extends SuperSuperA {}
        class A extends SuperA {}
        
        class B {
            public int a;
        }
        
        final A a = new A();
        
        final B b = new B();
        b.a = 0;
        
        ObjectConverter.convert(a, b);
        
        assertEquals(0, b.a);
    }
    
    @Test(expected=ConverterException.class)
    public void testBadConverter() {
        class A {
            byte[] a;
        }
        
        class B {
            @Convert(convertor=Object.class)
            String a;
        }
        
        final A a = new A();
        final B b = new B();
        
        ObjectConverter.convert(a, b);
    }
    
    @Test
    public void testConvertorIsApplied() {
        class A {
            @Convert(convertor=StringToByteArrayConverter.class)
            byte[] a;
        }
        
        class B {
            @Convert(convertor=ByteArrayToStringConverter.class)
            String a;
        }
        
        final A a = new A();
        a.a = "test".getBytes();
        final B b = new B();
        
        ObjectConverter.convert(a, b);
        
        assertEquals("test", b.a);
        
        //test reverse
        b.a = "tset";
        ObjectConverter.convert(b, a);
        assertArrayEquals("tset".getBytes(), a.a);
    }
    
    public static interface InterfEmbedB {
        int getA();
    }
    
    public static class EmbedB implements InterfEmbedB {
        int a;

        @Override
        public int getA() {
            return a;
        }
    }
    
    @Test
    public void testRecursiveConversion() {
        class EmbedA {
            int a;
        }
        
        class A {
            EmbedA ea = new EmbedA();
        }
        
        class B {
            @Convert(mapping="ea")
            EmbedB eb;
        }
        
        final A a = new A();
        a.ea.a = 5;
        
        final B b = new B();
        
        ObjectConverter.convert(a, b);
        
        assertEquals(a.ea.a, b.eb.a);
    }
    
    @Test
    public void testRecursiveConversionWithUninstantiableField() {
        class EmbedA {
            int a;
        }
        
        class A {
            EmbedA ea = new EmbedA();
        }
        
        class B {
            @Convert(mapping="ea", type=EmbedB.class)
            InterfEmbedB eb;
        }
        
        final A a = new A();
        a.ea.a = 5;
        
        final B b = new B();
        
        ObjectConverter.convert(a, b);
        
        assertEquals(a.ea.a, b.eb.getA());
    }
    
    @Test(expected=ConverterException.class)
    public void testRecursiveConversionWithUninstantiableFieldErrorIfTypeNotProvidedInAnnotation() {
        class EmbedA {
            int a;
        }
        
        class A {
            EmbedA ea = new EmbedA();
        }
        
        class B {
            @Convert(mapping="ea")
            InterfEmbedB eb;
        }
        
        final A a = new A();
        a.ea.a = 5;
        
        final B b = new B();
        
        ObjectConverter.convert(a, b);
    }
}
