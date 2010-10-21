package ro.calin.controller
{
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import ro.calin.model.MSlideShower;

	public class CSlideShower
	{
		public function CSlideShower(model:MSlideShower, random:Boolean, delay:int)
		{
			_model = model;
			_random = random;
			
			timerHandler(null);
			var timer:Timer = new Timer(delay);
			timer.addEventListener(TimerEvent.TIMER, timerHandler);
			timer.start();
		}
		
		private var _model:MSlideShower;
		private var _random:Boolean;
		
		private function timerHandler(e:TimerEvent):void {
			trace('timerHandler');
			if(_random) {
				var ind:int;
				do {
					ind = Math.floor(Math.random() * _model.images.length);
				} while(ind == _model.current);
				
				_model.current = ind;
			} else {
				_model.current++;
				if(_model.current == _model.images.length) _model.current = 0;
			}
			
			_model.image = _model.images[_model.current];
		}
	}
}