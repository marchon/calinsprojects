#include "Animation.h"

Animation::Animation()
{
	firstFrame = NULL;
	numberOfFrames = 0;
	currentFrame = 0;
	playingDir = STOP;
	delay = 0;
	lastFrameChangeTime = 0;
}

Animation::~Animation()
{
}

void Animation::startPlaying()
{
	playingDir = FORWORD;
	lastFrameChangeTime = SDL_GetTicks();
}

void Animation::startReversePlaying()
{
	playingDir = BACKWORD;
	lastFrameChangeTime = SDL_GetTicks();
}

void Animation::stopPlaying()
{
	playingDir = STOP;
}

void Animation::setFrame(int frameNumber)
{
	if(frameNumber < 0 || frameNumber >= numberOfFrames)
		return;

	currentFrame = frameNumber;
}

bool Animation::isAtLastFrame()
{
	return (currentFrame == (numberOfFrames - 1) && (SDL_GetTicks() - lastFrameChangeTime > delay - 15));
}

void Animation::changeDelay(Uint32 newDelay)
{
	delay = newDelay;
}

void Animation::updateAnimation()
{
	if(playingDir)
	{
		if(SDL_GetTicks() - lastFrameChangeTime > delay)
		{
			currentFrame += playingDir;

			if(currentFrame < 0) 
				currentFrame = numberOfFrames - 1;
			else if(currentFrame == numberOfFrames)
				currentFrame = 0;	

			lastFrameChangeTime = SDL_GetTicks();
		}
	}
}

void Animation::nextFrame()
{
	if(!playingDir)
	{
		currentFrame ++;
		if(currentFrame == numberOfFrames)
				currentFrame = 0;
	}
}

void Animation::previousFrame()
{
	if(!playingDir)
	{
		currentFrame --;
		if(currentFrame == 0)
				currentFrame = numberOfFrames - 1;
	}
}

void Animation::draw(Graphics *gr, int x, int y, int align)
{
	gr->drawImage(*(firstFrame + currentFrame), x, y, align);
}