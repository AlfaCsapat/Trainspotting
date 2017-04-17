package com.teamalpha.utils;

import java.awt.Color;

public class ColorHelper {

	//Alap sz�nek
	public static Color[] colors = { new Color(0, 0, 0),	//fekete
									 new Color(255, 0, 0),	//piros
									 new Color(0, 255, 0),	//z�ld
									 new Color(0, 0, 255)	//k�k
	};
	
	Color color;
	
	ColorHelper(int value) {
		color = colors[value];
	}
	
}
