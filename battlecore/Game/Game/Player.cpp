#include "Player.h"
#include "Camera.h"
#include "Explosion.h"

Player *Player::instanceOfPlayerVehicle = NULL;

Player::Player(int x, int y, int type, int dir) : 
		Vehicle(x, y, type, dir)
{
	instanceOfPlayerVehicle = this;
}
Player::~Player()
{
	instanceOfPlayerVehicle = NULL;
}

void Player::CreatePlayer(int x, int y, int type, int dir)
{
	if(!instanceOfPlayerVehicle)
	{
		new Player(x, y, type, dir);
		Camera::follow(instanceOfPlayerVehicle);
	}
}

bool Player::IsPlayerDead()
{
	return (instanceOfPlayerVehicle == NULL);
}
void Player::Accelerate()
{
	if(instanceOfPlayerVehicle)
		instanceOfPlayerVehicle->accelerate();
}
void Player::Decelerate()
{
	if(instanceOfPlayerVehicle)
		instanceOfPlayerVehicle->decelerate();
}
void Player::TurnLeft()
{
	if(instanceOfPlayerVehicle)
		instanceOfPlayerVehicle->turnLeft();
}
void Player::TurnRight()
{
	if(instanceOfPlayerVehicle)
		instanceOfPlayerVehicle->turnRight();
}

void Player::Shoot()
{
	if(instanceOfPlayerVehicle)
		instanceOfPlayerVehicle->shoot();
}