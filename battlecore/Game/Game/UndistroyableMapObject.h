#pragma once

#include "CollidableObject.h"
#include "Video.h"
#include "Animation.h"

class MapObjectCreator;

class UndistroyableMapObject : public CollidableObject
{
private:
	Animation *animation;
	Image *image;
protected:
	UndistroyableMapObject(int, float, float, float, float, Image **, int, unsigned int);
	UndistroyableMapObject(int, float, float, float, float, Image *);
	virtual ~UndistroyableMapObject();

	void draw(Graphics *, const Vector2D &);
	void collisionResponse(CollidableObject *);

public:
	friend class MapObjectCreator;
};

