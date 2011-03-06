package ro.calin.component.event
{
	import flash.events.Event;
	
	import ro.calin.component.model.SubcategoryModel;
	
	[Bindable]
	public class CategoryEvent extends Event
	{
		public static const CATEG_ITEM_CLICK:String = "categItemClick";
		
		public var subcategory:SubcategoryModel;
		
		public function CategoryEvent(type:String, subcategory:SubcategoryModel=null, 
									  bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.subcategory = subcategory;
		}
	}
}