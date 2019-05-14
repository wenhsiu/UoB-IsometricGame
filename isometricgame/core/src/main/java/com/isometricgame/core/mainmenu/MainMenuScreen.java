package com.isometricgame.core.mainmenu;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;

public class MainMenuScreen extends GameState {
	
	private enum bType{
			EXIT,
			PLAY
	};
	
    private final int EXIT_BUTTON_WIDTH = 100;
    private final int EXIT_BUTTON_HEIGHT = 115;
    private final int EXIT_BUTTON_X;
    private final int EXIT_BUTTON_Y = 100;
    
    private final int PLAY_BUTTON_WIDTH = 200;
    private final int PLAY_BUTTON_HEIGHT = 388;
    private final int PLAY_BUTTON_X;
    private final int PLAY_BUTTON_Y = 300;
    
    private GameManager gm;
    private SpriteBatch batch;

    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture playButtonActive;
    Texture playButtonInactive;

    public MainMenuScreen (GameManager gm) {
	    this.gm = gm;
	    
	    playButtonActive = new Texture("play_button_active.png");
	    playButtonInactive = new Texture("play_button_inactive.png");
	    PLAY_BUTTON_X = Gdx.graphics.getWidth()/2 - PLAY_BUTTON_WIDTH/2;
	    
	    exitButtonActive = new Texture("exit_button_active.png");
	    exitButtonInactive = new Texture("exit_button_inactive.png");
	    EXIT_BUTTON_X = Gdx.graphics.getWidth()/2 - EXIT_BUTTON_WIDTH/2;
	    
	    batch = new SpriteBatch();	    
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(211/255f, 238/255f, 255/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //original point is from topleft corner
        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();

        //Start drawing...
        batch.begin();
        
        if (mouseHovering(bType.EXIT, x, y)) {
            batch.draw(exitButtonActive, EXIT_BUTTON_X, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {Gdx.app.exit();}
        } else {
            batch.draw(exitButtonInactive, EXIT_BUTTON_X, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }

        if (mouseHovering(bType.PLAY, x, y)) {
            batch.draw(playButtonActive, PLAY_BUTTON_X, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {                
                gm.setCurrGameState("MAINGAME");;
            }
        } else {
            batch.draw(playButtonInactive, PLAY_BUTTON_X, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }
        
		batch.end();
    }

    private boolean mouseHovering(bType button, int x, int y) {
    	if(button == bType.EXIT) {
    		if(x > EXIT_BUTTON_X && 
    		   x < EXIT_BUTTON_X + EXIT_BUTTON_WIDTH &&
    		   y > EXIT_BUTTON_Y &&
    		   y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT) {
    			return true;
    		} else {return false;}
    	}else {
    		if(x > PLAY_BUTTON_X && 
    		   x < PLAY_BUTTON_X + PLAY_BUTTON_WIDTH &&
    		   y > PLAY_BUTTON_Y &&
    		   y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT) {
    			return true;
    	    } else {return false;}
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

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
    	exitButtonActive.dispose();
    	exitButtonInactive.dispose();
    	playButtonActive.dispose();
    	playButtonInactive.dispose();
    	batch.dispose();
    }
}