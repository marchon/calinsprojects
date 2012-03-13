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
	/**
	 * Menu model entries contain extra info about....
	 * TODO: continue...
	 */
	public class App extends SkinnableComponent
	{	
		private static const THUMBNAIL_HEIGHT:Number = 119;
		
		private static const MENU_MIDDLE:String = "menumiddle";
		private static const MENU_TOP:String = "menutop";
		private static const MENU_BOTTOM:String = "menubottom";
		
		
		//externalize???
		private static const TOP_LIST:Array = ['info', 'share'];
		private static const BOTTOM_LIST:Array = ['gallery'];
		
		private static const WALLPAPERS:String = "wp";
		
		[Bindable]
		public var currentSkinState:String = MENU_MIDDLE;
		
		private var menuModel:MenuModel;
		private var wallpapers:PictureViewerModel;
	
		
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
		
		public function App(menuModel:MenuModel, wallpaperModel:PictureViewerModel)
		{			
			super();
			
			this.menuModel = menuModel;
			this.wallpapers = wallpaperModel;
			
			setStyle("skinClass", AppSkin);
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
			if(TOP_LIST.indexOf(event.entry.label) >= 0) {
				currentSkinState = MENU_TOP;
			} else if(BOTTOM_LIST.indexOf(event.entry.label) >= 0) {
				currentSkinState = MENU_BOTTOM;
			}
		}
		
		private function menuItemHover(event:MenuEvent):void {
			//extra stores the model for the cagegory, otherwise it's not a category
			if(event.entry.extra != null) {
				var cm:CategoryViewerModel = event.entry.extra as CategoryViewerModel;
				
				if(cm != categoryViewer.model) {
					categoryViewer.model = cm;
					
					//set the height of the strip (max is screen height)
					categoryViewer.height = Math.min(this.height - menu.height, categoryViewer.model.subcategories.length * THUMBNAIL_HEIGHT);
					
					//move it above the corresponding menu item
					categoryViewer.x = menu.logo.width + menu.model.buttonWidth * (cm.extra as Number) - 1;
				}
			
				categoryViewer.visible = true;
			}
		}
		
		private function categoryRollOut(event:MouseEvent):void {
			categoryViewer.visible = false;
		}
	}
}