package ro.calin.component
{
	import flash.events.MouseEvent;
	
	import ro.calin.component.event.MenuEvent;
	import ro.calin.component.model.MenuEntryModel;
	import ro.calin.component.skin.MenuButtonSkin;
	
	import spark.components.Button;
	
	/**
	 * Menu button component.
	 * Contains the model of the button and dispatches a menu event when clicked.
	 * */
	public class MenuButton extends Button
	{
		/**
		 * The model.
		 * */
		private var _entry:MenuEntryModel;
		
		[Bindable]
		public function get entry():MenuEntryModel {return _entry;}
		public function set entry(value:MenuEntryModel):void {
			if(value == _entry) return;
			_entry = value;
			
			//display the label
			this.label = _entry.label;
		}
		
		public function MenuButton()
		{
			super();
			
			//default skin
			setStyle("skinClass", MenuButtonSkin);
			
			//when clicked, trigger a menu event with this entry
			addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
				dispatchEvent(new MenuEvent(MenuEvent.MENU_ITEM_CLICK, _entry, true));
			});
		}
	}
}