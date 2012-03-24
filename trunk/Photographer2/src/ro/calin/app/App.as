package ro.calin.app
{

	import flash.display.DisplayObject;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Utils3D;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	import flash.ui.Keyboard;
	
	import mx.core.IVisualElement;
	
	import ro.calin.component.CategoryViewer;
	import ro.calin.component.LoadingProgressBar;
	import ro.calin.component.Menu;
	import ro.calin.component.MenuButton;
	import ro.calin.component.PictureViewer;
	import ro.calin.component.Registry;
	import ro.calin.component.Subcategory;
	import ro.calin.component.event.CategoryEvent;
	import ro.calin.component.event.LoadingEvent;
	import ro.calin.component.event.MenuEvent;
	import ro.calin.component.model.CategoryViewerModel;
	import ro.calin.component.model.MenuModel;
	import ro.calin.component.model.PictureViewerModel;
	
	import spark.components.Button;
	import spark.components.Group;
	import spark.components.supportClasses.SkinnableComponent;
	import spark.core.ContentCache;
	import spark.core.IContentLoader;
	
	[SkinState("menumiddle")]
	[SkinState("menutop")]
	[SkinState("menubottom")]
	[SkinState("menubottomshowcat")]
	/**
	 * 
	 * This is the expected extra information:
	 * menuModel.extra - default menu state
	 * menuModel.entries[i].extra 
	 * 			- when a string: menu state when displaying subentries in this list
	 * 			- when a CategoryViewerModel: category model for that category
	 * 			- when a URLRequest: url to navigate at click
	 * CategoryViewerModel.extra - index of this category
	 * CategoryViewerModel.subcategories[i].extra - the PictureViewerModel for that subcategory
	 * 
	 * Category entries are assumed to be displayed in the menu when in menubottom state,
	 * and the category view will be displayed above the menu.
	 */
	public class App extends SkinnableComponent
	{	
		public static const MENU_MIDDLE:String = "menumiddle";
		public static const MENU_TOP:String = "menutop";
		public static const MENU_BOTTOM:String = "menubottom";
		private static const MENU_BOTTOM_CATEGORY:String = "menubottomshowcat"
		
		private static const WALLPAPERS:String = "wallpapers";
		private static const SUBCATEGORY_PIC_LIST:String = "subcategoryPicList";
		
		[Bindable]
		public var currentSkinState:String = MENU_MIDDLE;
		
		private var menuModel:MenuModel;
		private var wallpapers:PictureViewerModel;
		private var wallpaperAndThumbnailUrls:Array;
		
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
		
		[SkinPart(required="true")]
		public var externalContentGroup:Group;
		
		[SkinPart(required="true")]
		public var progressBar:LoadingProgressBar;
		
		public function App(menuModel:MenuModel, wallpaperModel:PictureViewerModel, listOfPicUrlsToPreload:Array)
		{			
			super();
			
			this.menuModel = menuModel;
			this.wallpapers = wallpaperModel;
			this.wallpaperAndThumbnailUrls = listOfPicUrlsToPreload;
			
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
				categoryViewer.addEventListener(MouseEvent.ROLL_OVER, categoryRollOver);
				categoryViewer.addEventListener(CategoryEvent.CATEG_ITEM_CLICK, categoryItemClick);
			}
			
			if(instance == progressBar) {
				loadWallpapersAndCategoryThumbnails();
			}
			
			if(instance == progressBar || instance == pictureViewer) {
				if(progressBar != null && pictureViewer != null) registerAndShowWallpapers();
			}
			
			if(instance == leftButton) {
				leftButton.addEventListener(MouseEvent.CLICK, leftButtonClick);
				stage.addEventListener(KeyboardEvent.KEY_DOWN, keyDown);
			}
			if(instance == rightButton) {
				rightButton.addEventListener(MouseEvent.CLICK, rightButtonClick);
			}
		}
		
		private function loadWallpapersAndCategoryThumbnails() : void {
			//TODOOOO: move outside and use priorities for a random wallpaper and menu logo
			//pass the progress bar as parameter??
			//pass the first random picture position
			var loader:ContentCache = new ContentCache();
			progressBar.load(loader, wallpaperAndThumbnailUrls, null);
			
			//register the cache loader so that components can access it and use it
			Registry.instance.register(WALLPAPERS, loader); //for viewer
			Registry.instance.register(Subcategory.IMAGE_LOADER_NAME, loader); //for subcategory
		}
		
		private function registerAndShowWallpapers() : void {
			pictureViewer.registerModel(WALLPAPERS, wallpapers);
			
			//TODO: slide when first pic is loaded
			pictureViewer.setActiveModel(WALLPAPERS);
			pictureViewer.slide(PictureViewer.DIR_DOWN, PictureViewer.MODE_RAND);
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
				categoryViewer.removeEventListener(MouseEvent.ROLL_OVER, categoryRollOver);
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
		
		override protected function getCurrentSkinState() : String {
			return currentSkinState;
		}
		
		protected function menuLogoClick(event:MenuEvent):void
		{
			if(externalContentGroup.numElements > 0) externalContentGroup.removeAllElements();
			changeCurrentState(menuModel.extra as String);
		}
		
		protected function menuItemClick(event:MenuEvent):void {
			if(externalContentGroup.numElements > 0) externalContentGroup.removeAllElements();
			
			if(event.entry.extra is String) changeCurrentState(event.entry.extra as String);
			else if(event.entry.extra is URLRequest) navigateToURL(event.entry.extra as URLRequest);
			else if(event.entry.extra is IVisualElement) externalContentGroup.addElement(event.entry.extra as IVisualElement);
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
			invalidateSkinState();
		}
		
		private function menuItemHover(event:MenuEvent):void {
			//extra stores the model for the cagegory, otherwise it's not a category
			if(event.entry.extra is CategoryViewerModel) {
				var cm:CategoryViewerModel = event.entry.extra as CategoryViewerModel;
				
				var actuallHeight:Number = cm.subcategories.length * cm.thumbHeight;
				var viewHeight:Number = Math.min(this.height - menu.height, actuallHeight);
				//reset scroll position to bottom
				categoryViewer.verticalScrollPosition = actuallHeight - viewHeight;
				
				if(cm != categoryViewer.model) {
					//set the height of the strip (max is screen height)
					categoryViewer.height = viewHeight;
					
					//move it above the corresponding menu item
					categoryViewer.x = menu.logo.width + menu.model.buttonWidth * (cm.extra as Number) - 1; //hardcoded adjustment(because of scaling)
				}
				
				categoryViewer.model = cm;
				categoryViewer.highlightAll = true;
				showCategory();
				
				if(event.target is MenuButton) { //should be
					(event.target as MenuButton).addEventListener(MouseEvent.ROLL_OUT, menuButtonRollOut);
				}
			}
		}
		
		private function menuButtonRollOut(event:MouseEvent):void {
			if(!rolloutIsAboveObject(event)) {
				hideCategory();
			}
			
			(event.target as MenuButton).removeEventListener(MouseEvent.ROLL_OUT, menuButtonRollOut);
		}
		
		private function rolloutIsAboveObject(event:MouseEvent):Boolean {
			var pos:Point = (event.target as DisplayObject).localToGlobal(new Point(0,0));
			return event.stageY < pos.y && event.stageX >= pos.x && event.stageX <= pos.x + (event.target as DisplayObject).width;
		}
		
		private function rolloutIsBelowObject(event:MouseEvent):Boolean {
			var pos:Point = (event.target as DisplayObject).localToGlobal(new Point(0,0));
			return event.stageY > pos.y && event.stageX >= pos.x && event.stageX <= pos.x + (event.target as DisplayObject).width;
		}
		
		private function categoryRollOut(event:MouseEvent):void {
			if(!rolloutIsBelowObject(event)) {
				hideCategory();
			}
		}
		
		private function categoryRollOver(evt:MouseEvent):void {
			if(currentSkinState != MENU_BOTTOM_CATEGORY) showCategory();
			categoryViewer.highlightAll = false;
		}
		
		private function hideCategory():void {
			currentSkinState = MENU_BOTTOM;
			invalidateSkinState();
		}
		
		private function showCategory():void {
			currentSkinState = MENU_BOTTOM_CATEGORY;
			invalidateSkinState();
		}
		
		protected function categoryItemClick(event:CategoryEvent):void
		{
			hideCategory();
			
			var model:PictureViewerModel = event.subcategory.extra as PictureViewerModel;
			
			if(model == null || model.pictures == null || model.pictures.length == 0) return;
			
			var loader:ContentCache = Registry.instance.check(SUBCATEGORY_PIC_LIST) as ContentCache;
			
			if(loader == null) {
				loader = new ContentCache();
				loader.enableQueueing = true;
				loader.maxActiveRequests = 10;
				Registry.instance.register(SUBCATEGORY_PIC_LIST, loader);
			}
			
			loader.removeAllCacheEntries();
			
			progressBar.load(loader, model.pictures, [0]); //first pic is priority, after loading do the slide
			pictureViewer.registerModel(SUBCATEGORY_PIC_LIST, model);
			leftButton.visible = rightButton.visible = false;
			
			var _inline:Function;
			progressBar.addEventListener(LoadingEvent.PRIORITY_LOAD_COMPLETE, _inline = function(event:LoadingEvent) : void {
				pictureViewer.setActiveModel(SUBCATEGORY_PIC_LIST);
				pictureViewer.slide(PictureViewer.DIR_UP, PictureViewer.MODE_FIRST);
				leftButton.visible = rightButton.visible = model.pictures.length > 1;
				progressBar.removeEventListener(LoadingEvent.PRIORITY_LOAD_COMPLETE, _inline);
			});
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
			if(!leftButton.visible) return;
			
			if(event.keyCode == Keyboard.LEFT)
				pictureViewer.slide(PictureViewer.DIR_RIGHT, PictureViewer.MODE_PREV);
			else if(event.keyCode == Keyboard.RIGHT)
				pictureViewer.slide(PictureViewer.DIR_LEFT, PictureViewer.MODE_NEXT);
		}
	}
}