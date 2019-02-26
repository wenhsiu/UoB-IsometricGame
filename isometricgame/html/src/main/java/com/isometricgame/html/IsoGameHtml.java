package com.isometricgame.html;

import com.isometricgame.core.IsoGame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class IsoGameHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new IsoGame();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
