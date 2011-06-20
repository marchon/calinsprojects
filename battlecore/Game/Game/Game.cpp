#include "Game.h"


bool Game::keyBuffer[TOTAL_KEYS];

/* MENU RELATED */

/* FONT */
Font *Game::menuFont = NULL;
/* FONT */
/* IMAGE */
Image *Game::logoImage = NULL;
/* IMAGE */

/* MENU TITLE */
char *Game::currentMenuTitle = NULL;
unsigned int Game::titleLen = 0;
int Game::cursorPosition = 0;
Uint32 Game::time = 0;
bool Game::cursorBlink = false;

void Game::drawMenuTitle(Graphics* gr)
{
	gr->setColor(ORANGE);

	gr->fillRect(0, gr->getHeight() / 5 - 30, gr->getWidth(), 2);

	char str[128];
	strcpy_s(str, 128, currentMenuTitle);
	str[cursorPosition] = '\0';

	gr->drawString(menuFont, str, gr->getWidth() - 100, gr->getHeight() / 5 - 52, RIGHT);

	if(!cursorBlink || Game::currentTimeMillis() % (2 * BLINK_INTERVAL) >= BLINK_INTERVAL)
		gr->fillRect(gr->getWidth() - 98, gr->getHeight() / 5 - 54, 10, 10);

	if(cursorBlink)
	{
		if(Game::currentTimeMillis() - time >= BLINKING_TIME)
		{
			cursorBlink = false;
			cursorPosition = 0;
			time = Game::currentTimeMillis();
		}
	}
	else if(Game::currentTimeMillis() - time >= INTERVAL_BETWEEN_TYPEING)
	{
		cursorPosition++;
		if(cursorPosition == titleLen)
			cursorBlink = true;
		time = Game::currentTimeMillis();
	}
}

void Game::setMenuTitle(char *str)
{
	currentMenuTitle = str;
	titleLen = (unsigned int)strlen(str);
	cursorPosition = 0;
	time = Game::currentTimeMillis();
	cursorBlink = false;
}
/* MENU TITLE */

/* MENU OPTIONS */
char **Game::currentMenuEntries = NULL;
unsigned int Game::currentMenuOptionsNumber = 0;
int Game::currentMenuCurrentOption = 0;

int Game::menuTranzitState = NO_TRANZIT;
bool Game::showCursor = false;
Uint32 Game::lastTimeDelayChanged = 0;
Uint32 Game::lastTimeCursorStateChanged = 0;
Uint32 Game::currentBlinkInterval = 0;

