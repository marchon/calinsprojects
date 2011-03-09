package ro.calin.utils
{
	import flash.utils.Dictionary;
	import flash.utils.describeType;
	import flash.utils.getDefinitionByName;
	import flash.utils.getQualifiedClassName;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.utils.ObjectUtil;

	public class XmlToObjectConverter
	{
		public function XmlToObjectConverter() {
			throw new Error("Cannot instantiate.");
		}
		
		public static function convertToObject(node:XML, object:Object):void {
			
			//1. for each attribute, set the corresponding property
			for each(var attr:XML in node.attributes()) {
				var key:String = attr.name().toString();
				
				if(!object.hasOwnProperty(key)) {
					throw new Error("Object [" + object.toString() + "] has no such property [" + key + "].");
				}
				
				//apparently conversion to simple types works automatically
				object[key] = attr.toString();
			}
			
			//2. for each child, get corresponding property by name
			//	2.1. if property is a list type
			//		2.1.1. get type from metadata of property
			//		2.1.2. for each child of this ch
			//			2.1.2.1. convert recursivly to an instance of type and add it to the array
			//	2.2. else(normal object) create object of that type and convert recursively
			for each (var child:XML in node.children()) {
				key = child.name().toString();
				
				if(!object.hasOwnProperty(key)) {
					throw new Error("Object [" + object.toString() + "] has no such property [" + key + "].");
				}
				
				var type:String = describeType(object).variable.(@name == key)[0].@type;
				
				if(type == "Array" || type == "mx.collections::ArrayList"  || 
				   type == "mx.collections::IList" || type == "mx.collections::ArrayCollection") {

					var classInfo:Object = ObjectUtil.getClassInfo(object);
					var elemType:* = null;
					if((elemType = (classInfo["metadata"][key]["Listof"]["type"] as String)) == null) {
						throw new Error("You must provide element type for property [" + key + "].");
					}
					
					//BE CAREFULL, THE COMPILATION UNIT MUST BE INCLUDED BY THE COMPILER
					//SO BE SURE TO REFFERENCE IT SOMEWARE(THE STRING METADATA IS NOT A REFFERENCE)
					elemType = getDefinitionByName(elemType) as Class;
					
					var elements:Array = [];
					
					for each (var nephew:XML in child.children()) {
						var element:Object = new elemType;
						convertToObject(nephew, element);
						elements.push(element);
					}
					
					if(type == "Array") {
						object[key] = elements;
					} else if(type == "mx.collections::ArrayList"  || type == "mx.collections::IList") {
						object[key] = new ArrayList(elements);
					} else if(type == "mx.collections::ArrayCollection") {
						object[key] = new ArrayCollection(elements);
					}
				} else {
					//TODO: contrary to popular belief, this works, see if it breaks in other cases
					var clazz:Class = getDefinitionByName(type) as Class;
					object[key] = new clazz();
					convertToObject(child, object[key]);
				}
			}
		}
	}
}