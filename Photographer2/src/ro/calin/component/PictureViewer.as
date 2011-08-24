package ro.calin.component
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.events.ProgressEvent;
	
	import mx.controls.Image;
	import mx.controls.ProgressBar;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElement;
	
	import ro.calin.component.model.PictureModel;
	import ro.calin.component.model.PictureViewerModel;
	import ro.calin.component.skin.PictureViewerSkin;
	import ro.calin.utils.BitmapProcessor;
	import ro.calin.utils.CacheableImage;
	import ro.calin.utils.ScaleCropBitmapProcessor;
	
	import spark.components.Application;
	import spark.components.Button;
	import spark.components.Panel;
	import spark.effects.Move;
	
	/**
	 * Component that provides posibility to slide through a set of pictures.
	 */
	public class PictureViewer extends Panel
	{
		/**
		 * The first picture.
		 * 
		 * In order to make a slide we need two pics.
		 */
		[SkinPart(required="true")]
		public var picture1:CacheableImage;
		
		/**
		 * Second picture.
		 */
		[SkinPart(required="true")]
		public var picture2:CacheableImage;
		
		[SkinPart(required="true")]
		public var leftButton:Button;
		
		[SkinPart(required="true")]
		public var rightButton:Button;
		
//		[SkinPart(required="true")]
		/**
		 * The progress bar for the component.
		 * TODO: load all pics at the beginning and show progress bar.
		 */
//		public var progressBar:ProgressBar;
		
		public var slideDownOnModelChange:Boolean = false;
		
		[Bindable]
		/**
		 * This is auto set to false if the set contains only one pic
		 */
		public var hasLeftRight:Boolean = true;
		
		/**
		 * Point to the picture currently on the screen.
		 */
		private var _currentPicture:Image;
		
		/**
		 * Points to the image currently outside the screen.
		 */
		private var _outsidePicture:Image;
		
		private var _model:PictureViewerModel;
		
		/**
		 * Index of the picture on the screen
		 * in models set of urls.
		 */
		private var _current:Number = 0;
		
		/**
		 * Used to slide.
		 */
		private var _moveAnim:Move;
		
		/**
		 * Used to process the images before caching(CachableImage).
		 */
		public var bitmapProcessor:BitmapProcessor = null;
		
		public function PictureViewer()
		{
			super();
			
			//set the default skin class
			setStyle("skinClass", PictureViewerSkin);
			
			_moveAnim = new Move();
		}
		
		public function get model():PictureViewerModel {return _model;}
		public function set model(value:PictureViewerModel):void {
			if(value == null) return;
		
			_model = value;
			
			_current = 0;
			if(_currentPicture) {
				_outsidePicture.source = PictureModel(_model.pictures[_current]).url;
				if(slideDownOnModelChange) slideDown();
				else slideUp();
			}
			
			if(_model.pictures.length > 1) hasLeftRight = true;
			else hasLeftRight = false;
			
//			triggerImageSetLoading();
		}
		
		override protected function partAdded(partName:String, instance:Object) : void { 
			super.partAdded(partName, instance);
			
			if(instance == picture1) {
				picture1.bitmapProcessor = bitmapProcessor;
				if(_model) {
					picture1.source = PictureModel(_model.pictures[_current]).url;
				}
				_currentPicture = picture1;
			}
			
			if(instance == picture2) {
				picture2.bitmapProcessor = bitmapProcessor;
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
			slideRight();
		}
		
		private function rightButton_clickHandler(event:MouseEvent) : void {
			_current++;
			if(_current == _model.pictures.length) {
				_current = 0;
			}
			_outsidePicture.source = PictureModel(_model.pictures[_current]).url;
			slideLeft();
		}
		
		
		//TODO: another anim should not start if one is currently in progres, maybe queue just one..
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
		
		private function slideDown():void {
			if(_moveAnim.isPlaying) return;
			
			_outsidePicture.x = 0;
			_outsidePicture.y = -this.height;
			_moveAnim.yBy = this.height;
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
		
		
		/*TODO: implement loading whole set at the beginning*/
//		private function triggerImageSetLoading() : void {
//			//			progressBar.visible = true;
//			bytesLoaded = 0;
//			bytesTotal = 0;
//			for each(var pic:PictureModel in _model.pictures) {
//				var loader:CacheableImage = new CacheableImage();
//				loader.bitmapProcessor = bitmapProcessor;
//				loader.source = pic.url;
//				loader.visible = false;
//				this.addElement(loader);
//				loader.addEventListener(ProgressEvent.PROGRESS, progressHandler);
//				loader.addEventListener(Event.COMPLETE, completeHandler);
//			}		
//		}
//		
//		private var bytesLoaded:uint;
//		private var bytesTotal:uint;
//		private var reg:Array = [];
//		private function progressHandler(e:ProgressEvent):void {
//			//add bytes total for all
//			if(reg.indexOf(e.currentTarget) < 0) {
//				bytesTotal += e.bytesTotal;
//				reg.push(e.currentTarget);
//			}
//			
//			bytesLoaded += e.bytesLoaded;
//			
//			progressBar.setProgress(bytesLoaded, bytesTotal);
//			progressBar.visible = bytesLoaded <= bytesTotal;
//		}
//		
//		private function completeHandler(e:Event):void {
//			reg.splice(reg.indexOf(e.currentTarget), 1);
//			this.removeElement(e.currentTarget as IVisualElement);
//			if(reg.length == 0) progressBar.visible = false;
//		}
	}
}