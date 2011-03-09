package ro.calin.component.model
{
	public class PictureViewerModel
	{
		[Listof(type="ro.calin.component.model.PictureModel")]
		public var pictures:Array;
		
		public var extra:Object;
	}
}