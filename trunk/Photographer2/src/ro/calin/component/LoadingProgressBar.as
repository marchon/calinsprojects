package ro.calin.component
{
	import flash.events.Event;
	import flash.events.HTTPStatusEvent;
	import flash.events.IOErrorEvent;
	import flash.events.ProgressEvent;
	import flash.events.SecurityErrorEvent;
	import flash.utils.Dictionary;
	
	import ro.calin.component.event.LoadingEvent;
	import ro.calin.component.skin.LoadingProgressBarSkin;
	
	import spark.components.supportClasses.SkinnableComponent;
	import spark.core.ContentCache;
	import spark.core.ContentRequest;
	import spark.core.IContentLoader;
	
	[Event(name="loadStart", type="ro.calin.component.event.LoadingEvent")]
	[Event(name="loadComplete", type="ro.calin.component.event.LoadingEvent")]
	[Event(name="priorityLoadComplete", type="ro.calin.component.event.LoadingEvent")]
	/**
	 * Loads a bunch of resources from a content loader, showing progress.
	 */
	public class LoadingProgressBar extends SkinnableComponent
	{
		[Bindable] public var color:uint = 0x00ff00;
		[Bindable] public var percentLoaded:Number = 0;
		
		public var defaultSize:int = 1024 * 512; //500k default
		
		/**
		 * Holds a map of requests for computing loading progress.
		 */
		private var loadingMap:Dictionary = new Dictionary();
		
		private var unfinishedRequests:int = 0;
		private var unfinishedPriorityRequests:int = 0;
		
		public function LoadingProgressBar()
		{
			super();
			setStyle("skinClass", LoadingProgressBarSkin);
		}
		
		public function load(contentLoader:ContentCache, resources:Array, priorityIndexes:Array) : Boolean {
			if(contentLoader == null || resources == null || resources.length == 0) return false;
			
			if(unfinishedRequests == 0)
				dispatchEvent(new LoadingEvent(LoadingEvent.LOAD_START));
			
			if(priorityIndexes.length > 0) contentLoader.prioritize("priority");
			for(var i:int = 0; i < resources.length; i++) {
				var hasPriority:Boolean = priorityIndexes != null && priorityIndexes.indexOf(i) != -1;
				
				var cr:ContentRequest;
				if(hasPriority) {
					//TODO: seems it's not working too well
					cr = contentLoader.load(resources[i], "priority");
				} else {
					cr = contentLoader.load(resources[i]);
				}
					
				if(!cr.complete) {
					unfinishedRequests++;
					if(hasPriority) unfinishedPriorityRequests++;
					registerHandlers(cr);
					loadingMap[cr] = {bytesLoaded : 0, bytesTotal : defaultSize, priority: hasPriority};
				}
			}
			
			compute();
			
			return true;
		}
		
		private function registerHandlers(req:ContentRequest) : void {
			req.addEventListener(ProgressEvent.PROGRESS, progress);
			req.addEventListener(Event.COMPLETE, complete);
			req.addEventListener(IOErrorEvent.IO_ERROR, complete);
			req.addEventListener(SecurityErrorEvent.SECURITY_ERROR, complete);
			req.addEventListener(HTTPStatusEvent.HTTP_STATUS, complete);
		}
		
		private function unregisterHandlers(req:ContentRequest) : void {
			req.removeEventListener(ProgressEvent.PROGRESS, progress);
			req.removeEventListener(Event.COMPLETE, complete);
			req.removeEventListener(IOErrorEvent.IO_ERROR, complete);
			req.removeEventListener(SecurityErrorEvent.SECURITY_ERROR, complete);
			req.removeEventListener(HTTPStatusEvent.HTTP_STATUS, complete);
		}
		
		/**
		 * Called with each progress event. 
		 */
		private function progress(event:ProgressEvent):void {
			loadingMap[event.target].bytesLoaded = event.bytesLoaded;
			loadingMap[event.target].bytesTotal = event.bytesTotal;
			
			compute();
		}
		
		/**
		 * Called with each complete event.
		 */
		private function complete(event:Event):void {
			if(event is HTTPStatusEvent) {
				//only if it's a http error it's complete
				if(HTTPStatusEvent(event).status % 100 != 4 && HTTPStatusEvent(event).status % 100 != 5) return;
			}
			
			unregisterHandlers(event.target as ContentRequest);
			
			if(loadingMap[event.target].priority) {
				unfinishedPriorityRequests--;
			}
			
//			delete loadingMap[event.target];
			
			compute();
			
			unfinishedRequests--;
			
			if(unfinishedRequests == 0) { 
				dispatchEvent(new LoadingEvent(LoadingEvent.LOAD_COMPLETE));
				loadingMap = new Dictionary();
			}
			
			if(unfinishedPriorityRequests == 0) {
				dispatchEvent(new LoadingEvent(LoadingEvent.PRIORITY_LOAD_COMPLETE));
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
			
			percentLoaded = total > 0? (loaded/total) * 100 : 100;
		}
	}
}