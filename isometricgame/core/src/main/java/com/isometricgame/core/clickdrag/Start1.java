package com.isometricgame.core.clickdrag;

import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Start1 extends GameState {
	
	private GameManager gm;
	private Texture texture;
	private TextureRegion background;
	private SpriteBatch batch;

    
	public Start1(GameManager gm) {
		super();	
		this.gm = gm;
		background = new TextureRegion(new Texture("clickanddrag/Level1.png"), 0, 0, 652, 759);
		batch = new SpriteBatch();
	}
       

    @Override
	public void render (float delta) {    	
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			//If Start1 can be trigger, the following states has to be created no matter they are existing or not.
			gm.newGameStateByName("GAMELEVEL1");
			gm.setCurrGameState("GAMELEVEL1");			
		}
	}

	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
    }
	
    
    @Override
    public void show() {   
    	// super.show();
    }

    @Override
    public void hide() {
    	super.hide();
    }

	@Override
	public void pause () {
		super.pause();
	}

	@Override
	public void resume () {
		super.resume();
	}

	@Override
	public void dispose () {
		// background.disxose();
	}

}
