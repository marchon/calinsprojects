#include "Camera.h"

DinamicObject *Camera::objectToFollow = NULL;

bool Camera::isOn = false;

Vector2D Camera::centerOfTheCamera;
Vector2D Camera::viewCenter;
Vector2D Camera::cameraShake;

int Camera::cameraWidth = 0;
int Camera::cameraHeight = 0;

int Camera::shakeIntensity = 0;

Uint32 Camera::lastIntensityChange = 0;
Uint32 Camera::timeForEachShakeLevel = 0;
Uint32 Camera::lastPositionChange = 0;
Uint32 Camera::timeForPositionChange = 0;


/* Manage state of camera */
bool Camera::isTurndOn()
{
	return isOn;
}

void Camera::turnOn(Graphics *gr)
{
	
	if(isOn || !Map::isMapLoaded())
		return;

	isOn = true;

	cameraWidth = gr->getWidth();
	cameraHeight = gr->getHeight() - Hud::hudMinHeight;

	centerOfTheCamera.x = float(cameraWidth / 2);
	centerOfTheCamera.y = float(cameraHeight / 2);

	shakeIntensity = 0;
}

void Camera::shutDown()
{
	isOn = false;
}
/* Manage state of camera */


/* Set the object that will be followed */
void Camera::follow(DinamicObject *object)
{
	if(!isOn)
		return;

	objectToFollow = object;
}
/* Set the object that will be followed */

/* Retain position until another follow request */
void Camera::rest()
{
	if(!isOn)
		return;

	objectToFollow = NULL;
}
/* Retain position until another follow request */

/* Is object in camera view */
bool Camera::isInCameraSight(DrawableObject *object)
{
	return ( (object->position.x + object->dimension.x / 2 >= viewCenter.x - cameraWidth / 2) &&
			 (object->position.y + object->dimension.y / 2 >= viewCenter.y - cameraHeight / 2) &&
			 (object->position.x - object->dimension.x / 2 < viewCenter.x + cameraWidth / 2) &&
			 (object->position.y - object->dimension.y / 2 < viewCenter.y + cameraHeight / 2) );
}
/* Is object in camera view */

/* Request a shake */
void Camera::requestShake(int intensity)
{
	if(!isOn)
		return;

	if(IS_VALID_INTENSITY(intensity) && intensity >= shakeIntensity)
	{
		shakeIntensity = intensity;
		
		timeForEachShakeLevel = TIME_BEFORE_INTENSITY_CHANGES(shakeIntensity);
		timeForPositionChange = TIME_BEFORE_NEW_SHAKE(shakeIntensity);

		srand(lastIntensityChange = CEngine::currentTimeMillis());

		lastPositionChange = 0;
	}
}
/* Request a shake */

/* Update camera: move after object and/or update shake(if needed) */
void Camera::update()
{
	if(!isOn)
		return;

	if(objectToFollow)
	{
		if(objectToFollow->position.x >= cameraWidth / 2 && objectToFollow->position.x < Map::getWidth() - cameraWidth / 2)
			centerOfTheCamera.x = objectToFollow->position.x;
		if(objectToFollow->position.y >= cameraHeight / 2 && objectToFollow->position.y < Map::getHeight() - cameraHeight / 2)
			centerOfTheCamera.y = objectToFollow->position.y;
	}

	viewCenter = centerOfTheCamera;

	if(shakeIntensity)
	{
		if( CEngine::currentTimeMillis() - lastPositionChange >= timeForPositionChange)
		{
			if(centerOfTheCamera.x < cameraWidth / 2 + shakeIntensity)
				cameraShake.x = (rand() % 2) * shakeIntensity;
			else
				if(centerOfTheCamera.x > Map::getWidth() - cameraWidth / 2 - shakeIntensity)
					cameraShake.x = ((rand() % 2) - 1) * shakeIntensity;
				else 
					cameraShake.x = (2 * (rand() % 2) - 1) * shakeIntensity;

			if(centerOfTheCamera.y < cameraHeight / 2 + shakeIntensity)
				cameraShake.y = (rand() % 2) * shakeIntensity;
			else
				if(centerOfTheCamera.y > Map::getHeight() - cameraHeight / 2 - shakeIntensity)
					cameraShake.y = ((rand() % 2) - 1) * shakeIntensity;
				else 
					cameraShake.y = (2 * (rand() % 2) - 1) * shakeIntensity;


			lastPositionChange = CEngine::currentTimeMillis();
		}
		
		if(CEngine::currentTimeMillis() - lastIntensityChange >= timeForEachShakeLevel)
		{
			shakeIntensity --;
			lastIntensityChange = CEngine::currentTimeMillis();
		}

		viewCenter += cameraShake;
	}
}
/* Update camera: move after object and/or update shake(if needed) */

/* Draw map and all visible objects */
void Camera::renderView(Graphics *gr)
{
	if(!isOn)
		return;

	Vector2D topLeft;
	Vector2D bottomRight;

	topLeft.x = viewCenter.x - cameraWidth / 2;
	topLeft.y = viewCenter.y - cameraHeight / 2;

	bottomRight.x = viewCenter.x + cameraWidth / 2;
	bottomRight.y = viewCenter.y + cameraHeight / 2;

	Map::drawMap(gr, topLeft, bottomRight);

	DrawableObject::drawAllObjectsInSite(gr, topLeft, bottomRight);
}
/* Draw map and all visible objects */