#include "MapObjectCreator.h"
#include "Dbg.h"

void MapObjectCreator::CreateMapObject(unsigned int type, int h, int x, int y, int dimx, int dimy, Image **frames, Collider **collider,int nbFrames, unsigned int animDelay)
{
	switch(type)
	{
	case UNCOLLIDABLE:
		if(nbFrames > 1)
			new UncollidableMapObject(h, float(x), float(y), float(dimx), float(dimy), frames, nbFrames, animDelay);
		else 
			if(nbFrames == 1)
				new UncollidableMapObject(h, float(x), float(y), float(dimx), float(dimy), frames[0]);

		break;
	case UNDISTROYABLE:
		UndistroyableMapObject *temp1;
		if(nbFrames > 1)
			temp1 = new UndistroyableMapObject(h, float(x), float(y), float(dimx), float(dimy), frames, nbFrames, animDelay);
		else 
			if(nbFrames == 1)
				temp1 = new UndistroyableMapObject(h, float(x), float(y), float(dimx), float(dimy), frames[0]);

		temp1->setColisionSurfaces(collider, nbFrames);

		break;
	case DISTROYABLE:
		DistroyableMapObject *temp2;
		if(nbFrames > 1)
			temp2 = new DistroyableMapObject(h, float(x), float(y), float(dimx), float(dimy), frames, nbFrames, animDelay);
		else 
			if(nbFrames == 1)
				temp2 = new DistroyableMapObject(h, float(x), float(y), float(dimx), float(dimy), frames[0]);

		temp2->setColisionSurfaces(collider, nbFrames);

		break;
	default:
		break;
	}
}