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

public class Complete extends GameState {

	private final int LEVEL_WIDTH = 600;
	private final int LEVEL_HEIGHT = 220;
	private final int LEVEL_X;
	private final int LEVEL_Y = 300;
	
	private GameManager gm;
	private Texture level, levelActive;
	private SpriteBatch batch;

    
	public Complete(GameManager gm) {
		super();	
		this.gm = gm;
		level = new Texture("clickanddrag/maingame_button.png");
		levelActive = new Texture("clickanddrag/maingame_button2.png");
		LEVEL_X = Gdx.graphics.getWidth()/2 - LEVEL_WIDTH/2;
		batch = new SpriteBatch();
	}

    @Override
	public void render (float delta) {    	
		Gdx.gl.glClearColor(255/255f, 234/255f, 188/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //original point is from topleft corner
        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();

		batch.begin();

        if (mouseHovering(x, y)) {
            batch.draw(levelActive, LEVEL_X, LEVEL_Y, LEVEL_WIDTH, LEVEL_HEIGHT);
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
				GameState gs = gm.getGameState("MINIGAME2");
				//set the first game state passed to true so that the trigger point can detect correctly
				gs.setPassState(true);
				//go back to GAMEMAIN
				gm.setCurrGameState("MAINGAME");
			}
        } else {
            batch.draw(level, LEVEL_X, LEVEL_Y, LEVEL_WIDTH, LEVEL_HEIGHT);
        }

		batch.end();
	}

	private boolean mouseHovering(int x, int y) {
		if(x > LEVEL_X && 
		   x < LEVEL_X + LEVEL_WIDTH &&
		   y > LEVEL_Y &&
		   y < LEVEL_Y + LEVEL_HEIGHT) {
			return true;
		} else {return false;}
    }

	@Override
	public void resize (int width, int height) {
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
		levelActive.dispose();
    	level.dispose();
    	batch.dispose();
	}
}