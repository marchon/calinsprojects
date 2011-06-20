#include "DinamicObject.h"
#include "Dbg.h"
#include "Map.h"

vector<DinamicObject *> DinamicObject::dinamicObjects;

float DinamicObject::time = 0.0f;

DinamicObject::DinamicObject(unsigned int h, float x, float y, float dimx, float dimy, CollidableObject::Type type,
							 float accel, float startSpd, float maxSpd, int numberOfDirections, int initialDirection) : 
							DistroyableObject(h, x, y, dimx, dimy, type), directions(numberOfDirections), direction(initialDirection), 
							diffAngle(360.0f / (float)(numberOfDirections)), velocity(startSpd, direction * diffAngle), 
							scalarAccel(accel), maxVelocityAbsoluteValue(maxSpd), issuedRequest(TEND_TO_STOP),
							lastDirModificationTime(0), minTimeBetweenDirModifs((Uint32)(3200 / directions))
{
	oldPosition.pos = position;
	oldPosition.vel = velocity;
	oldPosition.direction = direction;
	oldPosition.__changeVel2 = 0;

	dinamicObjects.push_back(this);
}



DinamicObject::~DinamicObject()
{
	size_t size = dinamicObjects.size();

	for(size_t i = 0; i < size; i++)
		if(dinamicObjects[i] == this)
		{
			vector<DinamicObject *>::iterator itr = dinamicObjects.begin() + i;
			dinamicObjects.erase(itr);
			break;
		}
}

void DinamicObject::accelerate()
{
	issuedRequest = ACCEL;
}

void DinamicObject::decelerate()
{
	issuedRequest = DECEL;
}

void DinamicObject::rotateLeft()
{
	oldPosition.vel = velocity;
	oldPosition.direction = direction;
	oldPosition.__changeVel2 = 1;

	velocity << diffAngle;
	direction --;
	if(direction == -1) direction = directions - 1;
	currentCollidableSurface = direction;
}

void DinamicObject::rotateRight()
{
	oldPosition.vel = velocity;
	oldPosition.direction = direction;
	oldPosition.__changeVel2 = 1;

	velocity >> diffAngle;
	direction ++;
	if(direction == directions) direction = 0;
	currentCollidableSurface = direction;
}

void DinamicObject::turnLeft()
{
	if(velocity != 0.0f && (SDL_GetTicks() - lastDirModificationTime > 
		minTimeBetweenDirModifs * (maxVelocityAbsoluteValue / abs(int(velocity.absoluteValue)))))
	{
		if(velocity > 0)
			rotateLeft();
		else
			rotateRight();
		
		lastDirModificationTime = SDL_GetTicks();
	}
}

void DinamicObject::turnRight()
{
	if(velocity != 0.0f && (SDL_GetTicks() - lastDirModificationTime > 
		minTimeBetweenDirModifs * (maxVelocityAbsoluteValue / abs(int(velocity.absoluteValue)))))
	{
		if(velocity > 0)
			rotateRight();		
		else
			rotateLeft();
		
		
		lastDirModificationTime = SDL_GetTicks();
	}
}


void DinamicObject::goBackToOldPosition()
{
	position = oldPosition.pos;

	if(oldPosition.__changeVel2)
	{
		velocity = oldPosition.vel;
		direction = oldPosition.direction;
		currentCollidableSurface = direction;
	}
}

void DinamicObject::updateObjectState()
{

	if(velocity != 0.0f)
	{
		oldPosition.pos = position;
		if(oldPosition.__changeVel2 == 1) oldPosition.__changeVel2 = 2;
		else if(oldPosition.__changeVel2 == 2) oldPosition.__changeVel2 = 0;

		position += (velocity * time);

		if(position.x - dimension.x / 2 < 0 || position.x + dimension.x / 2 > Map::getWidth() ||
			position.y - dimension.y / 2 < 0 || position.y + dimension.y / 2 > Map::getHeight())
		{
			goBackToOldPosition();
			velocity.absoluteValue *= (-0.1f);
		}
	}

	switch(issuedRequest)
	{
	case TEND_TO_STOP:
		if(velocity != 0.0f)
		{
			if(velocity > 0.0f)
				velocity -= ((scalarAccel / 2) * time);
			else
				velocity += ((scalarAccel / 2) * time);
			if(velocity < 0.2f && velocity > -0.2f) velocity = 0.0f;
		}
		break;
	case ACCEL:
		if(velocity > 0.0f)
		{
			if(velocity < maxVelocityAbsoluteValue)
				velocity += (scalarAccel* time);
		}
		else
		{
			velocity += ((scalarAccel * 2) * time);
		}
		issuedRequest = TEND_TO_STOP;
		break;
	case DECEL:
		if(velocity > 0.0f)
			velocity -= ((scalarAccel * 2) * time);
		else
		{
			if(velocity > - maxVelocityAbsoluteValue / 2)
				velocity -= (scalarAccel * time);
		}
		issuedRequest = TEND_TO_STOP;
		break;
	}
}


void DinamicObject::updateAllDinamicObjects(Uint32 timePerLastFrame)
{
	time = (float)timePerLastFrame / 1000.0f + 1 / ((float)timePerLastFrame * 300.0f);

	size_t size = dinamicObjects.size();

	for(size_t i = 0; i < size; i++)
	{
		dinamicObjects[i]->updateObjectState();
	}
}