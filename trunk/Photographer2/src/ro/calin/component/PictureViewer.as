package ro.calin.component
{
	import flash.events.MouseEvent;
	
	import mx.controls.Image;
	
	import spark.components.Button;
	import spark.components.supportClasses.SkinnableComponent;
	
	[SkinState("normal")]
	[SkinState("changing")]
	public class PictureViewer extends SkinnableComponent
	{
		[SkinPart(required="true")]
		public var picture:Image;
		
		[SkinPart(required="true")]
		public var leftButton:Button;
		
		[SkinPart(required="true")]
		public var rightButton:Button;
		
		private var _source:Array;
		
		private var _current:Number = 0;
		
		private var _isChanging:Boolean = false;
		
		[Bindable]
		public var hasLeftRight:Boolean = true;
		
		public function PictureViewer()
		{
			super();
		}
		
		public function get source():Array {return _source;}
		public function set source(value:Array):void {
			_source = value;
			
			_current = 0;
			if(picture) {
				picture.source = _source[_current];
			}
			
			if(_source.length > 1) hasLeftRight = true;
			else hasLeftRight = false;
		}
		
		override protected function getCurrentSkinState() : String {
			if(_isChanging) {
				return "changing";
			}
			
			return "normal";
		}
		
		override protected function partAdded(partName:String, instance:Object) : void { 
			super.partAdded(partName, instance);
			
			if(instance == picture) {
				if(_source) {
					picture.source = _source[_current];
				}
			}
			
			if(instance == leftButton) {
				leftButton.addEventListener(MouseEvent.CLICK, leftButton_clickHandler);
			}
			
			if(instance == rightButton) {
				rightButton.addEventListener(MouseEvent.CLICK, rightButton_clickHandler);
			}
		}
		
		override protected function partRemoved(partName:String, instance: Object) : void {
			super.partRemoved(partName, instance);
			
			if(instance == leftButton) {
				leftButton.removeEventListener(MouseEvent.CLICK, leftButton_clickHandler);
			}
			
			if(instance == rightButton) {
				rightButton.removeEventListener(MouseEvent.CLICK, rightButton_clickHandler);
			}
		}
		
		private function leftButton_clickHandler(event:MouseEvent) : void {
			_current--;
			if(_current == -1) {
				_current = _source.length - 1;
			}
			picture.source = _source[_current];
		}
		
		private function rightButton_clickHandler(event:MouseEvent) : void {
			_current++;
			if(_current == _source.length) {
				_current = 0;
			}
			picture.source = _source[_current];
		}
	}
}