int Game::handleMenuInputAndDrawMenuOptions(Graphics *gr)
{
	/* This is for deleteing selected option */
	static char *selectedOptionPtr; 

	int returnValue = NO_OPTION;

	/* Handle input */

	if(menuTranzitState == NO_TRANZIT)
	{
	
		if(IS_KEY_PRESSED(KEY_ESCAPE))
		{
				returnValue = BACK;
				FLUSH_KEYS();
		}

		else if(IS_KEY_PRESSED(KEY_UP))
		{
			currentMenuCurrentOption --;
			if(currentMenuCurrentOption < 0) 
				currentMenuCurrentOption = currentMenuOptionsNumber - 1;
			FLUSH_KEYS();
		}

		else if(IS_KEY_PRESSED(KEY_DOWN))
		{
			currentMenuCurrentOption ++;
			if(currentMenuCurrentOption >= currentMenuOptionsNumber) 
				currentMenuCurrentOption = 0;
			FLUSH_KEYS();
		}

		else if(IS_KEY_PRESSED(KEY_ENTER))
		{
			FLUSH_KEYS();

			menuTranzitState = TRANZIT_MAX_MIN;
			showCursor = true;
			lastTimeDelayChanged = Game::currentTimeMillis();
			lastTimeCursorStateChanged = Game::currentTimeMillis();
			currentBlinkInterval = MAX_DELAY;

			selectedOptionPtr = currentMenuEntries[currentMenuCurrentOption];
		}
	}

	/*Draw Menu*/

	gr->setColor(ORANGE);
	for(unsigned int i = 0; i < currentMenuOptionsNumber; i++)
	{
		if(menuTranzitState == NO_TRANZIT || i != currentMenuCurrentOption)
			gr->drawString(menuFont, currentMenuEntries[i], gr->getWidth() / 2 - 200, 
					gr->getHeight() / 5 + i * 18, VCENTER);
		else
		{
			gr->drawString(menuFont, selectedOptionPtr, gr->getWidth() / 2 - 200, 
					gr->getHeight() / 5 + i * 18, VCENTER);
		}
	}

	if(menuTranzitState == NO_TRANZIT)
	{
		gr->setColor(WHITE);
		gr->fillRect(gr->getWidth() / 2 - 215, gr->getHeight() / 5 + currentMenuCurrentOption * 18 - 7,
					10, 10);
	}
	else 
	{
		
			if(Game::currentTimeMillis() - lastTimeDelayChanged >= TIME_BETWEEN_DELAY_CHANGING)
			{
				if(menuTranzitState == TRANZIT_MIN_MAX)
				{
					currentBlinkInterval += AMOUNT_OF_TIME_ADDED;
					if(currentBlinkInterval >= MAX_DELAY + AMOUNT_OF_TIME_ADDED)
						menuTranzitState = NO_TRANZIT;
				}
				else
				{				
					currentBlinkInterval -= AMOUNT_OF_TIME_ADDED;
					if(currentBlinkInterval <= MIN_DELAY - AMOUNT_OF_TIME_ADDED)
					{
						menuTranzitState = NO_TRANZIT;
						returnValue = currentMenuCurrentOption;
					}
				}
				lastTimeDelayChanged = Game::currentTimeMillis();
			}
		


		if(Game::currentTimeMillis() - lastTimeCursorStateChanged > currentBlinkInterval)
		{
			if(menuTranzitState == TRANZIT_MAX_MIN)
			{
				if(*selectedOptionPtr != '\0') 
					selectedOptionPtr ++;
			}

			showCursor = !showCursor;
			lastTimeCursorStateChanged = Game::currentTimeMillis();
		}

		if(showCursor)
			gr->fillRect(gr->getWidth() / 2 - 215, gr->getHeight() / 5 + currentMenuCurrentOption * 18 - 7,
					10, 10);
	}

	gr->drawImage(logoImage, gr->getWidth() / 2, gr->getHeight() - LOGO_IMAGE_OFF_Y, HCENTER | DOWN);
	

	return returnValue;
}
void Game::setMenuEntries(char **entries, unsigned int nbEntries)
{
	currentMenuEntries = entries;
	currentMenuOptionsNumber = nbEntries;
	currentMenuCurrentOption = 0;

#ifdef MENU_ENTER_CURSOR_BLINK
	menuTranzitState = TRANZIT_MIN_MAX;
	showCursor = true;
	lastTimeDelayChanged = Game::currentTimeMillis();
	lastTimeCursorStateChanged = Game::currentTimeMillis();
	currentBlinkInterval = MIN_DELAY;
#endif
}
/* MENU OPTIONS */

/* MENU RELATED */



/* GAME STATES */

/* INIT STATE */
void Game::initState(Graphics *gr, Purpose purpose)
{
	switch(purpose)
	{
	case INIT_PURPOSE:
		break;
	case FRAME_PURPOSE:
		//Do init stuff if needed

		menuFont = FontManager::createFont(MENU_FONT_PATH, MENU_FONT_SIZE);
		logoImage = ImageManager::createImage(LOGO_IMAGE_PATH);
			
		StateManager::popAllStatesAndPushState(introState);
		break;
	case STOP_PURPOSE:
		break;
	default:
		break;
	}
}
/* INIT STATE */

/* INTRO STATE */
Image *Game::splashScreen = NULL;
Image *Game::pressSpace = NULL;

