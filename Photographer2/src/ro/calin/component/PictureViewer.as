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
	 * Component that provides posibillity to slide through a set of pictures.
	 */
	public class PictureViewer extends SkinnableContainer
	{
		public static const MODE_NEXT:int = 0;
		public static const MODE_PREV:int = 1;
		public static const MODE_RAND:int = 2;
		
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
		private var _models:Object;
		
		/**
		 * The current set of pictures to be displayed.
		 */
		private var _currentModel:PictureViewerModel;
		
		/**
		 * Index of the picture on the screen.
		 */
		private var _currentPicIndex:Number = 0;
		
		/**
		 * Used for the slide animation.
		 */
		private var _moveAnim:Move;
		
		/**
		 * Amout of data received if load is triggered.
		 */
		[Bindable] public var percentLoaded:Number = 0.0;
		
		/**
		 * Whether or not to show a loading bar. 
		 */
		[Bindable] public var loadingInProgress:Boolean = false;
		
		/**
		 * Holds a map of requests for computing loading progress.
		 */
		private var _loadingMap:Dictionary;
		
		/**
		 * Number of pictures that have not finished loading if preloading is atempted.
		 */
		private var _unloadedPicNb:int = 0;
		
		
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
				if(_outsidePicture) _outsidePicture.visible = false;
			});
			
			_models = new Object();
		}
		
		/**
		 * Registers a list of pictures, optionally preloading them. 
		 */
		public function registerModel(name:String, value:PictureViewerModel, preload:Boolean):void {
			//create or reuse a cache loader
			var cache:ContentCache = _models[name] != null? _models[name].cache : new ContentCache();
			cache.removeAllCacheEntries();
			
			//save the values
			_models[name] = {
				cache: cache,
				model: value
			};
			
			//preload if necesarry
			if(preload) load(cache, value);
		}
		
		/**
		 * Removes the model from the list of models.
		 */
		public function unregisterModel(name:String):void {
			_models[name] = null;
		}
		
		/**
		 * Set this set of pictures as the currently displayed model.
		 */
		public function setActiveModel(name:String):void {
			_currentModel = _models[name].model;
			picture1.contentLoader = _models[name].cache;
			picture2.contentLoader = _models[name].cache;
			
			_currentPicIndex = 0;
		}
		
		/**
		 * 1. Put outside pic in the right side, outside the visible screen.
		 * 2. Tell animation to move pics on x, from right to left, with an ammount equal to the width.
		 * 3. Start the animation.
		 * 
		 * In the end, the outside pic will get to x,y=0,0 and the inside pic
		 * will get to x,y=-width,0.
		 */
		public function slideLeft(mode:int):void {
			processMode(mode);
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
		public function slideRight(mode:int):void {
			processMode(mode);
			
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
		public function slideUp(mode:int):void {
			processMode(mode);
			
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
		public function slideDown(mode:int):void {
			processMode(mode);
			
			if(_moveAnim.isPlaying) return;
			
			_outsidePicture.x = 0;
			_outsidePicture.y = -this.height;
			_moveAnim.yBy = this.height;
			performSlide();
		}
		
		
		/**
		 * Starts loading the 
		 */
		private function load(cacheLoader:ContentCache, model:PictureViewerModel):void {			
			_loadingMap = new Dictionary();
			_unloadedPicNb = model.pictures.length;
			
			loadingInProgress = true;
			for(var i:int = 0; i < model.pictures.length; i++) {
				var cr:ContentRequest = cacheLoader.load(PictureModel(model.pictures[i]).url);
				if(!cr.complete) {
					cr.addEventListener(ProgressEvent.PROGRESS, function(event:ProgressEvent):void {
						progress(event);
					});
					cr.addEventListener(Event.COMPLETE, function(event:Event):void {
						complete(event);
					});
				}
				//need to hold a reference to cr, else the callbacks are not called
				_loadingMap[cr] = {bytesLoaded : 0, bytesTotal : 1024 * 1024}; //assume a pic has 1 mb 
			}
		}
		
		private function progress(event:ProgressEvent):void {
			_loadingMap[event.target] = {bytesLoaded : event.bytesLoaded, bytesTotal : event.bytesTotal};
			
			compute();
		}
		private function complete(event:Event):void {
			delete _loadingMap[event.target];
			
			compute();
			
			_unloadedPicNb--;
			
			if(_unloadedPicNb == 0) {
				loadingInProgress = false;
				percentLoaded = 0.0;
			}
		}
		
		private function compute():void {
			var loaded:Number = 0;
			var total:Number = 0;
			
			for each(var progressInfo:Object in _loadingMap) {
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
				_currentPicture = picture1;
			}
			
			if(instance == picture2) {
				_outsidePicture = picture2;
				_outsidePicture.visible = false;
			}
			
		}
		
		private function processMode(mode:int):void {
			switch(mode) {
				case MODE_NEXT:
					next();
					break;
				case MODE_PREV:
					previous();
					break;
				case MODE_RAND:
					randomize();
					break;
			}
		}
		
		private function next():void {
			_currentPicIndex--;
			if(_currentPicIndex == _currentModel.pictures.length) {
				_currentPicIndex = 0;
			}
		}
		
		private function previous():void {
			_currentPicIndex--;
			if(_currentPicIndex == -1) {
				_currentPicIndex = _currentModel.pictures.length - 1;
			}
		}
		
		private function randomize():void {
			_currentPicIndex = Math.floor(Math.random() * _currentModel.pictures.length);
		}
		
		
		/**
		 * Set the new url for the outside image.
		 * Set the targets(which are the same all the time).
		 * Start it.
		 * Current pic becomes outside pic and viceversa.
		 */
		private function performSlide():void {
			_outsidePicture.visible = true;
			
			_outsidePicture.source = PictureModel(_currentModel.pictures[_currentPicIndex]).url;
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