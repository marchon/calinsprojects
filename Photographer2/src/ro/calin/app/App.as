package ro.calin.app
{

	import mx.rpc.Fault;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import ro.calin.component.CategoryViewer;
	import ro.calin.component.Menu;
	import ro.calin.component.PictureViewer;
	
	import spark.components.supportClasses.SkinnableComponent;
	
	public class App extends SkinnableComponent
	{
		private var config:XML = null;
		
		[SkinPart(required="true")]
		public var pictureViewer:PictureViewer;
		
		[SkinPart(required="true")]
		public var menu:Menu;
		
		[SkinPart(required="true")]
		public var categoryViewer:CategoryViewer;
		
		public function App()
		{			
			super();
			setStyle("skinClass", AppSkin);
		}
		
		private function getConfig():void {
			var service:HTTPService = new HTTPService();
			service.url = 'config.xml';
			service.resultFormat = "e4x";
			service.addEventListener(ResultEvent.RESULT, function(event:ResultEvent):void {
				config = XML(event.result);
			});
			service.addEventListener(FaultEvent.FAULT, function(event:FaultEvent):void {
				
			});
			service.send();
		}
		
		override protected function partAdded(partName:String, instance:Object) : void { 
			super.partAdded(partName, instance);
			
		}
		
		override protected function partRemoved(partName:String, instance: Object) : void {
			super.partRemoved(partName, instance);
		}
	}
}