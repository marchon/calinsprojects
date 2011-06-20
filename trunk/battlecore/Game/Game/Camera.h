#pragma once

#include "Engine.h"
#include "Video.h"

#include "Vector2D.h"

#include "Map.h"
#include "Hud.h"

#include "DinamicObject.h"
#include "DrawableObject.h"

#include "CameraDefines.h"



class Camera
{
public:
	/* Object followed by camera, if NULL, retain position */
	static DinamicObject *objectToFollow;

	/* Is camera on? */
	static bool isOn;

	/* Vectors, viewCenter = centerOfTheCamera + cameraShake*/
	static Vector2D centerOfTheCamera;
	static Vector2D viewCenter;
	static Vector2D cameraShake;

	/* Dimensions */
	static int cameraWidth;
	static int cameraHeight;

	/* Shake intensity */
	static int shakeIntensity;

	/* Time handeling */
	static Uint32 lastIntensityChange;
	static Uint32 timeForEachShakeLevel;
	static Uint32 lastPositionChange;
	static Uint32 timeForPositionChange;

protected:
public:
	
	/* Manage state of camera */
	static bool isTurndOn();
	static void turnOn(Graphics *);
	static void shutDown();

	/* Set the object that will be followed */
	static void follow(DinamicObject *);
	/* Retain position until another follow request */
	static void rest();

	/* Is object in camera view */
	static bool isInCameraSight(DrawableObject *);

	/* Request a shake */
	static void requestShake(int);

	/* Update camera: move after object and/or update shake(if needed) */
	static void update();
	/* Draw map and all visible objects */
	static void renderView(Graphics *);
};