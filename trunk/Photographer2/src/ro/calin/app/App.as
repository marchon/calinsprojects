package ro.calin.app
{

	import flash.utils.setTimeout;
	
	import mx.collections.ArrayList;
	import mx.rpc.Fault;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import ro.calin.component.CategoryViewer;
	import ro.calin.component.Menu;
	import ro.calin.component.PictureViewer;
	import ro.calin.component.event.MenuEvent;
	import ro.calin.component.model.MenuEntryModel;
	
	import spark.components.supportClasses.SkinnableComponent;
	
	public class App extends SkinnableComponent
	{
		private var config:XML;
		
		private var _categories:ArrayList;
		
		[SkinPart(required="true")]
		public var pictureViewer:PictureViewer;
		
		[SkinPart(required="true")]
		public var menu:Menu;
		
		[SkinPart(required="true")]
		public var categoryViewer:CategoryViewer;
		
		public function get categories():ArrayList {
			return _categories;
		}
		
		public function App(xml:XML)
		{			
			super();
			
			this.config = xml;
			
			_categories = new ArrayList();
			for each (var category:XML in config.categories.category) {
				var entry:MenuEntryModel = new MenuEntryModel();
				entry.label = category.@name;
				_categories.addItem(entry);
			}
			
			setStyle("skinClass", AppSkin);
		}
		
		override protected function partAdded(partName:String, instance:Object) : void { 
			super.partAdded(partName, instance);
			
			if(instance == menu) {
				menu.addEventListener(MenuEvent.MENU_ITEM_CLICK, menuItemClick);
			}
			
		}
		
		override protected function partRemoved(partName:String, instance: Object) : void {
			super.partRemoved(partName, instance);
			
			if(instance == menu) {
				menu.removeEventListener(MenuEvent.MENU_ITEM_CLICK, menuItemClick);
			}
		}
		
		private function menuItemClick(event:MenuEvent):void {
			trace(width);
			trace(height);
		}
	}
}