package ro.calin.event
{
	import flash.events.Event;
	
	[Bindable]
	public class MenuEvent extends Event
	{
		public static const MENU_ITEM_CLICK:String = "itemClick";
		public static const MENU_LOGO_CLICK:String = "logoClick";
		
		public function MenuEvent(type:String, item:Object=null, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			_item = item;
		}
		
		private var _item:Object;
		public function get item():Object {
			return _item;
		}
	}
}