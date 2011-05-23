package ro.calin.component.model
{
	import mx.collections.IList;

	/**
	 * Model for the category viewer.
	 * Contains a list of subcategories.
	 */
	public class CategoryViewerModel
	{
		[Listof(type="ro.calin.component.model.SubcategoryModel")]
		public var subcategories:IList;
		
		public var extra:Object;
	}
}