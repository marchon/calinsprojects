package ro.calin.component
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.utils.setTimeout;
	
	import ro.calin.component.model.CategoryViewerModel;
	import ro.calin.component.model.SubcategoryModel;
	
	import spark.components.Image;
	import spark.components.Label;
	import spark.components.VGroup;
	import spark.effects.Animate;
	import spark.effects.animation.Animation;
	import spark.effects.animation.Keyframe;
	import spark.effects.animation.MotionPath;
	
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
		
		private var adjustFraction:Number = 0;
		private var adjustFraction1:Number = 0;
		private var recalculateFraction:Boolean = true;
		
		private var anim:Animate = new Animate();
		private var path:MotionPath = new MotionPath('verticalScrollPosition');
		
		public function CategoryViewer2()
		{
			super();
			gap = 0;
			addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			clipAndEnableScrolling = true;
			
			anim.target = this;
			var v:Vector.<MotionPath> = new Vector.<MotionPath>();
			v.push(path);
			anim.motionPaths = v;
		}

		public function set model(value:CategoryViewerModel):void {	
			anim.stop();
			
			if(_model == value) return;
			
			_model = value;
			
			removeAllElements();

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
				
				addElementAt(img, 0);
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
			
//			anim.stop();
//			path.keyframes = new <Keyframe>[new Keyframe(0, super.verticalScrollPosition), 
//				new Keyframe(Math.abs(super.verticalScrollPosition - value) / 2, value)];			
//			anim.play();
			
			super.verticalScrollPosition = value;
		}
		
		/**
		 * This method calculates the scrolling position each time the mouse moves.
		 * 
		 * mouseY		->	scroll
		 * (h/p, h-h/p) -> (0, ch-h)
		 */
		private function mouseMoveHandler(evt:MouseEvent):void {	
			if(recalculateFraction) {
				var p:int = 10;
				adjustFraction = ((contentHeight - height) / height) * p/(p-2);
				adjustFraction1 = (height - contentHeight) / (p-2);
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