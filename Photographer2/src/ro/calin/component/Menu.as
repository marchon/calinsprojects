package ro.calin.component
{
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.controls.Image;
	
	import ro.calin.component.model.MenuEntryModel;
	import ro.calin.component.model.MenuModel;
	import ro.calin.event.MenuEvent;
	
	import spark.components.DataGroup;
	import spark.components.supportClasses.SkinnableComponent;
	import spark.events.IndexChangeEvent;

	[Event(name="itemClick", type="ro.calin.event.MenuEvent")]
	[Event(name="logoClick", type="ro.calin.event.MenuEvent")]
	public class Menu extends SkinnableComponent
	{
		[SkinPart(required="false")]
		public var logo:Image;
		
		[SkinPart(required="true")]
		public var bar:DataGroup;
		
		private var _model:MenuModel;
		
		private var _logoSource:String;
		
		private var _menuState:Array = [];
		
		public function Menu() {
		}
		
		public function get model():MenuModel {return _model;}
		public function set model(value:MenuModel):void {
			if(_model == value) return;
			
			_model = value;
			
			if(bar) {
				pushMenu(_model.entries);
			}
		}
		
		public function get logoSource():String {return _logoSource;}
		public function set logoSource(value:String):void {
			if(value == _logoSource) return;
			_logoSource = value;
			
			if(logo) {
				logo.source = value;
			}			
		}
		
		private function pushMenu(entries:ArrayList):void {
			if(entries) {
				bar.dataProvider = entries;
				_menuState.push(entries);
				invalidateSkinState();
			}
		}
		
		private function popMenu():void {
			//don't pop first entry
			if(_menuState.length > 1) {
				_menuState.pop();
				var entries:ArrayList = _menuState[_menuState.length - 1];
				bar.dataProvider = entries;
				invalidateSkinState();
			}
		}

		override protected function partAdded(partName:String, instance:Object) : void { 
			super.partAdded(partName, instance); 
			if (instance == logo) {
				logo.source = _logoSource;
				logo.addEventListener(MouseEvent.CLICK, logo_clickHandler);
			}
			if (instance == bar) {
				if(_model) {
					pushMenu(_model.entries);
				}
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
			popMenu();
			dispatchEvent(new MenuEvent(MenuEvent.MENU_LOGO_CLICK));
		}
	
		private function buttonBar_changeHandler(evt:MenuEvent) : void {
			var selected:MenuEntryModel = evt.entry;
			pushMenu(selected.entries);
		}
	}
}