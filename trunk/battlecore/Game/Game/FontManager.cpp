#include "Font.h"


Font* FontManager::createFont(char *path, int size)
{
	Font* newFont = TTF_OpenFont(path, size);
	return newFont;
}
void FontManager::freeFont(Font *&font)
{
	if(font)
	{
		TTF_CloseFont(font);
		font = NULL;
	}
}