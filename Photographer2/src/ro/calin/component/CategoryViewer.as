package ro.calin.component
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.events.PropertyChangeEvent;
	
	import ro.calin.component.event.CategoryEvent;
	import ro.calin.component.model.CategoryViewerModel;
	import ro.calin.component.skin.CategoryViewerSkin;
	
	import spark.components.DataGroup;
	import spark.components.supportClasses.SkinnableComponent;
	
	/**
	 * Component which can display a vertical thumbnail strip.
	 * The viewport is limited to the height and it can be
	 * scrolled by moving the mouse towords the components top
	 * or bottom.
	 */
	[Event(name="categItemClick", type="ro.calin.component.event.CategoryEvent")]
	public class CategoryViewer extends SkinnableComponent
	{
		/**
		 * This is the group of thumbnails.
		 */
		[SkinPart(required="true")]
		public var thumbnailStrip:DataGroup;
		
		/**
		 * Contains information about pics shown here.
		 */
		private var _model:CategoryViewerModel;
		
		/**
		 * Specifies wether the strip is entirely highlighted,
		 * or every pic gets highlighted on mouseover.
		 */
		[Bindable]
		public var highlighted:Boolean = false;
		
		private var _newModel:Boolean = true;
		
		public function CategoryViewer() {
			//set the skin
			setStyle("skinClass", CategoryViewerSkin);
		}
		
		[Bindable]
		public function set model(value:CategoryViewerModel):void {
			_newModel = true;
			_model = value;
			
			//set the provider for the data group
			if(thumbnailStrip != null) {
				thumbnailStrip.dataProvider = _model.subcategories;
			}
		}
		
		public function get model():CategoryViewerModel {
			//create one to avoid npe in skin
			if(_model == null) _model = new CategoryViewerModel();
			
			return _model;
		}
		
		/**
		 * Attaching behaviour to components.
		 */
		override protected function partAdded(partName:String, instance:Object) : void {
			super.partAdded(partName, instance);
			
			if(instance == thumbnailStrip) {
				if(_model != null) {
					thumbnailStrip.dataProvider = _model.subcategories;
				}
				thumbnailStrip.addEventListener(MouseEvent.MOUSE_MOVE, thumbnailStrip_mouseMoveHandler);
				
				//clip it, scroll it!!! yeah
				thumbnailStrip.clipAndEnableScrolling = true;
			}
		}
		
		/**
		 * Probably is called when a skin part is removed(so never in this case)
		 */
		override protected function partRemoved(partName:String, instance: Object) : void {
			super.partRemoved(partName, instance);
			
			if(instance == thumbnailStrip) {
				thumbnailStrip.removeEventListener(MouseEvent.MOUSE_MOVE, thumbnailStrip_mouseMoveHandler);
			}
		}
		
		/**
		 * This method calculates the scrolling position each time the mouse moves.
		 * TODO:
		 * 	1. tap max value as to not get empty space under last pic
		 * 		- this might be because of pic scaling
		 *  	- if scale is very little, it is bearly noticeable
		 *  2. set scroll position to max scroll when the model changes (this should be done after all the pics are loaded)
		 *  3. try scrolling with animation
		 */
		private function thumbnailStrip_mouseMoveHandler(evt:MouseEvent):void {
			
			//first time pointer is over, remove highlighting
			if(highlighted) highlighted = false;
			
			var fr:Number = (thumbnailStrip.contentHeight - thumbnailStrip.height) / thumbnailStrip.height;
			var scroll:Number = fr * evt.stageY - fr * this.y;
			
			var ms:Number = maxScroll();
			if(scroll > ms) scroll = ms;
			
			thumbnailStrip.verticalScrollPosition = scroll;
		}
		
		private function maxScroll():Number {
			return thumbnailStrip.contentHeight - thumbnailStrip.height;  //haha, funny
		} 
		
	}
}