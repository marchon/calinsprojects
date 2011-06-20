#pragma once

#include "DrawableObject.h"
#include "Video.h"
#include "Animation.h"

class MapObjectCreator;

class UncollidableMapObject : public DrawableObject
{
private:
	Animation *animation;
	Image *image;
protected:
	UncollidableMapObject(int, float, float, float, float, Image **, int, unsigned int);
	UncollidableMapObject(int, float, float, float, float, Image *);
	virtual ~UncollidableMapObject();

	void draw(Graphics *, const Vector2D &);
public:
	friend class MapObjectCreator;
};

