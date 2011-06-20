#include "Animation.h"

vector<Animation *> AnimationManager::allAnimations;

Animation* AnimationManager::createAnimation(Image **pointerToFrameSequence, int numberOfFrames, Uint32 delayBetweenFrames)
{
	if(!pointerToFrameSequence || numberOfFrames < 0 || numberOfFrames >= MAX_NUMBER_OF_FRAMES)
		return NULL;

	Animation *newAnimation = new Animation();

	newAnimation->firstFrame = pointerToFrameSequence;
	newAnimation->numberOfFrames = numberOfFrames;
	newAnimation->delay = delayBetweenFrames;

	allAnimations.push_back(newAnimation);

	return newAnimation;
}

void AnimationManager::freeAnimation(Animation *& animation)
{
	size_t end = allAnimations.size();
	for(size_t i = 0; i < end; i++)
		if(allAnimations[i] == animation)
		{
			vector<Animation *>::iterator itr = allAnimations.begin() + i;
			allAnimations.erase(itr);
			break;
		}

	delete animation;
	animation = NULL;
}

void AnimationManager::updateAllAnimations()
{
	size_t end = allAnimations.size();
	for(size_t i = 0; i < end; i++)
		allAnimations[i]->updateAnimation();
}