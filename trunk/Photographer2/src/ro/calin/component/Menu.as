package ro.calin.component
{
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import ro.calin.component.event.MenuEvent;
	import ro.calin.component.model.MenuEntryModel;
	import ro.calin.component.model.MenuModel;
	import ro.calin.component.skin.MenuSkin;
	
	import spark.components.DataGroup;
	import spark.components.Image;
	import spark.components.supportClasses.SkinnableComponent;

	/**
	 * Component that represents a menu with potential submenus.
	 * When a menu item is clicked, the corresponding submenu replaces
	 * the current menu.
	 */
	[Event(name="itemClick", type="ro.calin.component.event.MenuEvent")]
	[Event(name="itemHover", type="ro.calin.component.event.MenuEvent")]
	[Event(name="logoClick", type="ro.calin.component.event.MenuEvent")]
	public class Menu extends SkinnableComponent
	{
		[SkinPart(required="false")]
		/**
		 * Image logo.
		 * */
		public var logo:Image;
		
		[SkinPart(required="true")]
		/**
		 * Groups togheter the menu buttons.
		 * */
		public var bar:DataGroup;
		
		/**
		 * The model of this menu.
		 * */
		private var _model:MenuModel;
		
		/**
		 * Stack used to keep track of the submenus,
		 * so that we can return to previous submenu/menu.
		 * */
		private var _menuState:Array = [];
		
		public function Menu() {
			//set the default skin class
			setStyle("skinClass", MenuSkin);
		}
		
		[Bindable]
		public function get model():MenuModel {
			if(_model == null) _model = new MenuModel();
			
			return _model;
		}
		public function set model(value:MenuModel):void {
			if(_model == value) return;
			
			_model = value;
			
			setContainerEntriesForEachEntry(_model.entries);
			
			//go to the first set of entries
			if(bar) {
				pushMenu(_model.entries);
			}
		}
		
		private function setContainerEntriesForEachEntry(entries:IList):void {
			for (var i:int = 0; i < entries.length; i++) {
				var entry:MenuEntryModel = entries.getItemAt(i) as MenuEntryModel;
				entry.containerEntries = entries;
				if(entry.entries) setContainerEntriesForEachEntry(entry.entries);
			}
		}
		
		public function getButtonWidth(entry:MenuEntryModel):Number {
			return isNaN(entry.buttonWidth)? _model.buttonWidth : entry.buttonWidth;
		}
		
		/**
		 * Update the button bar and push this list in the stack.
		 * */
		private function pushMenu(entries:IList):void {
			if(entries) {
				bar.dataProvider = entries;
				_menuState.push(entries);
				invalidateSkinState();
			}
		}
		
		/**
		 * Get back to previous list of items.
		 * */
		private function popMenu():void {
			//don't pop first entry
			if(_menuState.length > 1) {
				_menuState.pop();
				var entries:ArrayList = _menuState[_menuState.length - 1]; //peek
				bar.dataProvider = entries;
				invalidateSkinState();
			}
		}

		/**
		 * Called when the skin is applied.
		 * Do initialization stuff for visual components and add event listeners.
		 * */
		override protected function partAdded(partName:String, instance:Object) : void { 
			super.partAdded(partName, instance); 
			if (instance == logo) {
				logo.addEventListener(MouseEvent.CLICK, logo_clickHandler);
			}
			if (instance == bar) {
				if(_model) {
					pushMenu(_model.entries);
				}
				bar.addEventListener(MenuEvent.MENU_ITEM_CLICK, buttonBar_changeHandler);
			}
		}
		
		/**
		 * Called when skin is removed.
		 * */
		override protected function partRemoved(partName:String, instance: Object) : void {
			super.partRemoved(partName, instance);
			
			if (instance == logo) {
				logo.removeEventListener(MouseEvent.CLICK, logo_clickHandler);
			}
			if (instance == bar) {
				bar.removeEventListener(MenuEvent.MENU_ITEM_CLICK, buttonBar_changeHandler);
			}
		}
		
		/**
		 * When logo is clicked, pop the current menu and dispatch an event.
		 * 
		 * TODO: what happens if no logo has been specified?
		 * should have a default back button or smfn
		 * */
		private function logo_clickHandler(event:MouseEvent) : void {
			popMenu();
			dispatchEvent(new MenuEvent(MenuEvent.MENU_LOGO_CLICK));
		}
	
		/**
		 * Push the submenu for the clicked item.
		 * The event is already a menu event dispatched in MenuButton component,
		 * and it bubbles, so no need to dispatch again.
		 * */
		private function buttonBar_changeHandler(evt:MenuEvent) : void {
			var selected:MenuEntryModel = evt.entry;
			pushMenu(selected.entries);
		}
	}
}