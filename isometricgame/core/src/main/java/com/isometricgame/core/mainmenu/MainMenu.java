package com.isometricgame.core.mainmenu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;

public class MainMenu extends Game {
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}
