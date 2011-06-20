#include "Collider.h"

Uint32 getPixel(SDL_Surface *surface, unsigned x, unsigned y)
{
	  if (SDL_MUSTLOCK(surface))
			if (SDL_LockSurface(surface) < 0) 
				return 0;

	  int bpp = surface->format->BytesPerPixel;
	  /* Here p is the address to the pixel we want to retrieve */
	  Uint8 *p = (Uint8 *)surface->pixels + y * surface->pitch + x * bpp;

	  if (SDL_MUSTLOCK(surface)) 
			SDL_UnlockSurface(surface);

	  switch(bpp) 
	  {
	  case 1:
		  return *p;

	  case 2:
		  return *(Uint16 *)p;

	  case 3:
		  if(SDL_BYTEORDER == SDL_BIG_ENDIAN)
			  return p[0] << 16 | p[1] << 8 | p[2];
		  else
			  return p[0] | p[1] << 8 | p[2] << 16;

	  case 4:
		  return *(Uint32 *)p;

	  default:
		  return 0;       /* shouldn't happen, but avoids warnings */
	  }
}

Collider* getCollider(Image *image)
{
	SDL_Surface *surface = image->imageSurface;

	Collider *newCollider = bitmask_create(surface->w, surface->h);
	//bitmask_clear(newCollider); //it is done in bitmask_create

	for(int i = 0; i < surface->w; i++)
		for(int j = 0; j < surface->h; j++)
			if(getPixel(surface, i, j) != surface->format->colorkey &&
				i > 0 && i < surface->w - 1 &&							//eliminate the contour - looks cooler;))
				getPixel(surface, i - 1, j) != surface->format->colorkey &&
				getPixel(surface, i + 1, j) != surface->format->colorkey &&
				j > 0 && j < surface->h - 1 && 
				getPixel(surface, i, j - 1) != surface->format->colorkey &&
				getPixel(surface, i, j + 1) != surface->format->colorkey)
					bitmask_setbit(newCollider, i, j);


	return newCollider;
}