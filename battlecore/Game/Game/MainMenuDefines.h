#pragma once


/* MENU FONT */
#define MENU_FONT_PATH	"Fonts\\menu_font.ttf"
#define MENU_FONT_SIZE	20
/* MENU FONT */

/* LOGO IMAGE */
#define LOGO_IMAGE_PATH	"Images\\Logo.bmp"
#define LOGO_IMAGE_OFF_Y	40
/* LOGO IMAGE */

/* MENU NAMES */
#define MAIN_MENU		"Main Menu"
#define PAUSE_MENU		"Pause Menu"
#define MAP_MENU		"Battlefields"
#define VEHICLE_MENU	"Vehicles"
#define OPTION_MENU		"Options"
#define CREDITS_MENU	"Credits"
/* MENU NAMES */



/* MENUS DRAW AND HANDLE INPUT */

/* DELAYS FOR MENU TITLE CURSSOR */
#define INTERVAL_BETWEEN_TYPEING	100
#define BLINKING_TIME				3000
#define BLINK_INTERVAL				250
/* DELAYS FOR MENU TITLE CURSSOR */

/* RETURN CODES */
#define NO_OPTION	-1
#define BACK		-2
/* RETURN CODES */

/* DELAYS FOR MENU SELECTION */
#define MIN_DELAY	51
#define MAX_DELAY	201
#define AMOUNT_OF_TIME_ADDED	50
#define TIME_BETWEEN_DELAY_CHANGING	500
/* DELAYS FOR MENU SELECTION */

/* MENU TRANZIT STATE */
#define NO_TRANZIT	0
#define TRANZIT_MIN_MAX	1
#define TRANZIT_MAX_MIN	2
//#define MENU_ENTER_CURSOR_BLINK
/* MENU TRANZIT STATE */

/* MENU OPTIONS */
#define TOTAL_MAIN_MENU_OPTIONS	4

#define MAIN_MENU_OPTIONS	{					\
								"New Game",		\
								"Options",		\
								"Credits",		\
								"Quit"			\
							}

#define NEW_GAME	0
#define OPTIONS		1
#define CREDITS		2
#define QUIT		3
/* MENU OPTIONS */

/* PAUSE MENU */

#define TOTAL_PAUSE_MENU_OPTIONS	4

#define PAUSE_MENU_OPTIONS	{								\
								"Resume Game",				\
								"Restart Game",				\
								"Back To Main Menu",		\
								"Quit Game"					\
							}

#define RESUME_GAME			0
#define RESTART_GAME		1
#define BACK_TO_MAIN_MENU	2
#define QUIT_GAME			3
/* PAUSE MENU */

/* CREDITS */
#define CREDITS_DELAY_WRITE1			150
#define CREDITS_DELAY_WRITE2			25
#define CREDITS_DELAY_DELETE			45
#define CREDITS_BLINK_TIME				2000

#define STATE_BLINK				0
#define STATE_WRITE				1
#define STATE_DELETE			2

#define TOTAL_CREDITS_ENTRIES	11

#define CREDITS_ENTRIES		{																			\
								"Executive Producers . . .  Calin Avasilcai and Ovidiu Raianu",			\
								"Game Designers . . .  Calin Avasilcai and Ovidiu Raianu",				\
								"Head Of Developement . . .  Calin Avasilcai",							\
								"Lead Programmer . . .  Calin Avasilcai",								\
								"Programmers . . .  Calin Avasilcai and Andi Postelnicu",				\
								"Head Of Art . . .  Ovidiu Raianu",										\
								"Artists . . .  Ovidiu Raianu",											\
								"Head Of Resources . . .  Cristian Baba",								\
								"Studio Manager . . .  Dana Palko",										\
								"QA . . .  Ovidiu Raianu",												\
								"Copyright 2007. All Rights Reserved!"									\
							}
/* CREDITS */

/* MENUS DRAW AND HANDLE INPUT */

