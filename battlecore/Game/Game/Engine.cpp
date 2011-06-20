#include "Engine.h"
#include <windows.h>


CEngine *CEngine::gameInstance = NULL;

CEngine::CEngine()
{
	
	//Cannot instantiate more than one object
	if(gameInstance)
		exit(1); 

	gameInstance = this;

	screenGraphics = NULL;

	isRunning = true;

	m_bMinimized = false;

	totalTiksPerOneFrame = 0;
	totalTiksPerTenFrames = 0;
	framesPerSecond = 0;
	fpsCounter = 0;

	showFps_Tpf = false;

	m_showCursor = true;

	fpsLimit = 0; //no limitation

	fpsFont = NULL;

	Graphics::doGraphicsInit();

	configurations = Graphics::getSupportedVideoModes();

	for(int i = VIDEO_CONFIG_MAX - 1; i >= 0; i --)
	{
		if(configurations[i].isAcceptedByHardwere)
		{
			setScreenResolution(configurations[i]);
			break;
		}

		else
			if(i == 0) exit(1); //cannot set any resolution
	}
}

CEngine::~CEngine()
{
	if(fpsFont)
		FontManager::freeFont(fpsFont);
	Graphics::doGraphicsShutDown();
}
 
void CEngine::Start()
{	
	doAditionalInit();
	run();
	doCleanUp();
}

void CEngine::run()
{
	/* MAIN LOOP */
	while( isRunning )
	{

		/* Get time before start drawing frame */
		ticksBeforeRenderingFrame = SDL_GetTicks();

		/*Ceck events*/
		handleEvents();

		/* If the game is minimized */
		if(m_bMinimized)
		{
			//SDL_WaitEvent(&event);
			continue;
		}

		/* Update and Paint */
		paint(screenGraphics);

		/* Show fps if desired */
		if(showFps_Tpf && fpsFont)
		{				
			screenGraphics->setColor(0, 0, 0);
			screenGraphics->fillRect(0, 0, 110, 36);
			screenGraphics->setColor(255, 255, 255);
			sprintf_s(fpsString, 25, "FPS: %d", framesPerSecond);
			screenGraphics->drawString(fpsFont, fpsString, 0, 0, 0);
			sprintf_s(fpsString, 25,"TPF: %dms", totalTiksPerOneFrame);
			screenGraphics->drawString(fpsFont, fpsString, 0, 18, 0);
		}

		/* Render on actual screen */
		screenGraphics->updateScreen();

		/* Limit fps if desired */
		if(fpsLimit)
		{
			int toSleep =  1000 / fpsLimit - (SDL_GetTicks() - ticksBeforeRenderingFrame);
			if(toSleep > 0) SDL_Delay((Uint32)toSleep);
		}

		/* Calculate the time it took to render last frame */
		totalTiksPerOneFrame = SDL_GetTicks() - ticksBeforeRenderingFrame;

		/* If ten frames haven't been rendered since last update... */
		if(fpsCounter < 10)
		{
			/* Increase FPS counter */
			fpsCounter ++;
			/* Add current frames time */
			totalTiksPerTenFrames += totalTiksPerOneFrame;
		}
		/* Else */
		else
		{
			/* Calculate frame rate in one second */
			if(totalTiksPerTenFrames > 0)
				framesPerSecond = 10000 / totalTiksPerTenFrames;
			/* Reset frame counter and time counter */
			fpsCounter = 0;
			totalTiksPerTenFrames = 0;
		}
	}
}
 
void CEngine::handleEvents()
{
	while ( SDL_PollEvent( &event ) ) 
	{
		switch ( event.type ) 
		{
		case SDL_KEYDOWN:			 
			keyPressed( event.key.keysym.sym );
			break;
 
		case SDL_KEYUP:
			keyReleased( event.key.keysym.sym );
			break;
 
		case SDL_QUIT:
			isRunning = true;
			break;
 
		case SDL_MOUSEMOTION:
			if(m_showCursor)
				mouseMoved(
					event.button.button, 
					event.motion.x, 
					event.motion.y, 
					event.motion.xrel, 
					event.motion.yrel);
			break;
 
		case SDL_MOUSEBUTTONUP:
			if(m_showCursor)
				mouseButtonUp(
					event.button.button, 
					event.motion.x, 
					event.motion.y, 
					event.motion.xrel, 
					event.motion.yrel);
			break;
 
		case SDL_MOUSEBUTTONDOWN:
			if(m_showCursor)
				mouseButtonDown(
					event.button.button, 
					event.motion.x, 
					event.motion.y, 
					event.motion.xrel, 
					event.motion.yrel);
			break;
 
		case SDL_ACTIVEEVENT:
			if ( event.active.state & SDL_APPACTIVE ) 
			{
				if ( event.active.gain ) 
				{
					/* Gain focus */

					m_bMinimized = false;
					showNotify();
				} else 
				{
					/* Lost focus */

					m_bMinimized = true;
					hideNotify();
				}
			}
			
			break;
		} 
	}	
}
 
void CEngine::setScreenResolution(const Configuration &config)
{
	screenGraphics = Graphics::createVideoGraphics(config);
}

int CEngine::getFramesPerSecond()
{
	return framesPerSecond;
}

Uint32 CEngine::getTimePerFrame()
{
	return totalTiksPerOneFrame;
}

void CEngine::showFps()
{
	if(!fpsFont)
		fpsFont = FontManager::createFont(FPS_FONT_PATH, FPS_FONT_SIZE);
	showFps_Tpf = true;
}
void CEngine::hideFps()
{
	showFps_Tpf = false;
}

void CEngine::showCursor()
{
	SDL_ShowCursor( SDL_ENABLE );
	m_showCursor = true;
}

void CEngine::hideCursor()
{
	SDL_ShowCursor( SDL_DISABLE );
	m_showCursor = false;
}

void CEngine::limitFps(int fps)
{
	if(fps < 0) fpsLimit = 0;
	else fpsLimit = fps;
}
void CEngine::quit()
{
	isRunning = false;
}
 

/* These are static */
Uint32 CEngine::currentTimeMillis()
{
	return SDL_GetTicks();
}

void CEngine::wait(Uint32 mlsecs)
{
	SDL_Delay(mlsecs);
}



 