package ro.calin.component
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import ro.calin.component.event.CategoryEvent;
	import ro.calin.component.model.SubcategoryModel;
	import ro.calin.component.skin.SubcategorySkin;
	
	import spark.components.Image;
	import spark.components.Label;
	import spark.components.supportClasses.SkinnableComponent;
	import spark.core.ContentCache;
	import spark.core.IContentLoader;
	
	[SkinState("normal")]
	[SkinState("hovered")]
	public class Subcategory extends SkinnableComponent
	{
		public static var loader:IContentLoader = new ContentCache();
		
		//TODO: bitmap image not scaling as inteded
		[SkinPart(required="true")]
		public var image:Image;
		
		[SkinPart(required="true")]
		public var subcatName:Label;
		
		[SkinPart(required="true")]
		public var subcatDesc:Label;
		
		public var thumbWidth:Number;
		public var scaleFrom:Number;
		public var scaleTo:Number;
		
		public var _imageLoadedHandler:Function = null;
		
		public function get imageLoadedHandler():Function {return _imageLoadedHandler;}
		public function set imageLoadedHandler(value:Function):void {
			if(imageLoadedHandler != null && image != null) {
				image.removeEventListener(Event.COMPLETE, _imageLoadedHandler);
			}
			
			_imageLoadedHandler = value;
			
			if(imageLoadedHandler != null && image != null) {
				image.addEventListener(Event.COMPLETE, _imageLoadedHandler);
			}
		}
		
		[Bindable]
		public var alwaysHighlight:Boolean;
		
		private var _model:SubcategoryModel;
		
		private var _isHovered:Boolean = false;
		
		public function Subcategory() {
			setStyle("skinClass", SubcategorySkin);
		}
		
		public function get model():SubcategoryModel {return _model;}
		public function set model(value:SubcategoryModel):void {
			if(_model == value) return;
			
			_model = value;
			if(image) {
				image.source = _model.picUrl;
			}
			if(subcatName) {
				subcatName.text = _model.name;
			}
			if(subcatDesc) {
				subcatDesc.text = _model.description;
			}
		}
		
		public function get leftMargin():Number {
			//TODO: cache this value, cuz it's constant for all???
			return (scaleTo - scaleFrom) * thumbWidth / 2;
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
				image.contentLoader = loader;
				
				if(_model) {
					image.source == _model.picUrl;	
				}
				
				image.addEventListener(MouseEvent.ROLL_OVER, image_rollOver);
				image.addEventListener(MouseEvent.ROLL_OUT, image_rollOut);
				image.addEventListener(MouseEvent.CLICK, image_click);
				
				if(_imageLoadedHandler != null) {
					image.addEventListener(Event.COMPLETE, _imageLoadedHandler);
				}
			}
			if(instance == subcatName && _model) {
				subcatName.text = _model.name;
			}
			if(instance == subcatDesc && _model) {
				subcatDesc.text = _model.description;
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
			dispatchEvent(new CategoryEvent(CategoryEvent.CATEG_ITEM_CLICK, _model, true));
		}
	}
}