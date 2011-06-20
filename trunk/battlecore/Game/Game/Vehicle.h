#pragma once

#include "Video.h"
#include "DinamicObject.h"
#include "Engine.h"

#include "GeneralDefines.h"


#define DISROYER				0
#define ORECORE					1
#define MAX_VEHICLES			2


#define VEHICLE_DIRECTIONS	16
#define VEHICLE_HEIGHT		5


#define INFO_FILE		"attrib.info"
#define PREVIEW_FILE	"preview.bmp"
static char *vehiclesPath[MAX_VEHICLES] = 
{
	"Vehicles\\Distroyer\\",
	"Vehicles\\OreCore\\"
};

#define SHOOTING_RATE	400

#define STATUS_W				16
#define STATUS_H				1
#define INITIAL_STATUS_OFF		0
#define MAX_STATUS_OFF			5
#define STATUS_CHANGE_AMOUNT	1
#define TIME_TO_NEXT_OFF		110

#define ARM_INVINCIBLE			1000
#define ARM_ARM_PERCENTAGE		0.75f
#define ARM_HP_PERCENTAGE		0.25f

struct VehicleCaracteristics
{
	char name[128];
	unsigned int accel;
	unsigned int maxSpeed;
	unsigned int manevrability;
	unsigned int armor;
	unsigned int hitpoints;
	Image *preview;
};

class IntelligentVehicle;

class Vehicle : public DinamicObject
{
private:
	static Image **vehicleDirectios[MAX_VEHICLES];
	static Collider **collideSurfaces[MAX_VEHICLES];
	static VehicleCaracteristics *vehicleCaracteristics;

	int statusR;
	int statusG;
	int statusB;

	int statusOff;
	int statusOffChangeDirection;
	Uint32 lastStatusOffChange;

protected:
	Image **currentVehicleDirections;

	int armor;
	int maxArmor;
	int hitpoints;
	int maxHitpoints;

	Uint32 lastShootTime;
	
	Vehicle(int, int, int, int);
	virtual ~Vehicle();	

	void shoot();
	void draw(Graphics *, const Vector2D &);
	void collisionResponse(CollidableObject *);

	void showStatus(int, int, int);
public:

	static VehicleCaracteristics* loadAllVehiclesInfo();
	static int unloadAllVehiclesInfo();
	static int loadVehicle(int);
	static int unloadVehicle(int);

	friend class IntelligentVehicle;
};