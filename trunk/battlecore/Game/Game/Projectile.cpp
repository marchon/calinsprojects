#include "Projectile.h"
#include "Explosion.h"
#include "Game.h"


Image *Projectile::projectileFrames[PROJECTILE_FRAMES];
Animation *Projectile::projectileAnimation = NULL;
Collider *Projectile::projectileCollideSurface = NULL;

Projectile::Projectile(float x, float y, int nbDir, int dir,  CollidableObject *luncher) : 
	DinamicObject(PROJECTILE_HEIGHT, x, y, float(projectileFrames[0]->getWidth()),
		float(projectileFrames[0]->getWidth()), TYPE_PROJECTILE, PROJECTILE_ACCEL,
		PROJECTILE_SPEED, PROJECTILE_SPEED, nbDir, dir)
{
	setColisionSurfaces(&projectileCollideSurface, 1);
	timeWhenSpawned = Game::gameInstance->currentTimeMillis();
	ignoredObject = luncher;
}
Projectile::~Projectile()
{
}

void Projectile::draw(Graphics *gr, const Vector2D &positionOnScreen)
{
	projectileAnimation->draw(gr, TO_SCREEN_COORDS(positionOnScreen.x), TO_SCREEN_COORDS(positionOnScreen.y),
		HCENTER | VCENTER);
}

void Projectile::collisionResponse(CollidableObject *collisionObject)
{
	queueForDistroy();
	Explosion::createExplosion(EXPLOSION_MEDIUM, position.x, position.y);
}

void Projectile::updateObjectState()
{
	position += (velocity * time);

	if(Game::gameInstance->currentTimeMillis() - timeWhenSpawned > TIME_TO_LIVE)
	{
		queueForDistroy();
		Explosion::createExplosion(EXPLOSION_SMALL, position.x, position.y);
	}
	else if(position.x - dimension.x / 2 < 0 || position.x + dimension.x / 2 > Map::getWidth() ||
			position.y - dimension.y / 2 < 0 || position.y + dimension.y / 2 > Map::getHeight())
	{
		queueForDistroy();
	}
}

void Projectile::loadProjectile()
{
	if(projectileAnimation == NULL)
	{
		for(int i = 0; i < PROJECTILE_FRAMES; i++)
		{
			char pathToFrame[256];
			sprintf_s(pathToFrame, 256, PROJECTILE_FRAMES_PATH, i);

			if((projectileFrames[i] = ImageManager::createImage(pathToFrame)) == NULL)
				return;
		}

		projectileAnimation = AnimationManager::createAnimation(projectileFrames, PROJECTILE_FRAMES, DELAY_BETWEEM_FRAMES);
		projectileAnimation->startPlaying();
		projectileCollideSurface = getCollider(projectileFrames[0]);
	}
}

void Projectile::unloadProjectile()
{
	if(projectileAnimation != NULL)
	{
		freeCollider(projectileCollideSurface);
		AnimationManager::freeAnimation(projectileAnimation);
		for(int i = 0; i < PROJECTILE_FRAMES; i++)
			ImageManager::freeImage(projectileFrames[i]);
	}	
}

void Projectile::createProjectile(float x, float y, int nbDir, int dir, CollidableObject *luncher)
{
	if(projectileAnimation != NULL)
	{
		new Projectile(x, y, nbDir, dir, luncher);
	}
}