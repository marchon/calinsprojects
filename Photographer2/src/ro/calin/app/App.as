package ro.calin.app
{

	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayList;
	
	import ro.calin.component.CategoryViewer;
	import ro.calin.component.Menu;
	import ro.calin.component.PictureViewer;
	import ro.calin.component.event.CategoryEvent;
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
		
		public static const MENU_MIDDLE:String = "menumiddle";
		public static const MENU_TOP:String = "menutop";
		public static const MENU_BOTTOM:String = "menubottom";
		
		private static const WALLPAPERS:String = "wp";
		private static const PICS:String = "pcs";
		
		[Bindable]
		public var currentSkinState:String = MENU_BOTTOM;
		
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
			
			currentSkinState = menuModel.extra as String;
			
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
				categoryViewer.addEventListener(CategoryEvent.CATEG_ITEM_CLICK, categoryItemClick);
			}
			
			if(instance == pictureViewer) {
				pictureViewer.registerModel(WALLPAPERS, wallpapers, true);
				pictureViewer.setActiveModel(WALLPAPERS);
				pictureViewer.slide(PictureViewer.DIR_DOWN, PictureViewer.MODE_RAND);
			}
			
			if(instance == leftButton) {
				leftButton.addEventListener(MouseEvent.CLICK, leftButtonClick);
				stage.addEventListener(KeyboardEvent.KEY_DOWN, keyDown);
			}
			if(instance == rightButton) {
				rightButton.addEventListener(MouseEvent.CLICK, rightButtonClick);
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
				categoryViewer.removeEventListener(CategoryEvent.CATEG_ITEM_CLICK, categoryItemClick);
			}
			
			if(instance == leftButton) {
				leftButton.removeEventListener(MouseEvent.CLICK, leftButtonClick);
				stage.removeEventListener(KeyboardEvent.KEY_DOWN, keyDown);
			}
			if(instance == rightButton) {
				rightButton.removeEventListener(MouseEvent.CLICK, rightButtonClick);
			}
		}
		
		protected function menuLogoClick(event:MenuEvent):void
		{
			changeCurrentState(menuModel.extra as String);
		}
		
		protected function menuItemClick(event:MenuEvent):void {
			if(event.entry.extra is String) changeCurrentState(event.entry.extra as String)	
		}
		
		protected function changeCurrentState(state:String):void
		{	
			leftButton.visible = rightButton.visible = false;
			pictureViewer.setActiveModel(WALLPAPERS);
			
			switch(state) {
				case MENU_MIDDLE:
					if(currentSkinState == MENU_TOP) pictureViewer.slide(PictureViewer.DIR_DOWN, PictureViewer.MODE_RAND);
					else pictureViewer.slide(PictureViewer.DIR_UP, PictureViewer.MODE_RAND);
					break;
				case MENU_TOP:
					pictureViewer.slide(PictureViewer.DIR_UP, PictureViewer.MODE_RAND);
					break;
				case MENU_BOTTOM:
					pictureViewer.slide(PictureViewer.DIR_DOWN, PictureViewer.MODE_RAND);
					break;
			}
			
			currentSkinState = state;
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
		
		protected function categoryItemClick(event:CategoryEvent):void
		{
			var model:PictureViewerModel = event.subcategory.extra as PictureViewerModel;
			
			pictureViewer.registerModel(PICS, model, true);
			pictureViewer.setActiveModel(PICS);
			pictureViewer.slide(PictureViewer.DIR_UP, PictureViewer.MODE_FIRST);
			
			leftButton.visible = rightButton.visible = model.pictures.length > 1;
		}
		
		protected function leftButtonClick(event:MouseEvent):void
		{
			pictureViewer.slide(PictureViewer.DIR_RIGHT, PictureViewer.MODE_PREV);
		}
		
		protected function rightButtonClick(event:MouseEvent):void
		{
			pictureViewer.slide(PictureViewer.DIR_LEFT, PictureViewer.MODE_NEXT);
		}
		
		protected function keyDown(event:KeyboardEvent):void
		{
			if(event.keyCode == Keyboard.LEFT)
				pictureViewer.slide(PictureViewer.DIR_RIGHT, PictureViewer.MODE_PREV);
			else if(event.keyCode == Keyboard.RIGHT)
				pictureViewer.slide(PictureViewer.DIR_LEFT, PictureViewer.MODE_NEXT);
		}
	}
}