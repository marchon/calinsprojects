#include "Map.h"


/* DIMENSIONS */
int Map::tileWidth = 0;
int Map::tileHeight = 0;
int Map::mapWidth = 0;
int Map::mapHeight = 0;
/* DIMENSIONS */

/* MAP */
char *Map::map = NULL;
Image *Map::mapTiles = NULL;
/* MAP */

/* LOADED FLAG */
bool Map::isLoaded = false;
/* LOADED FLAG */

/* MAP OBJECTS CONTAINER */
unsigned int Map::numberOfObjectsOnMap;
ObjectContainer Map::objectContainer[NUMBER_OF_POTENTIAL_MAP_OBJECTS];
/* MAP OBJECTS CONTAINER */


vector<MapCharacteristics *> Map::mapsCharacteristics;

	
/* LOADING RELATED RUTINES */
int Map::loadMap(char *name)
{	
	
	/* Initialize characteristics */
	tileWidth = 0;
	tileHeight = 0;
	mapWidth = 0;
	mapHeight = 0;

	map = NULL;
	mapTiles = NULL;

	numberOfObjectsOnMap = 0;
	/* Initialize characteristics */


	/* Read characteristics */
	FILE *file;
	size_t size = 2 * strlen(name) + strlen(MAP_FOLDER) + 10;
	char *path = new char[size];
	/* Read characteristics */

	/* Open tile BMP */
	strcpy_s(path, size, MAP_FOLDER);
	strcat_s(path, size, name);
	strcat_s(path, size, "\\");
	strcat_s(path, size, name);
	strcat_s(path, size, ".tls");

	mapTiles = ImageManager::createImage(path);

	if(!mapTiles)
		return -1;
	/* Open tile BMP */

	/* Open .map file that contains the offsets of tiles */
	strcpy_s(path, size, MAP_FOLDER);
	strcat_s(path, size, name);
	strcat_s(path, size, "\\");
	strcat_s(path, size, name);
	strcat_s(path, size, ".map");

	if(fopen_s(&file, path, "rb"))
	{
		ImageManager::freeImage(mapTiles);
		return -1;
	}
	/* Open .map file that contains the offsets of tiles */

	/* Read caracteristics */
	if(fread(&tileWidth, sizeof(char), 1, file) != 1||
		fread(&tileHeight, sizeof(char), 1, file) != 1||
		fread(&mapWidth, sizeof(char), 1, file) != 1||
		fread(&mapHeight, sizeof(char), 1, file) != 1)
	{
		fclose(file);
		ImageManager::freeImage(mapTiles);
		return -1;
	}
	/* Read caracteristics */

	/* Read offsets */
	map = new char[mapWidth * mapHeight];
	
	if(fread(map, sizeof(char), mapWidth * mapHeight, file) != mapWidth * mapHeight)
	{
		delete[] map;
		fclose(file);
		ImageManager::freeImage(mapTiles);
		return -1;
	}
	/* Read offsets */

	/* Clean up */
	fclose(file);
	/* Clean up */


	/* Open .obj file to read the object ids for the current map */
	strcpy_s(path, size, MAP_FOLDER);
	strcat_s(path, size, name);
	strcat_s(path, size, "\\");
	strcat_s(path, size, name);
	strcat_s(path, size, ".obj");

	if(fopen_s(&file, path, "rt"))  //TODO: make this a binary file
	{
		delete[] map;
		ImageManager::freeImage(mapTiles);
		return -1;
	}
	/* Open .obj file to read the object ids for the current map */

	/* Read number of objects */
	if(fscanf(file, "%d", &numberOfObjectsOnMap) != 1)
	{
		fclose(file);
		delete[] map;
		ImageManager::freeImage(mapTiles);
		return -1;
	}
	/* Read number of objects */

	unsigned int posx, posy;
	unsigned int id;

	/* Initialize the object container */
	for(int i = 0; i < NUMBER_OF_POTENTIAL_MAP_OBJECTS; i++)
	{
		objectContainer[i].objectCharacteristics = NULL;
		objectContainer[i].numberOfFrames = 0;
	}
	/* Initialize the object container */

	/* Load objects if needed and instantiate them */ 
	for(unsigned int i = 0; i < numberOfObjectsOnMap; i++)
	{
		/* Read object id and position */
		if(fscanf(file, "%u%u%u", &id, &posx, &posy) != 3)
		{
			unloadMap();

			fclose(file);
			
			return -1;
		}
		/* Read object id and position */


		if(id >= NUMBER_OF_POTENTIAL_MAP_OBJECTS)
			continue;

		/* Load the correspondig object for current id if it is not loaded yet */
		if(objectContainer[id].objectCharacteristics == NULL)
		{
			/* Allocate memory */
			objectContainer[id].objectCharacteristics = new ObjectCharacteristics;
			/* Allocate memory */

			char pathToObjInfo[256]; 
			char pathToObjFrameDir[256];
			char pathToObjFrame[256];
			char idStr[5];

			/* Open object information file */
			strcpy_s(pathToObjInfo, 256, MAP_OBJECT_FOLDER);
			sprintf_s(idStr, 5, "%d", id);
			strcat_s(pathToObjInfo, 256, idStr);
			strcpy_s(pathToObjFrameDir, 256, pathToObjInfo);

			strcat_s(pathToObjInfo, 256, OBJECT_INFO_FILE);
			strcat_s(pathToObjFrameDir, 256, OBJECT_FRAME_FOLDER);

			FILE *objeInfoFile;			
			if(fopen_s(&objeInfoFile, pathToObjInfo, "rt"))  //TODO: make this a binary file
			{
				unloadMap();

				fclose(file);

				return -1;
			}
			/* Open object information file */
		
			/* Read characteristics */
			if(fscanf(objeInfoFile, "%u%u%u%u%u%u", &objectContainer[id].objectCharacteristics->type, 
				&objectContainer[id].numberOfFrames, 
				&objectContainer[id].objectCharacteristics->delay,
				&objectContainer[id].objectCharacteristics->dimx,
				&objectContainer[id].objectCharacteristics->dimy,
				&objectContainer[id].objectCharacteristics->height) != 6)
			{
				fclose(objeInfoFile);
				fclose(file);
				delete[] map;
				ImageManager::freeImage(mapTiles);
				return -1;
			}
			/* Read characteristics */

			/* Load frames for objects */
			objectContainer[id].objectFrames = new Image*[objectContainer[id].numberOfFrames];
			if(objectContainer[id].objectCharacteristics->type != UNCOLLIDABLE) //if it's collidable
				objectContainer[id].collideSurfaces = new Collider*[objectContainer[id].numberOfFrames];
			else 
				objectContainer[id].collideSurfaces = NULL;

			for(unsigned int frm = 0; frm < objectContainer[id].numberOfFrames; frm++)
			{
				strcpy_s(pathToObjFrame, 256, pathToObjFrameDir);
				sprintf_s(idStr, 5, "%d", frm);
				strcat_s(pathToObjFrame, 256, idStr);
				strcat_s(pathToObjFrame, 256, ".frm");
				objectContainer[id].objectFrames[frm] = ImageManager::createImage(pathToObjFrame);

				if(!objectContainer[id].objectFrames[frm])
				{
					unloadMap();

					fclose(objeInfoFile);
					fclose(file);
					
					return -1;
				}
				if(objectContainer[id].objectCharacteristics->type != UNCOLLIDABLE)
					objectContainer[id].collideSurfaces[frm] = getCollider(objectContainer[id].objectFrames[frm]);
			}
			/* Load frames for objects */

			/* Close file */
			fclose(objeInfoFile);
			/* Close file */
		}
		/* Load the correspondig object for current id if it is not loaded yet */

		/* Instantiate an object with the following characteristics */
		MapObjectCreator::CreateMapObject(objectContainer[id].objectCharacteristics->type,
										objectContainer[id].objectCharacteristics->height,
										posx, posy,
										objectContainer[id].objectCharacteristics->dimx,
										objectContainer[id].objectCharacteristics->dimy,
										objectContainer[id].objectFrames,
										objectContainer[id].collideSurfaces,
										objectContainer[id].numberOfFrames,
										objectContainer[id].objectCharacteristics->delay);
		/* Instantiate an object with the following characteristics */

	}
	/* Load objects if needed and instantiate them */


	/* Delete unuseful information */
	for(int i = 0; i < NUMBER_OF_POTENTIAL_MAP_OBJECTS; i++)
		if(objectContainer[i].objectCharacteristics != NULL)
			delete objectContainer[i].objectCharacteristics;
	/* Delete unuseful information */

	/* Clean Up */
	fclose(file);

	delete path;
	/* Clean Up */

	/* Set Loaded flag */
	isLoaded = true;
	/* Set Loaded flag */

	return 0;
}

