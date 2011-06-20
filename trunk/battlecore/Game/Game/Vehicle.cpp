#include "Vehicle.h"
#include "Dbg.h"
#include "Explosion.h"
#include "Projectile.h"


Image **Vehicle::vehicleDirectios[MAX_VEHICLES];
Collider **Vehicle::collideSurfaces[MAX_VEHICLES];
VehicleCaracteristics *Vehicle::vehicleCaracteristics = NULL;



Vehicle::Vehicle(int x, int y, int type, int initialDirection) : 
	DinamicObject(VEHICLE_HEIGHT, float(x), float(y), float(vehicleDirectios[type][0]->getWidth()),
		float(vehicleDirectios[type][0]->getHeight()), TYPE_VEHICLE, float(vehicleCaracteristics[type].accel), 0.0f,
		float(vehicleCaracteristics[type].maxSpeed), VEHICLE_DIRECTIONS, 
		initialDirection), statusR(-1), statusG(-1), statusB(-1)
{
	currentVehicleDirections = vehicleDirectios[type];
	maxArmor = armor = vehicleCaracteristics[type].armor;
	maxHitpoints = hitpoints = vehicleCaracteristics[type].hitpoints;

	lastShootTime = 0;

	/* Set the surfaces for collision cecking */
	setColisionSurfaces(collideSurfaces[type], VEHICLE_DIRECTIONS);
	currentCollidableSurface = initialDirection;
}

Vehicle::~Vehicle()
{
}

void Vehicle::shoot()
{
	if(SDL_GetTicks() - lastShootTime >= SHOOTING_RATE)
	{
		Vector2D projPos = position;
		Vector2DAngleBased tmp = velocity;

		tmp.absoluteValue = 40;
		projPos += tmp;
		
		Projectile::createProjectile(projPos.x, projPos.y, VEHICLE_DIRECTIONS, direction, this);

		lastShootTime = SDL_GetTicks();
	}
}

void Vehicle::draw(Graphics *gr, const Vector2D &screenPosition)
{
	gr->drawImage(currentVehicleDirections[direction], TO_SCREEN_COORDS(screenPosition.x), TO_SCREEN_COORDS(screenPosition.y), HCENTER | VCENTER);

	if(statusR >= 0)
	{
		gr->setColor(statusR, statusG, statusB);

		int topLeftX = TO_SCREEN_COORDS(screenPosition.x - dimension.x / 2);
		int topLeftY = TO_SCREEN_COORDS(screenPosition.y - dimension.y / 2);

		int topRightX = TO_SCREEN_COORDS(screenPosition.x + dimension.x / 2);
		int topRightY = TO_SCREEN_COORDS(screenPosition.y - dimension.y / 2);

		int bottomLeftX = TO_SCREEN_COORDS(screenPosition.x - dimension.x / 2);
		int bottomLeftY = TO_SCREEN_COORDS(screenPosition.y + dimension.y / 2);

		int bottomRightX = TO_SCREEN_COORDS(screenPosition.x + dimension.x / 2);
		int bottomRightY = TO_SCREEN_COORDS(screenPosition.y + dimension.y / 2);

		gr->fillRect(topLeftX + statusOff, topLeftY + statusOff, STATUS_W, STATUS_H);
		gr->fillRect(topLeftX + statusOff, topLeftY + statusOff, STATUS_H, STATUS_W);

		gr->fillRect(topRightX - STATUS_W - statusOff, topRightY + statusOff, STATUS_W, STATUS_H);
		gr->fillRect(topRightX - STATUS_H - statusOff, topRightY + statusOff, STATUS_H, STATUS_W);

		gr->fillRect(bottomLeftX + statusOff, bottomLeftY - STATUS_H - statusOff, STATUS_W, STATUS_H);
		gr->fillRect(bottomLeftX + statusOff, bottomLeftY - STATUS_W - statusOff, STATUS_H, STATUS_W);

		gr->fillRect(bottomRightX - STATUS_W - statusOff, bottomRightY - STATUS_H - statusOff, STATUS_W, STATUS_H);
		gr->fillRect(bottomRightX - STATUS_H - statusOff, bottomRightY - STATUS_W - statusOff, STATUS_H, STATUS_W);

		if(CEngine::currentTimeMillis() - lastStatusOffChange >= TIME_TO_NEXT_OFF)
		{
			statusOff += statusOffChangeDirection;

			if(abs(statusOff) == MAX_STATUS_OFF)
				statusOffChangeDirection = -statusOffChangeDirection;

			lastStatusOffChange = CEngine::currentTimeMillis();
		}

		int hpDimX = bottomRightX - bottomLeftX - 25 * 2;

		gr->setColor(BLACK);

		gr->fillRect(bottomLeftX + 25, bottomLeftY, hpDimX, 4);

		
		float hpMax = float(maxHitpoints);
		float hp = float(hitpoints);

		gr->setColor(int(255.0f * ((hp > hpMax / 2) ? (2 * (hpMax - hp) / hpMax) : 1)), 
					 int(255.0f * ((hp > hpMax / 2) ? 1 : (2 * hp / hpMax))), 0);
		

		gr->fillRect(bottomLeftX + 25, bottomLeftY, hitpoints * hpDimX / maxHitpoints, 4);

		gr->setColor(BLACK);

		gr->fillRect(bottomLeftX + 25, bottomLeftY + 7, hpDimX, 4);

		gr->setColor(BLUE);

		gr->fillRect(bottomLeftX + 25, bottomLeftY + 7, armor * hpDimX / maxArmor, 4);
	}
}


