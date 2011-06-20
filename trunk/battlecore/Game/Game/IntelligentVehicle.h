#pragma once

#include "Vehicle.h"
#include "Player.h"

#include "Dbg.h"

#define MAX_INTELLIGENT_VEHICLES	50
#define LINE_OF_SIGHT_RADIUS		500
#define GO_BACK_RADIUS				140

#define SQUER(x)		((x)*(x))
#define ABS(x)			((x) > 0 ? (x) : (-(x)))

class IntelligentVehicle : public Vehicle
{
private:
	static vector<IntelligentVehicle *> intelligentVehicles;
protected:
	Vehicle *huntedVehicle;

	IntelligentVehicle(int, int, int, int);
	~IntelligentVehicle();

	void think();
	void takeActionIfNeeded();
public:
	static void createIntelligentVehicle(int, int, int, int);
	static void updateAllIntelligentVehicles();
};
