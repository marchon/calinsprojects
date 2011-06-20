#pragma once

#include "DinamicObject.h"
#include "Animation.h"


#define PROJECTILE_FRAMES		2
#define PROJECTILE_FRAMES_PATH	"Animations\\Ammo\\Projectile\\%d.bmp"

#define PROJECTILE_SPEED		400
#define PROJECTILE_ACCEL		0

#define DELAY_BETWEEM_FRAMES	100
#define TIME_TO_LIVE			1200

#define PROJECTILE_HEIGHT		6

#define PROJECTILE_DAMAGE		20


class Projectile : public DinamicObject
{
private:
	static Image *projectileFrames[PROJECTILE_FRAMES];
	static Animation *projectileAnimation;
	static Collider *projectileCollideSurface;
protected:
	Uint32 timeWhenSpawned;

	Projectile(float, float, int, int, CollidableObject *);
	~Projectile();

	void draw(Graphics *, const Vector2D &);
	void collisionResponse(CollidableObject *);

	void updateObjectState();
public:
	static void loadProjectile();
	static void unloadProjectile();

	static void createProjectile(float, float, int, int, CollidableObject *);
};