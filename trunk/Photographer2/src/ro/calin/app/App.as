package ro.calin.app
{

	import flash.display.DisplayObject;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	import flash.ui.Keyboard;
	
	import mx.controls.Alert;
	import mx.core.IVisualElement;
	import mx.events.BrowserChangeEvent;
	import mx.managers.BrowserManager;
	import mx.utils.Base64Decoder;
	import mx.utils.Base64Encoder;
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
	import ro.calin.component.model.MenuEntryModel;
	import ro.calin.component.model.MenuModel;
	import ro.calin.component.model.PictureViewerModel;
	
	import spark.components.Button;
	import spark.components.Group;
	import spark.components.supportClasses.SkinnableComponent;
	import spark.core.ContentCache;
	import spark.core.IViewport;
	
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
		private static const enc:Base64Encoder = new Base64Encoder();
		private static const dec:Base64Decoder = new Base64Decoder();
		
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
		private var pictureLists:Object;
		private var currentPictureList:String = "";
		
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
		
		public function App(menuModel:MenuModel, wallpaperModel:PictureViewerModel, progressBar:LoadingProgressBar, picsInSubcategory:Object)
		{			
			super();
			
			this.menuModel = menuModel;
			this.wallpapers = wallpaperModel;
			this.progressBar = progressBar;
			this.pictureLists = picsInSubcategory;
			
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
			clearCurrentPictureFromUrl();
		}
		
		protected function menuItemClick(event:MenuEvent):void {
			if(externalContentGroup.numElements > 0) externalContentGroup.removeAllElements();
			
			if(event.entry.extra is String) {
				changeCurrentState(event.entry.extra as String);
			}
			else if(event.entry.extra is URLRequest) navigateToURL(event.entry.extra as URLRequest);
			else if(event.entry.extra is Array) {
				var arr:Array = event.entry.extra as Array;
				showPictures(arr[0] as PictureViewerModel);
				externalContentGroup.addElement(arr[1] as IVisualElement);
			}
			else if(event.entry.extra is PictureViewerModel) {
				showPictures(event.entry.extra as PictureViewerModel);
				currentPictureList = event.entry.label;
				saveCurrentPictureToUrl(currentPictureList, 0);
			}
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
			if(!pointerIsAboveObject(event)) {
				hideCategory();
			}
			
			(event.target as MenuButton).removeEventListener(MouseEvent.ROLL_OUT, menuButtonRollOut);
		}
		
		protected function categoryRollOut(event:MouseEvent):void {
			if(!pointerIsBelowObject(event)) {
				hideCategory();
			}
		}
		
		protected function categoryItemClick(event:CategoryEvent):void
		{
			hideCategory();
			
			if(event.subcategory.extra is PictureViewerModel) {
				var model:PictureViewerModel = event.subcategory.extra as PictureViewerModel;
				showPictures(model);
				currentPictureList = event.subcategory.name;
				saveCurrentPictureToUrl(currentPictureList, 0);
			}
		}
		
		protected function leftButtonClick(event:MouseEvent):void
		{
			pictureViewer.slide(PictureViewer.DIR_RIGHT, PictureViewer.MODE_PREV);
			saveCurrentPictureToUrl(currentPictureList, pictureViewer.index);
		}
		
		protected function rightButtonClick(event:MouseEvent):void
		{
			pictureViewer.slide(PictureViewer.DIR_LEFT, PictureViewer.MODE_NEXT);
			saveCurrentPictureToUrl(currentPictureList, pictureViewer.index);
		}
		
		protected function keyDown(event:KeyboardEvent):void
		{
			if(!leftButton.visible) return;
			
			if(event.keyCode == Keyboard.LEFT) {
				pictureViewer.slide(PictureViewer.DIR_RIGHT, PictureViewer.MODE_PREV);
				saveCurrentPictureToUrl(currentPictureList, pictureViewer.index);
			}
			else if(event.keyCode == Keyboard.RIGHT) {
				pictureViewer.slide(PictureViewer.DIR_LEFT, PictureViewer.MODE_NEXT);
				saveCurrentPictureToUrl(currentPictureList, pictureViewer.index);
			}
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
		
		private function pointerIsAboveObject(event:MouseEvent):Boolean {
			var pos:Point = (event.target as DisplayObject).localToGlobal(new Point(0,0));
			return event.stageY < pos.y && event.stageX >= pos.x && event.stageX <= pos.x + (event.target as DisplayObject).width;
		}
		
		private function pointerIsBelowObject(event:MouseEvent):Boolean {
			var pos:Point = (event.target as DisplayObject).localToGlobal(new Point(0,0));
			var width:Number = (event.target as DisplayObject).width;
			var height:Number = event.target is IViewport? (event.target as IViewport).contentHeight : (event.target as DisplayObject).height;
			
			//hack (category viewer extends with description)
			if(event.target is CategoryViewer) {
				width = (event.target as CategoryViewer).model.thumbWidth;
			}
			
			return event.stageY > pos.y + height && event.stageX >= pos.x && event.stageX <= pos.x + width;
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
		
		private function showPictures(model:PictureViewerModel, pos:Number = NaN) : void {
			if(model == null || model.pictures == null || model.pictures.length == 0) return;
			
			var loader:ContentCache = Registry.instance.check(SUBCATEGORY_PIC_LIST) as ContentCache;
			
			if(loader == null) {
				loader = new ContentCache();
				loader.enableQueueing = true;
				loader.maxActiveRequests = 3; //1???
				Registry.instance.register(SUBCATEGORY_PIC_LIST, loader);
			}
			
			loader.removeAllCacheEntries();
			
			progressBar.load(loader, model.pictures, [isNaN(pos)?0:pos]); //first pic has priority, after loading do the slide
			pictureViewer.registerModel(SUBCATEGORY_PIC_LIST, model);
			leftButton.visible = rightButton.visible = false;
			
			var _inline:Function;
			progressBar.addEventListener(LoadingEvent.PRIORITY_LOAD_COMPLETE, _inline = function(event:LoadingEvent) : void {
				pictureViewer.setActiveModel(SUBCATEGORY_PIC_LIST);
				pictureViewer.slide(PictureViewer.DIR_UP, isNaN(pos)?PictureViewer.MODE_FIRST : -pos); //neg means position
				leftButton.visible = rightButton.visible = model.pictures.length > 1;
				progressBar.removeEventListener(LoadingEvent.PRIORITY_LOAD_COMPLETE, _inline);
			});
		}
		
		override protected function attachSkin() : void {
			super.attachSkin();
			
			BrowserManager.getInstance().init("");
			loadPictureFromUrl();
		}
		
		private function loadPictureFromUrl(): void {
			var o:Object = URLUtil.stringToObject(decode(BrowserManager.getInstance().fragment));
			
			if(o.hasOwnProperty("s") && o.hasOwnProperty("p")) 
			{
				changeCurrentState(MENU_BOTTOM);
				showPictures(pictureLists[o["s"]], parseInt(o["p"]));
				currentPictureList = o["s"];
				menu.changeMenuState(menuModel.entries.getItemAt(0) as MenuEntryModel); //assumes gallery is at 0
			}
		}
		
		private function saveCurrentPictureToUrl(subcategory:String, pos:int) : void {
			var o:Object = {
				s: subcategory,
				p: pos
			};
			
			BrowserManager.getInstance().setFragment(encode(URLUtil.objectToString(o)));
		}
		
		private function clearCurrentPictureFromUrl():void {
			BrowserManager.getInstance().setFragment('');
		}
		
		private function encode(s:String):String {
			enc.encode(s);
			return enc.toString();
		}
		
		private function decode(s:String):String {
			dec.decode(s);
			return dec.toByteArray().toString();
		}
		
	}
}