package ro.calin.component
{
	import ro.calin.event.CategoryEvent;
	
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	
	import spark.components.DataGroup;
	import spark.components.supportClasses.SkinnableComponent;
	
	[SkinState("idle")]
	[SkinState("sliding")]
	[Event(name="categItemClick", type="event.CategoryEvent")]
	public class CategoryViewer extends SkinnableComponent
	{
		[SkinPart(required="true")]
		public var thumbnailStrip:DataGroup;
		
		private var _categories:ArrayCollection;
		
		public function set categories(value:ArrayCollection):void {
			_categories = value;
			
			if(thumbnailStrip != null) {
				thumbnailStrip.dataProvider = _categories;
			}
		}
		
		public function get categories():ArrayCollection {
			return _categories;
		}
		
		private var _lastStageY:Number = -1;
		
		public function CategoryViewer()
		{
		}
		
		override protected function partAdded(partName:String, instance:Object) : void {
			super.partAdded(partName, instance);
			
			if(instance == thumbnailStrip) {
				if(_categories != null) {
					thumbnailStrip.dataProvider = _categories;
				}
				thumbnailStrip.addEventListener(MouseEvent.MOUSE_MOVE, thumbnailStrip_mouseMoveHandler);
				thumbnailStrip.clipAndEnableScrolling = true;
			}
		}
		
		override protected function partRemoved(partName:String, instance: Object) : void {
			super.partRemoved(partName, instance);
			
			if(instance == thumbnailStrip) {
				thumbnailStrip.removeEventListener(MouseEvent.MOUSE_MOVE, thumbnailStrip_mouseMoveHandler);
			}
		}
		
		private function thumbnailStrip_mouseMoveHandler(evt:MouseEvent):void {
			//why is thumbnailStrip.contentHeight == thumbnailStrip.height???
			var fr:Number = (thumbnailStrip.contentHeight - this.height) / this.height;
			
			thumbnailStrip.verticalScrollPosition = fr * evt.stageY - fr * this.y;
		}
	}
}