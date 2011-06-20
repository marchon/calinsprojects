#pragma once

#include "CollidableObject.h"

#include "Dbg.h"

class DistroyableObject : public CollidableObject
{
private:
	static vector<DistroyableObject *> queuedForDestroy;
protected:
	DistroyableObject(unsigned int, float, float, float, float, CollidableObject::Type);
	virtual ~DistroyableObject();
	void queueForDistroy();
public:
	static void distroyQueuedObjects();
};