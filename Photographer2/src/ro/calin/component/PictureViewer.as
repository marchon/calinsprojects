package ro.calin.component
{
	import flash.events.Event;
	
	import mx.events.EffectEvent;
	
	import ro.calin.component.model.PictureViewerModel;
	import ro.calin.component.skin.PictureViewerSkin;
	
	import spark.components.SkinnableContainer;
	import spark.core.IContentLoader;
	import spark.effects.Move;
	
	/**
	 * Component that provides posibillity to slide through a set of pictures.
	 */
	public class PictureViewer extends SkinnableContainer
	{
		public static const MODE_NEXT:int 	= 0;
		public static const MODE_PREV:int 	= 1;
		public static const MODE_RAND:int 	= 2;
		public static const MODE_FIRST:int 	= 3;
		
		public static const DIR_UP:int 		= 0;
		public static const DIR_DOWN:int  	= 1;
		public static const DIR_LEFT:int 	= 2;
		public static const DIR_RIGHT:int	= 3;
		
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
		 * Registers a list of pictures by a name.
		 * Optionally, it can associate a content loader with the list.
		 */
		public function registerModel(name:String, value:PictureViewerModel):void {
			//do not register if already registered
			if(_models[name] != null && _models[name] == value) return;
			//do not register if empty
			if(value.pictures == null || value.pictures.length == 0) return;
			
			//save the values
			_models[name] = value;
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
			if(_models[name] == null || _currentModel == _models[name]) return;
			
			_currentModel = _models[name];
			try {
				var loader:IContentLoader = Registry.instance.check(name) as IContentLoader;
				picture1.contentLoader = loader;
				picture2.contentLoader = loader;
			} catch(e:*) {}
			
			_currentPicIndex = 0;
		}
		
		/**
		 * Slide pic to NEXT, PREVIOUS, or RANDOM in this current picture list, 
		 * in one of the four directions.
		 */
		public function slide(direction:int, mode:int):void {
			if(_currentModel == null || _moveAnim.isPlaying) return;
			
			switch(direction) {
				case DIR_UP:
					_outsidePicture.x = 0;
					_outsidePicture.y = this.height;
					_moveAnim.yBy = -this.height;
					break;
				case DIR_DOWN:
					_outsidePicture.x = 0;
					_outsidePicture.y = -this.height;
					_moveAnim.yBy = this.height;
					break;
				case DIR_LEFT:
					_outsidePicture.x = this.width;
					_outsidePicture.y = 0;
					_moveAnim.xBy = -this.width;
					break;
				case DIR_RIGHT:
					_outsidePicture.x = -this.width;
					_outsidePicture.y = 0;
					_moveAnim.xBy = this.width;
					break;
			}
			
			switch(mode) {
				case MODE_NEXT:
					_currentPicIndex++;
					if(_currentPicIndex == _currentModel.pictures.length) {
						_currentPicIndex = 0;
					}
					break;
				case MODE_PREV:
					_currentPicIndex--;
					if(_currentPicIndex == -1) {
						_currentPicIndex = _currentModel.pictures.length - 1;
					}
					break;
				case MODE_RAND:
					_currentPicIndex = Math.floor(Math.random() * _currentModel.pictures.length);
					break;
				case MODE_FIRST:
					_currentPicIndex = 0;
					break;
			}
			
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
			
			_outsidePicture.source = _currentModel.pictures[_currentPicIndex] as String;
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
	}
}