void Game::introState(Graphics *gr, Purpose purpose)
{
	switch(purpose)
	{
	case INIT_PURPOSE:
		splashScreen = ImageManager::createImage(SPLASH_IMAGE_PATH);
		pressSpace = ImageManager::createImage(PRESS_SPACE_IMAGE_PATH);
		break;
	case FRAME_PURPOSE:
		
		if(IS_KEY_PRESSED(KEY_SPACE))
			StateManager::popAllStatesAndPushState(mainMenuState);

		gr->clearGraphics();
		gr->drawImage(splashScreen, gr->getWidth() / 2 + SPLASH_X_OFF, gr->getHeight() / 2 + SPLASH_Y_OFF, HCENTER | VCENTER);
		if(Game::currentTimeMillis() % (PRESS_SPACE_DELAY * 2) > PRESS_SPACE_DELAY)
			gr->drawImage(pressSpace, gr->getWidth() + PRESS_SPACE_X_OFF, gr->getHeight() + PRESS_SPACE_Y_OFF, HCENTER | VCENTER);

		break;
	case STOP_PURPOSE:
		ImageManager::freeImage(splashScreen);
		ImageManager::freeImage(pressSpace);
		break;
	default:
		break;
	}
}
/* INTRO STATE */

/* MAIN MENU STATE */
char *Game::mainMenuStrings[TOTAL_MAIN_MENU_OPTIONS] = MAIN_MENU_OPTIONS;

void Game::mainMenuState(Graphics *gr, Purpose purpose)
{
	switch(purpose)
	{
	case INIT_PURPOSE:
		
		setMenuTitle(MAIN_MENU);
		setMenuEntries(mainMenuStrings, TOTAL_MAIN_MENU_OPTIONS);

		break;
	case FRAME_PURPOSE:
	
		gr->clearGraphics();

		drawMenuTitle(gr);

		switch(handleMenuInputAndDrawMenuOptions(gr))
		{
		case NEW_GAME:
			StateManager::pushState(chooseMapState);
			break;
		case OPTIONS:
			break;
		case CREDITS:
			StateManager::pushState(creditsState);
			break;
		case QUIT:
			gameInstance->quit();
			break;
		}

		break;
	case STOP_PURPOSE:
		break;
	default:
		break;
	}
}
/* MAIN MENU STATE */

/* CREDITS STATE */
char *Game::creditsStrings[TOTAL_CREDITS_ENTRIES] = CREDITS_ENTRIES;
char Game::currentCreditsString[256];
unsigned int Game::currentString = 0;
unsigned int Game::currentPositionInCurrentString = 0;
char Game::replacedChar = 0;
Uint32 Game::creditsTimer = 0;
int Game::currentState = STATE_BLINK;
int Game::curOffX = 0;
Uint32 Game::writeSpeed = CREDITS_DELAY_WRITE1;

void Game::initCredits()
{
	currentString = 0;
	currentPositionInCurrentString = 0;

	strcpy_s(currentCreditsString, 256, creditsStrings[currentString]);

	replacedChar = currentCreditsString[currentPositionInCurrentString];
	currentCreditsString[currentPositionInCurrentString] = '\0';

	creditsTimer = Game::currentTimeMillis();

	currentState = STATE_BLINK;

	curOffX = 0;

	writeSpeed = CREDITS_DELAY_WRITE1;
}

