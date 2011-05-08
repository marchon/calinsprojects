package ro.calin.utils
{
	import flash.display.BitmapData;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	/**
	 * Processes bitmap data in the way described in 
	 * process() method.
	 */
	public class ScaleCropBitmapProcessor implements BitmapProcessor
	{
		/**
		 * Width of destination bitmap.
		 */
		public var width:Number;
		
		/**
		 * Height of destination bitmap.
		 */
		public var height:Number;
		
		public function ScaleCropBitmapProcessor() {
		}
		
		public function process(bitmapData:BitmapData):BitmapData
		{
			var w:Number = bitmapData.width;
			var h:Number = bitmapData.height;
			var dw:Number = w / width;
			var dh:Number = h / height;
			
			var scale:Number = 1;
			var sourceRect:Rectangle = null;
			var destPoint:Point = new Point(0, 0);
			if(w/h >= 1.85) {
				//case 1: panafuckingramic, scale on width, center on vert
				scale = width / w;
				destPoint.y = (height - h * scale) / 2;
			} else if(dh > 1 && dw > 1) {
				//case 2 - bigger on bolth: scale on smallest, then crop on the other
				if(dh > dw) {
					//scale on width, because it's smaller
					scale = width / w;
					sourceRect = new Rectangle(0, (h * scale - height) / 2, width, height);
				} else {
					//scale on height
					scale = height / h;
					sourceRect = new Rectangle((w * scale - width) / 2, 0, width, height);
				}
			} else if(dh > 1) {
				//case 3 - bigger just on height: scale on height, center on horiz
				scale = height / h;
				destPoint.x = (width - w * scale) / 2;
			} else if(dw > 1) {
				//case 4 - bigger just on width: scale on width, center on vert
				scale = width / w;
				destPoint.y = (height - h * scale) / 2;
			} else {
				//case 5 - smaller: center on both
				destPoint.x = (width - w) / 2;
				destPoint.y = (height - h) / 2;
			}
			
			var m : Matrix = new Matrix();
			if(scale != 1) m.scale(scale, scale);
			if(sourceRect == null) sourceRect = new Rectangle(0, 0, w * scale, h * scale);
			
			//scale
			var bd : BitmapData = new BitmapData(w * scale, h * scale);
			bd.draw(bitmapData, m, null, null, null, true);
			
			//crop and center
			//TODO: externalize bg color???
			var bd2 : BitmapData = new BitmapData(width, height, false, 0x000000);
			bd2.copyPixels(bd, sourceRect, destPoint);
			
			return bd2;
		}
	}
}