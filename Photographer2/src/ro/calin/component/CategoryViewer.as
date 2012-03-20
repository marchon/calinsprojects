package ro.calin.component
{
	import flash.events.MouseEvent;
	
	import mx.binding.utils.BindingUtils;
	
	import ro.calin.component.model.CategoryViewerModel;
	import ro.calin.component.model.SubcategoryModel;
	
	import spark.components.VGroup;
	import spark.effects.Animate;
	import spark.effects.animation.MotionPath;
	
	/**
	 * Component which can display a vertical thumbnail strip.
	 * The viewport is limited to the height and it can be
	 * scrolled by moving the mouse towords the components top
	 * or bottom.
	 */
	[Event(name="categItemClick", type="ro.calin.component.event.CategoryEvent")]
	public class CategoryViewer extends VGroup
	{
		/**
		 * Contains information about pics shown here.
		 */
		private var _model:CategoryViewerModel;

		private var adjustFraction:Number = 0;
		private var adjustFraction1:Number = 0;
		private var recalculateFraction:Boolean = true;
		
		[Bindable]
		public var highlightAll:Boolean = true;
		
		public function CategoryViewer()
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
			for(var i:int = 0; i < _model.subcategories.length; i++) {
				var sc:Subcategory = new Subcategory();
				
				sc.model = _model.subcategories.getItemAt(i) as SubcategoryModel;
				sc.thumbWidth = _model.thumbWidth;
				BindingUtils.bindProperty(sc, "alwaysHighlight", this, "highlightAll");
				
				addElementAt(sc, 0);
			}
			
			recalculateFraction = true;
		}
		
		public function get model():CategoryViewerModel {
			return _model;
		}
		
		override public function set height(value:Number):void {
			super.height = value;
			recalculateFraction = true;
		}
		
		public function set scroll(value:Number):void  {
			super.verticalScrollPosition = value;
		}
		
		/**
		 * This method calculates the scrolling position each time the mouse moves.
		 * 
		 * mouseY   ->	scroll
		 * (p, h-p) -> (0, ch-h)
		 */
		private function mouseMoveHandler(evt:MouseEvent):void {	
			if(recalculateFraction) {
				adjustFraction = (contentHeight - height) / (height - _model.thumbHeight);
				adjustFraction1 = ((height - contentHeight) * _model.thumbHeight / 2) / (height - _model.thumbHeight);
				recalculateFraction = false;
			}
			
			if(adjustFraction == 0) return;
			var relativeY:Number = evt.stageY - this.y;
			
			var scroll:Number = adjustFraction * relativeY + adjustFraction1;
			
			//cap it for overflow
			var max:Number = contentHeight - height;
			if(scroll > max) scroll = max;
			if(scroll < 0) scroll = 0;
			
			this.scroll = scroll;
		}
	}
}