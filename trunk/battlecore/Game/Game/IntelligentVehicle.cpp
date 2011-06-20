#include "IntelligentVehicle.h"

vector<IntelligentVehicle *> IntelligentVehicle::intelligentVehicles;

IntelligentVehicle::IntelligentVehicle(int x, int y, int type, int dir) : 
		Vehicle(x, y, type, dir)
{
	intelligentVehicles.push_back(this);
	huntedVehicle = Player::instanceOfPlayerVehicle;

	showStatus(rand()%256, rand()%256, rand()%256);
}
IntelligentVehicle::~IntelligentVehicle()
{
	size_t size = intelligentVehicles.size();

	for(size_t i = 0; i < size; i++)
		if(intelligentVehicles[i] == this)
		{
			vector<IntelligentVehicle *>::iterator itr = intelligentVehicles.begin() + i;
			intelligentVehicles.erase(itr);
			break;
		}
}

void IntelligentVehicle::think()
{
	if(huntedVehicle)
	{
		float dx;
		float dy;

		float sqrx;
		float sqry;

		if((sqrx = SQUER(ABS( (dx = (huntedVehicle->position.x - position.x)) ))) + 
			(sqry = SQUER(ABS( (dy = (huntedVehicle->position.y - position.y)) ))) < 
			SQUER(LINE_OF_SIGHT_RADIUS))
		{

			float wantedAngle = ATAN(dy, dx);		
			if(wantedAngle < 0.0f) 
				wantedAngle += 360;

			float currentAngle = velocity.angle;

			float diffAngel = ABS(currentAngle - wantedAngle);

			if(sqrx + sqry > SQUER(GO_BACK_RADIUS))
			{
				accelerate();

				if(diffAngel <= 20)
					shoot();
				else
				{
					if(currentAngle < wantedAngle)
						if(diffAngel > 180)
							turnLeft();
						else
							turnRight();
					else 
						if(diffAngel <= 180)
							turnLeft();
						else
							turnRight();
				}
			}
			else
				if(diffAngel >= 120 && diffAngel <= 180)
					accelerate();
				else
					decelerate();
		}
	}

	if(Player::instanceOfPlayerVehicle == NULL) huntedVehicle = NULL;
}
void IntelligentVehicle::takeActionIfNeeded()
{

}

void IntelligentVehicle::createIntelligentVehicle(int x, int y, int type, int dir)
{
	if(intelligentVehicles.size() < MAX_INTELLIGENT_VEHICLES)
		new IntelligentVehicle(x, y, type, dir);
}
void IntelligentVehicle::updateAllIntelligentVehicles()
{
	size_t size = intelligentVehicles.size();

	for(size_t i = 0; i < size; i++)
	{
		intelligentVehicles[i]->think();
		intelligentVehicles[i]->takeActionIfNeeded();
	}
}