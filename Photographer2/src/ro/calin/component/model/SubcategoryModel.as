package ro.calin.component.model
{
	/**
	 * Model for the subcategory component.
	 * Contains an thumbnail url, a name and a desc.
	 */
	[Bindable]
	public class SubcategoryModel
	{
		public var picUrl:String;
		public var name:String;
		public var description:String;
		
		public var extra:Object = null;
	}
}