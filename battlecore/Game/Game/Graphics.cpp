#include "Video.h"

Configuration Graphics::Configurations[VIDEO_CONFIG_MAX] =
{
	{640, 480, 16, false, false},
	{800, 600, 16, false, false},
	{1024, 768, 16, false, false},
	{640, 480, 32, false, false},
	{800, 600, 32, false, false},
	{1024, 768, 32, false, false},
	{640, 480, 16, true, false},
	{800, 600, 16, true, false},
	{1024, 768, 16, true, false},
	{640, 480, 32, true, false},
	{800, 600, 32, true, false},
	{1024, 768, 32, true, false}
};

bool Graphics::configsTested = false;

Graphics *Graphics::currentGraphics = NULL;

Graphics::Graphics()
{
	destinationSurface = NULL;
}

Graphics::~Graphics()
{
}

int Graphics::getWidth()
{
	return destinationSurface->w;
}

int Graphics::getHeight()
{
	return destinationSurface->h;
}

void Graphics::setClip(int x, int y, int width, int height)
{
	SDL_Rect clipRect;
	
	clipRect.x = x;
	clipRect.y = y;
	clipRect.w = width;
	clipRect.h = height;

	SDL_SetClipRect(destinationSurface, &clipRect);
}

void Graphics::clearGraphics()
{
	SDL_FillRect(destinationSurface, NULL, 0);
}

void Graphics::setColor(int r, int g, int b)
{
	currentColor = SDL_MapRGB(destinationSurface->format, r, g, b);
	currentSDLColor.r = r;
	currentSDLColor.g = g;
	currentSDLColor.b = b;
}

void Graphics::drawImage(Image *image, int x, int y, int anchor)
{
	if(!image)
		return;

	SDL_Rect position;

	position.x = x;
	position.y = y;

	if(anchor & HCENTER)  position.x = x - image->getWidth() / 2;
	else if(anchor & RIGHT)	position.x = x - image->getWidth();

	if(anchor & VCENTER)  position.y = y - image->getHeight() / 2;
	else if(anchor & DOWN)	position.y = y - image->getHeight();

	
	SDL_BlitSurface(image->imageSurface, NULL, destinationSurface, &position);
}

void Graphics::drawImage(Image *image, int x, int y, int xx, int yy, int ww, int hh)
{
	if(!image)
		return;

	SDL_Rect position;

	position.x = x;
	position.y = y;

	SDL_Rect clipRect;

	clipRect.x = xx;
	clipRect.y = yy;
	clipRect.w = ww;
	clipRect.h = hh;

	SDL_BlitSurface(image->imageSurface, &clipRect, destinationSurface, &position);
}
int Graphics::drawString(Font *font, const char *string, int x, int y, int anchor)
{
	if(font == NULL)
		return -1;
	
	SDL_Surface *textSurface;

	if(!(textSurface = TTF_RenderText_Blended(font, string, currentSDLColor)))
	{
		return -1;
	}
	else
	{
		int w;
		SDL_Rect position;

		position.x = x;
		position.y = y;

		if(anchor & HCENTER)  position.x = x - textSurface->w / 2;
		else if(anchor & RIGHT)	position.x = x - textSurface->w;

		if(anchor & VCENTER)  position.y = y - textSurface->h / 2;
		else if(anchor & DOWN)	position.y = y - textSurface->h;

		SDL_BlitSurface(textSurface, NULL, destinationSurface, &position);
		
		w = textSurface->w;

		SDL_FreeSurface(textSurface);

		return w;
	}
}

void Graphics::drawRect(int x, int y, int width, int height)
{
	//TODO: implemnent this method if needed
}

void Graphics::fillRect(int x, int y, int width, int height)
{
	SDL_Rect rect;
	
	rect.x = x;
	rect.y = y;
	rect.w = width;
	rect.h = height;

	SDL_FillRect(destinationSurface, &rect, currentColor);
}

void Graphics::drawLine(int x, int y, int width, int height)
{
	//TODO: implemnent this method if needed
}

void Graphics::updateScreen()
{
	SDL_Flip(destinationSurface);
}

Graphics* Graphics::createVideoGraphics(const Configuration &configuration)
{
	if(!configuration.isAcceptedByHardwere)
		return NULL;

	Graphics* newGraphics = new Graphics();

	newGraphics->destinationSurface = SDL_SetVideoMode(configuration.width, 
								configuration.height, 
								configuration.bitsPerPixel,
								SDL_FULLSCREEN | (configuration.useHardwere ? (SDL_HWSURFACE | SDL_DOUBLEBUF | SDL_HWACCEL) : SDL_SWSURFACE));
	
	if(newGraphics->destinationSurface == NULL)
	{
		delete newGraphics;
		return NULL;
	}

	newGraphics->setColor(0, 0, 0);

	if(currentGraphics != NULL) delete currentGraphics;
	currentGraphics = newGraphics;

	return newGraphics;
}

Configuration* Graphics::getSupportedVideoModes()
{
	if(configsTested) 
		return Configurations;
	else
	{
		for(int i = 0; i < VIDEO_CONFIG_MAX; i++)
				if(Configurations[i].bitsPerPixel = SDL_VideoModeOK(
												Configurations[i].width,
												Configurations[i].height,
												Configurations[i].bitsPerPixel,
												SDL_FULLSCREEN | 
												(Configurations[i].useHardwere ? 
												(SDL_HWSURFACE | SDL_DOUBLEBUF | SDL_HWACCEL) : SDL_SWSURFACE)))
													Configurations[i].isAcceptedByHardwere = true;

		configsTested = true;
		return Configurations;
	}
}

void Graphics::doGraphicsInit()
{
	// Initialize SDL's subsystems
	if ( SDL_Init( SDL_INIT_VIDEO ) < 0 ) 
	{
		//handle error
		exit( 1 );
	}

	// Initialize True type font 
	if (TTF_Init() < 0) 
	{
		//handle error
		exit(1);
	}

	/* Assure that sdl subsistems will be shut down when application exits */
	atexit( SDL_Quit );
	atexit( TTF_Quit );
}

void Graphics::doGraphicsShutDown()
{
	if(currentGraphics != NULL) 
		delete currentGraphics;
	// Shut down SDL
	SDL_Quit();
	TTF_Quit();
}