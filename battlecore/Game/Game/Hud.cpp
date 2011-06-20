#include "Hud.h"


Image *Hud::hudImage = NULL;
Image *Hud::statusImage = NULL;
Image *Hud::hudAnimImages[NB_FRAMES];
Animation *Hud::hudAnimation = NULL;

int Hud::hudMinHeight = 0;

int Hud::loadHud()
{
	hudImage = ImageManager::createImage(HUD_PATH);
	statusImage = ImageManager::createImage(STATUS_PATH);
	if(!hudImage)
		return -1;

	for(int i = 0; i < NB_FRAMES; i++)
	{
		char path[256];
		char id[5];
			
		strcpy_s(path, 256, HUD_ANIMATION_PATH);
		sprintf_s(id, 5, "%d", i);
		strcat_s(path, 256, id);
		strcat_s(path, 256, ".bmp");

		hudAnimImages[i] = ImageManager::createImage(path);

		if(!hudAnimImages[i])
		{
			for(int j = 0; j < i; j++)
			{		
				ImageManager::freeImage(hudAnimImages[j]);
			}

			ImageManager::freeImage(hudImage);

			return -1;
		}
	}

	hudAnimation = AnimationManager::createAnimation(hudAnimImages, NB_FRAMES, FRAME_DELAY);
	hudAnimation->startPlaying();

	hudMinHeight = HUD_HEIGHT;

	return 0;
}
void Hud::unloadHud()
{
	if(hudMinHeight)
	{
		AnimationManager::freeAnimation(hudAnimation);
		for(int i = 0; i < NB_FRAMES; i++)
		{		
			ImageManager::freeImage(hudAnimImages[i]);
		}

		ImageManager::freeImage(hudImage);
		ImageManager::freeImage(statusImage);
	}
}
void Hud::drawHud(Graphics *gr)
{
	if(hudMinHeight)
	{
		hudAnimation->draw(gr, ANIM_X, gr->getHeight() - ANIM_Y, LEFT | DOWN);
		gr->drawImage(hudImage, gr->getWidth() / 2, gr->getHeight(), HCENTER | DOWN);

		if(Player::instanceOfPlayerVehicle)
		{
			float playerMaxHp = (float)(Player::instanceOfPlayerVehicle->maxHitpoints);
			float playerHp = (float)(Player::instanceOfPlayerVehicle->hitpoints);

			int playerMaxArmor = Player::instanceOfPlayerVehicle->maxArmor;
			int playerArmor = Player::instanceOfPlayerVehicle->armor;

			gr->setColor(int(255.0f * ((playerHp > playerMaxHp / 2) ? (2 * (playerMaxHp - playerHp) / playerMaxHp) : 1)), 
						 int(255.0f * ((playerHp > playerMaxHp / 2) ? 1 : (2 * playerHp / playerMaxHp))), 0);
			gr->fillRect(gr->getWidth() / 2 + 50, gr->getHeight() - 86, 170 * playerHp / playerMaxHp, 14);

			gr->setColor(BLUE);
			gr->fillRect(gr->getWidth() / 2  + 50, gr->getHeight() - 37, 150 * playerArmor / playerMaxArmor, 14);
		}

		gr->drawImage(statusImage, gr->getWidth() / 2 + 110 , gr->getHeight() - 10, HCENTER | DOWN);
	}
}