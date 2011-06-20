#include "DistroyableObject.h"

#include "Camera.h"

vector<DistroyableObject *> DistroyableObject::queuedForDestroy;

DistroyableObject::DistroyableObject(unsigned int h, float x, float y, float dimx, float dimy, CollidableObject::Type type) : 
								CollidableObject(h, x, y, dimx, dimy, type)
{
}

DistroyableObject::~DistroyableObject()
{	
}

void DistroyableObject::queueForDistroy()
{
	size_t size = queuedForDestroy.size();

	for(size_t i = 0; i < size; i++)
		if(queuedForDestroy[i] == this)
			return;

	queuedForDestroy.push_back(this);
}

void DistroyableObject::distroyQueuedObjects()
{
	
	while(!queuedForDestroy.empty())
	{
		DistroyableObject *objToDistroy = queuedForDestroy[queuedForDestroy.size() - 1];

		queuedForDestroy.pop_back();

		if(Camera::objectToFollow == objToDistroy)
			Camera::rest();

		delete objToDistroy;
	}
}
