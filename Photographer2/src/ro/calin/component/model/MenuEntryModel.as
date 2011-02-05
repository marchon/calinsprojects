package ro.calin.component.model
{
	import mx.collections.ArrayList;

	/**
	 * The model for the menu component.
	 * Besides the usual properties, it contains a
	 * generic object to hold various data if needed 
	 * and a list of subentries.
	 * */
	public class MenuEntryModel
	{
		public var label:String;
		public var color:uint;
		public var fontColor:uint;
		public var fontSize:Number;
		
		public var extra:Object;
		
		public var entries:ArrayList;
		
		public function MenuEntryModel(label:String=null, color:uint=0, extra:Object=null, 
									   entries:ArrayList=null, fontColor:uint=0xffffff, 
									   fontSize:Number=30)
		{
			this.label = label;
			this.color = color;
			this.fontColor = fontColor;
			this.fontSize = fontSize;
			this.extra = extra;
			this.entries = entries;
		}
	}
}