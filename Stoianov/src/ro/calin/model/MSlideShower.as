package ro.calin.model
{
	import mx.controls.Alert;

	public class MSlideShower
	{
		public function MSlideShower(root:String, images:String, separator:String, random:Boolean, delay:int)
		{
			_root = root;
			_images = images.split(separator);
			_current = -1;
			_random = random;
			_delay = delay;
		}
		
		private var _random:Boolean;
		public function get random():Boolean {
			return _random;
		}
		
		private var _delay:int;
		public function get delay():int {
			return _delay;
		}
		
		
		private var _images:Array;
		public function get images():Array {
			return _images;
		}
		
		private var _current:int;
		public function get current():int {
			return _current;
		}
		public function set current(value:int):void{
			_current = value;
		}
		
		private var _root:String;
		private var _image:String;
		
		[Bindable]
		public function get image():String {
			return _root + "/" + _image;
		}
		
		public function set image(value:String):void {
			_image = value;			
		}
	}
}