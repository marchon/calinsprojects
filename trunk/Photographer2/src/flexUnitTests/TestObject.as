package flexUnitTests
{
	import mx.collections.IList;

	public class TestObject
	{
		public var a:int;
		public var b:String;
		public var c:TestEmbedded;
		
		[Listof(type="flexUnitTests.TestEmbedded")]
		public var embeds:IList;
		
		[Mapof(keyname="key", type="flexUnitTests.TestEmbedded")]
		public var map:Object;
		
		public var content:String;
	}
}