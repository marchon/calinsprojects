package ro.calin.utils
{
	import flash.display.BitmapData;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	public class Fit2ScreenBitmapProcessor implements BitmapProcessor
	{
		private var _aw:Number;
		private var _ah:Number;
		
		public function Fit2ScreenBitmapProcessor(width:Number, height:Number)
		{
			_aw = width;
			_ah = height;
		}
		
		public function process(bitmapData:BitmapData):BitmapData
		{
			var w:Number = bitmapData.width;
			var h:Number = bitmapData.height;
			var dw:Number = w / _aw;
			var dh:Number = h / _ah;
			
			var scale:Number = 1;
			var sourceRect:Rectangle = null;
			var destPoint:Point = new Point(0, 0);
			
			//magic ratio
			if(w/h <= 1.85) {
				//not panoramic, scale on height
				scale = _ah / h;
				
				if(w * scale > _aw) {
					//crop left+right(happens only on wired res)
					sourceRect = new Rectangle((w * scale - _aw) / 2, 0, _aw, _ah);
				} else {
					//center on horiz
					destPoint.x = (_aw - w * scale) / 2;
				}
			} else {
				//panoramic, scale on width, center on vert
				scale = _aw / w;
				destPoint.y = (_ah - h * scale) / 2;
			}
			
			var m : Matrix = new Matrix();
			if(scale != 1) m.scale(scale, scale);
			if(sourceRect == null) sourceRect = new Rectangle(0, 0, w * scale, h * scale);
			
			//scale
			var bd : BitmapData = new BitmapData(w * scale, h * scale);
			bd.draw(bitmapData, m, null, null, null, true);
			
			//crop and center
			var bd2 : BitmapData = new BitmapData(_aw, _ah, false, 0x000000);
			bd2.copyPixels(bd, sourceRect, destPoint);
			
			return bd2;
		}
	}
}