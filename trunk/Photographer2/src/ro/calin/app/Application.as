package ro.calin.app
{
	import mx.controls.Alert;
	import mx.rpc.Fault;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import spark.components.supportClasses.SkinnableComponent;
	
	public class Application extends SkinnableComponent
	{
		public function Application()
		{			
			super();
			setStyle("skinClass", ApplicationSkin);
			
			var service:HTTPService = new HTTPService();
			service.url = 'config.xml';
			service.resultFormat = "e4x";
			service.addEventListener(ResultEvent.RESULT, function(event:ResultEvent):void {
				var config:XML = XML(event.result);
				Alert.show(config);
			});
			service.addEventListener(FaultEvent.FAULT, function(event:FaultEvent):void {
				
			});
			service.send();
		}
	}
}