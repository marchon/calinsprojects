package ro.calin.app
{

	import mx.collections.ArrayList;
	import mx.rpc.Fault;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import ro.calin.component.CategoryViewer;
	import ro.calin.component.Menu;
	import ro.calin.component.PictureViewer;
	import ro.calin.component.model.MenuEntryModel;
	
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
		
		public function get categories():ArrayList {
			var cat1:MenuEntryModel = new MenuEntryModel();
			cat1.label = "test1";
			
			var cat2:MenuEntryModel = new MenuEntryModel();
			cat2.label = "test2";
			
			return new ArrayList([cat1, cat2]);
		}
		
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