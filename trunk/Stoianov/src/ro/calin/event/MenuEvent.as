package ro.calin.event
{
	import flash.events.Event;
	
	[Bindable]
	public class MenuEvent extends Event
	{
		public static var MENU_ITEM_CLICK:String = "mic";
		public static var MENU_LABEL_CLICK:String = "mlc";
		
		public function MenuEvent(type:String, option:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			_option = option;
		}
		
		private var _option:String;
		public function get option():String {
			return _option;
		}
	}
}