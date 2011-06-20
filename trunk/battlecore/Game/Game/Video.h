#pragma once

#include "SDL.h"
#include "Font.h"

#include "bitmask.h"

#define VIDEO_CONFIG_MAX	12


#define	LEFT		1
#define	RIGHT		1<<1
#define	HCENTER		1<<2
#define	UP			1<<3
#define	DOWN		1<<4
#define	VCENTER		1<<5



struct Configuration
{
	int width;
	int height;
	int bitsPerPixel;
	bool useHardwere;

	bool isAcceptedByHardwere;
};

class Image;

class Graphics
{
private:
	SDL_Surface *destinationSurface;
	Uint32 currentColor;
	SDL_Color currentSDLColor;
	
	static Configuration Configurations[VIDEO_CONFIG_MAX];
	static bool configsTested;
	static Graphics *currentGraphics;

protected:
public:
	Graphics();
	~Graphics();

	/* Get the screen width and height */
	int getWidth();
	int getHeight();

	/* Set the current clip of the screen, that is, the drawble area */
	void setClip(int, int, int, int);
	/* Clear the video surface - basicly fills a rectangle with black */
	void clearGraphics();
	/* Sets the current color R G B */
	void setColor(int, int, int);
	
	/* Draws an image at the specified coordinates with the specified alignment */
	void drawImage(Image *, int, int, int);
	/* Draws a part of an image at the specified coordinates with the TOP|LEFT alignment */
	void drawImage(Image *, int, int, int, int, int, int);
	/* Draws a string at the specified ccoorcinates with the specified alignment using the current font 
	   Returns width of printed string.*/
	int drawString(Font *, const char *, int, int, int);
	
	/* Primitives */
	void drawRect(int, int, int, int); //not implemented
	void fillRect(int, int, int, int);
	void drawLine(int, int, int, int); //not implemented

	/* Flips the buffer in the video memory */
	void updateScreen(); //TODO: try other ways

	/* Returns a new Graphic object. Allways use this procedure for creating a graphics object.
	It will automaticly replace the current graphics object */
	static Graphics* createVideoGraphics(const Configuration &);
	/* Returns the configuration list with the video modes tested */
	static Configuration* getSupportedVideoModes();

	/* Initializes SDL video subsistems */
	static void doGraphicsInit();
	/* Shuts down  SDL video subsistems */
	static void doGraphicsShutDown();
};

/** 
Class Image
	- supports bmp loading
	- supports creating empty image
	- supports getting graphics context for rendering purposes
**/

class Image
{
private:

	SDL_Surface *imageSurface;

	Image();
	~Image();

protected:	
public:

	int getHeight();
	int getWidth();

	void setTransparency(Uint8, Uint8, Uint8);

	friend class Graphics;
	friend class ImageManager;

	/* THIS IS UGLY! BUT WHAT WERE I TO DO?? :-??*/
	friend bitmask* getCollider(Image *);
};


/**
Class ImageManager
	- provides support for creating, destroying and keeping track of created images
	- DO NOT INTSTANTIATE IMAGE OBJECTS IN ANY OTHER WAY( even if u can't because the constructor is private:)) )
**/

class ImageManager
{
private:
	static int numberOfImages;

protected:
public:
	static Image* createImage(char *);
	static void freeImage(Image *&);

	static int getNumberOfImages();
};