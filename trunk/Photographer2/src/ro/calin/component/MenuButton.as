package ro.calin.component
{
	import flash.events.MouseEvent;
	
	import ro.calin.component.event.MenuEvent;
	import ro.calin.component.model.MenuEntryModel;
	import ro.calin.component.skin.MenuButtonSkin;
	
	import spark.components.Group;
	import spark.components.Image;
	import spark.components.supportClasses.SkinnableComponent;
	import spark.core.IContentLoader;
	
	/**
	 * Menu button component.
	 * Contains the model of the button and dispatches a menu event when clicked.
	 * */
	public class MenuButton extends SkinnableComponent
	{
		[SkinPart(required="true")]
		public var container:Group;
		
		/**
		 * The model.
		 * */
		private var _entry:MenuEntryModel;
		
		[Bindable]
		public function get entry():MenuEntryModel {
			if(_entry == null) _entry = new MenuEntryModel();
			return _entry;
		}
		public function set entry(value:MenuEntryModel):void {
			if(value == _entry || value == null) return;
			_entry = value;
			
			if(container != null) {
				processImage();
			}
		}
		
		override protected function partAdded(partName:String, instance:Object) : void { 
			super.partAdded(partName, instance);
			
			if(instance == container && _entry != null) {
				processImage();
			}
		}
		
		private function processImage():void {
			if(_entry.imageSrc != null) {
				var img:Image = new Image();
				img.source = _entry.imageSrc;
				try {
					img.contentLoader = Registry.instance.check(Menu.IMAGE_LOADER_NAME) as IContentLoader;
				} catch(ex:*) {}
				container.addElementAt(img, 0);
			} else {
				//fix for item renderer caching (I guess...)
				if(container.getElementAt(0) is Image) container.removeElementAt(0);
			}
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
			
			addEventListener(MouseEvent.ROLL_OVER, function(event:MouseEvent):void {
				dispatchEvent(new MenuEvent(MenuEvent.MENU_ITEM_HOVER, _entry, true));
			});
		}
	}
}