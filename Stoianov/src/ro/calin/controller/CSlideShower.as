package ro.calin.controller
{
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import ro.calin.model.MSlideShower;

	public class CSlideShower
	{
		public function CSlideShower(model:MSlideShower)
		{
			_model = model;
			
			timerHandler(null);
			_timer = new Timer(_model.delay);
			_timer.addEventListener(TimerEvent.TIMER, timerHandler);
			_timer.start();
		}
		
		private var _model:MSlideShower;
		private var _timer:Timer;
		
		private function timerHandler(e:TimerEvent):void {
			if(_model.random) {
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
		
		public function pause():void {
			_timer.stop();
		}
		
		public function resume():void {
			_timer.start();
		}
	}
}