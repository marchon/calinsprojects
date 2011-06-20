#pragma once

/* SHAKE REQUEST */
#define MIN_INTENSITY			3
#define MAX_INTENSITY			10

#define IS_VALID_INTENSITY(x)	((x >= MIN_INTENSITY) && (x <= MAX_INTENSITY))

#define TIME_BEFORE_INTENSITY_CHANGES(intensity)	((150 * (intensity) + 950 )/ 7)
#define TIME_BEFORE_NEW_SHAKE(intensity)			(((-15) * (intensity) + 255) / 7)

#define SHAKE_INTENSITY_LOW		MIN_INTENSITY
#define SHAKE_INTENSITY_HIGH	MAX_INTENSITY
/* SHAKE REQUEST */