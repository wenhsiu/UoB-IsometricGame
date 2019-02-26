package com.isometricgame.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.isometricgame.core.IsoGame;

public class IsoGameDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = true; 
		config.width = 900; 
		new LwjglApplication(new IsoGame(), config);
	}
}
