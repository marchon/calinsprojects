#pragma once

#include "DrawableObject.h"
#include "Collider.h"

class CollidableObject : public DrawableObject
{
private:
	static vector<CollidableObject *> collidableObjects;
	static unsigned int _itr1_pos;
	static unsigned int _itr2_pos;
protected:

	enum Type {
		TYPE_PROJECTILE = 0,
		TYPE_ROCKET,
		TYPE_VEHICLE,
		TYPE_DISTROYABLE_MAP_OBJECT,
		TYPE_NO_COLLISION_RESPONSE,
		TYPE_NO_COLLISION_AT_ALL
	};


	Collider **colidebleSurfaces;
	unsigned int numberOfCollidableSurfaces;
	unsigned int currentCollidableSurface;

	CollidableObject *ignoredObject;

	CollidableObject(unsigned int, float, float, float, float, CollidableObject::Type);
	virtual ~CollidableObject();

	void setColisionSurfaces(Collider **, unsigned int);

	virtual void collisionResponse(CollidableObject *) = 0;
public:
	Type type;

	static void doCollisionCeckingAndResponse();
};