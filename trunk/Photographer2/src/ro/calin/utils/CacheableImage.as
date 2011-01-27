package ro.calin.utils
{
	import mx.controls.Image;
	
	public class CacheableImage extends Image
	{
		private var _cacheBitmap:Boolean = true;
		private var _processor:BitmapProcessor = null;
		
		public function CacheableImage()
		{
			super();
		}
		
		
	}
}