void Vehicle::collisionResponse(CollidableObject *collisionObject)
{
	switch(collisionObject->type)
	{
	case TYPE_VEHICLE:
	case TYPE_DISTROYABLE_MAP_OBJECT:
	case TYPE_NO_COLLISION_RESPONSE:
		{
			goBackToOldPosition();
			if(!oldPosition.__changeVel2)
			{
				position -= velocity * time;
				velocity *= (-0.25f);
			}
		}
		break;
	case TYPE_PROJECTILE:
		if(armor > 0)
		{
			float actuallDamage = PROJECTILE_DAMAGE - (PROJECTILE_DAMAGE * (float)(armor)) / ARM_INVINCIBLE;

			armor -= int(ARM_ARM_PERCENTAGE * actuallDamage);
			hitpoints -= int(ARM_HP_PERCENTAGE * actuallDamage);

			if(armor < 0)
				armor = 0;
		}
		else
			hitpoints -= PROJECTILE_DAMAGE;
		
		if(hitpoints <= 0)
		{
			hitpoints = 0;
			Explosion::createExplosion(EXPLOSION_SHOCKWAVE, TO_SCREEN_COORDS(position.x), TO_SCREEN_COORDS(position.y));
			Explosion::createExplosion(EXPLOSION_BIG, TO_SCREEN_COORDS(position.x), TO_SCREEN_COORDS(position.y + 10));
			queueForDistroy();
		}
		break;
	case TYPE_ROCKET:
		break;
	}
}

void Vehicle::showStatus(int r, int g, int b)
{
	statusR = r;
	statusG = g;
	statusB = b;

	statusOff = INITIAL_STATUS_OFF;
	statusOffChangeDirection = STATUS_CHANGE_AMOUNT;
	lastStatusOffChange = CEngine::currentTimeMillis();
}

VehicleCaracteristics* Vehicle::loadAllVehiclesInfo()
{
		/* Load vehicle attributes */
		vehicleCaracteristics = new VehicleCaracteristics[MAX_VEHICLES];

		for(int type = 0; type < MAX_VEHICLES; type ++)
		{

			char path[256];

			strcpy_s(path, 256, vehiclesPath[type]);
			strcat_s(path, 256, INFO_FILE);

			FILE *file;

			if(fopen_s(&file, path, "rb"))
			{
				return NULL;
			}

			if(fscanf(file, "%s%u%u%u%u%u",
				vehicleCaracteristics[type].name,
				&vehicleCaracteristics[type].accel,
				&vehicleCaracteristics[type].maxSpeed,
				&vehicleCaracteristics[type].manevrability,
				&vehicleCaracteristics[type].hitpoints,
				&vehicleCaracteristics[type].armor) != 6)
			{
				fclose(file);
				return NULL;
			}

			fclose(file);

			/* Load preview */

			strcpy_s(path, 256, vehiclesPath[type]);
			strcat_s(path, 256, PREVIEW_FILE);

			vehicleCaracteristics[type].preview = ImageManager::createImage(path);
		}

		return vehicleCaracteristics;
}
int Vehicle::unloadAllVehiclesInfo()
{
	for(int type = 0; type < MAX_VEHICLES; type ++)
		ImageManager::freeImage(vehicleCaracteristics[type].preview);
		
	delete[] vehicleCaracteristics;
	
	return 0;
}

int Vehicle::loadVehicle(int type)
{
	if(type < MAX_VEHICLES)
	{
		/* Load images for each direction */
		vehicleDirectios[type] = new Image*[VEHICLE_DIRECTIONS];
		collideSurfaces[type] = new Collider*[VEHICLE_DIRECTIONS];

		for(int i = 0; i < VEHICLE_DIRECTIONS; i++)
		{
			char pathToDirection[256];
			char id[5];
			
			strcpy_s(pathToDirection, 256, vehiclesPath[type]);
			sprintf_s(id, 5, "%d", i);
			strcat_s(pathToDirection, 256, id);
			strcat_s(pathToDirection, 256, ".bmp");

			vehicleDirectios[type][i] = ImageManager::createImage(pathToDirection);
			collideSurfaces[type][i] = getCollider(vehicleDirectios[type][i]);
		}
	}

	return 0;
}
int Vehicle::unloadVehicle(int type)
{
	if(type < MAX_VEHICLES)
	{
		for(int i = 0; i < VEHICLE_DIRECTIONS; i++)
		{
			ImageManager::freeImage(vehicleDirectios[type][i]);
			freeCollider(collideSurfaces[type][i]);
		}

		delete[] vehicleDirectios[type];
		delete[] collideSurfaces[type];
	}

	return 0;
}