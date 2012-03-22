package ro.calin.component.event
{
	import flash.events.Event;
	
	[Bindable]
	public class LoadComplete extends Event
	{
		public static const LOAD_COMPLETE:String = "lcomp";
		
		public function LoadComplete(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}