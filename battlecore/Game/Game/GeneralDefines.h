#pragma once


/* KEY DEFINES */
#define KEY_ESCAPE	0
#define KEY_UP		1
#define KEY_DOWN	2
#define KEY_LEFT	3
#define KEY_RIGHT	4
#define KEY_ENTER	5
#define KEY_SPACE	6
#define TOTAL_KEYS	7

#define PRESS_KEY(key)		(keyBuffer[key] = true)//(keyBuffer |= key)
#define RELEASE_KEY(key)	(keyBuffer[key] = false)//(keyBuffer &= (~key))
#define FLUSH_KEYS()		for(int i = 0; i < TOTAL_KEYS; i++) keyBuffer[i] = false//(keyBuffer = 0)
#define IS_KEY_PRESSED(key)	(keyBuffer[key])//(keyBuffer & key)
/* KEY DEFINES */

/* COLOR DEFINES */
#define WHITE	255,	255,	255
#define ORANGE	230,	128,	50
#define BLACK	0,		0,		0
#define RED		255,	0,		0
#define BLUE	50,		50,		255
/* COLOR DEFINES */