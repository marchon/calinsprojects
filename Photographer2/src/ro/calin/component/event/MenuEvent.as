package ro.calin.component.event
{
	import flash.events.Event;
	
	import ro.calin.component.model.MenuEntryModel;
	
	[Bindable]
	public class MenuEvent extends Event
	{
		public static const MENU_ITEM_CLICK:String = "itemClick";
		public static const MENU_LOGO_CLICK:String = "logoClick";
		
		public var entry:MenuEntryModel;
		
		public function MenuEvent(type:String, entry:MenuEntryModel=null, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.entry = entry;
		}
	}
}