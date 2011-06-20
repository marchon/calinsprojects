#pragma once

#include "Video.h"
#include <vector>
using namespace std;

#define FORWORD		1
#define STOP		0
#define BACKWORD	-1

class AnimationManager;

class Animation
{
private:
	Image** firstFrame;
	int numberOfFrames;

	int currentFrame;

	int playingDir;

	Uint32 delay;
	Uint32 lastFrameChangeTime;

	Animation();
	~Animation();

protected:
public:

	void startPlaying();
	void startReversePlaying();
	void stopPlaying();

	void setFrame(int);
	bool isAtLastFrame();

	void changeDelay(Uint32);

	void updateAnimation();
	
	void nextFrame();
	void previousFrame();

	void draw(Graphics*, int, int, int);

	friend class AnimationManager;
};

#define MAX_NUMBER_OF_FRAMES	50

class AnimationManager
{
private:
	static vector<Animation *> allAnimations;
protected:
public:
	static Animation* createAnimation(Image **, int, Uint32);
	static void freeAnimation(Animation *&);

	static void updateAllAnimations();
};