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
import com.badlogic.gdx.utils.Timer;

public class GameLevel1 extends GameState {

	private final int NEXT_WIDTH = 210;
	private final int NEXT_HEIGHT = 100;
	private final int NEXT_X = 930;
	private final int NEXT_Y = 300;
	
	private GameManager gm;
	private TextureRegion background;
	private Texture complete, completeActive;
	private SpriteBatch batch;
	private Puzzles puzzles;
	private boolean isRead = false;

	private Texture intro = new Texture("clickanddrag/game_intro1.png");
    
	public GameLevel1(GameManager gm) {
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
		batch.draw(intro, 30, 550, 1000, 150);
		batch.end();
		
		puzzles.render();
		

		// TODO: change the game intro

		// if(!isRead) {
		// 	batch.begin();
		// 	batch.draw(intro, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// 	if(Gdx.input.isKeyPressed(Keys.SPACE)) {
		// 		isRead = true;
		// 	}
		// 	batch.end();
		// }
		
		if(puzzles.checkTagetAnswer()){
			batch.begin();
			if (mouseHovering(x, y)) {
	            batch.draw(completeActive, NEXT_X, NEXT_Y, NEXT_WIDTH, NEXT_HEIGHT);
	            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
	            	// set the first game state passed to true so that the trigger point can detect correctly
					passed = true;
					gm.inventoryAddMedals();
	                // go back to GAMEMAIN
					gm.setCurrGameState("MAINGAME");
	            }
	        } else {
	            batch.draw(complete, NEXT_X, NEXT_Y, NEXT_WIDTH, NEXT_HEIGHT);
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
		background = new TextureRegion(new Texture("clickanddrag/background.png"), 0, 0, 652, 759);
		batch = new SpriteBatch();
		puzzles = new Puzzles();
		puzzles.create();
		complete = new Texture("clickanddrag/continue_button.png");
		completeActive = new Texture("clickanddrag/continue_button2.png");
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
		complete.dispose();
		completeActive.dispose();
	}

}
