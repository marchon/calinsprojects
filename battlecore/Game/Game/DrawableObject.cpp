#include "Drawableobject.h"

vector<DrawableObject *> DrawableObject::drawableObjects;

DrawableObject::DrawableObject(unsigned int h, float x, float y, float dimx, float dimy) : height(h),
								position(x, y), dimension(dimx, dimy)
{
	size_t size = drawableObjects.size();
	size_t i = 0;
	bool inserted = false;

	while(i < size)
	{
		if(h < drawableObjects[i]->height && (!i || h >= drawableObjects[i-1]->height))
		{
			vector<DrawableObject *>::iterator itr = drawableObjects.begin() + i;
			drawableObjects.insert(itr, this);

			inserted = true;
			break;
		}

		i++;
	}

	if(!inserted) 
		drawableObjects.push_back(this);
}

DrawableObject::~DrawableObject()
{
	size_t size = drawableObjects.size();

	for(size_t i = 0; i < size; i++)
		if(drawableObjects[i] == this)
		{
			vector<DrawableObject *>::iterator itr = drawableObjects.begin() + i;
			drawableObjects.erase(itr);
			break;
		}
}

void DrawableObject::drawAllObjectsInSite(Graphics *gr, const Vector2D &topLeft, const Vector2D &bottomRight)
{
	if(!drawableObjects.empty())
	{
		size_t size = drawableObjects.size();

		for(size_t i = 0; i < size; i++)
		{
			if( drawableObjects[i]->position.x + drawableObjects[i]->dimension.x / 2 >= topLeft.x &&
				drawableObjects[i]->position.y + drawableObjects[i]->dimension.y / 2 >= topLeft.y &&
				drawableObjects[i]->position.x - drawableObjects[i]->dimension.x / 2 < bottomRight.x &&
				drawableObjects[i]->position.y - drawableObjects[i]->dimension.y / 2 < bottomRight.y)
			{
				Vector2D coordinatesOnScreen;
				
				coordinatesOnScreen.x = drawableObjects[i]->position.x - topLeft.x;
				coordinatesOnScreen.y = drawableObjects[i]->position.y - topLeft.y;

				drawableObjects[i]->draw(gr, coordinatesOnScreen);
			}

		}
	}
}

void DrawableObject::destroyAllObjectsFromBase()
{
	while(!drawableObjects.empty())
		delete drawableObjects[0];
}