package ro.calin.component
{
	import flash.events.MouseEvent;
	
	import ro.calin.component.model.MenuEntryModel;
	import ro.calin.event.MenuEvent;
	
	import spark.components.Button;
	
	public class MenuButton extends Button
	{
		private var _entry:MenuEntryModel;
		
		[Bindable]
		public function get entry():MenuEntryModel {return _entry;}
		public function set entry(value:MenuEntryModel):void {
			if(value == _entry) return;
			_entry = value;
			
			this.label = _entry.label;
		}
		
		public function MenuButton()
		{
			super();
			
			addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
				dispatchEvent(new MenuEvent(MenuEvent.MENU_ITEM_CLICK, _entry, true));
			});
		}
	}
}