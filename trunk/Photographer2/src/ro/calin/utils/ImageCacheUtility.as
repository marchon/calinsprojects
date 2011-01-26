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
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.controls.Image;
	import mx.core.FlexGlobals;
	import mx.rpc.events.HeaderEvent;
	
	import spark.components.Application;
	
	public class ImageCacheUtility
	{
		private static var imageCache : ImageCacheUtility;
		
		private var imageDictionary:ArrayCollection = new ArrayCollection();
		private var _cacheLimit:Number = 50;
		private var _app:Application = (FlexGlobals.topLevelApplication as Application);
		private var _aw:Number = _app.width;
		private var _ah:Number = _app.height;
		
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
	
		public function cacheImage(id:String, source:Image):*
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
			
			return new Bitmap(bd);
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
		
		//http://www.flexer.info/2008/10/16/how-to-crop-and-resize-an-image-used-as-background-for-canvas/
		private function getBitmapData( target : Image ) : BitmapData
	   	{
			var w:Number = target.contentWidth;
			var h:Number = target.contentHeight;
			var dw:Number = w - _aw;
			var dh:Number = h - _ah;
			
			var scale:Number = 1;
			var sourceRect:Rectangle = null;
			var destPoint:Point = new Point(0, 0);
			if(dh > 0 && dw > 0) {
				//case 1 - bigger on bolth: scale on smallest, then crop on the other
				if(dh > dw) {
					//scale on width, because it's smaller
					scale = _aw / w;
					sourceRect = new Rectangle(0, (h * scale - _ah) / 2, _aw, _ah);
				} else {
					//scale on height
					scale = _ah / h;
					sourceRect = new Rectangle((w * scale - _aw) / 2, 0, _aw, _ah);
				}
			} else if(dh > 0) {
				//case 2 - bigger just on height: scale on height, center on horiz
				scale = _ah / h;
				destPoint.x = (_aw - w * scale) / 2;
			} else if(dw > 0) {
				//case 3 - bigger just on width: scale on width, center on vert
				scale = _aw / w;
				destPoint.y = (_ah - h * scale) / 2;
			} else {
				//case 4 - smaller: center on both
				destPoint.x = (_aw - w) / 2;
				destPoint.y = (_ah - h) / 2;
			}
			
			var m : Matrix = new Matrix();
			if(scale != 1) m.scale(scale, scale);
			if(sourceRect == null) sourceRect = new Rectangle(0, 0, w * scale, h * scale);
			
			//scale
			var bd : BitmapData = new BitmapData(w * scale, h * scale);
			bd.draw((target.content as Bitmap).bitmapData, m, null, null, null, true);
			
			//crop and center
			var bd2 : BitmapData = new BitmapData(_aw, _ah, false, 0x000000);
			bd2.copyPixels(bd, sourceRect, destPoint);
			
	    	return bd2;
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