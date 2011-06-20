This document is supposed to clearly explain the structure of a map and how it stocated on the HDD.
In the Map Directory you can find several folders.
One of them is named "MapObjects" and doesn't concer us right now, the other ones are the acctual maps(/map names).
In each map directory youu can find three files:
		- [map_name].tls - bitmap of tiles
		- [map_name].map - a binary file that stores tile position in map(this is the output of map_editor.exe)
		- [map_name].obj - a text file(which can be edited with eny text editor) that contains info about objects
		Structure of [map_name].obj:
			- 1-st line: number of objects that the map contains(n)
			- 2-nd to (n+1)-th line: object_id position_x position_y (in this order)
								REMEMBER!!! THE COORDINATES ARE FOR THE CENTER OF THE OBJECT!

Now that we've cleard that out, let's move to the MapObjects folder.
This folder contains several folders named "0", "1", "2", etc.
INPORTANT NOTE: The maximum number of types is 256(that is 0-255); If more types need to be added contact the programmer(me:D).
The numerotation coresppondes to the object_id form [map_name].obj(So if you want object 5 on map you place a line in [map_name].obj like this: "5 50 500", which means that object 5 will be placed at x = 50 y = 500 coordinates).

Lets go on... In each objet folder you can find a file and another folder.
The file is named "object.info" and it is a text file structured like this:
		- 1-st line: object_type, which can be:
								- 0 - unclollidable(which is not collidable nor distroyable)
								- 1 - undestroyable(which is collidable but not distroyable)
								- 2 - destroyable(which is both of the above)
								AS A GENERAL RULE: AN OBJECT THAT'S NOT COLLIDABLE IS NOT DESTROYABLE EIGHTER!
								EXCEPT AN EXPLOSION OBJECT WHICH DOES JUST THAT BUT IT IS NOT A MAP OBJECT.
		- 2-nd line: number of frames the animations for this object has(if it is just one image, yes, you've guest...just write "1")
		- 3-th line: delay between frames(if it is a single image it doesn't count, so place 0 for the sake of good structuring)
		- 4-th line and 5-th line: the dimensions of the object, in pixels, on x axis and y axis, in this order.
		- 6-th line: the height of the object(this is inportant for the drawing order, e.g.: when an object is under another)

The folder is called "Frames" and contains as many *.frm files as it has been specifyed in "object.info"(IT BETTER NOT CONTAIN LESS!!!)
The files are named 0.frm, 1.frm, 2.frm, and so on, and they are plain bitmap pictures.


I hope this has been of help.

Regards,
Calin