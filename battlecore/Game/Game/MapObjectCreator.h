#pragma once

#include "Video.h"
#include "UncollidableMapObject.h"
#include "UndistroyableMapObject.h"
#include "DistroyableMapObject.h"
#include "Collider.h"

#define UNCOLLIDABLE	0
#define UNDISTROYABLE	1
#define DISTROYABLE		2

class MapObjectCreator
{
public:
	static void CreateMapObject(unsigned int, int, int, int, int, int, Image **, Collider **,int, unsigned int);
};