package ro.calin.app.model
{
	import mx.collections.IList;
	
	import ro.calin.component.model.CategoryViewerModel;
	import ro.calin.component.model.MenuModel;

	public class Config
	{
		public var menu:MenuModel;
		public var categories:IList /*of CategoryViewerModel*/;
		public var picturesets:IList /*of PictureViewerModel*/; 
	}
}