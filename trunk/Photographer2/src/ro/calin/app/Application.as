package ro.calin.app
{
	import spark.components.supportClasses.SkinnableComponent;
	
	public class Application extends SkinnableComponent
	{
		public function Application()
		{			
			super();
			setStyle("skinClass", ApplicationSkin);
		}
	}
}