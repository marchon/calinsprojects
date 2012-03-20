package ro.calin.component.model
{
	import mx.collections.IList;

	/**
	 * Model for the category viewer.
	 * Contains a list of subcategories.
	 */
	[Bindable]
	public class CategoryViewerModel
	{
		public var subcategories:IList;
		
		public var thumbHeight:Number = 119;
		public var thumbWidth:Number = 220;
		public var scaleFrom:Number = 1.0;
		public var scaleTo:Number = 1.01;
		
		public var extra:Object;
	}
}