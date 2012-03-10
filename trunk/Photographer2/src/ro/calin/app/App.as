package ro.calin.app
{

	import flash.utils.setTimeout;
	
	import mx.collections.ArrayList;
	import mx.controls.Alert;
	import mx.rpc.Fault;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import ro.calin.component.CategoryViewer;
	import ro.calin.component.Menu;
	import ro.calin.component.PictureViewer;
	import ro.calin.component.event.MenuEvent;
	import ro.calin.component.model.CategoryViewerModel;
	import ro.calin.component.model.MenuEntryModel;
	import ro.calin.component.model.PictureModel;
	import ro.calin.component.model.PictureViewerModel;
	import ro.calin.component.model.SubcategoryModel;
	
	import spark.components.supportClasses.SkinnableComponent;
	
	public class App extends SkinnableComponent
	{	
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
			
			parseXMLandPopulateModel(xml);
			
			setStyle("skinClass", AppSkin);
		}
		
		private function parseXMLandPopulateModel(xml:XML):void {
			_categories = parseCategories(xml.categories.category);
		}
		
		private function parseCategories(categories:XMLList):ArrayList {
			var categoryList:ArrayList = new ArrayList();
			
			for each (var category:XML in categories) {
				var model:MenuEntryModel = new MenuEntryModel();
				model.label = category.@name;
				var categoryModel:CategoryViewerModel = new CategoryViewerModel();
				categoryModel.subcategories = parseSubcategories(category.subcategory);
				model.extra = categoryModel;
				categoryList.addItem(model);
			}
			
			return categoryList;
		}
		
		private function parseSubcategories(subcategories:XMLList):ArrayList {
			var subcategoryList:ArrayList = new ArrayList();
			
			for each (var subcategory:XML in subcategories) {
				var model:SubcategoryModel = new SubcategoryModel();
				model.name = subcategory.@name;
				model.description = subcategory.@description;
				model.picUrl = subcategory.@thumbnailPath;
				var pictureModel:PictureViewerModel = new PictureViewerModel();
				pictureModel.pictures = parsePictures(subcategory.picture); 
				model.extra = pictureModel;
				subcategoryList.addItem(model);
			}
			
			return subcategoryList;
		}
		
		private function parsePictures(pictures:XMLList):Array {
			var pictureList:Array = new Array();
			
			for each (var picture:XML in pictures) {
				var model:PictureModel = new PictureModel;
				model.url = picture.@path;
				pictureList.push(model);
			}
			
			return pictureList;
		}
		
		override protected function partAdded(partName:String, instance:Object) : void { 
			super.partAdded(partName, instance);
			
			if(instance == menu) {
				menu.addEventListener(MenuEvent.MENU_ITEM_CLICK, menuItemClick);
				menu.addEventListener(MenuEvent.MENU_ITEM_HOVER, menuItemHover);
			}
			
		}
		
		override protected function partRemoved(partName:String, instance: Object) : void {
			super.partRemoved(partName, instance);
			
			if(instance == menu) {
				menu.removeEventListener(MenuEvent.MENU_ITEM_CLICK, menuItemClick);
				menu.removeEventListener(MenuEvent.MENU_ITEM_HOVER, menuItemHover);
			}
		}
		
		private function menuItemClick(event:MenuEvent):void {

		}
		
		private function menuItemHover(event:MenuEvent):void {
			if(event.entry.extra != null) {
				categoryViewer.model = event.entry.extra as CategoryViewerModel;
				categoryViewer.x = 500; //you're here
			}
		}
	}
}