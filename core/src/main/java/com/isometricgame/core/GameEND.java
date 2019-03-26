package com.isometricgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import gameManager.GameManager;
import gameManager.GameState;

public class GameEND extends GameState{
	
	private Sprite sprite;
	private SpriteBatch batch;
	private Texture bkgTexture;
	private GameManager gm;
	
	public GameEND(GameManager gm) {
		super();
		this.gm = gm;
		bkgTexture = new Texture(Gdx.files.internal("gameOver.png"));		
		sprite = new Sprite(bkgTexture);
		batch = new SpriteBatch();
	}
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		sprite.draw(batch);
		batch.end();
		
	 	if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
	 		gm.setCurrGameState("MAINGAME");
	 	}
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);		
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
		bkgTexture.dispose();		
	}

}
