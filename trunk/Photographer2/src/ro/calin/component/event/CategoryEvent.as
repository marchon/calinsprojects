package ro.calin.component.event
{
	import flash.events.Event;
	
	[Bindable]
	public class CategoryEvent extends Event
	{
		public static const CATEG_ITEM_CLICK:String = "categItemClick";
		
		public function CategoryEvent(type:String, item:Object=null, bubbles:Boolean=false, cancelable:Boolean=false)
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