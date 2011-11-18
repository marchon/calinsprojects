package ro.calin.app
{
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.IVisualElement;
	
	import ro.calin.component.event.MenuEvent;
	import ro.calin.component.model.MenuEntryModel;
	import ro.calin.component.model.MenuModel;
	import ro.calin.component.skin.MenuSkin;
	
	import spark.components.Button;
	import spark.components.DataGroup;
	import spark.components.HGroup;
	import spark.components.Image;
	import spark.components.supportClasses.SkinnableComponent;

	public class App extends SkinnableComponent
	{
		[SkinPart(required="true")]
		public var picViewer:PictureViewer;
		
		[SkinPart(required="true")]
		public var menu:Menu;
		
		[SkinPart(required="true")]
		public var catViewer:CategoryViewer;
		
		[SkinPart(required="true")]
		public var leftButton:Button;
		
		[SkinPart(required="true")]
		public var rightButton:Button;
		
		
		public function App() {
//			setStyle("skinClass", MenuSkin);
		}

		/**
		 * Called when the skin is applied.
		 * Do initialization stuff for visual components and add event listeners.
		 * */
		override protected function partAdded(partName:String, instance:Object) : void { 
			super.partAdded(partName, instance); 
//			if (instance == logo) {
//				logo.addEventListener(MouseEvent.CLICK, logo_clickHandler);
//			}
			
		}
		
		/**
		 * Called when skin is removed.
		 * */
		override protected function partRemoved(partName:String, instance: Object) : void {

		}
		
		
	}
}