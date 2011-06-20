#pragma once

#include "Video.h"
#include "Animation.h"
#include "Player.h"

#define HUD_HEIGHT	120
#define HUD_PATH	"Images\\Hud\\Hud.bmp"
#define STATUS_PATH "Images\\Hud\\Health_status.bmp"

#define HUD_ANIMATION_PATH	"Animations\\Hud\\"
#define NB_FRAMES			12
#define FRAME_DELAY			150

#define ANIM_X	9
#define ANIM_Y	16

class Hud
{
private:
	static Image *hudImage;
	static Image *statusImage;
	static Image *hudAnimImages[NB_FRAMES];
	static Animation *hudAnimation;
protected:
public:
	static int hudMinHeight;
	static int loadHud();
	static void unloadHud();
	static void drawHud(Graphics *);
};