#include "State.h"

State *StateManager::currentState = NULL;

char StateManager::doAction = 0;
State *StateManager::newState = NULL;


void StateManager::pushState(void (*stateHandler)(Graphics *, Purpose))
{
	if(!stateHandler || doAction != ACTION_NONE) 
		return;

	newState = new State();

	newState->stateHandler = stateHandler;
	newState->previousState = currentState;

	doAction = ACTION_PUSH;
}

void StateManager::changeTopState(void (*stateHandler)(Graphics *, Purpose))
{
	if(!currentState || !stateHandler || doAction != ACTION_NONE) 
		return;

	newState = new State();

	newState->stateHandler = stateHandler;
	newState->previousState = currentState->previousState;

	doAction = ACTION_CHANGE_TOP;
}

void StateManager::popState()
{
	if(!currentState || doAction != ACTION_NONE)
		return;

	doAction = ACTION_POP;
}

void StateManager::popAllStatesAndPushState(void (*stateHandler)(Graphics *, Purpose))
{
	if(doAction != ACTION_NONE)
		return;

	newState = new State();

	newState->stateHandler = stateHandler;
	newState->previousState = NULL;

	doAction = ACTION_POP_ALL_AND_PUSH;
}

void StateManager::updateCurrentState(Graphics *gr)
{

	if(doAction)
	{
		switch(doAction)
		{
		case ACTION_PUSH:
			if(currentState)
				currentState->stateHandler(gr, STOP_PURPOSE);

			currentState = newState;
			newState = NULL;

			currentState->stateHandler(gr, INIT_PURPOSE);
			break;
		case ACTION_CHANGE_TOP:
			if(currentState)
			{
				currentState->stateHandler(gr, STOP_PURPOSE);
				delete currentState;
			}

			currentState = newState;
			newState = NULL;

			currentState->stateHandler(gr, INIT_PURPOSE);
			break;
		case ACTION_POP:
			currentState->stateHandler(gr, STOP_PURPOSE);

			State *prev;
			prev = currentState->previousState;
			delete currentState;
			currentState = prev;

			if(currentState)
				currentState->stateHandler(gr, INIT_PURPOSE);
			break;
		case ACTION_POP_ALL_AND_PUSH:
			currentState->stateHandler(gr, STOP_PURPOSE);
			while(currentState)
			{
				State *toDelete = currentState;
				currentState = currentState->previousState;
				delete toDelete;
			}

			currentState = newState;
			newState = NULL;

			currentState->stateHandler(gr, INIT_PURPOSE);
			break;
		}

		doAction = ACTION_NONE;
	}
	else
		if(currentState)
			currentState->stateHandler(gr, FRAME_PURPOSE);
}