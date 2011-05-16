package ro.calin.app.model
{
	import ro.calin.component.model.MenuModel;

	/**
	 * This class is an in-memory representation of the 
	 * configuration xml. 
	 * It contains the whole model:
	 * 	- menu
	 *  - pictures
	 *  - categories
	 */
	public class Config
	{
		public var menu:MenuModel;
		
		[Mapof(keyname="key", type="ro.calin.component.model.CategoryViewerModel")]
		public var categories:Object;
		
		[Mapof(keyname="key", type="ro.calin.component.model.PictureViewerModel")]
		public var picturesets:Object;
		
		[Mapof(keyname="key", type="ro.calin.app.model.TextModel")]
		public var textset:Object;
	}
}