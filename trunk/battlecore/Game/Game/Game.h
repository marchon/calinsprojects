#pragma once

#include "Engine.h"
#include "State.h"
#include "Font.h"

#include "Dbg.h"

#include "Map.h"
#include "Vehicle.h"
#include "Camera.h"
#include "Explosion.h"
#include "Player.h"
#include "Hud.h"
#include "Projectile.h"
#include "IntelligentVehicle.h"

/* DEFINES */
#include "GeneralDefines.h"
#include "MainMenuDefines.h"
#include "GameDefines.h"
#include "IntroDefines.h"
/* DEFINES */


class Game: public CEngine
{
private:

	/* KEY HANDLEING */
	static bool keyBuffer[TOTAL_KEYS];
	/* KEY HANDLEING */


	/* MENU RELATED */
	static Font *menuFont;
	static Image *logoImage;

	static char *currentMenuTitle;
	static unsigned int titleLen;
	static int cursorPosition;
	static bool cursorBlink;
	static Uint32 time;

	static void drawMenuTitle(Graphics *);
	static void setMenuTitle(char *);


	static char **currentMenuEntries;
	static unsigned int currentMenuOptionsNumber;
	static int currentMenuCurrentOption;

	static int menuTranzitState;
	static bool showCursor;
	static Uint32 lastTimeDelayChanged;
	static Uint32 lastTimeCursorStateChanged;
	static Uint32 currentBlinkInterval;

	static int handleMenuInputAndDrawMenuOptions(Graphics *);
	static void setMenuEntries(char **, unsigned int);
	/* MENU RELATED */


/* GAME STATES */

	/* INIT STATE */
	static void initState(Graphics *, Purpose);
	/* INIT STATE */

	/* INTRO STATE */
	static Image *splashScreen;
	static Image *pressSpace;

	static void introState(Graphics *, Purpose);
	/* INTRO STATE */

	/* MAIN MENU STATE */
	static char *mainMenuStrings[TOTAL_MAIN_MENU_OPTIONS];
	static void mainMenuState(Graphics *, Purpose);
	/* MAIN MENU STATE */

	/* CHOOSE MAP STATE */
	static vector<MapCharacteristics *> mapsCharacteristics;
	static char **mapNames;

	static void chooseMapState(Graphics *, Purpose);
	/* CHOOSE MAP STATE */

	/* CHOOSE VEHICLE STATE */
	static VehicleCaracteristics *vehicleAttributes;
	static char *vehicleNames[MAX_VEHICLES];

	static void chooseVehicleState(Graphics *, Purpose);
	/* CHOOSE VEHICLE STATE */

	/* CREDITS STATE */
	static char *creditsStrings[TOTAL_CREDITS_ENTRIES];
	static char currentCreditsString[256];
	static unsigned int currentString;
	static unsigned int currentPositionInCurrentString;
	static char replacedChar;
	static Uint32 creditsTimer;
	static int currentState;
	static int curOffX;
	static Uint32 writeSpeed;

	static void initCredits();
	static int drawCredits(Graphics *);
	static void creditsState(Graphics *, Purpose);
	/* CREDITS STATE */
	
	/* INGAME STATES */
	/* ACTUAL GAME STATE */
	static char *mapName;
	static int playerVehicleID;

	static void gameState(Graphics *, Purpose);
	/* ACTUAL GAME STATE */

	/* LOADING/UNLOADING GAME STATE */
	static int loadingStep;
	static char *loadingMessages[TOTAL_LOAD_STEPS];
	static char *unloadingMessages[TOTAL_LOAD_STEPS];
	static bool unloadToMainMenu;

	static void loadGameState(Graphics *, Purpose);
	static void unloadGameState(Graphics *, Purpose);
	/* LOADING/UNLOADING GAME STATE */

	/* PAUSE STATE */
	static char *pauseMenuStrings[TOTAL_PAUSE_MENU_OPTIONS];
	static void pauseState(Graphics *, Purpose);
	/* PAUSE STATE */
	/* INGAME STATES */

/* GAME STATES */
protected:

	/* Overloaded/inherited from CEngine */
	void paint(Graphics *);
	
	void keyPressed(const int&);
	void keyReleased(const int&);
	void mouseMoved(const int&, const int&, const int&, const int&, const int&);
	void mouseButtonUp(const int&, const int&, const int&, const int&, const int&);
	void mouseButtonDown(const int&, const int&, const int&, const int&, const int&);

	void showNotify();
	void hideNotify();
 
	void doAditionalInit();
	void doCleanUp();
};