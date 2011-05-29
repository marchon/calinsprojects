package flexUnitTests
{
	import flash.utils.Dictionary;
	
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
					<map>
						<embed key="e1" embdA="1.1" />
						<embed key="e2" embdA="1.2" />
						<embed key="e3" embdA="1.3" />
					</map>
				
					<![CDATA[BLA BLA BLA]]>
				  </test>;
		}
		
		
		[Test]
		public function testConvertToObject():void
		{
			var obj:TestObject = new TestObject();
			
			var converter:XmlToObjectConverter = new XmlToObjectConverter();
			
			converter.convertToObject(xml, obj);
			
			Assert.assertEquals(123, obj.a);
			Assert.assertEquals("test", obj.b);
			Assert.assertEquals(123.123, obj.c.embdA);
			
			Assert.assertEquals(1.1, (obj.embeds.getItemAt(0) as TestEmbedded).embdA);
			Assert.assertEquals(1.2, (obj.embeds.getItemAt(1) as TestEmbedded).embdA);
			Assert.assertEquals(1.3, (obj.embeds.getItemAt(2) as TestEmbedded).embdA);
			
			Assert.assertEquals(1.1, (obj.map["e1"] as TestEmbedded).embdA);
			Assert.assertEquals(1.2, (obj.map["e2"] as TestEmbedded).embdA);
			Assert.assertEquals(1.3, (obj.map["e3"] as TestEmbedded).embdA);
			
			Assert.assertEquals("BLA BLA BLA", obj["content"]);
		}
		
		[Test]
		public function testConvertToObjectWithExternalMappings():void {
			var obj:TestObjectNoMetadata = new TestObjectNoMetadata();
			
			var d:Dictionary = new Dictionary();
			d[TestObjectNoMetadata] = {
				embeds : ["Listof", TestEmbedded],
				map : ["Mapof", "key", TestEmbedded]
			};
			
			var converter:XmlToObjectConverter = new XmlToObjectConverter(d);
			
			converter.convertToObject(xml, obj);
			
			Assert.assertEquals(123, obj.a);
			Assert.assertEquals("test", obj.b);
			Assert.assertEquals(123.123, obj.c.embdA);
			
			Assert.assertEquals(1.1, (obj.embeds.getItemAt(0) as TestEmbedded).embdA);
			Assert.assertEquals(1.2, (obj.embeds.getItemAt(1) as TestEmbedded).embdA);
			Assert.assertEquals(1.3, (obj.embeds.getItemAt(2) as TestEmbedded).embdA);
			
			Assert.assertEquals(1.1, (obj.map["e1"] as TestEmbedded).embdA);
			Assert.assertEquals(1.2, (obj.map["e2"] as TestEmbedded).embdA);
			Assert.assertEquals(1.3, (obj.map["e3"] as TestEmbedded).embdA);
			
			Assert.assertEquals("BLA BLA BLA", obj["content"]);
		}
	}
}