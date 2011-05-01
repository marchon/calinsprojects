package ro.calin.utils
{
	import flash.display.BitmapData;

	/**
	 * An implementation of this interface can be used
	 * by @link{CacheableImage} to process bitmap data
	 * before cache-ing it.
	 */
	public interface BitmapProcessor
	{
		function process(bitmapData:BitmapData):BitmapData;		
	}
}