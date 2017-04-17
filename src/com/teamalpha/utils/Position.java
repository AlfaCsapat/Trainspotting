package com.teamalpha.utils;

/**
 * Egy terepasztal-beli poz�ci�t le�r� t�pus/oszt�ly.
 */
public class Position {
	public float x, y;

	/********************************************/
	public Position() { x = 0; y = 0; }
	public Position(float _x, float _y) { x = _x; y = _y; }
	public Position(Position copy) { x = copy.x; y = copy.y; }
}
