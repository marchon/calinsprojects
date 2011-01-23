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
*/

package ro.calin.utils
{
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.geom.Matrix;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.controls.Image;
	
	public class ImageCacheUtility
	{
		private static var imageCache : ImageCacheUtility;
		
		private var imageDictionary:ArrayCollection = new ArrayCollection();
		private var _cacheLimit:Number = 50;
		
		public function set cacheLimit(num:Number):void
		{
			_cacheLimit = num;
		}
		
		public function get cacheLimit():Number
		{
			return _cacheLimit;
		}
		
		public function ImageCacheUtility() 
	   	{
	   		if (ImageCacheUtility.imageCache != null )
					throw new Error( "Only one instance should be instantiated" );
	   	}
	   	
	   	public static function getInstance() : ImageCacheUtility
		{
			if ( imageCache == null )
				imageCache = new ImageCacheUtility();
				
			return imageCache;
	  	}
	
		public function cacheImage(id:String, source:Image):void
		{
			for each ( var newObj:Object in imageDictionary)
			{
				if(newObj.id == id) return;
			}
			var bd : BitmapData = getBitmapData( source );
			var obj:Object = new Object();
			obj.id = id;
			obj.data = bd;
			imageDictionary.addItem(obj);
			checkLimit();
		}
		
		public function loadImage(id:String):*
		{
			var bm:Bitmap = new Bitmap();
			for each ( var obj:Object in imageDictionary){
				if(obj.id == id) {
   					bm = new Bitmap( obj.data );
   					return bm;
				}
			}
			return id;
		}
		
		private function getBitmapData( target : Image ) : BitmapData
	   	{
	   		var bd : BitmapData = new BitmapData( target.contentWidth, target.contentHeight);
	    	var m : Matrix = new Matrix();
			//http://www.flexer.info/2008/10/16/how-to-crop-and-resize-an-image-used-as-background-for-canvas/
	    	bd.draw( target, m );
	    	return bd;
	   	}
	   	
	   	public function clear():void
	   	{
	   		imageDictionary.removeAll();
	   	}
	   	
	   	public function removeImage(id:String):void
	   	{
	   		var i:Number = 0;
	   		for each ( var obj:Object in imageDictionary){
				if(obj.id == id) {
   					imageDictionary.removeItemAt(i);
   					return;
				}
				i++
			}
	   	}
	   	
	   	private function checkLimit():void
	   	{
	   		var i:Number = 0;
	   		while(imageDictionary.length > _cacheLimit)
	   		{
	   			imageDictionary.removeItemAt(i);
	   			i++;
	   		}
	   	}
	}
}