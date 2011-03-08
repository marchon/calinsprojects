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
		
		
		//TODO: get the type from object property type, not the map
		public static function convert(xml:XML, map:Object):* {
			//handle a simple text node
			if(xml.nodeKind() == "text") {
				return xml.toString();
			}
			
			var name:String = xml.name().toString();
			var clazz:Class = map[name];
			
			if(clazz == null){
				throw new Error("No mapping for [" + name + "].");
			}
			
			
			var obj:Object = new clazz();
			
			for each(var attr:XML in xml.attributes()) {
				var key:String = attr.name().toString();
				
				if(!obj.hasOwnProperty(key)) {
					if(map[key] == null || !obj.hasOwnProperty(key = map[key]))
						throw new Error("Object [" + name + "] has no such property [" + key + "].");
				}
				
				//apparently conversion to simple types works automatically
				obj[key] = attr.toString();
			}
			
			//assume all the children corespond to the same object
			var children:XMLList = xml.children();
			
			if(children.length() > 0) {
				for each (var child:XML in children) {
					key = child.name().toString();
					
					if(!obj.hasOwnProperty(key)) {
						if(map[key] == null || !obj.hasOwnProperty(key = map[key]))
							throw new Error("Object [" + name + "] has no such property [" + key + "].");
					}
					
					var arrayChildren:XMLList = child.children();
					
					var type:String = describeType(obj).variable.(@name == key)[0].@type;
					
					if(arrayChildren.length() == 1 && type != "Array"
						&& type != "mx.collections::IList" && type != "mx.collections::ArrayList" &&
						type != "mx.collections::ArrayCollection") {
						//no array, just an object
						obj[key] = convert(arrayChildren[0], map);
					} else {
						//array of objects
						var array:Array = new Array();
						for each (var arrayChild:XML in arrayChildren) {
							var childObj:Object = convert(arrayChild, map);
							array.push(childObj);
						}
						
						//types of lists
						if(type == "Array") {
							obj[key] = array;
						} else if(type == "mx.collections::ArrayList"  || type == "mx.collections::IList") {
							obj[key] = new ArrayList(array);
						} else if(type == "mx.collections::ArrayCollection") {
							obj[key] = new ArrayCollection(array);
						} else {
							throw new Error("Property [" + key + "] is not an array type.");
						}
					}
				}
			}
			
			return obj;
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