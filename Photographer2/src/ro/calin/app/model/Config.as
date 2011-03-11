package ro.calin.app.model
{
	import mx.collections.IList;
	
	import ro.calin.component.model.CategoryViewerModel;
	import ro.calin.component.model.MenuModel;

	public class Config
	{
		public var menu:MenuModel;
		
		[Mapof(key="key", type="ro.calin.component.model.CategoryViewerModel")]
		public var categories:Object;
		
		[Mapof(key="key", type="ro.calin.component.model.PictureViewerModel")]
		public var picturesets:Object; 
	}
}