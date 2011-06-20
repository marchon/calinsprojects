#pragma once

#include "bitmask.h"
#include "Video.h"

typedef bitmask_t Collider;



/* Create a collider */
Uint32 getPixel(SDL_Surface *, unsigned, unsigned);
Collider* getCollider(Image *);

/* Delete a collider */
#define freeCollider(Collider_ptr) bitmask_free(Collider_ptr)


/* Ceck for collisions between two colliders */
/* TODO: make more rutines available */

/* This assumes that the offsets are measured from top - left corner */
#define isColliding_topLeft(Collider_ptr_1, Collider_ptr_2, x1, y1, x2, y2)	\
	bitmask_overlap(Collider_ptr_1, Collider_ptr_2, x2 - x1, y2 - y1)

/* This assumes that the offsets are measured from center corner */
#define isColliding_Center(Collider_ptr_1, Collider_ptr_2, x1, y1, x2, y2)	\
	bitmask_overlap(Collider_ptr_1, Collider_ptr_2, (x2 - Collider_ptr_2->w / 2) - (x1 - Collider_ptr_1->w / 2), \
	(y2 - Collider_ptr_2->h / 2) - (y1 - Collider_ptr_1->h / 2))

#define getCollisionNormalX(Collider_ptr_1, Collider_ptr_2, x1, y1, x2, y2)							\
							(bitmask_overlap_area(Collider_ptr_1, Collider_ptr_2,					\
							(x2 - Collider_ptr_2->w / 2) - (x1 - Collider_ptr_1->w / 2) + 1,		\
							(y2 - Collider_ptr_2->h / 2) - (y1 - Collider_ptr_1->h / 2)) -			\
							 bitmask_overlap_area(Collider_ptr_2, Collider_ptr_2,					\
							 (x2 - Collider_ptr_2->w / 2) - (x1 - Collider_ptr_1->w / 2) - 1,		\
							 (y2 - Collider_ptr_2->h / 2) - (y1 - Collider_ptr_1->h / 2)))

#define getCollisionNormalY(Collider_ptr_1, Collider_ptr_2, x1, y1, x2, y2)							\
							(bitmask_overlap_area(Collider_ptr_1, Collider_ptr_2,					\
							(x2 - Collider_ptr_2->w / 2) - (x1 - Collider_ptr_1->w / 2),			\
							(y2 - Collider_ptr_2->h / 2) - (y1 - Collider_ptr_1->h / 2) + 1) -		\
							 bitmask_overlap_area(Collider_ptr_2, Collider_ptr_2,					\
							 (x2 - Collider_ptr_2->w / 2) - (x1 - Collider_ptr_1->w / 2),			\
							 (y2 - Collider_ptr_2->h / 2) - (y1 - Collider_ptr_1->h / 2) - 1))

//#define isColliding isColliding_topLeft
#define isColliding	isColliding_Center

/* That's all for now */