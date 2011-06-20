#pragma once

#include "Video.h"
#include "Audio.h"
#include "Font.h"

#define FPS_FONT_PATH	"C:\\WINDOWS\\Fonts\\verdana.ttf"
#define FPS_FONT_SIZE	16


class CEngine  
{
private:

	/* Graphics object */
	Graphics *screenGraphics;
	
	/* Game loop related */
	bool isRunning;
	bool m_bMinimized;

	/* Measuring time and frame rate related members */
	Uint32 ticksBeforeRenderingFrame;
	Uint32 totalTiksPerOneFrame;
	Uint32 totalTiksPerTenFrames;
	int framesPerSecond;
	int fpsCounter;

	bool showFps_Tpf;
	char fpsString[25];
	Font *fpsFont;

	int fpsLimit;

	/* Cursor */
	bool m_showCursor;

	//Event handler object
	SDL_Event event;
	
	/* Method that contains the main loop */
	void run();
	/* Method that calls the event handlers */
	void handleEvents();

protected:

	/* The following methods are to be implemented in the derived class */

	/* Paint Method */
	virtual void paint(Graphics *) = 0;	

	/* Event Handlers -- "pseudo-callbacks":)*/
	virtual void keyPressed(const int&) = 0;
	virtual void keyReleased(const int&) = 0;
	virtual void mouseMoved(const int&, const int&, const int&, const int&, const int&) = 0;
	virtual void mouseButtonUp(const int&, const int&, const int&, const int&, const int&) = 0;
	virtual void mouseButtonDown(const int&, const int&, const int&, const int&, const int&) = 0;

	/* This is for lost / regain focus */
	virtual void showNotify() = 0;
	virtual void hideNotify() = 0;

	/* Initialization methond */
	virtual void doAditionalInit() = 0;
	/* Clean up method */
	virtual void doCleanUp() = 0;

public:

	/* Instance */
	static CEngine *gameInstance;

	/* Configurations */
	Configuration *configurations;

	/* Set Screen Resolution */
	void setScreenResolution(const Configuration &);

	/* Get frames per second */
	int getFramesPerSecond();

	/* Get time per frame */
	Uint32 getTimePerFrame();

	/* Show / hide fps/tpf */
	void showFps();
	void hideFps();

	/* Show / hide cursor */
	void showCursor();
	void hideCursor();

	/* Set fps limitation -- 0 - no limitation*/
	void limitFps(int);

	/* Quit the main loop */
	void quit();

	/* Constructor -- initializes the engine*/
	CEngine();

	/* Destructor */
	virtual ~CEngine();

	/* This method starts the angine */
	void Start();

	/* Get current time - made static */
	static Uint32 currentTimeMillis(); //=)) ouuu...this brings back memories
	static void wait(Uint32);
};
 
