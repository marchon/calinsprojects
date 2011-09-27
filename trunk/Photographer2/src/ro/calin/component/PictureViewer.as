package ro.calin.component
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.events.ProgressEvent;
	import flash.utils.Dictionary;
	
	import mx.binding.utils.BindingUtils;
	import mx.controls.Alert;
	import mx.events.EffectEvent;
	
	import ro.calin.component.model.PictureModel;
	import ro.calin.component.model.PictureViewerModel;
	import ro.calin.component.skin.PictureViewerSkin;
	
	import spark.components.Button;
	import spark.components.SkinnableContainer;
	import spark.core.ContentCache;
	import spark.core.ContentRequest;
	import spark.core.IContentLoader;
	import spark.effects.Move;
	
	/**
	 * Component that provides posibility to slide through a set of pictures.
	 */
	public class PictureViewer extends SkinnableContainer
	{
		/**
		 * The first picture.
		 * 
		 * In order to make a slide we need two pics loaded at the same time.
		 */
		[SkinPart(required="true")]
		public var picture1:ClippedImage;
		
		/**
		 * Second picture.
		 */
		[SkinPart(required="true")]
		public var picture2:ClippedImage;
		
		[SkinPart(required="true")]
		public var leftButton:Button;
		
		[SkinPart(required="true")]
		public var rightButton:Button;
			
		/**
		 * When the model is changed, the slide will performe in the indicated direction.
		 */
		public var slideDownOnModelChange:Boolean = false;
		
		[Bindable]
		/**
		 * If this is false, the buttons w'ont be displayed.
		 * This is set to false if the set contains only one pic.
		 */
		public var hasLeftRight:Boolean = false;
		
		/**
		 * Points to the picture currently on the screen.
		 * When the slide happens, these to are swapped.
		 */
		private var _currentPicture:ClippedImage;
		
		/**
		 * Points to the image currently outside the screen.
		 */
		private var _outsidePicture:ClippedImage;
		
		/**
		 * The list of picture urls.
		 */
		private var _model:PictureViewerModel;
		
		/**
		 * Index of the picture on the screen.
		 */
		private var _current:Number = 0;
		
		/**
		 * Used for the slide animation.
		 */
		private var _moveAnim:Move;
		
		private var loader:ContentCache;
		
		[Bindable] public var percentLoaded:Number = 0;
		[Bindable] public var loadingInProgress:Boolean = false;
		
		
		/**
		 * Constructor, sets the skin and creates the animation object.
		 */
		public function PictureViewer()
		{
			super();
			
			//set the default skin class
			setStyle("skinClass", PictureViewerSkin);
			
			_moveAnim = new Move();
			_moveAnim.addEventListener(EffectEvent.EFFECT_END, function(event:Event):void {
				//when the picture gets outside, make it invisible
				_outsidePicture.visible = false;
			});
			
			loader = new ContentCache();
		}
		
		/**
		 * Model getter.
		 */
		public function get model():PictureViewerModel {return _model;}
		
		/**
		 * Model setter.
		 * It aditionally performes a slide if the control is on stage.
		 */
		public function set model(value:PictureViewerModel):void {
			if(value == null) return;
		
			_model = value;
			
			_current = 0;
			if(_currentPicture) {
				_outsidePicture.source = PictureModel(_model.pictures[_current]).url;
				if(slideDownOnModelChange) slideDown();
				else slideUp();
			}
			
			//just one pic -> makes no sens to show buttons
			if(_model.pictures.length > 1) hasLeftRight = true;
			else hasLeftRight = false;
			
			//start loading progress
			//TODO: provide posibillity to have sets of pictures that will not be removed
			//eg: wallpapers
			loader.removeAllCacheEntries();
			dict = new Dictionary();
			picNb = _model.pictures.length;
			
			loadingInProgress = true;
			percentLoaded = 0.0;
			for(var i:int = 0; i < _model.pictures.length; i++) {
				var cr:ContentRequest = loader.load(PictureModel(_model.pictures[i]).url);
				if(!cr.complete) {
					cr.addEventListener(ProgressEvent.PROGRESS, function(event:ProgressEvent):void {
						progress(event);
					});
					cr.addEventListener(Event.COMPLETE, function(event:Event):void {
						complete(event);
					});
				}
				dict[cr] = {bytesLoaded : 0, bytesTotal : 0};
			}
		}
		
		private var dict:Dictionary;
		private var picNb:int = 0;
		private function progress(event:ProgressEvent):void {
			dict[event.target] = {bytesLoaded : event.bytesLoaded, bytesTotal : event.bytesTotal};
			
			compute();
		}
		private function complete(event:Event):void {
			delete dict[event.target];
			
			compute();
			
			picNb--;
			
			if(picNb == 0) {
				loadingInProgress = false;
			}
		}
		
		private function compute():void {
			var loaded:Number = 0;
			var total:Number = 0;
			
			for each(var progressInfo:Object in dict) {
				loaded += progressInfo.bytesLoaded;
				total += progressInfo.bytesTotal;
			}
			
			percentLoaded = total > 0? (loaded/total) : 1;
		}
		
		
		
		/**
		 * When ths skin parts are created, make the pointers for outside and inside pics
		 * point to pic1/pic2 and add click listeners for the buttons.
		 */
		override protected function partAdded(partName:String, instance:Object) : void { 
			super.partAdded(partName, instance);
			
			if(instance == picture1) {
				if(_model) {
					picture1.source = PictureModel(_model.pictures[_current]).url;
				}
				_currentPicture = picture1;
				picture1.contentLoader = loader;
			}
			
			if(instance == picture2) {
				_outsidePicture = picture2;
				_outsidePicture.visible = false;
				picture2.contentLoader = loader;
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
		
		/**
		 * Left click: go to privious pic in list, slide to right.
		 */
		private function leftButton_clickHandler(event:MouseEvent) : void {
			_current--;
			if(_current == -1) {
				_current = _model.pictures.length - 1;
			}
			
			slideRight();
		}
		
		/**
		 * Right click: go to next pic in list, slide to left.
		 */
		private function rightButton_clickHandler(event:MouseEvent) : void {
			_current++;
			if(_current == _model.pictures.length) {
				_current = 0;
			}

			slideLeft();
		}
		
		/**
		 * 1. Put outside pic in the right side, outside the visible screen.
		 * 2. Tell animation to move pics on x, from right to left, with an ammount equal to the width.
		 * 3. Start the animation.
		 * 
		 * In the end, the outside pic will get to x,y=0,0 and the inside pic
		 * will get to x,y=-width,0.
		 */
		private function slideLeft():void {
			if(_moveAnim.isPlaying) return;
			
			_outsidePicture.x = this.width;
			_outsidePicture.y = 0;
			_moveAnim.xBy = -this.width;
			performSlide();
		}
		
		/**
		 * 1. Put outside pic in the left side, outside the visible screen.
		 * 2. Tell animation to move pics on x, from left to right, with an ammount equal to the width.
		 * 3. Start the animation.
		 * 
		 * In the end, the outside pic will get to x,y=0,0 and the inside pic
		 * will get to x,y=width,0.
		 */
		private function slideRight():void {
			if(_moveAnim.isPlaying) return;
			
			_outsidePicture.x = -this.width;
			_outsidePicture.y = 0;
			_moveAnim.xBy = this.width;
			performSlide();
		}
		
		/**
		 * 1. Put outside pic in the bottom side, outside the visible screen.
		 * 2. Tell animation to move pics on y, from bottom to top, with an ammount equal to the height.
		 * 3. Start the animation.
		 * 
		 * In the end, the outside pic will get to x,y=0,0 and the inside pic
		 * will get to x,y=-height,0.
		 */
		private function slideUp():void {
			if(_moveAnim.isPlaying) return;
			
			_outsidePicture.x = 0;
			_outsidePicture.y = this.height;
			_moveAnim.yBy = -this.height;
			performSlide();
		}
		
		/**
		 * 1. Put outside pic in the top side, outside the visible screen.
		 * 2. Tell animation to move pics on y, from top to bottom, with an ammount equal to the height.
		 * 3. Start the animation.
		 * 
		 * In the end, the outside pic will get to x,y=0,0 and the inside pic
		 * will get to x,y=height,0.
		 */
		private function slideDown():void {
			if(_moveAnim.isPlaying) return;
			
			_outsidePicture.x = 0;
			_outsidePicture.y = -this.height;
			_moveAnim.yBy = this.height;
			performSlide();
		}
		
		/**
		 * Set the new url for the outside image.
		 * Set the targets(which are the same all the time).
		 * Start it.
		 * Current pic becomes outside pic and viceversa.
		 */
		private function performSlide():void {
			_outsidePicture.visible = true;
			
			_outsidePicture.source = PictureModel(_model.pictures[_current]).url;
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