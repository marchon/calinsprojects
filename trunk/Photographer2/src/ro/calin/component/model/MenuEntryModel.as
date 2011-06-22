package ro.calin.component.model
{
	import mx.collections.ArrayList;
	import mx.collections.IList;

	/**
	 * The model for the menu component.
	 * Besides the usual properties, it contains a
	 * generic object to hold various data if needed 
	 * and a list of subentries.
	 */
	[Bindable]
	public class MenuEntryModel
	{
		public var label:String = null;
		public var imageUrl:String = null;
		public var color:uint = 0x000000;
		public var fontColor:uint = 0xffffff;
		public var fontSize:Number = 30;
		public var buttonWidth:Number = NaN;
		
		public var entries:IList = null;
		
		public var extra:Object = null;
	}
}