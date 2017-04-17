package com.teamalpha;

/**
 * Egy terepasztal-beli poz�ci�t le�r� t�pus/oszt�ly.
 */
public class Position {
	float x, y;

	/********************************************/
	Position() { x = 0; y = 0; }
	Position(float _x, float _y) { x = _x; y = _y; }
	Position(Position copy) { x = copy.x; y = copy.y; }
}
