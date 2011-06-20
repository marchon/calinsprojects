#pragma once

#include "Vehicle.h"

class Hud;

class Player : public Vehicle
{
private:
	//static Player *instanceOfPlayerVehicle;
protected:
	Player(int, int, int, int);
	~Player();
public:
	static Player *instanceOfPlayerVehicle;
	static void CreatePlayer(int, int, int, int);
	static bool IsPlayerDead();

	static void Accelerate();
	static void Decelerate();
	static void TurnLeft();
	static void TurnRight();

	static void Shoot();

	friend class Hud;
};