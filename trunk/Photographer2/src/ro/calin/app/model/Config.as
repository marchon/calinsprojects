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
		
		public var categories:Object;
		public var picturesets:Object;
		public var textset:Object;
	}
}