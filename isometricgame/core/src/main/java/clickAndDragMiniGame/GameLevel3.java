package clickAndDragMiniGame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import clickAndDragGameManager.clickAndDragGameManager;
import clickAndDragGameManager.clickAndDragGameState;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import gameManager.GameManager;
import gameManager.GameState;

public class GameLevel3 extends clickAndDragGameState {
	
	private clickAndDragGameManager gm;
	private TextureRegion background;
	private Texture done;
	private SpriteBatch batch;
	private Puzzles puzzles;
	private boolean complete = false;
    
	public GameLevel3(clickAndDragGameManager gm) {
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

			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
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
		background = new TextureRegion(new Texture("background.png"), 0, 0, 652, 759);
		batch = new SpriteBatch();
		puzzles = new Puzzles();
		puzzles.create();
		done = new Texture("check.png");
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
