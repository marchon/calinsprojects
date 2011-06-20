#include "CollidableObject.h"

vector<CollidableObject *> CollidableObject::collidableObjects;
unsigned int CollidableObject::_itr1_pos = 0;
unsigned int CollidableObject::_itr2_pos = 0;

CollidableObject::CollidableObject(unsigned int h, float x, float y, float dimx, float dimy, CollidableObject::Type objType) : 
								DrawableObject(h, x, y, dimx, dimy)
{
	colidebleSurfaces = NULL;
	numberOfCollidableSurfaces = 0;;
	currentCollidableSurface = 0;

	type = objType;

	/* 
	The vector looks like this:
		|TYPE_PROJECTILE:TYPE_ROCKET|TYPE_VEHICLE|TYPE_DISTROYABLE_MAP_OBJECT:TYPE_NO_COLLISION_RESPONSE|
		 ^							 ^			  ^
		 0							 _itr1_pos	  _itr2_pos
	*/

	switch(objType)
	{
	case TYPE_PROJECTILE:
	case TYPE_ROCKET:
			collidableObjects.insert(collidableObjects.begin(), this);
			_itr1_pos++;
			_itr2_pos++;
			break;
			
	case TYPE_VEHICLE:
			collidableObjects.insert(collidableObjects.begin() + _itr1_pos, this);
			_itr2_pos++;
			break;
			
	case TYPE_DISTROYABLE_MAP_OBJECT:
	case TYPE_NO_COLLISION_RESPONSE:
			collidableObjects.insert(collidableObjects.end(), this);
			break;
	default:
		break;
	}

	ignoredObject = NULL;
}

CollidableObject::~CollidableObject()
{
	size_t size = collidableObjects.size();

	for(size_t i = 0; i < size; i++)
		if(collidableObjects[i] == this)
		{
			vector<CollidableObject *>::iterator itr = collidableObjects.begin() + i;
			collidableObjects.erase(itr);
			
			/* Make proper adjustments to pointers */
			if(i < _itr1_pos) _itr1_pos--;
			if(i < _itr2_pos) _itr2_pos--;

			break;
		}
}

void CollidableObject::setColisionSurfaces(Collider **colliders, unsigned int nbColliders)
{
	colidebleSurfaces = colliders;
	numberOfCollidableSurfaces = nbColliders;	
	currentCollidableSurface = 0;
}



void CollidableObject::doCollisionCeckingAndResponse()
{
	size_t size = collidableObjects.size();
	
	/* 
	The collision detection and response is done in the following way:
		- for each "not TYPE_DISTROYABLE_MAP_OBJECT or TYPE_NO_COLLISION_RESPONSE" object ceck
		collision with other objects starting from the next position in the vector
		- if collision is found then the collisionResponse handler method is called for bolth
		the first and the seccond object(if an object has "TYPE_NO_COLLISION_RESPONSE" type, 
		then his handler is not called, though it has to be implemented)
		- the handlers argument for an object is the type of the object it collided with 
	*/

	for(unsigned int i = 0; i < _itr2_pos; i++)
	{
		if(collidableObjects[i]->colidebleSurfaces == NULL)  //i guess only explosion objects will be in this situation :-??
				continue;

		for(unsigned int j = ((i < _itr1_pos)? _itr1_pos : (i + 1)); j < size; j++)
		{					//^bullets and rockets do not collide with themselvs
			if(collidableObjects[j]->colidebleSurfaces == NULL ||  //i guess only explosion objects will be in this situation :-??
				collidableObjects[j] == collidableObjects[i]->ignoredObject)
				continue;

			if(isColliding(collidableObjects[i]->colidebleSurfaces[collidableObjects[i]->currentCollidableSurface],
							collidableObjects[j]->colidebleSurfaces[collidableObjects[j]->currentCollidableSurface],
							TO_SCREEN_COORDS(collidableObjects[i]->position.x),
							TO_SCREEN_COORDS(collidableObjects[i]->position.y),
							TO_SCREEN_COORDS(collidableObjects[j]->position.x),
							TO_SCREEN_COORDS(collidableObjects[j]->position.y)
						  )
			  )
			{
				/* First object handler calling */
				collidableObjects[i]->collisionResponse(collidableObjects[j]);
	
				/* Second object handler calling if needed */
				if(collidableObjects[j]->type != TYPE_NO_COLLISION_RESPONSE)
					collidableObjects[j]->collisionResponse(collidableObjects[i]);
			}
			
		}
	}
}