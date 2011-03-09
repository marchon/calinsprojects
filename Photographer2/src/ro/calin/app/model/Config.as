package ro.calin.app.model
{
	import mx.collections.IList;
	
	import ro.calin.component.model.CategoryViewerModel;
	import ro.calin.component.model.MenuModel;

	public class Config
	{
		public var menu:MenuModel;
		
		[Listof(type="ro.calin.component.model.CategoryViewerModel")]
		public var categories:IList;
		
		[Listof(type="ro.calin.component.model.PictureViewerModel")]
		public var picturesets:IList; 
	}
}