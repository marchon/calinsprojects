#pragma once

#include "DistroyableObject.h"
#include "Video.h"
#include "Animation.h"

class MapObjectCreator;

class DistroyableMapObject : public DistroyableObject
{
private:
	Animation *animation;
	Image *image;
protected:
	DistroyableMapObject(int, float, float, float, float, Image **, int, unsigned int);
	DistroyableMapObject(int, float, float, float, float, Image *);
	virtual ~DistroyableMapObject();

	void draw(Graphics *, const Vector2D &);	
	void collisionResponse(CollidableObject *);

public:
	friend class MapObjectCreator;
};

