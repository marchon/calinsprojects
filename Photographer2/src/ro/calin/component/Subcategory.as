package ro.calin.component
{
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
		public static const IMAGE_LOADER_NAME:String = "subcategoryLoader";
		
		[SkinPart(required="true")]
		public var image:Image;
		
		[SkinPart(required="true")]
		public var subcatName:Label;
		
		[SkinPart(required="true")]
		public var subcatDesc:Label;
		
		public var thumbWidth:Number;
		
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
				try {
					image.contentLoader = Registry.instance.check(IMAGE_LOADER_NAME) as IContentLoader;
				} catch(ex:*) {}
				image.source = _model.picUrl;
			}
			if(subcatName) {
				subcatName.text = _model.name;
			}
			if(subcatDesc) {
				subcatDesc.text = _model.description;
			}
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
				try {
					image.contentLoader = Registry.instance.check(IMAGE_LOADER_NAME) as IContentLoader;
				} catch(ex:*) {}
				
				if(_model) {
					image.source = _model.picUrl;	
				}
				
				image.addEventListener(MouseEvent.ROLL_OVER, image_rollOver);
				image.addEventListener(MouseEvent.ROLL_OUT, image_rollOut);
				image.addEventListener(MouseEvent.CLICK, image_click);
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