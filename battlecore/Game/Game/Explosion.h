#pragma once

#include "Video.h"
#include "Animation.h"
#include "DistroyableObject.h"
#include "Camera.h"

#define EXPLOSION_SMALL			0
#define EXPLOSION_MEDIUM		1
#define EXPLOSION_BIG			2
#define EXPLOSION_SHOCKWAVE		3
#define NB_TYPES				4

#define EXPLOSION_PATH				"Animations\\Explosions\\Explosion%d\\%d.frm"

#define EXPLOSION_HEIGHT	10


static Uint32 nbFrames[NB_TYPES] = 
{
	4,
	17,
	17,
	9
};

static Uint32 delay[NB_TYPES] = 
{
	115,
	130,
	130,
	80
};


class Explosion : public DistroyableObject
{
private:
	static Image** explosionImages[NB_TYPES];

	static vector<Explosion *> explosions;

	Animation *explosionAnimation;
protected:
	Explosion(float, float, float, float, Image**, unsigned int, unsigned int);
	~Explosion();

	void killIfEnded();

	void draw(Graphics *, const Vector2D &);
	void collisionResponse(CollidableObject *);
public:
	static void loadExplosions();
	static void unloadExplosions();

	static void createExplosion(int, float, float);

	static void explosionsCeckUp();
};