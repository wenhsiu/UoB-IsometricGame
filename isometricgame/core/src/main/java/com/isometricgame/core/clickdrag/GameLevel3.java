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

public class GameLevel3 extends GameState {
	
	private final int NEXT_WIDTH = 210;
	private final int NEXT_HEIGHT = 100;
	private final int NEXT_X = 930;
	private final int NEXT_Y = 300;

	private GameManager gm;
	private TextureRegion background;
	private Texture next, nextActive;
	private SpriteBatch batch;
	private Puzzles puzzles;
	private boolean complete = false;
    
	public GameLevel3(GameManager gm) {
		super();	
		this.gm = gm;
	}
       

    @Override
	public void render (float delta) {    	
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		puzzles.render();

		if(puzzles.checkTagetAnswer()){
			batch.begin();
			if (mouseHovering(x, y)) {
	            batch.draw(nextActive, NEXT_X, NEXT_Y, NEXT_WIDTH, NEXT_HEIGHT);
	            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {                
	                gm.newGameStateByName("COMPLETE");
	                gm.setCurrGameState("COMPLETE");;
	            }
	        } else {
	            batch.draw(next, NEXT_X, NEXT_Y, NEXT_WIDTH, NEXT_HEIGHT);
	        }
			batch.end();			
		}
	}

	private boolean mouseHovering(int x, int y) {
		if(x > NEXT_X && 
		   x < NEXT_X + NEXT_WIDTH &&
		   y > NEXT_Y &&
		   y < NEXT_Y + NEXT_HEIGHT) {
			return true;
		} else {return false;}
    }

	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
    }
	
    
    @Override
    public void show() {   
    	// super.show();
		background = new TextureRegion(new Texture("clickanddrag/background.png"));
		batch = new SpriteBatch();
		puzzles = new Puzzles();
		puzzles.create();
		next = new Texture("clickanddrag/continue_button.png");
		nextActive = new Texture("clickanddrag/continue_button2.png");
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
		puzzles.dispose();
		next.dispose();
		nextActive.dispose();
	}

}
