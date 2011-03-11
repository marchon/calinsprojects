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
    
    public static class A1 {
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
    
    public static class B1 {
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
    
    @Test
    public void testBasicCase() {
        final A1 a = new A1();
        a.a = 5;
        a.b = true;
        
        final B1 b = new B1();
        b.a = 0;
        b.b = false;
        
        ObjectConverter.convert(a, b);
        
        assertEquals(a.getA(), b.getA());
        assertEquals(a.isB(), b.isB());
    }
    
    public static class A2 {
        private int a;
        private String b;
        private Date c;
        private Integer d;
		public int getA() {
			return a;
		}
		public void setA(int a) {
			this.a = a;
		}
		public String getB() {
			return b;
		}
		public void setB(String b) {
			this.b = b;
		}
		public Date getC() {
			return c;
		}
		public void setC(Date c) {
			this.c = c;
		}
		public Integer getD() {
			return d;
		}
		public void setD(Integer d) {
			this.d = d;
		}
    }
    
    public static class B2 {
        @Convert(exclude=true)
        private int a;
        
        private String b;
        
        @Convert(group="g1")
        private Date c;
        
        @Convert(group="g2")
        private Integer d;

		public int getA() {
			return a;
		}

		public void setA(int a) {
			this.a = a;
		}

		public String getB() {
			return b;
		}

		public void setB(String b) {
			this.b = b;
		}

		public Date getC() {
			return c;
		}

		public void setC(Date c) {
			this.c = c;
		}

		public Integer getD() {
			return d;
		}

		public void setD(Integer d) {
			this.d = d;
		}
    }
    
    @Test
    public void testExcludeAndGrouping() {
        final A2 a = new A2();
        a.a = 5;
        a.b = "test";
        a.c = new Date();
        a.d = 10;
        
        final B2 b = new B2();
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

    public static class A3 {
        private int differentName;

		public int getDifferentName() {
			return differentName;
		}

		public void setDifferentName(int differentName) {
			this.differentName = differentName;
		}
    }
    
    public static class B3 {
        @Convert(mapping="differentName")
        private int a;

		public int getA() {
			return a;
		}

		public void setA(int a) {
			this.a = a;
		}
    }
    
    @Test
    public void testMappingToDifferentFieldName() {
        final A3 a = new A3();
        a.differentName = 5;
        
        final B3 b = new B3();
        b.a = 0;
        
        ObjectConverter.convert(a, b);
        
        assertEquals(a.differentName, b.a);
    }
    
    
    public static abstract class SuperSuperA4 {
        private int a;

		public int getA() {
			return a;
		}

		public void setA(int a) {
			this.a = a;
		}
    }
    
    public static abstract class SuperA4 extends SuperSuperA4 {
    }
    public static class A4 extends SuperA4 {
    }
    
    public static class B4 {
    	private int a;

		public int getA() {
			return a;
		}

		public void setA(int a) {
			this.a = a;
		}
    }
    @Test
    public void testCopyFieldFromSuperclassDefinition() {
        final A4 a = new A4();
        a.setA(5);
        
        final B4 b = new B4();
        b.a = 0;
        
        ObjectConverter.convert(a, b);
        
        assertEquals(a.getA(), b.a);
    }
    
    public static abstract class SuperSuperA5 {}
    public static abstract class SuperA5 extends SuperSuperA5 {}
    public static class A5 extends SuperA5 {}
    
    public static class B5 {
        private int a;

		public int getA() {
			return a;
		}

		public void setA(int a) {
			this.a = a;
		}
    }
    
    @Test
    public void testNoSuchFieldFoundInClassHierarchy() {
        final A5 a = new A5();
        
        final B5 b = new B5();
        b.a = 0;
        
        ObjectConverter.convert(a, b);
        
        assertEquals(0, b.a);
    }
    
    public static class A6 {
        byte[] a;

		public byte[] getA() {
			return a;
		}

		public void setA(byte[] a) {
			this.a = a;
		}
    }
    
    public static class B6 {
        @Convert(convertor=Object.class)
        String a;

		public String getA() {
			return a;
		}

		public void setA(String a) {
			this.a = a;
		}
    }
    
    @Test(expected=ConverterException.class)
    public void testBadConverter() {
        
        
        final A6 a = new A6();
        final B6 b = new B6();
        
        ObjectConverter.convert(a, b);
    }
    
    
    public static class A7 {
        @Convert(convertor=StringToByteArrayConverter.class)
        byte[] a;

		public byte[] getA() {
			return a;
		}

		public void setA(byte[] a) {
			this.a = a;
		}
    }
    
    public static class B7 {
        @Convert(convertor=ByteArrayToStringConverter.class)
        String a;

		public String getA() {
			return a;
		}

		public void setA(String a) {
			this.a = a;
		}
    }
    @Test
    public void testConvertorIsApplied() {
        final A7 a = new A7();
        a.a = "test".getBytes();
        final B7 b = new B7();
        
        ObjectConverter.convert(a, b);
        
        assertEquals("test", b.a);
        
        //test reverse
        b.a = "tset";
        ObjectConverter.convert(b, a);
        assertArrayEquals("tset".getBytes(), a.a);
    }
    
    public static class EmbedA8 {
        int a;

		public int getA() {
			return a;
		}

		public void setA(int a) {
			this.a = a;
		}
    }
    
    public static class A8 {
        private EmbedA8 ea = new EmbedA8();

		public EmbedA8 getEa() {
			return ea;
		}

		public void setEa(EmbedA8 ea) {
			this.ea = ea;
		}
    }
    
    public static interface InterfEmbedB8 {
        int getA();
        void setA(int a);
    }
    
    public static class EmbedB8 implements InterfEmbedB8 {
        int a;

        @Override
        public int getA() {
            return a;
        }

        @Override
		public void setA(int a) {
			this.a = a;
		}
    }
    
    public static class B8 {
        @Convert(mapping="ea")
        private EmbedB8 eb;

		public EmbedB8 getEb() {
			return eb;
		}

		public void setEb(EmbedB8 eb) {
			this.eb = eb;
		}
    }
    
    @Test
    public void testRecursiveConversion() {
        final A8 a = new A8();
        a.ea.a = 5;
        
        final B8 b = new B8();
        
        ObjectConverter.convert(a, b);
        
        assertEquals(a.ea.a, b.eb.a);
    }
    
    public static class EmbedA9 {
        int a;

		public int getA() {
			return a;
		}

		public void setA(int a) {
			this.a = a;
		}
    }
    
    public static class A9 {
        EmbedA9 ea = new EmbedA9();

		public EmbedA9 getEa() {
			return ea;
		}

		public void setEa(EmbedA9 ea) {
			this.ea = ea;
		}
    }
    
    public static class B9 {
        @Convert(mapping="ea", type=EmbedB8.class)
        InterfEmbedB8 eb;

		public InterfEmbedB8 getEb() {
			return eb;
		}

		public void setEb(InterfEmbedB8 eb) {
			this.eb = eb;
		}
    }
    
    @Test
    public void testRecursiveConversionWithUninstantiableField() {
        final A9 a = new A9();
        a.ea.a = 5;
        
        final B9 b = new B9();
        
        ObjectConverter.convert(a, b);
        
        assertEquals(a.ea.a, b.eb.getA());
    }
    
     
    public static class B10 {
        @Convert(mapping="ea")
        InterfEmbedB8 eb;

		public InterfEmbedB8 getEb() {
			return eb;
		}

		public void setEb(InterfEmbedB8 eb) {
			this.eb = eb;
		}
    }
    
    @Test(expected=ConverterException.class)
    public void testRecursiveConversionWithUninstantiableFieldErrorIfTypeNotProvidedInAnnotation() {
        final A9 a = new A9();
        a.ea.a = 5;
        
        final B10 b = new B10();
        
        ObjectConverter.convert(a, b);
    }
    
    public static class A11 {
    	private EmbedA8[] arr;

		public EmbedA8[] getArr() {
			return arr;
		}

		public void setArr(EmbedA8[] arr) {
			this.arr = arr;
		}
    }
    
    public static class B11 {
    	@Convert(type=EmbedB8.class)
    	private EmbedB8[] arr;

		public EmbedB8[] getArr() {
			return arr;
		}

		public void setArr(EmbedB8[] arr) {
			this.arr = arr;
		}
    }
    
    @Test
    public void testObjectConversionWithArray() {
    	final A11 a = new A11();
    	EmbedA8 ea1 = new EmbedA8();
    	ea1.a = 11;
    	EmbedA8 ea2 = new EmbedA8();
    	ea2.a = 12;
        a.arr = new EmbedA8[]{ea1, ea2};
        
        final B11 b = new B11();
        
        ObjectConverter.convert(a, b);
        
        assertEquals(a.arr[0].a, b.arr[0].a);
        assertEquals(a.arr[1].a, b.arr[1].a);
    }
}
