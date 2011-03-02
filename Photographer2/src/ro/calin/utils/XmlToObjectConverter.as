package ro.calin.utils
{
	import flash.utils.Dictionary;
	import flash.utils.describeType;
	import flash.utils.getQualifiedClassName;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;

	public class XmlToObjectConverter
	{
		public function XmlToObjectConverter() {
			throw new Error("Cannot instantiate.");
		}
		
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
					var key1:String = map[key];
					if(key1 == null || !obj.hasOwnProperty(key = key1))
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
						key1 = map[key];
						if(key1 == null || !obj.hasOwnProperty(key = key1))
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
	}
}