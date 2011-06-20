#include "UncollidableMapObject.h"

UncollidableMapObject::UncollidableMapObject(int h, float x, float y, float dimx, float dimy, Image **frames, int nbFrames, unsigned int delay) : 
											DrawableObject(h, x, y, dimx, dimy)
{
	animation = AnimationManager::createAnimation(frames, nbFrames, delay);
	image = NULL;
	if(nbFrames > 1) 
		animation->startPlaying();
}

UncollidableMapObject::UncollidableMapObject(int h, float x, float y, float dimx, float dimy, Image *img) :
											DrawableObject(h, x, y, dimx, dimy)
{
	image = img;
	animation = NULL;
}

UncollidableMapObject::~UncollidableMapObject()
{
	if(animation)
		AnimationManager::freeAnimation(animation);
}

void UncollidableMapObject::draw(Graphics *gr, const Vector2D &screenPosition)
{
	if(animation)
		animation->draw(gr, TO_SCREEN_COORDS(screenPosition.x), TO_SCREEN_COORDS(screenPosition.y), HCENTER | VCENTER);
	else
		gr->drawImage(image, TO_SCREEN_COORDS(screenPosition.x), TO_SCREEN_COORDS(screenPosition.y), HCENTER | VCENTER);
}