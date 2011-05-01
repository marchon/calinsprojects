package ro.calin.component.model
{
	import mx.collections.IList;

	/**
	 * The model for the menu component. 
	 */
	[Bindable]
	public class MenuModel
	{
		[Listof(type="ro.calin.component.model.MenuEntryModel")]
		public var entries:IList;
		public var extra:Object;
		
		public var buttonWidth:Number = 1;
		public var color:Number = 1;
		public var alpha:Number = 1;
		
		public var logoUrl:String;
	}
}