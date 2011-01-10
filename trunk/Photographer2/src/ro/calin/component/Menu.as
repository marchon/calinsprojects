package ro.calin.component
{
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.controls.Image;
	
	import ro.calin.event.MenuEvent;
	
	import spark.components.DataGroup;
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
		public var bar:DataGroup;
		
		private var _entries:ArrayCollection;
		
		private var _logoSource:String;
		
		private var _isMainMenu:Boolean = true;
		
		public function Menu() {
		}
		
		public function get entries():ArrayCollection {return _entries;}
		public function set entries(value:ArrayCollection):void {
			if(_entries == value) return;
			
			_entries = value;
			bar.dataProvider = _entries;
		}
		
		public function get logoSource():String {return _logoSource;}
		public function set logoSource(value:String):void {
			if(value == _logoSource) return;
			_logoSource = value;
			
			if(logo) {
				logo.source = value;
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
				bar.dataProvider = _entries;
				bar.addEventListener(MenuEvent.MENU_ITEM_CLICK, buttonBar_changeHandler);
			}
		}
		
		override protected function partRemoved(partName:String, instance: Object) : void {
			super.partRemoved(partName, instance);
			
			if (instance == logo) {
				logo.removeEventListener(MouseEvent.CLICK, logo_clickHandler);
			}
			if (instance == bar) {
				bar.removeEventListener(MenuEvent.MENU_ITEM_CLICK, buttonBar_changeHandler);
			}
		}
		
		private function logo_clickHandler(event:MouseEvent) : void {
			if(!_isMainMenu) {
				bar.dataProvider = _entries;
				_isMainMenu = true;
				invalidateSkinState();
			}
			
			dispatchEvent(new MenuEvent(MenuEvent.MENU_LOGO_CLICK));
		}
	
		private function buttonBar_changeHandler(evt:MenuEvent) : void {
			var selected:Object = evt.item;
			if(_isMainMenu) {
				//we are in main menu and a button has been clicked
				if(selected.type == "parent") {
					bar.dataProvider = selected.subentry;
					_isMainMenu = false;
					invalidateSkinState();
				}
			}
		}
	}
}