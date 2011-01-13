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
		public var picture1:Image;
		
		[SkinPart(required="true")]
		public var picture2:Image;
		
		private var _currentPicture:Image;
		private var _outsidePicture:Image;
		
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
			if(_currentPicture) {
				_currentPicture.source = _source[_current];
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
			
			if(instance == picture1) {
				if(_source) {
					picture1.source = _source[_current];
					_currentPicture = picture1;
				}
			}
			
			if(instance == picture2) {
				_outsidePicture = picture2;
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
			
			_outsidePicture.source = _source[_current];
			
			_isChanging = true;
			invalidateSkinState();
		}
		
		private function rightButton_clickHandler(event:MouseEvent) : void {
			_current++;
			if(_current == _source.length) {
				_current = 0;
			}
			picture1.source = _source[_current];
		}
	}
}