package ro.calin.component
{
	import flash.events.Event;
	
	import mx.events.FlexEvent;
	import mx.graphics.BitmapSmoothingQuality;
	
	import org.osmf.layout.ScaleMode;
	
	import spark.components.Group;
	import spark.core.ContentCache;
	import spark.core.IContentLoader;
	import spark.primitives.BitmapImage;
	
	public class ClippedImage extends Group
	{	
		private var image:BitmapImage;		
		
		public function ClippedImage()
		{
			image = new BitmapImage();
			addElement(image);
			
			image.verticalCenter = 0;
			image.horizontalCenter = 0;
			image.scaleMode = ScaleMode.LETTERBOX;
			image.smooth = true;
			image.smoothingQuality = BitmapSmoothingQuality.HIGH;
			
//			this.addEventListener(ResizeEvent.RESIZE, scaleImage);
			image.addEventListener(FlexEvent.READY, scaleImage);
			this.clipAndEnableScrolling = true;
		}
		
		public function get source():Object {
			return image.source;
		}
		public function set source(value:Object):void {
			image.source = value;
		} 
		
		public function get contentLoader():IContentLoader {
			return image.contentLoader;
		}
		public function set contentLoader(value:IContentLoader):void {
			image.contentLoader = value;
		} 
		
		protected function scaleImage(event:Event):void 
		{
			//todo: as this is called more than once, 
			///an ideea would be to store the results and apply before redrawing
			//(but maybe this is already done in the setters of height/width
			
			if(image /*and is loaded*/) {
				var w:Number = image.sourceWidth;
				var h:Number = image.sourceHeight;
				var dw:Number = w / this.width;
				var dh:Number = h / this.height; //height is not reported properly
				
//				trace('w=', w, ',h=', h, ',aw=', this.width, ',ah=', this.height);
				
				if(w/h >= 1.85) {
//					trace('case 1: panafuckingramic, scale on width, center on vert');
					image.width = this.width;
				} else if(dh > 1 && dw > 1) {
//					trace('case 2 - bigger on bolth: scale on smallest, then crop on the other');
					if(dh > dw) {
//						trace("scale on width, because it's smaller");
						image.width = this.width;
					} else {
//						trace('scale on height');
						image.height = this.height;
					}
				} else if(dh > 1) {
//					trace('case 3 - bigger just on height: scale on height');
					image.height = this.height;
				} else if(dw > 1) {
//					trace('case 4 - bigger just on width: scale on width');
					image.width = this.width;
				}
			}
		}
	}
}