int Game::drawCredits(Graphics *gr)
{

	curOffX = gr->drawString(menuFont, currentCreditsString, 
					gr->getWidth() / 2 - 400, gr->getHeight() / 2 - 150, LEFT) + 3;

	switch(currentState)
	{
	case STATE_BLINK:
		if(Game::currentTimeMillis() % (2 * BLINK_INTERVAL) >= BLINK_INTERVAL)
			gr->fillRect(gr->getWidth() / 2 - 400 + curOffX, 
						gr->getHeight() / 2 - 150, 10, 10);

		if(Game::currentTimeMillis() - creditsTimer >= CREDITS_BLINK_TIME)
		{
			if(currentPositionInCurrentString == strlen(creditsStrings[currentString]))
				currentState = STATE_DELETE;
			else
				currentState = STATE_WRITE;
			creditsTimer = Game::currentTimeMillis();
		}
		break;
	case STATE_WRITE:

		if(Game::currentTimeMillis() - creditsTimer >= writeSpeed)
		{
			currentCreditsString[currentPositionInCurrentString] = replacedChar;

			if(currentCreditsString[currentPositionInCurrentString] == '\0')
			{
				currentState = STATE_BLINK;
				break;
			}

			currentPositionInCurrentString ++;

			replacedChar = currentCreditsString[currentPositionInCurrentString];
			currentCreditsString[currentPositionInCurrentString] = '\0';

			if(currentCreditsString[currentPositionInCurrentString - 2] == ' ' &&
				currentCreditsString[currentPositionInCurrentString - 1] == ' ')
			{
					currentState = STATE_BLINK;
					writeSpeed = CREDITS_DELAY_WRITE2;
			}


			creditsTimer = Game::currentTimeMillis();
		}

		gr->fillRect(gr->getWidth() / 2 - 400 + curOffX, 
						gr->getHeight() / 2 - 150, 10, 10);

		break;

		case STATE_DELETE:

			if(Game::currentTimeMillis() - creditsTimer >= CREDITS_DELAY_DELETE)
			{
				currentPositionInCurrentString --;
				currentCreditsString[currentPositionInCurrentString] = '\0';
				creditsTimer = Game::currentTimeMillis();
			}

			if(!currentPositionInCurrentString)
			{
				currentString ++;
				currentPositionInCurrentString = 0;
				if(currentString == TOTAL_CREDITS_ENTRIES)
					return -1;

				strcpy_s(currentCreditsString, 256, creditsStrings[currentString]);

				replacedChar = currentCreditsString[currentPositionInCurrentString];
				currentCreditsString[currentPositionInCurrentString] = '\0';

				creditsTimer = Game::currentTimeMillis();
				currentState = STATE_BLINK;

				writeSpeed = CREDITS_DELAY_WRITE1;
			}

			gr->fillRect(gr->getWidth() / 2 - 400 + curOffX, 
						gr->getHeight() / 2 - 150, 10, 10);
			break;
	}

	return 0;
}

void Game::creditsState(Graphics *gr, Purpose purpose)
{
	switch(purpose)
	{
	case INIT_PURPOSE:		
		setMenuTitle(CREDITS_MENU);
		initCredits();

		break;
	case FRAME_PURPOSE:
	
		gr->clearGraphics();

		drawMenuTitle(gr);

		if(IS_KEY_PRESSED(KEY_ESCAPE) || drawCredits(gr))
			StateManager::popState();

		break;
	case STOP_PURPOSE:
		break;
	default:
		break;
	}
}
/* CREDITS STATE */

/* CHOOSE MAP STATE */
vector<MapCharacteristics *> Game::mapsCharacteristics;
char **Game::mapNames = NULL;

void Game::chooseMapState(Graphics *gr, Purpose purpose)
{
	switch(purpose)
	{
	case INIT_PURPOSE:
		FLUSH_KEYS();

		mapsCharacteristics = Map::getMapListAndInfo();

		mapNames = new char*[mapsCharacteristics.size()];

		for(int i = 0; i < mapsCharacteristics.size(); i++)
			mapNames[i] = mapsCharacteristics[i]->name;


		setMenuTitle(MAP_MENU);
		setMenuEntries(mapNames, mapsCharacteristics.size());

		break;
	case FRAME_PURPOSE:

		gr->clearGraphics();

		drawMenuTitle(gr);

		int option; 
		if((option = handleMenuInputAndDrawMenuOptions(gr)) != NO_OPTION)
		{
			if(option == BACK)	
				StateManager::popState();
			else
			{
				mapName = mapNames[option];
				StateManager::pushState(chooseVehicleState);
			}
		}
		
		break;
	case STOP_PURPOSE:
		delete[] mapNames;
		break;
	default:
		break;
	}
}
/* CHOOSE MAP STATE */

/* CHOOSE VEHICLE STATE */
VehicleCaracteristics *Game::vehicleAttributes = NULL;
char *Game::vehicleNames[MAX_VEHICLES];

