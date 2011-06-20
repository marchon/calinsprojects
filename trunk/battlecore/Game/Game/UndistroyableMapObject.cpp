#include "UndistroyableMapObject.h"

UndistroyableMapObject::UndistroyableMapObject(int h, float x, float y, float dimx, float dimy, Image **frames, int nbFrames, unsigned int delay) : 
	CollidableObject(h, x, y, dimx, dimy, TYPE_NO_COLLISION_RESPONSE)
{
	animation = AnimationManager::createAnimation(frames, nbFrames, delay);
	image = NULL;
	if(nbFrames > 1) 
		animation->startPlaying();
}

UndistroyableMapObject::UndistroyableMapObject(int h, float x, float y, float dimx, float dimy, Image *img) :
	CollidableObject(h, x, y, dimx, dimy, TYPE_NO_COLLISION_RESPONSE)
{
	image = img;
	animation = NULL;
}

UndistroyableMapObject::~UndistroyableMapObject()
{
	if(animation)
		AnimationManager::freeAnimation(animation);
}

void UndistroyableMapObject::draw(Graphics *gr, const Vector2D &screenPosition)
{
	if(animation)
		animation->draw(gr, TO_SCREEN_COORDS(screenPosition.x), TO_SCREEN_COORDS(screenPosition.y), HCENTER | VCENTER);
	else
		gr->drawImage(image, TO_SCREEN_COORDS(screenPosition.x), TO_SCREEN_COORDS(screenPosition.y), HCENTER | VCENTER);
}


void UndistroyableMapObject::collisionResponse(CollidableObject *)
{
	/* response */
}