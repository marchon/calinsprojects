package ro.calin.component.model
{
	import mx.collections.ArrayList;

	public class MenuEntryModel
	{
		public var label:String;
		public var color:uint;
		public var extra:Object;
		
		public var entries:ArrayList;
		
		public function MenuEntryModel(label:String=null, color:uint=0, extra:Object=null, entries:ArrayList=null)
		{
			this.label = label;
			this.color = color;
			this.extra = extra;
			this.entries = entries;
		}
	}
}