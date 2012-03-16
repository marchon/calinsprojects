package ro.calin.component
{
	import flash.events.MouseEvent;
	
	import ro.calin.component.model.CategoryViewerModel;
	import ro.calin.component.model.SubcategoryModel;
	
	import spark.components.Image;
	import spark.components.Label;
	import spark.components.VGroup;
	
	/**
	 * Component which can display a vertical thumbnail strip.
	 * The viewport is limited to the height and it can be
	 * scrolled by moving the mouse towords the components top
	 * or bottom.
	 */
	[Event(name="categItemClick", type="ro.calin.component.event.CategoryEvent")]
	public class CategoryViewer2 extends VGroup
	{
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
		
		public function CategoryViewer2()
		{
			super();
			gap = 0;
			addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			clipAndEnableScrolling = true;
		}
		
		public function set model(value:CategoryViewerModel):void {
			if(_model == value) return;
			
			_model = value;
			
			removeAllElements();
			verticalScrollPosition = 0;
			//why does this not work
			for(var i:int = 0; i < _model.subcategories.length; i++) {
//				var sc:Subcategory = new Subcategory();
//				
//				sc.model = _model.subcategories.getItemAt(i) as SubcategoryModel;
//				sc.thumbWidth = _model.thumbWidth;
//				sc.scaleFrom = _model.scaleFrom;
//				sc.scaleTo = _model.scaleTo;
//				sc.alwaysHighlight = highlighted;
				
				var img:Image = new Image();
				img.source = (_model.subcategories.getItemAt(i) as SubcategoryModel).picUrl;
				
				addElement(img);
			}
			
		}
		
		public function get model():CategoryViewerModel {
			return _model;
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
		private function mouseMoveHandler(evt:MouseEvent):void {
			
			//first time pointer is over, remove highlighting
			if(highlighted) highlighted = false;
			
			var fr:Number = (contentHeight - height) / height;
			var scroll:Number = fr * evt.stageY - fr * this.y;
			
			var ms:Number = contentHeight - height;
			if(scroll > ms) scroll = ms;
			
			verticalScrollPosition = scroll;
		}
		
	}
}