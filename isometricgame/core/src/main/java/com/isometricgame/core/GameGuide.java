package com.isometricgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;

public class GameGuide extends GameState{
	
	private Sprite sprite;
	private SpriteBatch batch;
	private Texture mapGuide;
	private GameManager gm;
	
	public GameGuide(GameManager gm) {
		super();
		this.gm = gm;
		mapGuide = new Texture(Gdx.files.internal("mapguide.png"));		
		batch = new SpriteBatch();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(mapGuide, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
	 		gm.setCurrGameState("MENU");
	 	}
	}

	@Override
	public void resize(int width, int height) {		
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
        mapGuide.dispose();		
        batch.dispose();
	}
}
