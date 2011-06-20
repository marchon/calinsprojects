#pragma once

#include "Video.h"

#define ACTION_NONE				0
#define ACTION_PUSH				1
#define ACTION_CHANGE_TOP		2
#define ACTION_POP				3
#define ACTION_POP_ALL_AND_PUSH	4

enum Purpose
{
    STOP_PURPOSE = 0,
    INIT_PURPOSE,
    FRAME_PURPOSE,
    NO_PURPOSE
};

class State
{
private:
	/* Pointer to state handler */
	void (*stateHandler)(Graphics *, Purpose);
	/* Pointer to previous state */
	State *previousState;

	State();
	~State();
protected:
public:

	friend class StateManager;
};

class StateManager
{
private:
	/* Current state */
	static State *currentState;

	static char doAction;
	static State *newState;

protected:
public:
	static void pushState(void (*)(Graphics *, Purpose));
	static void changeTopState(void (*)(Graphics *, Purpose));
	static void popState();
	static void popAllStatesAndPushState(void (*)(Graphics *, Purpose));

	static void updateCurrentState(Graphics *);
};