void Game::chooseVehicleState(Graphics *gr, Purpose purpose)
{
	switch(purpose)
	{
	case INIT_PURPOSE:
		FLUSH_KEYS();

		vehicleAttributes = Vehicle::loadAllVehiclesInfo();

		setMenuTitle(VEHICLE_MENU);

		for(int i = 0; i < MAX_VEHICLES; i++)
			vehicleNames[i] = vehicleAttributes[i].name;

		setMenuEntries(vehicleNames, MAX_VEHICLES);
			
		break;
	case FRAME_PURPOSE:

		gr->clearGraphics();

		drawMenuTitle(gr);

		int option; 
		if((option = handleMenuInputAndDrawMenuOptions(gr)) != NO_OPTION)
		{
			if(option == BACK)
				StateManager::popState();
			else
			{
				playerVehicleID = option;
				StateManager::popAllStatesAndPushState(loadGameState);
			}
		}			

		/* Draw Preview and Attributes */
		gr->drawImage(vehicleAttributes[currentMenuCurrentOption].preview, gr->getWidth() - 30, gr->getHeight() / 5 - 10, RIGHT);

		char attribute[128];

		gr->setColor(ORANGE);

		sprintf_s(attribute, 128, "HP: %d", vehicleAttributes[currentMenuCurrentOption].hitpoints);
		gr->drawString(menuFont, attribute, gr->getWidth() - vehicleAttributes[currentMenuCurrentOption].preview->getWidth() - 55, 
			gr->getHeight() / 5 - 10, RIGHT);

		sprintf_s(attribute, 128, "Armor: %d", vehicleAttributes[currentMenuCurrentOption].armor);
		gr->drawString(menuFont, attribute, gr->getWidth() - vehicleAttributes[currentMenuCurrentOption].preview->getWidth() - 55, 
			gr->getHeight() / 5 - 10 + 18, RIGHT);
				
		sprintf_s(attribute, 128, "Acceleration: %d", vehicleAttributes[currentMenuCurrentOption].accel);
		gr->drawString(menuFont, attribute, gr->getWidth() - vehicleAttributes[currentMenuCurrentOption].preview->getWidth() - 55, 
			gr->getHeight() / 5 - 10 + 36, RIGHT);

		sprintf_s(attribute, 128, "Top Speed: %d", vehicleAttributes[currentMenuCurrentOption].maxSpeed);
		gr->drawString(menuFont, attribute, gr->getWidth() - vehicleAttributes[currentMenuCurrentOption].preview->getWidth() - 55, 
			gr->getHeight() / 5 - 10 + 54, RIGHT);

		sprintf_s(attribute, 128, "Manevrability: %d", vehicleAttributes[currentMenuCurrentOption].manevrability);
		gr->drawString(menuFont, attribute, gr->getWidth() - vehicleAttributes[currentMenuCurrentOption].preview->getWidth() - 55, 
			gr->getHeight() / 5 - 10 + 72, RIGHT);

		break;
	case STOP_PURPOSE:
		break;
	default:
		break;
	}
}
/* CHOOSE VEHICLE STATE */


/* INGAME STATES */

/* LOADING/UNLOADING GAME STATE */
int Game::loadingStep = 0;
char *Game::loadingMessages[TOTAL_LOAD_STEPS] = LOADING_MESSAGES;
char *Game::unloadingMessages[TOTAL_LOAD_STEPS] = UNLOADING_MESSAGES;
bool Game::unloadToMainMenu = true;


void Game::loadGameState(Graphics *gr, Purpose purpose)
{
	switch(purpose)
	{
	case INIT_PURPOSE:
		loadingStep = 0;	
		break;
	case FRAME_PURPOSE:
		gr->clearGraphics();

		gr->setColor(ORANGE);
		gr->drawString(menuFont, loadingMessages[loadingStep],
			gr->getWidth() / 2, gr->getHeight() - LOAD_MESSAGE_OFF_Y, HCENTER | VCENTER);
				
		switch(loadingStep)
		{
		case 0:
			FLUSH_KEYS();
			break;
		case 1:
			Map::loadMap(mapName);
			break;
		case 2:
			Hud::loadHud();
			break;
		case 3:
			Camera::turnOn(gr);
			break;
		case 4:
			Vehicle::loadVehicle(playerVehicleID);
			break;
		case 5:
			Explosion::loadExplosions();			
			break;
		case 6:
			Projectile::loadProjectile();			
			break;
		case 7:
			Player::CreatePlayer(400, 400, playerVehicleID, 0);
			IntelligentVehicle::createIntelligentVehicle(1000, 400, playerVehicleID, 8);
			IntelligentVehicle::createIntelligentVehicle(1200, 400, playerVehicleID, 8);
			IntelligentVehicle::createIntelligentVehicle(300, 1000, playerVehicleID, 8);
			IntelligentVehicle::createIntelligentVehicle(500, 3000, playerVehicleID, 8);
			break;
		case 8:
			break;
		default:
			StateManager::changeTopState(gameState);
			break;
		}
	
		loadingStep ++;

		Game::wait(WAIT_AMOUNT_LOAD);

		break;
	case STOP_PURPOSE:
		break;
	default:
		break;
	}
}

