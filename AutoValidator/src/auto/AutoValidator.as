package auto
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	import mx.controls.Alert;
	import mx.events.PropertyChangeEvent;
	import mx.utils.ObjectUtil;

	public class AutoValidator extends EventDispatcher
	{
		private var validableFields:Object = {};
		
		private var error:Object = {};
		
		[Bindable(event="errorsChanged")]
		public function getError(property:String):String {
			return error[property];
		}
		
		public function AutoValidator()
		{
			var clazz:Object = ObjectUtil.getClassInfo(this);
			var fields:Array = clazz['properties'] as Array;
			for(var i:int = 0; i < fields.length; i++) {
				var fieldName:String = QName(fields[i]).localName;
				if(clazz['metadata'][fieldName]['Validate'] != null && 
					clazz['metadata'][fieldName]['Bindable'] != null) {
					validableFields[fieldName] = clazz['metadata'][fieldName]['Validate'];
					
					var validator:Object = validableFields[fieldName];
					
					if(!validator['condition'] || validator['condition'] == 'required') {
						validator['type'] = 'required';
					} else if((validator['condition'] as String).indexOf('func:') == 0) {
						validator['type'] = 'func';
						validator['condition'] = this[(validator['condition'] as String).substr(5)];
					} else {
						validator['type'] = 'pattern';
						validator['condition'] = new RegExp(validator['condition']);
					}
					
					error[fieldName] = '';
				}
			}
			
			this.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, onPropertyChange);
			
			for(var property:String in validableFields) {
				//TODO:should do validation later
				//should make a func to validate all, to be called from child
				validate(property, this[property], false);
			}
		}
		
		private function onPropertyChange(event:PropertyChangeEvent):void {
			validate(event.property, event.newValue);
		}
		
		private function validate(property:Object, value:Object, dispatch:Boolean=true):void {
			if(validableFields[property]) {
				var validator:Object = validableFields[property];
				var old:Object = error[property];
				
				//TODO:optimize this
				switch(validator['type']) {
					case 'required':
						if(!value || value == '') error[property] = validator['error'];	
						else error[property] = '';
						break;
					case 'pattern':
						if(!value || !validator['condition'].test(value.toString())) error[property] = validator['error'];
						else error[property] = '';
						break;
					case 'func':
						var satisfied:Boolean = validator['condition'].call(this, value);
						if(!satisfied) error[property] = validator['error'];
						else error[property] = '';
						break;
				}

				if(dispatch && old != error[property]) dispatchEvent(new Event("errorsChanged"));
			}
		}
	}
}