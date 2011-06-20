#pragma once
#include <cmath>

#define PI 3.14159265f
#define COS(x) cosf((float)x * PI / 180.0f)
#define SIN(x) sinf((float)x * PI / 180.0f)
#define ATAN(y, x) (atan2f(y, x) * 180.0f / PI)

struct Vector2DAngleBased /*Not really logical from a "MATH" point of view*/
{
	float absoluteValue;
	float angle;

	Vector2DAngleBased(float a = 0.0f, float b = 0.0f) : absoluteValue(a), angle(b) {};
	Vector2DAngleBased(const Vector2DAngleBased &vect) : absoluteValue(vect.absoluteValue), angle(vect.angle) {};

	Vector2DAngleBased& operator+=(const float& scalar)
	{
		absoluteValue += scalar;
		return *this;
	}

	Vector2DAngleBased& operator-=(const float& scalar)
	{
		absoluteValue -= scalar;
		return *this;
	}

	Vector2DAngleBased& operator*=(const float& scalar)
	{
		absoluteValue *= scalar;
		return *this;
	}

	Vector2DAngleBased& operator/=(const float& scalar)
	{
		absoluteValue /= scalar;
		return *this;
	}

	Vector2DAngleBased operator*(const float& scalar)
	{
		Vector2DAngleBased res(*this);
		res.absoluteValue *= scalar;

		return res;
	}

	Vector2DAngleBased operator/(const float& scalar)
	{
		Vector2DAngleBased res(*this);
		res.absoluteValue /= scalar;

		return res;
	}

	Vector2DAngleBased& operator=(const float& scalar)
	{
		absoluteValue = scalar;
		return *this;
	}


	void operator<<(const float& angleDecrement)
	{
		angle -= angleDecrement;
		if(angle < 0) angle += 360;
	}

	void operator>>(const float& angleIncrement)
	{
		angle += angleIncrement;
		if(angle >= 360) angle -= 360;
	}


	bool operator!=(const float& scalar)
	{
		return (absoluteValue != scalar);
	}

	bool operator<(const float& scalar)
	{
		return (absoluteValue < scalar);
	}

	bool operator>(const float& scalar)
	{
		return (absoluteValue > scalar);
	}

};


struct Vector2D
{
	float x;
	float y;

	Vector2D(float a = 0.0f, float b = 0.0f) : x(a), y(b) {};
	Vector2D(const Vector2D& vect) : x(vect.x), y(vect.y) {};

	Vector2D& operator+=(const Vector2D& vect)
	{
		this->x += vect.x;
		this->y += vect.y;

		return *this;
	}

	Vector2D& operator+=(const Vector2DAngleBased& vect)
	{
		this->x += COS(vect.angle) * vect.absoluteValue;
		this->y += SIN(vect.angle) * vect.absoluteValue;

		return *this;
	}

	Vector2D& operator-=(const Vector2DAngleBased& vect)
	{
		this->x -= COS(vect.angle) * vect.absoluteValue;
		this->y -= SIN(vect.angle) * vect.absoluteValue;

		return *this;
	}

	Vector2D& operator*=(const float& scalar)
	{
		this->x *= scalar;
		this->y *= scalar;

		return *this;
	}

	Vector2D& operator/=(const float& scalar)
	{
		this->x /= scalar;
		this->y /= scalar;

		return *this;
	}
};