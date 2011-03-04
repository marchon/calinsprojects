package ro.calin.component
{
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	
	import ro.calin.component.event.CategoryEvent;
	import ro.calin.component.model.CategoryViewerModel;
	import ro.calin.component.skin.CategoryViewerSkin;
	
	import spark.components.DataGroup;
	import spark.components.supportClasses.SkinnableComponent;
	
	[Event(name="categItemClick", type="event.CategoryEvent")]
	public class CategoryViewer extends SkinnableComponent
	{
		[SkinPart(required="true")]
		public var thumbnailStrip:DataGroup;
		
		public var thumbnailWidth:Number = 100;
		public var scale:Number = 1;
		
		private var _model:CategoryViewerModel;
		
		public function CategoryViewer() {
			setStyle("skinClass", CategoryViewerSkin);
		}
		
		public function set model(value:CategoryViewerModel):void {
			_model = value;
			
			if(thumbnailStrip != null) {
				thumbnailStrip.dataProvider = _model.subcategories;
			}
		}
		
		public function get model():CategoryViewerModel {
			return _model;
		}
		
		override protected function partAdded(partName:String, instance:Object) : void {
			super.partAdded(partName, instance);
			
			if(instance == thumbnailStrip) {
				if(_model != null) {
					thumbnailStrip.dataProvider = _model.subcategories;
					thumbnailStrip.verticalScrollPosition = 0;
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
			
			//magical voodoo math stuff
			thumbnailStrip.verticalScrollPosition = fr * evt.stageY - fr * this.y;
		}
	}
}