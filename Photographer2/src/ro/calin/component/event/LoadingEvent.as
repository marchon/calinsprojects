package ro.calin.component.event
{
	import flash.events.Event;
	
	[Bindable]
	public class LoadingEvent extends Event
	{
		public static const LOAD_COMPLETE:String = "loadComplete";
		public static const LOAD_START:String = "loadStart";
		public static const PRIORITY_LOAD_COMPLETE:String = "priorityLoadComplete";
		
		public function LoadingEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}