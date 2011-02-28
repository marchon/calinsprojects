package test
{
	import auto.AutoValidator;
	
	public class ValidableEntity extends AutoValidator
	{
		[Bindable]
		[Validate(condition="required", error="must.respect.condition")]
		public var field1:String;
		
		[Bindable]
		[Validate(condition="([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}", 
				error="must.respect.condition")]
		public var field2:String;
		
		[Bindable]
		[Validate(condition="func:testEquality", error="must.respect.condition")]
		public var field3:String;
		
		public function testEquality(val:String):Boolean {
			return val == field2;
		}
		
		public function ValidableEntity()
		{
			super();
		}
	}
}