package ro.calin.app
{

	import flash.events.MouseEvent;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayList;
	
	import ro.calin.component.CategoryViewer;
	import ro.calin.component.Menu;
	import ro.calin.component.PictureViewer;
	import ro.calin.component.event.MenuEvent;
	import ro.calin.component.model.CategoryViewerModel;
	import ro.calin.component.model.MenuEntryModel;
	import ro.calin.component.model.MenuModel;
	import ro.calin.component.model.PictureModel;
	import ro.calin.component.model.PictureViewerModel;
	import ro.calin.component.model.SubcategoryModel;
	
	import spark.components.Button;
	import spark.components.supportClasses.SkinnableComponent;
	
	[SkinState("menumiddle")]
	[SkinState("menutop")]
	[SkinState("menubottom")]
	public class App extends SkinnableComponent
	{	
		private static const MENU_MIDDLE:String = "menumiddle";
		private static const MENU_TOP:String = "menutop";
		private static const MENU_BOTTOM:String = "menubottom";
		
		private static const WALLPAPERS:String = "wp";
		
		[Bindable]
		public var currentSkinState:String = MENU_MIDDLE;
		
		private var categories:ArrayList;
		private var menuModel:MenuModel;
		private var wallpapers:PictureViewerModel;
		
		/**
		 * Maps category model with it's index, so that we know where to display the category component.
		 */
		private var categoryIndexMap:Dictionary = new Dictionary();
		
		[SkinPart(required="true")]
		public var pictureViewer:PictureViewer;
		
		[SkinPart(required="true")]
		public var menu:Menu;
		
		[SkinPart(required="true")]
		public var categoryViewer:CategoryViewer;
		
		[SkinPart(required="true")]
		public var leftButton:Button;
		
		[SkinPart(required="true")]
		public var rightButton:Button;
		
		public function App(menuModel:MenuModel, xmlModel:XML)
		{			
			super();
			
			//parse external model
			parseXMLandPopulateModel(xmlModel);
			
			//set internal model
			this.menuModel = menuModel;
			
			//link external with internal(attach categories to 'gallery' menu entry)
			this.menuModel.entries.getItemAt(0).entries = categories;
			
			setStyle("skinClass", AppSkin);
		}
		
		private function parseXMLandPopulateModel(xml:XML):void {
			categories = parseCategories(xml.categories[0].category);
			wallpapers = parseWallpapers(xml.wallpapers[0]);
		}
		
		private function parseCategories(categories:XMLList):ArrayList {
			var categoryList:ArrayList = new ArrayList();
			
			var idx:Number = 0;
			for each (var category:XML in categories) {
				var model:MenuEntryModel = new MenuEntryModel();
				model.label = category.@name;
				model.color = uint(category.@color);
				var categoryModel:CategoryViewerModel = new CategoryViewerModel();
				categoryModel.subcategories = parseSubcategories(category.subcategory);
				model.extra = categoryModel;
				categoryIndexMap[categoryModel] = idx++;
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
				model.picUrl = subcategory.@picUrl;
				var pictureModel:PictureViewerModel = new PictureViewerModel();
				pictureModel.pictures = parsePictures(subcategory.picture); 
				model.extra = pictureModel;
				subcategoryList.addItem(model);
			}
			
			return subcategoryList;
		}
		
		private function parseWallpapers(wallpaperList:XML):PictureViewerModel {
			var model:PictureViewerModel = new PictureViewerModel();

			model.pictures = parsePictures(wallpaperList.picture);
			
			return model;
		}
		
		private function parsePictures(pictures:XMLList):Array {
			var pictureList:Array = new Array();
			
			for each (var picture:XML in pictures) {
				var model:PictureModel = new PictureModel;
				model.url = picture.@url;
				pictureList.push(model);
			}
			
			return pictureList;
		}
		
		override protected function partAdded(partName:String, instance:Object) : void { 
			super.partAdded(partName, instance);
			
			if(instance == menu) {
				menu.model = menuModel;
				menu.addEventListener(MenuEvent.MENU_LOGO_CLICK, menuLogoClick);
				menu.addEventListener(MenuEvent.MENU_ITEM_CLICK, menuItemClick);
				menu.addEventListener(MenuEvent.MENU_ITEM_HOVER, menuItemHover);
			}
			
			if(instance == categoryViewer) {
				categoryViewer.addEventListener(MouseEvent.ROLL_OUT, categoryRollOut);
			}
			
			if(instance == pictureViewer) {
				pictureViewer.registerModel(WALLPAPERS, wallpapers, true);
				
				pictureViewer.setActiveModel(WALLPAPERS);
				pictureViewer.slide(PictureViewer.DIR_DOWN, PictureViewer.MODE_RAND);
			}
			
		}
		
		override protected function partRemoved(partName:String, instance: Object) : void {
			super.partRemoved(partName, instance);
			
			if(instance == menu) {
				menu.removeEventListener(MenuEvent.MENU_LOGO_CLICK, menuLogoClick);
				menu.removeEventListener(MenuEvent.MENU_ITEM_CLICK, menuItemClick);
				menu.removeEventListener(MenuEvent.MENU_ITEM_HOVER, menuItemHover);
			}
			
			if(instance == categoryViewer) {
				categoryViewer.removeEventListener(MouseEvent.ROLL_OUT, categoryRollOut);
			}
		}
		
		protected function menuLogoClick(event:MenuEvent):void
		{
			currentSkinState = MENU_MIDDLE;
		}
		
		
		protected function menuItemClick(event:MenuEvent):void
		{
			switch(event.entry.label) {
				//hardcoded
				case 'gallery':
					currentSkinState = MENU_BOTTOM;
					break;
				case 'info':
				case 'share':
					currentSkinState = MENU_TOP;
			}
		}
		
		private function menuItemHover(event:MenuEvent):void {
			//extra stores the model for the cagegory, otherwise it's not a category
			if(event.entry.extra != null) {
				var cm:CategoryViewerModel = event.entry.extra as CategoryViewerModel;
				
				if(cm != categoryViewer.model) {
					categoryViewer.model = cm;
					
					//set the height of the strip (max is screen height)
					categoryViewer.height = Math.min(this.height - menu.height, categoryViewer.model.subcategories.length * 119);
					
					//move it above the corresponding menu item
					categoryViewer.x = menu.logo.width + menu.model.buttonWidth * categoryIndexMap[cm] - 1;
				}
			
				categoryViewer.visible = true;
			}
		}
		
		private function categoryRollOut(event:MouseEvent):void {
			categoryViewer.visible = false;
		}
	}
}