bool Map::isMapLoaded()
{
	return isLoaded;
}

void Map::unloadMap()
{
	if(!isLoaded)
		return;

	/* Free object frames */
	for(int i = 0; i < NUMBER_OF_POTENTIAL_MAP_OBJECTS; i++)
	{
		if(objectContainer[i].numberOfFrames)
		{
			for(unsigned int j = 0; j < objectContainer[i].numberOfFrames; j++)
				ImageManager::freeImage(objectContainer[i].objectFrames[j]);

			if(objectContainer[i].collideSurfaces != NULL)
			{
				for(unsigned int j = 0; j < objectContainer[i].numberOfFrames; j++)
					freeCollider(objectContainer[i].collideSurfaces[j]);


				delete[] objectContainer[i].collideSurfaces;
			}

			delete[] objectContainer[i].objectFrames;
		}
	}
	/* Free object frames */
	
	/* Free tiles */
	delete[] map;
	ImageManager::freeImage(mapTiles);
	/* Free tiles */

	/* Reset loaded flag*/
	isLoaded = false;
	/* Reset loaded flag*/
}
/* LOADING RELATED RUTINES */

/* GET DIMENSIONS */
int Map::getWidth()
{
	if(!isLoaded)
		return -1;

	return tileWidth * mapWidth;
}

