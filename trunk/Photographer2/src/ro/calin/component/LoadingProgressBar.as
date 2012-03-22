package ro.calin.component
{
	import flash.events.Event;
	import flash.events.HTTPStatusEvent;
	import flash.events.IOErrorEvent;
	import flash.events.ProgressEvent;
	import flash.events.SecurityErrorEvent;
	import flash.utils.Dictionary;
	
	import ro.calin.component.event.LoadComplete;
	import ro.calin.component.skin.LoadingProgressBarSkin;
	
	import spark.components.supportClasses.SkinnableComponent;
	import spark.core.ContentRequest;
	
	public class LoadingProgressBar extends SkinnableComponent
	{
		[Bindable] public var color:uint = 0x00ff00;
		[Bindable] public var percentLoaded:Number = 0;
		
		public var defaultSize:int = 1024 * 1024;
		
		/**
		 * Holds a map of requests for computing loading progress.
		 */
		private var loadingMap:Dictionary = new Dictionary();
		
		private var unfinishedRequests:int = 0;
		
		public function LoadingProgressBar()
		{
			super();
			setStyle("skinClass", LoadingProgressBarSkin);
		}
		
		public function showLoading(requests:Array) : Boolean {
			if(unfinishedRequests > 0) return false;
			
			unfinishedRequests = 0;
			for(var i:int = 0; i < requests.length; i++) {
				var cr:ContentRequest = requests[i];
				if(!cr.complete) {
					unfinishedRequests++;
					cr.addEventListener(ProgressEvent.PROGRESS, progress);
					cr.addEventListener(Event.COMPLETE, complete);
					cr.addEventListener(IOErrorEvent.IO_ERROR, complete);
					cr.addEventListener(SecurityErrorEvent.SECURITY_ERROR, complete);
					cr.addEventListener(HTTPStatusEvent.HTTP_STATUS, function(event:HTTPStatusEvent) : void {
						if(event.status % 100 == 4 || event.status % 100 == 5)
							complete(event);
					});
				}

				loadingMap[cr] = {bytesLoaded : 0, bytesTotal : defaultSize};
			}
			
			compute();
			
			return true;
		}
		
		/**
		 * Called with each progress event. 
		 */
		private function progress(event:ProgressEvent):void {
			loadingMap[event.target] = {bytesLoaded : event.bytesLoaded, bytesTotal : event.bytesTotal};
			
			compute();
		}
		
		/**
		 * Called with each complete event.
		 */
		private function complete(event:Event):void {
			delete loadingMap[event.target];
			
			compute();
			
			unfinishedRequests--;
			
			if(unfinishedRequests == 0) { 
				dispatchEvent(new LoadComplete(LoadComplete.LOAD_COMPLETE));
			}
		}
		
		/**
		 * Called to update the progress for loading.
		 */
		private function compute():void {
			var loaded:Number = 0;
			var total:Number = 0;
			
			for each(var progressInfo:Object in loadingMap) {
				loaded += progressInfo.bytesLoaded;
				total += progressInfo.bytesTotal;
			}
			
			percentLoaded = total > 0? (loaded/total) * 100 : 0;
		}
	}
}