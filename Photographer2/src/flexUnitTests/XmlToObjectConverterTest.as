package flexUnitTests
{
	import flexunit.framework.Assert;
	
	import mx.collections.IList;
	
	import ro.calin.utils.XmlToObjectConverter;
	
	public class XmlToObjectConverterTest
	{	
		private var xml:XML;
		
		[Before]
		public function setUp():void
		{
			xml = <test a="123" b="test">
					<c embdA="123.123" />
					<embeds>
						<embed embdA="1.1" />
						<embed embdA="1.2" />
						<embed embdA="1.3" />
					</embeds>
				  </test>;
		}
		
		
		[Test]
		public function testConvertToObject():void
		{
			var obj:TestObject = new TestObject();
			
			XmlToObjectConverter.convertToObject(xml, obj);
			
			Assert.assertEquals(123, obj.a);
			Assert.assertEquals("test", obj.b);
			Assert.assertEquals(123.123, obj.c.embdA);
			
			Assert.assertEquals(1.1, (obj.embeds.getItemAt(0) as TestEmbedded).embdA);
			Assert.assertEquals(1.2, (obj.embeds.getItemAt(1) as TestEmbedded).embdA);
			Assert.assertEquals(1.3, (obj.embeds.getItemAt(2) as TestEmbedded).embdA);
		}
	}
}