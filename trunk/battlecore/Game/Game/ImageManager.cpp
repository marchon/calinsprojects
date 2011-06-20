#include "Video.h"

#define AUTOMATIC_TRANSPARENCY

int ImageManager::numberOfImages = 0;

Image* ImageManager::createImage(char *pathToBmp)
{
	if(pathToBmp == NULL)
		return NULL;

	Image *newImage = new Image();
	SDL_Surface *tempSurface;
	
	tempSurface = SDL_LoadBMP(pathToBmp);
	if(tempSurface == NULL)
	{
		delete newImage;
		return NULL;
	}

	newImage->imageSurface = SDL_DisplayFormat(tempSurface);
	SDL_FreeSurface(tempSurface);
	if(newImage->imageSurface == NULL)
	{
		delete newImage;
		return NULL;
	}
	
	numberOfImages ++;

#ifdef AUTOMATIC_TRANSPARENCY
	newImage->setTransparency(255, 0, 255);
#endif

	return newImage;
}

void ImageManager::freeImage(Image *&image)
{
	if(image == NULL) 
		return;

	if(image->imageSurface == NULL) 
		return;

	SDL_FreeSurface(image->imageSurface);
	delete image;

	image = NULL;

	numberOfImages--;
}

int ImageManager::getNumberOfImages()
{
	return numberOfImages;
}