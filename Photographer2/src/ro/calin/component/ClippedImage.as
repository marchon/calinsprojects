package ro.calin.component
{
	import flash.events.Event;
	
	import mx.events.FlexEvent;
	import mx.events.ResizeEvent;
	import mx.graphics.BitmapSmoothingQuality;
	
	import org.osmf.layout.ScaleMode;
	
	import spark.components.Group;
	import spark.core.IContentLoader;
	import spark.primitives.BitmapImage;
	
	/**
	 * Image container that automatically centers ands scales the image after a certain algorithm
	 * and clips the parts that fall outside the width and height. 
	 * 
	 * Scaling algorithm:
	 * if aspect ratio > 1.85 (panoramic)
	 * 		:show it all: scale on width
	 * 
	 * if both width and height exceed display area
	 * 		:scale on the smallest (excess from the other dimension will be cropped)
	 * 	
	 * if just one of the dimension is bigger
	 *		:scale on that dimension
	 * 		
	 */
	public class ClippedImage extends Group
	{	
		private var image:BitmapImage;		
		
		public function ClippedImage()
		{
			//create image and add it to the container
			image = new BitmapImage();
			addElement(image);
			
			//keep it centered
			image.verticalCenter = 0;
			image.horizontalCenter = 0;
			
			//use best algo for scaling
			image.smooth = true;
			image.smoothingQuality = BitmapSmoothingQuality.HIGH;
			
			//clip what falls outside
			this.clipAndEnableScrolling = true;

			//update scaling on image loading
			image.addEventListener(FlexEvent.READY, scaleImage);
			
			//update scaling on resize
			this.addEventListener(ResizeEvent.RESIZE, scaleImage);
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
			if(visible && image && !isNaN(image.sourceWidth)) {
				var w:Number = image.sourceWidth;
				var h:Number = image.sourceHeight;
				var rw:Number = w / this.width;
				var rh:Number = h / this.height;
				
				var scale:Number = NaN;
				
				if(w/h >= 1.85) {
					scale = this.width / w;
				} else if(rh > 1 && rw > 1) {
					if(rh > rw) {
						scale = this.width / w;
					} else {
						scale = this.height / h;
					}
				} else if(rh > 1) {
					scale = this.height / h;
				} else if(rw > 1) {
					scale = this.width / w;
				}
				
				if(!isNaN(scale)) {
					image.scaleX = scale;
					image.scaleY = scale;
				}
			}
		}
	}
}