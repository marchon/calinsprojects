#pragma once

/* WINDOWS STUFF */
#define BUFSIZE MAX_PATH
#define _WIN32_WINNT 0x0501
/* WINDOWS STUFF */


/* FUCK WCHAR_T!!! - convert it to char array*/
#define WCHAR_TO_CHAR(wch, ch)				\
{											\
	unsigned int i;							\
	for(i = 0; i < wcslen(wch); i++)		\
		ch[i] = char(wch[i]);				\
	ch[i] = '\0';							\
}
/* FUCK WCHAR_T!!! */

/* PATHS */
#define MAP_FOLDER "Maps\\"
#define MAP_OBJECT_FOLDER "Maps\\MapObjects\\"
#define MAP_OBJECT_FOLDER_NAME "MapObjects"
#define OBJECT_INFO_FILE "\\Object.info"
#define OBJECT_FRAME_FOLDER "\\Frames\\"
/* PATHS */

/* MAXIMUM NUMBER OF DIFFRENT MAPOBJECTS */
#define NUMBER_OF_POTENTIAL_MAP_OBJECTS	256
/* MAXIMUM NUMBER OF DIFFRENT MAPOBJECTS */