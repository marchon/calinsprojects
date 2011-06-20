#pragma once

#include "Video.h"
#include "Vector2D.h"
#include "MapObjectCreator.h"

#include "Dbg.h"

/* WINDOWS SPECIFIC PROCEDURES OF HANDELING FILES AND FOLDERS */
#include <windows.h>
#include <malloc.h>
#include <tchar.h> 
#include <wchar.h> 
#include <strsafe.h>
/* WINDOWS SPECIFIC PROCEDURES OF HANDELING FILES AND FOLDERS */

/* VECTOR */
#include <vector>
using namespace std;
/* VECTOR */

/* DEFINES */
#include "MapDefines.h"
/* DEFINES */


/* OBJECT CONTAINERS */
struct ObjectCharacteristics
{
	unsigned int type;
	unsigned int delay;
	unsigned int dimx;
	unsigned int dimy;
	unsigned int height;
};

struct ObjectContainer
{
	ObjectCharacteristics *objectCharacteristics;
	Image **objectFrames;
	Collider **collideSurfaces;
	unsigned int numberOfFrames;
};
/* OBJECT CONTAINERS */

/* MAP PREVIEW */
struct MapCharacteristics
{
	char *name;
	unsigned int maxNumberOfPlayers;
	
	unsigned int *xCoord;
	unsigned int *yCoord;

	Image *mapPreview;
};
/* MAP PREVIEW */

class Map
{
private:

	/* DIMENSIONS */
	static int tileWidth;
	static int tileHeight;
	static int mapWidth;
	static int mapHeight;
	/* DIMENSIONS */

	/* MAP */
	static char *map;
	static Image *mapTiles;
	/* MAP */

	/* LOADED FLAG */
	static bool isLoaded;
	/* LOADED FLAG */


	/* MAP OBJECTS CONTAINER */
	static unsigned int numberOfObjectsOnMap;	
	static ObjectContainer objectContainer[NUMBER_OF_POTENTIAL_MAP_OBJECTS];
	/* MAP OBJECTS CONTAINER */

	static vector<MapCharacteristics *> mapsCharacteristics;

protected:
public:

	/* LOADING RELATED RUTINES */
	static int loadMap(char*);
	static bool isMapLoaded();
	static void unloadMap();
	/* LOADING RELATED RUTINES */

	/* GET DIMENSIONS */
	static int getWidth();
	static int getHeight();
	/* GET DIMENSIONS */

	/* DRAW MAP GIVEN THE GRAPHICS CONTEXT */
	static void drawMap(Graphics*, const Vector2D &, const Vector2D &);
	/* DRAW MAP GIVEN THE GRAPHICS CONTEXT */

	static vector<MapCharacteristics *> getMapListAndInfo();
};