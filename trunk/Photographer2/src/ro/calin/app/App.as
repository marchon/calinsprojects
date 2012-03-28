package ro.calin.app
{

	import flash.display.DisplayObject;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	import flash.ui.Keyboard;
	
	import mx.core.IVisualElement;
	import mx.events.BrowserChangeEvent;
	import mx.managers.BrowserManager;
	import mx.utils.URLUtil;
	
	import ro.calin.component.CategoryViewer;
	import ro.calin.component.LoadingProgressBar;
	import ro.calin.component.Menu;
	import ro.calin.component.MenuButton;
	import ro.calin.component.PictureViewer;
	import ro.calin.component.Registry;
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
		public static const MENU_BOTTOM_CATEGORY:String = "menubottomshowcat"
		
		public static const WALLPAPERS:String = "wallpapers";
		public static const SUBCATEGORY_PIC_LIST:String = "subcategoryPicList";
		
		[Bindable]
		public var currentSkinState:String = MENU_MIDDLE;
		
		private var menuModel:MenuModel;
		private var wallpapers:PictureViewerModel;
		private var progressBar:LoadingProgressBar;
		
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
		
		public function App(menuModel:MenuModel, wallpaperModel:PictureViewerModel, progressBar:LoadingProgressBar)
		{			
			super();
			
			this.menuModel = menuModel;
			this.wallpapers = wallpaperModel;
			this.progressBar = progressBar;
			
			currentSkinState = menuModel.extra as String;
			
			setStyle("skinClass", AppSkin);
			
			BrowserManager.getInstance().addEventListener(BrowserChangeEvent.BROWSER_URL_CHANGE, loadState);
			BrowserManager.getInstance().init("");
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
			
			if(instance == pictureViewer) {
				pictureViewer.registerModel(WALLPAPERS, wallpapers);
				pictureViewer.setActiveModel(WALLPAPERS);
				pictureViewer.slide(PictureViewer.DIR_DOWN, PictureViewer.MODE_FIRST);
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
		
		
		/*
		 *	HANDLERS
		 */
		
		protected function menuLogoClick(event:MenuEvent):void
		{
			if(externalContentGroup.numElements > 0) externalContentGroup.removeAllElements();
			changeCurrentState(menuModel.extra as String);
			
			saveState();
		}
		
		protected function menuItemClick(event:MenuEvent):void {
			if(externalContentGroup.numElements > 0) externalContentGroup.removeAllElements();
			
			if(event.entry.extra is String) changeCurrentState(event.entry.extra as String);
			else if(event.entry.extra is URLRequest) navigateToURL(event.entry.extra as URLRequest);
			else if(event.entry.extra is Array) {
				var arr:Array = event.entry.extra as Array;
				showPictures(arr[0] as PictureViewerModel);
				externalContentGroup.addElement(arr[1] as IVisualElement);
			}
			else if(event.entry.extra is PictureViewerModel) showPictures(event.entry.extra as PictureViewerModel);
			
			saveState();
		}
		
		protected function menuItemHover(event:MenuEvent):void {
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
		
		protected function menuButtonRollOut(event:MouseEvent):void {
			if(!rolloutIsAboveObject(event)) {
				hideCategory();
			}
			
			(event.target as MenuButton).removeEventListener(MouseEvent.ROLL_OUT, menuButtonRollOut);
		}
		
		protected function categoryRollOut(event:MouseEvent):void {
			if(!rolloutIsBelowObject(event)) {
				hideCategory();
			}
		}
		
		protected function categoryItemClick(event:CategoryEvent):void
		{
			hideCategory();
			
			if(event.subcategory.extra is PictureViewerModel) {
				var model:PictureViewerModel = event.subcategory.extra as PictureViewerModel;
				showPictures(model);
			}
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
		
		/*
		*	END HANDLERS
		*/
		
		
		
		
		private function changeCurrentState(state:String):void
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
		
		private function rolloutIsAboveObject(event:MouseEvent):Boolean {
			var pos:Point = (event.target as DisplayObject).localToGlobal(new Point(0,0));
			return event.stageY < pos.y && event.stageX >= pos.x && event.stageX <= pos.x + (event.target as DisplayObject).width;
		}
		
		private function rolloutIsBelowObject(event:MouseEvent):Boolean {
			var pos:Point = (event.target as DisplayObject).localToGlobal(new Point(0,0));
			return event.stageY > pos.y && event.stageX >= pos.x && event.stageX <= pos.x + (event.target as DisplayObject).width;
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
		
		private function showPictures(model:PictureViewerModel) : void {
			if(model == null || model.pictures == null || model.pictures.length == 0) return;
			
			var loader:ContentCache = Registry.instance.check(SUBCATEGORY_PIC_LIST) as ContentCache;
			
			if(loader == null) {
				loader = new ContentCache();
				loader.enableQueueing = true;
				loader.maxActiveRequests = 3; //1???
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
		
		private function loadState(event:BrowserChangeEvent): void {
			var o:Object = URLUtil.stringToObject(BrowserManager.getInstance().fragment);
			loadState2(o);
		}
		
		private function loadState2(o:Object) : void {
			if(pictureViewer && menu && categoryViewer && leftButton && rightButton && externalContentGroup) {
				if(o.ms) changeCurrentState(o.ms);
			} else {
				callLater(loadState2, [o]);
			}
		}
		
		private function saveState() : void {
			var o:Object = {
				ms: currentSkinState
			};
			BrowserManager.getInstance().setFragment(URLUtil.objectToString(o));
		}
		
	}
}