void Game::unloadGameState(Graphics *gr, Purpose purpose)
{
	switch(purpose)
	{
	case INIT_PURPOSE:
		loadingStep = 0;
		break;
	case FRAME_PURPOSE:
		
		gr->clearGraphics();

		gr->setColor(ORANGE);
		gr->drawString(menuFont, unloadingMessages[loadingStep],
			gr->getWidth() / 2, gr->getHeight() - LOAD_MESSAGE_OFF_Y, HCENTER | VCENTER);

		switch(loadingStep)
		{
		case 0:
			DrawableObject::destroyAllObjectsFromBase();
			break;
		case 1:
			Projectile::unloadProjectile();
			break;
		case 2:
			Explosion::unloadExplosions();
			break;
		case 3:
			Vehicle::unloadAllVehiclesInfo();
			break;
		case 4:
			Vehicle::unloadVehicle(playerVehicleID);
			break;
		case 5:
			Camera::shutDown();	
			break;
		case 6:
			Hud::unloadHud();	
			break;
		case 7:
			Map::unloadMap();
			break;
		case 8:
			break;
		default:
			if(unloadToMainMenu)
				StateManager::changeTopState(mainMenuState);
			else 
				gameInstance->quit();
			break;
		}

		Game::wait(WAIT_AMOUNT_UNLOAD);

		loadingStep ++;
		break;
	case STOP_PURPOSE:
		break;
	default:
		break;
	}
}
/* LOADING/UNLOADING GAME STATE */

/* ACTUAL GAME STATE */
char *Game::mapName = NULL;
int Game::playerVehicleID = 0;

void Game::gameState(Graphics *gr, Purpose purpose)
{

	switch(purpose)
	{
	case INIT_PURPOSE:	
		break;
	case FRAME_PURPOSE:

		/* Handle input */
		if(IS_KEY_PRESSED(KEY_ESCAPE))
		{
			StateManager::pushState(pauseState);
		}
		
		if(IS_KEY_PRESSED(KEY_LEFT))
			Player::TurnLeft();
		if(IS_KEY_PRESSED(KEY_RIGHT))
			Player::TurnRight();
		if(IS_KEY_PRESSED(KEY_UP))
			Player::Accelerate();
		if(IS_KEY_PRESSED(KEY_DOWN))
			Player::Decelerate();
		if(IS_KEY_PRESSED(KEY_SPACE))
			Player::Shoot();
		/* Handle input */

	/* Update view */

		/* Ceck explosions */
		Explosion::explosionsCeckUp();
		/* Ceck explosions */
		
		/* Distroy objects that are queued for distuction */
		DistroyableObject::distroyQueuedObjects();
		/* Distroy objects that are queued for distuction */

		/* Change animations current frame if needed */
		AnimationManager::updateAllAnimations();
		/* Change animations current frame if needed */

		/* Update AI */
		IntelligentVehicle::updateAllIntelligentVehicles();
		/* Update AI */

		/* Move dinamic objects if needed */
		DinamicObject::updateAllDinamicObjects(gameInstance->getTimePerFrame());
		/* Move dinamic objects if needed */

		/* Do collision ceking and treat any collision that is found */
		CollidableObject::doCollisionCeckingAndResponse();
		/* Do collision ceking and treat any collision that is found */

		/* Update camera position and do camera stuff */
		Camera::update();
		/* Update camera position and do camera stuff */

		/* Draw view */
		Camera::renderView(gr);
		/* Draw view */

		/* Draw HUD */
		Hud::drawHud(gr);
		/* Draw HUD */

	/* Update view */

	/* Ceck if player died */
		if(Player::IsPlayerDead())
		{
			unloadToMainMenu = true;
			StateManager::popAllStatesAndPushState(unloadGameState);
		}
	/* Ceck if player died */

		break;
	case STOP_PURPOSE:	
		break;
	default:
		break;
	}
}
/* ACTUAL GAME STATE */

