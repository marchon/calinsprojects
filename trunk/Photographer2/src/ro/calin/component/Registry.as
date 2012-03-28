package ro.calin.component
{
	public final class Registry
	{
		private static var _instance:Registry = new Registry();
		
		public function Registry()
		{
			if (_instance != null)
			{
				throw new Error("Registry can only be accessed through Registry.instance");
			}
		}
		
		public static function get instance():Registry
		{
			return _instance;
		}
		
		private var registry:Object = new Object();
		
		public function register(name:String, object:Object) : void {
			registry[name] = object;
		}
		
		public function check(name:String) : Object {
			return registry[name];
		}
		
		public function unregister(name:String) : void {
			registry[name] = null;
		}
	}
}