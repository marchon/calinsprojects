package ro.calin.component
{
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.controls.Image;
	
	import ro.calin.event.MenuEvent;
	
	import spark.components.ButtonBar;
	import spark.components.supportClasses.SkinnableComponent;
	import spark.events.IndexChangeEvent;

	[SkinState("mainmenu")]
	[SkinState("submenu")]
	[Event(name="itemClick", type="ro.calin.event.MenuEvent")]
	[Event(name="logoClick", type="ro.calin.event.MenuEvent")]
	public class Menu extends SkinnableComponent
	{
		[SkinPart(required="false")]
		public var logo:Image;
		
		[SkinPart(required="true")]
		public var bar:ButtonBar;
		
		private var _entries:ArrayCollection;
		
		private var _logoSource:String;
		
		private var _isMainMenu:Boolean = true;
		
		private var _mainMenuIndex:Number = -1;
		
		public function Menu()
		{
		}
		
		public function get entries():ArrayCollection {return _entries;}
		public function set entries(value:ArrayCollection):void {
			if(_entries == value) return;
			
			_entries = value;
			populateBarWithCurrentEntries(_entries);
		}
		
		public function get logoSource():String {return _logoSource;}
		public function set logoSource(value:String):void {
			if(value == _logoSource) return;
			_logoSource = value;
			
			if(logo) {
				logo.source = value;
			}			
		}
		
		private function populateBarWithCurrentEntries(entries:ArrayCollection) : void {
			if(bar && entries) {
				var curEntries:ArrayList = new ArrayList();
				for(var i:int = 0; i < entries.length; i++) {
					curEntries.addItem(entries[i].label);
				}
				
				bar.dataProvider = curEntries;
				bar.selectedIndex = -1;
			}
		}
		
		override protected function getCurrentSkinState() : String {
			if(_isMainMenu) {
				return "mainmenu";
			}
			
			return "submenu";
		}
		
		override protected function partAdded(partName:String, instance:Object) : void { 
			super.partAdded(partName, instance); 
			if (instance == logo) {
				logo.source = _logoSource;
				logo.addEventListener(MouseEvent.CLICK, logo_clickHandler);
			}
			if (instance == bar) {
				populateBarWithCurrentEntries(_entries);
				bar.addEventListener(IndexChangeEvent.CHANGE, buttonBar_changeHandler);
			}
		}
		
		override protected function partRemoved(partName:String, instance: Object) : void {
			super.partRemoved(partName, instance);
			
			if (instance == logo) {
				logo.removeEventListener(MouseEvent.CLICK, logo_clickHandler);
			}
			if (instance == bar) {
				bar.removeEventListener(IndexChangeEvent.CHANGE, buttonBar_changeHandler);
			}
		}
		
		private function logo_clickHandler(event:MouseEvent) : void {
			if(!_isMainMenu) {
				populateBarWithCurrentEntries(_entries);
				_isMainMenu = true;
				invalidateSkinState();
			}
			
			dispatchEvent(new MenuEvent(MenuEvent.MENU_LOGO_CLICK));
		}
	
		private function buttonBar_changeHandler(event:IndexChangeEvent) : void {
			var index:Number = event.newIndex;
			if(index == -1) return;
			
			var selected:Object;
			if(_isMainMenu) {
				//we are in main menu and a button has been clicked
				_mainMenuIndex = index;
				
				selected = _entries[index];
				if(selected.type == "parent") {
					populateBarWithCurrentEntries(selected.subentry);
					_isMainMenu = false;
					invalidateSkinState();
				}
			} else {
				//we are in a submenu
				selected = _entries[_mainMenuIndex].subentry[index];
			}
			
			dispatchEvent(new MenuEvent(MenuEvent.MENU_ITEM_CLICK, selected));
		}
	}
}