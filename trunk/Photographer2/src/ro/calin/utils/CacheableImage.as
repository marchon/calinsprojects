/*
ImageCache
version 1.0.0
Created by Michael Ritchie (Mister)
mister@thanksmister.com
http://www.thanksmister.com

Simple component to cache Images loaded from URLs.  This could 
easily be expanded to cache any type of information loaded into 
the Image control. For more information, check Ely Greenfields post:

http://www.quietlyscheming.com/blog/2007/01/29/new-flex-componentsample-superimage/

This is release under a Creative Commons license. More information can be
found here: 

http://creativecommons.org/licenses/by/2.5/

Modified by Calin Avasilcai
*/

package ro.calin.utils
{
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.LoaderInfo;
	import flash.events.Event;
	import flash.system.LoaderContext;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Image;
	import mx.core.mx_internal;
	
	use namespace mx_internal;
	
	/**
	 * Image class that permits bitmap caching
	 * for avoiding subsequent calls to server 
	 * for already loaded images.
	 */ 
	public class CacheableImage extends Image
	{
		/**
		 * Static list of cached images. The actual bitmaps
		 * are mapped by the image urls.
		 * TODO: relpaced with an ordered map.
		 */
		private static var imageDictionary:ArrayCollection = new ArrayCollection();
		
		/**
		 * Maximum number of bitmaps to be kept in the cache.
		 * Default: 50.
		 */ 
		private static var _cacheLimit:Number = 50;
		
		public static function set cacheLimit(num:Number):void {
			_cacheLimit = num;
		}
		
		public static function get cacheLimit():Number {
			return _cacheLimit;
		}
		
		/**
		 * Clears all the cached images.
		 * 
		 */
		public static function clearCache():void {
			imageDictionary.removeAll();
		}
		
		/**
		 * Boolean flag that indicates if this instance
		 * caches loaded bitmaps.
		 * Default: true.
		 */
		private var _cacheBitmap:Boolean = true;
		
		/**
		 * An instance of a bitmap processor which will process
		 * the bitmap before caching. If null, the bitmap won't
		 * be processed.
		 * Default: null.
		 */
		private var _bitmapProcessor:BitmapProcessor = null;
		
		public function CacheableImage() {
			super();
		}
		
		public function set cacheBitmap(value:Boolean):void {
			_cacheBitmap = value;
		}
		
		public function get cacheBitmap():Boolean {
			return _cacheBitmap;
		}
		
		public function set bitmapProcessor(value:BitmapProcessor):void {
			_bitmapProcessor = value;
		}
		
		public function get bitmapProcessor():BitmapProcessor {
			return _bitmapProcessor;
		}
		
		/**
		 * Overridden source setter, it first searches the cache
		 * for the bitmap. If the cache contains the bitmap, it uses
		 * that bitmap instead of requesting the image again.
		 */
		public override function set source(value:Object):void {
			if(_cacheBitmap) {
				if(value is String || value is Object){
					value = searchCache(String(value));
					
					var lc:LoaderContext = new LoaderContext();
					lc.checkPolicyFile = true;
					super.loaderContext = lc;
				}
			}
			
			super.source = value;
		}
		
		/**
		 * Overriden complete event handler, adds the new loaded 
		 * image to the cache. If the image must be processed, it
		 * replaces the source with the processed image.
		 */
		override mx_internal function contentLoaderInfo_completeEventHandler(event:Event):void {
			if(_cacheBitmap) {
				if (LoaderInfo(event.target).loader != contentHolder)
					return;
	
				if(source is String && source != ""){
					var b:Bitmap = processAndCacheImage(source as String);
					if(_bitmapProcessor != null && b != null) {
						super.source = b;  
					}
				}
			}
			
			super.contentLoaderInfo_completeEventHandler(event);
		}
		
		/**
		 * This method applies the filter on the bitmap if needed,
		 * then ads the processed bitmap to the cache.
		 * If the cache exceeds the limit of bitmaps, it removes
		 * the first.
		 * 
		 * @return the processed bitmap
		 */
		private function processAndCacheImage(id:String):Bitmap
		{
			for each ( var newObj:Object in imageDictionary) {
				//returns null if already cached, should not get here
				//because the complete event is not triggered
				//unless the image controll actually loads the
				//image and that happens if it is not in the
				//cache
				if(newObj.id == id) return null;
			}
			var bd : BitmapData = getProcessedBitmapData();
			
			//key-value pair (url, bitmap data)
			var obj:Object = new Object();
			obj.id = id;
			obj.data = bd;
			imageDictionary.addItem(obj);
			
			if(imageDictionary.length > _cacheLimit) {
				imageDictionary.removeItemAt(0);
			}
			
			return new Bitmap(bd);
		}
		
		/**
		 * This method searches the cache for an image by an id(url).
		 * If it finds it, it returns the cached bitmap, else it returns
		 * the passed in id.
		 * 
		 * If a cached bitmap is found, the cached bitmap is moved
		 * in front. This is done with the purpose of avoiding the
		 * removal of frequently used cached data (if tha cache exceeds
		 * a certain limit, items in the back get removed).
		 * 
		 * @return a bitmap or the passed in string
		 */
		private function searchCache(id:String):*
		{
			var bm:Bitmap = new Bitmap();
			for(var i:int = 0; i < imageDictionary.length; i++) {
				var obj:Object = imageDictionary[i];
				if(obj.id == id) {
					//move last accesed in front, 
					//if it's not already there
					if(i < imageDictionary.length - 1) {
						imageDictionary.removeItemAt(i);
						imageDictionary.addItem(obj);
					}
					
					bm = new Bitmap(obj.data);
					return bm;
				}
			}
			return id;
		}
		
		/**
		 * Gets this image bitmap data, and processes it
		 * using the BitmapProcessor, if there is one.
		 * 
		 * @return the bitmap data to be cached
		 */
		private function getProcessedBitmapData() : BitmapData {
			var bd:BitmapData = (this.content as Bitmap).bitmapData;
			if(_bitmapProcessor != null) {
				bd = _bitmapProcessor.process(bd);
			} else {
				//clone it, just to be sure
				bd = bd.clone(); 
			}
			
			return bd;
		}
	}
}