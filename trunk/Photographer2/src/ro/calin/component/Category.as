package ro.calin.component
{
	import flash.events.MouseEvent;
	
	import mx.controls.Image;
	
	import ro.calin.event.CategoryEvent;
	
	import spark.components.supportClasses.SkinnableComponent;
	
	[SkinState("normal")]
	[SkinState("hovered")]
	public class Category extends SkinnableComponent
	{
		[SkinPart(required="true")]
		public var image:Image;
		
		private var _categoryData:Object;
		
		private var _isHovered:Boolean = false;
		
		public function Category()
		{
			super();
		}
		
		public function get categoryData():Object {return _categoryData;}
		public function set categoryData(value:Object):void {
			if(_categoryData == value) return;
			
			_categoryData = value;
			image.source = _categoryData.coverPic;
		}
		
		override protected function getCurrentSkinState() : String {
			if(_isHovered) {
				return "hovered";
			}
			
			return "normal";
		}
		
		override protected function partAdded(partName:String, instance:Object) : void { 
			super.partAdded(partName, instance); 
			
			if(instance == image) {
				if(_categoryData) image.source == _categoryData.coverPic;
				image.addEventListener(MouseEvent.ROLL_OVER, image_rollOver);
				image.addEventListener(MouseEvent.ROLL_OUT, image_rollOut);
				image.addEventListener(MouseEvent.CLICK, image_click);
			}
		}
		
		override protected function partRemoved(partName:String, instance: Object) : void {
			super.partRemoved(partName, instance);
			
			if(instance == image) {
				image.removeEventListener(MouseEvent.ROLL_OVER, image_rollOver);
				image.removeEventListener(MouseEvent.ROLL_OUT, image_rollOut);
				image.removeEventListener(MouseEvent.CLICK, image_click);
			}
		}
		
		private function image_rollOver(evt:MouseEvent):void {
			_isHovered = true;
			invalidateSkinState();
		}
		
		private function image_rollOut(evt:MouseEvent):void {
			_isHovered = false;
			invalidateSkinState();
		}
		
		private function image_click(evt:MouseEvent):void {
			dispatchEvent(new CategoryEvent(CategoryEvent.CATEG_ITEM_CLICK, _categoryData, true));
		}
	}
}