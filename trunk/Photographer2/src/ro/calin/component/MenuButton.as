package ro.calin.component
{
	import spark.components.Button;
	
	public class MenuButton extends Button
	{
		[Bindable]
		public var aniColor:uint;
		
		public function MenuButton()
		{
			super();
		}
	}
}