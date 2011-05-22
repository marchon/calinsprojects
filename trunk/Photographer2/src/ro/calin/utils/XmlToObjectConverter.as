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
		public function XmlToObjectConverter() {
			throw new Error("Cannot instantiate.");
		}
		
		/**
		 * Fills the object with the information in xml format provided
		 * by node. It operates recursively and instantiates objects for
		 * inner properties(the types are provided as annotations).
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
		 */
		public static function convertToObject(node:XML, object:Object):void {
			
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
				
				var type:String = getType(object, propertyName);
				var classInfo:Object = ObjectUtil.getClassInfo(object);
				
				if(type == "Array" || type == "mx.collections::ArrayList"  || 
				   type == "mx.collections::IList" || type == "mx.collections::ArrayCollection") {
					var elemType:* = null;
					if(classInfo["metadata"][propertyName]["Listof"] == null) {
						//["metadata"][x] represents the metadata of property x
						throw new Error("You must provide element type Metadata(Listof) for property [" + propertyName + "].");
					}
					
					//get string repr of type from metadata
					if((elemType = (classInfo["metadata"][propertyName]["Listof"]["type"] as String)) == null) {
						throw new Error("You must provide element type for property [" + propertyName + "].");
					}
					
					//BE CAREFULL, THE COMPILATION UNIT MUST BE INCLUDED BY THE COMPILER
					//SO BE SURE TO REFFERENCE IT SOMEWARE(THE STRING METADATA IS NOT A REFFERENCE)
					
					//get the class from string repr
					elemType = getDefinitionByName(elemType) as Class;
					
					var elements:Array = [];
					
					//for each child create a new object and convert recursivly
					for each (var nephew:XML in child.children()) {
						var element:Object = new elemType;
						convertToObject(nephew, element);
						elements.push(element);
					}
					
					//set the array
					if(type == "Array") {
						object[propertyName] = elements;
					} else if(type == "mx.collections::ArrayList"  || type == "mx.collections::IList") {
						object[propertyName] = new ArrayList(elements);
					} else if(type == "mx.collections::ArrayCollection") {
						object[propertyName] = new ArrayCollection(elements);
					}
				} else if(type == "Object" && classInfo["metadata"][propertyName]["Mapof"] != null) {
					//build a map: same as for array, but use an object with keys
					var keyAttrName:String = classInfo["metadata"][propertyName]["Mapof"]["keyname"];
					elemType = null;
					if((elemType = (classInfo["metadata"][propertyName]["Mapof"]["type"] as String)) == null) {
						throw new Error("You must provide element type for property [" + propertyName + "].");
					}
					
					//BE CAREFULL, THE COMPILATION UNIT MUST BE INCLUDED BY THE COMPILER
					//SO BE SURE TO REFFERENCE IT SOMEWARE(THE STRING METADATA IS NOT A REFFERENCE)
					elemType = getDefinitionByName(elemType) as Class;
					
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
		
		private static function getType(obj:Object, propertyName:String):String {
			var typeInfo:XML = describeType(obj);
			var propertyInfo:XMLList = typeInfo['variable'];
			if(propertyInfo.length() == 0) propertyInfo = typeInfo['accessor']; //this is for bindables
			return propertyInfo.(@name == propertyName)[0].@type;
		}
	}
}