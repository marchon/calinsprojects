#include "DistroyableMapObject.h"

DistroyableMapObject::DistroyableMapObject(int h, float x, float y, float dimx, float dimy, Image **frames, int nbFrames, unsigned int delay) : 
	DistroyableObject(h, x, y, dimx, dimy, TYPE_DISTROYABLE_MAP_OBJECT)
{
	animation = AnimationManager::createAnimation(frames, nbFrames, delay);
	image = NULL;
	if(nbFrames > 1) 
		animation->startPlaying();
}

DistroyableMapObject::DistroyableMapObject(int h, float x, float y, float dimx, float dimy, Image *img) :
	DistroyableObject(h, x, y, dimx, dimy, TYPE_DISTROYABLE_MAP_OBJECT)
{
	image = img;
	animation = NULL;
}

DistroyableMapObject::~DistroyableMapObject()
{
	if(animation)
		AnimationManager::freeAnimation(animation);
}

void DistroyableMapObject::draw(Graphics *gr, const Vector2D &screenPosition)
{
	if(animation)
		animation->draw(gr, TO_SCREEN_COORDS(screenPosition.x), TO_SCREEN_COORDS(screenPosition.y), HCENTER | VCENTER);
	else
		gr->drawImage(image, TO_SCREEN_COORDS(screenPosition.x), TO_SCREEN_COORDS(screenPosition.y), HCENTER | VCENTER);
}


void DistroyableMapObject::collisionResponse(CollidableObject *)
{
	/* collision response */
}