/* PAUSE MENU STATE */
char *Game::pauseMenuStrings[TOTAL_PAUSE_MENU_OPTIONS] = PAUSE_MENU_OPTIONS;

void Game::pauseState(Graphics *gr, Purpose purpose)
{
	switch(purpose)
	{
	case INIT_PURPOSE:
		
		setMenuTitle(PAUSE_MENU);
		setMenuEntries(pauseMenuStrings, TOTAL_PAUSE_MENU_OPTIONS);
		FLUSH_KEYS();

		break;
	case FRAME_PURPOSE:
	
		gr->clearGraphics();

		drawMenuTitle(gr);

		switch(handleMenuInputAndDrawMenuOptions(gr))
		{
		case RESUME_GAME:
			StateManager::popState();
			break;
		case RESTART_GAME:
			//StateManager::popAllStatesAndPushState(gameState);
			break;
		case BACK_TO_MAIN_MENU:
			unloadToMainMenu = true;
			StateManager::popAllStatesAndPushState(unloadGameState);
			break;
		case QUIT_GAME:
			unloadToMainMenu = false;
			StateManager::popAllStatesAndPushState(unloadGameState);
			break;
		case BACK:
			StateManager::popState();
			break;
		}

		break;
	case STOP_PURPOSE:
		break;
	default:
		break;
	}
}
/* PAUSE MENU STATE */

/* INGAME STATES */

/* GAME STATES */


/* METHODS FROM CENGINE */
void Game::doAditionalInit()
{

#ifdef _DEBUG
	Dbg::initDbg();
#endif
	showFps();
	//setScreenResolution(configurations[11]);

	FLUSH_KEYS();
	hideCursor();
	StateManager::pushState(initState);
}
 
void Game::paint(Graphics* gr)
{
	StateManager::updateCurrentState(gr);
}
 
void Game::keyPressed(const int& iKeyEnum)
{        
    switch (iKeyEnum)
    {
    case SDLK_LEFT:
		PRESS_KEY(KEY_LEFT);
      break;
    case SDLK_RIGHT:
		PRESS_KEY(KEY_RIGHT);
      break;
    case SDLK_UP:
		PRESS_KEY(KEY_UP);
      break;
    case SDLK_DOWN:
		PRESS_KEY(KEY_DOWN);
      break;
	case SDLK_RETURN:
		PRESS_KEY(KEY_ENTER);
      break;
	case SDLK_ESCAPE:
		PRESS_KEY(KEY_ESCAPE);
		break;
	case SDLK_SPACE:
		PRESS_KEY(KEY_SPACE);
		break;
    }
}
 
 
void Game::keyReleased(const int& iKeyEnum)
{
	switch (iKeyEnum)
	{
	case SDLK_LEFT:
		RELEASE_KEY(KEY_LEFT);
	  break;
	case SDLK_RIGHT:
		RELEASE_KEY(KEY_RIGHT);
	  break;
	case SDLK_UP:
		RELEASE_KEY(KEY_UP);
	  break;
	case SDLK_DOWN:
		RELEASE_KEY(KEY_DOWN);
	  break;
	case SDLK_RETURN:
		RELEASE_KEY(KEY_ENTER);
      break;
	case SDLK_ESCAPE:
		RELEASE_KEY(KEY_ESCAPE);
		break;
	case SDLK_SPACE:
		RELEASE_KEY(KEY_SPACE);
		break;
	}
}
 
void Game::mouseMoved(const int& iButton, 
			   const int& iX, 
			   const int& iY, 
			   const int& iRelX, 
			   const int& iRelY)
{
}
 
void Game::mouseButtonUp(const int& iButton, 
			      const int& iX, 
			      const int& iY, 
			      const int& iRelX, 
			      const int& iRelY)
{
}
 
void Game::mouseButtonDown(const int& iButton, 
				const int& iX, 
				const int& iY, 
				const int& iRelX, 
				const int& iRelY)
{
}
  
void Game::doCleanUp()
{
#ifdef _DEBUG
	Dbg::shutDownDbg();
#endif

	FontManager::freeFont(menuFont);
	ImageManager::freeImage(logoImage);
}

  
void Game::hideNotify()
{
}

void Game::showNotify()
{
}

/* METHODS FROM CENGINE */