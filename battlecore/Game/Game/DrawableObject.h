#pragma once

#include "Vector2D.h"
#include "Video.h"

#include <vector>
using namespace std;

#include "ObjectDefines.h"

#include "Dbg.h"


class Camera;

class DrawableObject
{
private:
	static vector<DrawableObject *> drawableObjects;
protected:
	const unsigned int height;
	Vector2D position;
	Vector2D dimension;

	DrawableObject(unsigned int, float, float, float, float);
	virtual ~DrawableObject();
	virtual void draw(Graphics *, const Vector2D &) = 0;

public:
	static void drawAllObjectsInSite(Graphics*, const Vector2D &, const Vector2D &);
	static void destroyAllObjectsFromBase();


	friend class Camera;
};