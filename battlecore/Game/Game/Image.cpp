#include "Video.h"

Image::Image()
{
	imageSurface = NULL;
}

Image::~Image()
{
}

int Image::getWidth()
{
	return imageSurface->w;
}

int Image::getHeight()
{
	return imageSurface->h;
}

void Image::setTransparency(Uint8 r, Uint8 g, Uint8 b)
{
	SDL_SetColorKey(imageSurface, SDL_RLEACCEL | SDL_SRCCOLORKEY, SDL_MapRGB(imageSurface->format, r, g, b));
}