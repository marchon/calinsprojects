package ro.calin.component.model
{
	import mx.collections.IList;

	/**
	 * The model for the menu component. 
	 */
	[Bindable]
	public class MenuModel
	{
		public var buttonWidth:Number = 100;
		public var color:Number = 0x000000;
		public var alpha:Number = 1;
		public var logoUrl:String = "logo.jpg";
		
		public var entries:IList;
		
		public var extra:Object;
	}
}