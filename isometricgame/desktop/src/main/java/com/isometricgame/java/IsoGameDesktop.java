package com.isometricgame.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.isometricgame.core.IsoGame;

public class IsoGameDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = true;
		config.width = 1200;
		config.height = 750;
		config.resizable = false;
		LwjglApplication app = new LwjglApplication(new IsoGame(), config);
	}
}


