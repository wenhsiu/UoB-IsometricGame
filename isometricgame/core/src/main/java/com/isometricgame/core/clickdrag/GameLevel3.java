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
	
	private GameManager gm;
	private TextureRegion background;
	private Texture done;
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

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		puzzles.render();

		if(puzzles.checkTagetAnswer()){
			batch.begin();
			batch.draw(done, 50, 600, 100, 100);
			batch.end();

			// if(!gm.getGameState("COMPLETE").getPassState()) {
			// 	gm.setCurrGameState("COMPLETE");
			// }

			if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {				
				gm.setCurrGameState("COMPLETE");
			}
			
		}
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
		done = new Texture("clickanddrag/check.png");
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
	}

}
