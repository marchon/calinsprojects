#pragma once

#include "DistroyableObject.h"

class DinamicObject : public DistroyableObject
{
private:
	static vector<DinamicObject *> dinamicObjects;

	
protected:
	static float time;

	const float scalarAccel;
	const float maxVelocityAbsoluteValue;

	const float diffAngle;
	const int directions;
	int direction;
	
	Vector2DAngleBased velocity;

	enum Request {TEND_TO_STOP = 0, ACCEL, DECEL} issuedRequest;	

	Uint32 lastDirModificationTime;
	const Uint32 minTimeBetweenDirModifs;

	/*
		FOR SWITCH TO OLD POSITION -- this will not be necessary for all moving objects,
		therefor try to optimize things !!!
	*/

	struct OldPosition
	{
		Vector2D pos;
		Vector2DAngleBased vel;
		int direction;
		int __changeVel2;
	} oldPosition;

	DinamicObject(unsigned int, float, float, float, float, CollidableObject::Type, float, float, float, int, int);

	virtual ~DinamicObject();

	void accelerate();
	void decelerate();

	void rotateLeft();
	void rotateRight();

	void turnLeft();
	void turnRight();

	void goBackToOldPosition();
	  
	virtual void updateObjectState();
public:
	static void updateAllDinamicObjects(Uint32);
};