int Map::getHeight()
{
	if(!isLoaded)
		return -1;

	return tileHeight * mapHeight;
}
/* GET DIMENSIONS */

/* DRAW MAP GIVEN THE GRAPHICS CONTEXT */
void Map::drawMap(Graphics *gr, const Vector2D &topLeft, const Vector2D &bottomRight)
{
	if(!isLoaded)
		return;

	if(topLeft.x < 0 || topLeft.y < 0 ||
		bottomRight.x >= tileWidth * mapWidth ||
		bottomRight.y >= tileHeight * mapHeight)
		return;

	int left = TO_SCREEN_COORDS(topLeft.x);
	int top = TO_SCREEN_COORDS(topLeft.y);
	int right = TO_SCREEN_COORDS(bottomRight.x);
	int bottom = TO_SCREEN_COORDS(bottomRight.y);
	
	for(int i = left / tileWidth; i < right / tileWidth + 1; i++)
		for(int j = top / tileHeight; j < bottom / tileHeight + 1; j++)
		{
			if(i + j * mapWidth >= mapWidth * mapHeight) break;

			int a = (map[i + j * mapWidth] * tileWidth) %  mapTiles->getWidth();
			int b = ((map[i + j * mapWidth] * tileWidth) /  mapTiles->getWidth()) * tileHeight;

			gr->drawImage(mapTiles, (i - left / tileWidth) * tileWidth - left % tileWidth,
							(j - top / tileHeight) * tileHeight - top % tileHeight, a, b, tileWidth, tileHeight);
		}
			
}
/* DRAW MAP GIVEN THE GRAPHICS CONTEXT */

vector<MapCharacteristics *> Map::getMapListAndInfo()
{	

	if(!mapsCharacteristics.empty())
		return mapsCharacteristics;

	WIN32_FIND_DATA FindFileData;
	HANDLE hFind = INVALID_HANDLE_VALUE;
	LPTSTR DirSpec;
	size_t length_of_arg;

	DirSpec = (LPTSTR) malloc (BUFSIZE);
	StringCbLength(TEXT(MAP_FOLDER), BUFSIZE, &length_of_arg);
	StringCbCopyN(DirSpec, BUFSIZE, TEXT(MAP_FOLDER), length_of_arg+1);
	StringCbCatN(DirSpec, BUFSIZE, TEXT("*"), 2*sizeof(TCHAR));

	hFind = FindFirstFile(DirSpec, &FindFileData);

	char name[BUFSIZE];

	if (hFind != INVALID_HANDLE_VALUE) 
	{
		WCHAR_TO_CHAR(FindFileData.cFileName, name);
		if((FindFileData.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) && 
			strcmp(name, ".") && strcmp(name, "..") && strcmp(name, MAP_OBJECT_FOLDER_NAME))
		{
			size_t size = strlen(name) + 1; //you can never be to sure

			MapCharacteristics *mapChr = new MapCharacteristics;
			mapChr->name = new char[size];
			strcpy_s(mapChr->name, size, name);

			mapsCharacteristics.push_back(mapChr);
		}
		
		while (FindNextFile(hFind, &FindFileData) != 0) 
		{
			WCHAR_TO_CHAR(FindFileData.cFileName, name);
			if((FindFileData.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) && 
				strcmp(name, ".") && strcmp(name, "..") && strcmp(name, MAP_OBJECT_FOLDER_NAME))
			{
				size_t size = strlen(name) + 1; //you can never be to sure

				MapCharacteristics *mapChr = new MapCharacteristics;
				mapChr->name = new char[size];
				strcpy_s(mapChr->name, size, name);

				mapsCharacteristics.push_back(mapChr);
			}
		}
	}
	
	FindClose(hFind);
	
	return mapsCharacteristics;	
}