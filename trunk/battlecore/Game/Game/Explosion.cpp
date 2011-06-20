#include "Explosion.h"

Image** Explosion::explosionImages[NB_TYPES];
vector<Explosion *> Explosion::explosions;

Explosion::Explosion(float x, float y, float dimx, float dimy, Image** frames, unsigned int nbFrames, unsigned int delay) : 
	DistroyableObject(EXPLOSION_HEIGHT, x, y, dimx, dimy, TYPE_NO_COLLISION_AT_ALL)
{
	explosionAnimation = AnimationManager::createAnimation(frames, nbFrames, delay);
	explosionAnimation->startPlaying();

	explosions.push_back(this);
}

Explosion::~Explosion()
{
	AnimationManager::freeAnimation(explosionAnimation);

	for(unsigned int i = 0; i < explosions.size(); i++)
		if(explosions[i] == this)
		{
			vector<Explosion *>::iterator itr = explosions.begin() + i;
			explosions.erase(itr);
			break;
		}
}

void Explosion::draw(Graphics *gr, const Vector2D &positionOnScreen)
{
	explosionAnimation->draw(gr, TO_SCREEN_COORDS(positionOnScreen.x), TO_SCREEN_COORDS(positionOnScreen.y), HCENTER | VCENTER);
}

void Explosion::killIfEnded()
{
	if(explosionAnimation->isAtLastFrame())
		queueForDistroy();
}

void Explosion::collisionResponse(CollidableObject *)
{
	/* doesn't collide */
}

void Explosion::loadExplosions()
{
	for(int i = 0; i < NB_TYPES; i++)
	{
		explosionImages[i] = new Image*[nbFrames[i]];

		for(int j = 0; j < nbFrames[i]; j++)
		{
			char path[256];
			sprintf_s(path, 256, EXPLOSION_PATH, i, j);

			explosionImages[i][j] = ImageManager::createImage(path);
		}
	}
}
void Explosion::unloadExplosions()
{
	for(int i = 0; i < NB_TYPES; i++)
	{
		for(int j = 0; j < nbFrames[i]; j++)
			ImageManager::freeImage(explosionImages[i][j]);

		delete[] explosionImages[i];
	}
	
}
void Explosion::createExplosion(int type, float x, float y)
{
	DrawableObject *object = new Explosion(x, y, float(explosionImages[type][0]->getWidth()),
								float(explosionImages[type][0]->getHeight()), explosionImages[type], 
								nbFrames[type], delay[type]);

	if(type == EXPLOSION_MEDIUM)
	{
		if(Camera::isInCameraSight(object))
			Camera::requestShake(SHAKE_INTENSITY_LOW);
	}
	else if(type == EXPLOSION_BIG)
	{
		if(Camera::isInCameraSight(object))
			Camera::requestShake(SHAKE_INTENSITY_HIGH);
	}
}

void Explosion::explosionsCeckUp()
{
	if(!explosions.empty())
	{
		size_t size = explosions.size();

		for(unsigned int i = 0; i < size; i++)
			explosions[i]->killIfEnded();
	}
}