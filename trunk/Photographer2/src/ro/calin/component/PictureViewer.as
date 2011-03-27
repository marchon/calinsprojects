package ro.calin.component
{
	import flash.events.MouseEvent;
	
	import mx.controls.Image;
	import mx.core.FlexGlobals;
	
	import ro.calin.component.model.PictureModel;
	import ro.calin.component.model.PictureViewerModel;
	import ro.calin.component.skin.PictureViewerSkin;
	import ro.calin.utils.BitmapProcessor;
	import ro.calin.utils.CacheableImage;
	import ro.calin.utils.Fit2ScreenBitmapProcessor;
	import ro.calin.utils.ScaleCropBitmapProcessor;
	
	import spark.components.Application;
	import spark.components.Button;
	import spark.components.supportClasses.SkinnableComponent;
	import spark.effects.Move;
	
	///http://livedocs.adobe.com/flex/3/html/help.html?content=cursormgr_4.html
	///http://userflex.wordpress.com/2008/07/28/image-caching/
	public class PictureViewer extends SkinnableComponent
	{
		[SkinPart(required="true")]
		public var picture1:CacheableImage;
		
		[SkinPart(required="true")]
		public var picture2:CacheableImage;
		
		[SkinPart(required="true")]
		public var leftButton:Button;
		
		[SkinPart(required="true")]
		public var rightButton:Button;
		
		[Bindable]
		public var hasLeftRight:Boolean = true;
		
		private var _currentPicture:Image;
		private var _outsidePicture:Image;
		
		private var _model:PictureViewerModel;
		private var _current:Number = 0;
		
		private var _moveAnim:Move;
		
		private var _bitmapProcessor:BitmapProcessor;
		
		public function PictureViewer()
		{
			super();
			
			//set the default skin class
			setStyle("skinClass", PictureViewerSkin);
			
			_moveAnim = new Move();
			
			var app:Application = (FlexGlobals.topLevelApplication as Application);
			_bitmapProcessor = new ScaleCropBitmapProcessor(app.width, app.height);
		}
		
		public function get model():PictureViewerModel {return _model;}
		public function set model(value:PictureViewerModel):void {
			//another anim should not start if one is currently in progres, maybe queue just one..
			_model = value;
			
			_current = 0;
			if(_currentPicture) {
				_outsidePicture.source = PictureModel(_model.pictures[_current]).url;
				slideUp();
			}
			
			if(_model.pictures.length > 1) hasLeftRight = true;
			else hasLeftRight = false;
		}
		
		override protected function partAdded(partName:String, instance:Object) : void { 
			super.partAdded(partName, instance);
			
			if(instance == picture1) {
				picture1.bitmapProcessor = _bitmapProcessor;
				if(_model) {
					picture1.source = PictureModel(_model.pictures[_current]).url;
				}
				_currentPicture = picture1;
			}
			
			if(instance == picture2) {
				picture2.bitmapProcessor = _bitmapProcessor;
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
				_current = _model.pictures.length - 1;
			}
			
			_outsidePicture.source = PictureModel(_model.pictures[_current]).url;
			slideLeft();
		}
		
		private function rightButton_clickHandler(event:MouseEvent) : void {
			_current++;
			if(_current == _model.pictures.length) {
				_current = 0;
			}
			_outsidePicture.source = PictureModel(_model.pictures[_current]).url;
			slideRight();
		}
		
		private function slideLeft():void {
			if(_moveAnim.isPlaying) return;
			
			_outsidePicture.x = this.width;
			_outsidePicture.y = 0;
			_moveAnim.xBy = -this.width;
			performSlide();
		}
		
		private function slideRight():void {
			if(_moveAnim.isPlaying) return;
			
			_outsidePicture.x = -this.width;
			_outsidePicture.y = 0;
			_moveAnim.xBy = this.width;
			performSlide();
		}
		
		private function slideUp():void {
			if(_moveAnim.isPlaying) return;
			
			_outsidePicture.x = 0;
			_outsidePicture.y = this.height;
			_moveAnim.yBy = -this.height;
			performSlide();
		}
		
		private function performSlide():void {
			_moveAnim.targets = [_currentPicture, _outsidePicture];
			_moveAnim.play();
			
			//switch between current and outside.
			var temp:* = _currentPicture;
			_currentPicture = _outsidePicture;
			_outsidePicture = temp;
			
			//reset anim
			_moveAnim.xBy = 0;
			_moveAnim.yBy = 0;
		}
	}
}