package ro.calin.utils
{
	import flash.utils.Dictionary;
	import flash.utils.describeType;
	import flash.utils.getDefinitionByName;
	import flash.utils.getQualifiedClassName;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.messaging.management.Attribute;
	import mx.utils.ObjectUtil;
	import mx.utils.object_proxy;

	/**
	 * Provides a convenient way of transforming data
	 * in xml format to internal object in-memory representation.
	 * It does this with the help of annotations on properties of
	 * AS classes.
	 * 
	 * //TODO: specify xmlattr-objprop mappings (now it's done by name)
	 * //TODO: provide another mechanism to do list/map type specs, in order
	 * to externalize this info from the model(annotations are in the model)
	 * eg: wrapper around ObjectUtil.getClassInfo() and pass an object with info
	 */
	public class XmlToObjectConverter
	{
		private var mappings:Object;
		
		public function XmlToObjectConverter(mappings:Object = null) {
			this.mappings = mappings;
		}
		
		/**
		 * Fills the object with the information in xml format provided
		 * by node. It operates recursively and instantiates objects for
		 * inner properties.
		 * 
		 * 
		 * <elem attr1="val1">								elem:{
		 * 	<innerelem1 attr1="val1"/>							attr1:'val1',
		 * 	<innerlist1>										innerelem1:{attr1:'val1'},
		 * 		<item1 a="b"/>										innerlist1:[
		 * 		<item2 />											{a:'b'},
		 * 	</innerlist1>					-----------\			{}
		 *  <innermap1>						-----------/		],					
		 * 		<item1 key="key1" d="e"/>							innermap1:{
		 * 		<item2 key="key2"/>									key1: {d:'e'},	
		 * 	</innermap1>											key2: {}												
		 * </elem>												}
		 * 													}
		 * 
		 * Lists or Maps item types are provided as class metadata
		 * or as an object which contains some mappings
		 * 
		 * AS annotated class:
		 * class Elem {
		 * 	public var attr1:String;
		 *  public var innerelem1:MyFancyClass; //which has a string attribute
		 * 	
		 *  [Listof(type="my.package.AnotherFancyClass")]
		 *  public var innerlist1:Array; //or IList or ArrayCollection
		 * 
		 *  [Mapof(keyname="key", type="my.package.YetAnotherFancyClass")]
		 *  public var innermap1:Object;
		 * }
		 * 
		 * Mapping object:
		 * 
		 * mappings = {
		 * 		my.package.Elem : {
		 * 			innerlist1 : ["Listof", AnotherFancyClass.class],
		 * 			innermap1 : ["Mapof", "key", YetAnotherFancyClass.class]
		 * 		}
		 * }
		 * 
		 */
		public function convertToObject(node:XML, object:Object):void {
			var propertyName:String = null;
			
			//1. for each attribute, set the corresponding property
			for each(var attr:XML in node.attributes()) {
				propertyName = attr.name().toString();
				
				//if the property is not defined, just add it
				//conversion to simple types works automat(g)ically
				object[propertyName] = attr.toString();
			}
			
			//2. for each child, get corresponding property by name
			//	2.1. if property is a list type
			//		2.1.1. get type from metadata of property
			//		2.1.2. for each child of this ch
			//			2.1.2.1. convert recursivly to an instance of type and add it to the array
			//	2.2. if property is a map type(is an object with Mapof metadata)
			//		2.2.1. [same as list but one of attrs is key for map]
			//	2.3. else(normal object) create object of that type and convert recursively
			for each (var child:XML in node.children()) {
				if(child.nodeKind() == "text") {
					//TODO: map to a property somehow
					object["content"] = child.toString();
					continue;
				}
				
				propertyName = child.name().toString();
				
				if(!object.hasOwnProperty(propertyName)) {
					throw new Error("Object [" + object.toString() + "] has no such property [" + propertyName + "].");
				}
				
				var type:String = getPropertyType(object, propertyName);
				
				if(isArrayType(type)) {
					var elemType:* = null;
					
					//get string repr of type from metadata
					if((elemType = getArrayItemType(object, propertyName)) == null) {
						throw new Error("You must provide element type for property [" + propertyName + "].");
					}
					
					//BE CAREFULL, THE COMPILATION UNIT MUST BE INCLUDED BY THE COMPILER
					//SO BE SURE TO REFFERENCE IT SOMEWARE(THE STRING METADATA IS NOT A REFFERENCE)
					
					//get the class from string repr
					if(elemType is String) elemType = getDefinitionByName(elemType) as Class;
					
					if(!(elemType is Class)) {
						throw new Error("Property type specified for items of [" + propertyName + "] is not valid: " + elemType);
					}
					
					var elements:Array = [];
					
					//for each child create a new object and convert recursivly
					for each (var nephew:XML in child.children()) {
						var element:Object = new elemType;
						convertToObject(nephew, element);
						elements.push(element);
					}
					
					//set the array
					object[propertyName] = createProperListTypeFromArray(type, elements);
				} else if(isMapType(object, propertyName, type)) {
					//build a map: same as for array, but use an object with keys
					var keyTypePair:Array = getMapItemTypeAndKey(object, propertyName);
					
					var keyAttrName:String = keyTypePair[0];
					elemType = null;
					if((elemType = keyTypePair[1]) == null) {
						throw new Error("You must provide element type for property [" + propertyName + "].");
					}
					
					//BE CAREFULL, THE COMPILATION UNIT MUST BE INCLUDED BY THE COMPILER
					//SO BE SURE TO REFFERENCE IT SOMEWARE(THE STRING METADATA IS NOT A REFFERENCE)
					if(elemType is String) elemType = getDefinitionByName(elemType) as Class;
					
					if(!(elemType is Class)) {
						throw new Error("Property type specified for items of [" + propertyName + "] is not valid: " + elemType);
					}
					
					var map:Object = {};
					
					for each (nephew in child.children()) {
						//do not consider the xml attribute specifying the key as an attribute of the object
						//it needs to be deleted
						var mapKey:String = nephew.attribute(keyAttrName)[0].toString();
						delete nephew.attribute(keyAttrName)[0];
						
						element = new elemType;
						convertToObject(nephew, element);
						
						map[mapKey] = element;
					}
					
					object[propertyName] = map;
				} else {
					//get the type directly(no need to specify it in md)
					var clazz:Class = getDefinitionByName(type) as Class;

					object[propertyName] = new clazz();
					convertToObject(child, object[propertyName]);
				}
			}
		}
		
		private function isArrayType(type:String):Boolean {
			return (type == "Array" || type == "mx.collections::ArrayList" || 
				type == "mx.collections::IList" || type == "mx.collections::ArrayCollection");
		}
		
		private function isMapType(obj:Object, propertyName:String, type:String):Boolean {
			//is object and has map metadata
			return type == "Object" && getMapItemTypeAndKey(obj, propertyName)[0];
		}
		
		private function createProperListTypeFromArray(type:String, arr:*) : * {
			if(type == "mx.collections::ArrayList"  || type == "mx.collections::IList") {
				arr = new ArrayList(arr);
			} else if(type == "mx.collections::ArrayCollection") {
				arr = new ArrayCollection(arr);
			}
			
			return arr;
		}
		
		private function getPropertyType(obj:Object, propertyName:String):String {
			//TODO: cache result??
			var typeInfo:XML = describeType(obj);
			var propertyInfo:XMLList = typeInfo['variable'];
			if(propertyInfo.length() == 0) propertyInfo = typeInfo['accessor']; //this is for bindables
			return propertyInfo.(@name == propertyName)[0].@type;
		}
		
		private function getArrayItemType(obj:Object, propertyName:String):* {
			var type:* = null;
			//returns string or class
			
			if(mappings) {
				var cls:Class = getDefinitionByName(getQualifiedClassName(obj)) as Class;
				if(mappings[cls] == null || mappings[cls][propertyName] == null ||
					mappings[cls][propertyName][0] != "Listof")
					return null;
				
				type = mappings[cls][propertyName][1];
			}
			
			if(!type) {
				//try to get info from metadata on the object
				var classInfo:Object = ObjectUtil.getClassInfo(obj);
				if(classInfo["metadata"] == null || 
					classInfo["metadata"][propertyName] == null ||
					classInfo["metadata"][propertyName]["Listof"] == null) 
						return null;
				
				type =  classInfo["metadata"][propertyName]["Listof"]["type"];
			}
			
			return type;
		}
		
		private function getMapItemTypeAndKey(obj:Object, propertyName:String):Array {
			var keyTypePair:Array = [null, null];
			
			if(mappings) {
				var cls:Class = getDefinitionByName(getQualifiedClassName(obj)) as Class;
				if(mappings[cls] == null || mappings[cls][propertyName] == null ||
					mappings[cls][propertyName][0] != "Mapof")
						return keyTypePair;
				
				keyTypePair[0] = mappings[cls][propertyName][1];
				keyTypePair[1] = mappings[cls][propertyName][2];
			}
			
			//try to get info from metadata
			if(!keyTypePair[0]) {
				var classInfo:Object = ObjectUtil.getClassInfo(obj);
				if(classInfo["metadata"] == null || 
					classInfo["metadata"][propertyName] == null ||
					classInfo["metadata"][propertyName]["Mapof"] == null) 
					return keyTypePair;
				
				keyTypePair[0] = classInfo["metadata"][propertyName]["Mapof"]["keyname"];
				keyTypePair[1] = classInfo["metadata"][propertyName]["Mapof"]["type"];
				
			}
			
			return keyTypePair;
		}
	}
}