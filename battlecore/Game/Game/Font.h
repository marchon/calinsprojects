#pragma once

#include "SDL_TTF.h"

typedef TTF_Font Font;

class FontManager
{
private:
protected:
public:
	static Font* createFont(char *, int);
	static void freeFont(Font *&);
};