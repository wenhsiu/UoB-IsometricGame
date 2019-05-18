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
            PLAY,
            TUTORIAL
	};
    
    private final int GAME_TITLE_WIDTH = 754;
    private final int GAME_TITLE_HEIGHT = 200;
    private final int GAME_TITLE_X;
    private final int GAME_TITLE_Y = 475;

    private final int PLAY_BUTTON_WIDTH = 300;
    private final int PLAY_BUTTON_HEIGHT = 100;
    private final int PLAY_BUTTON_X;
    private final int PLAY_BUTTON_Y = 275;

    private final int TUTORIAL_BUTTON_WIDTH = 350;
    private final int TUTORIAL_BUTTON_HEIGHT = 100;
    private final int TUTORIAL_BUTTON_X;
    private final int TUTORIAL_BUTTON_Y = 175;

    private final int EXIT_BUTTON_WIDTH = 245;
    private final int EXIT_BUTTON_HEIGHT = 100;
    private final int EXIT_BUTTON_X;
    private final int EXIT_BUTTON_Y = 75;
    
    private GameManager gm;
    private SpriteBatch batch;

    Texture menuBackground;
    Texture gameTitle;
    Texture tutorialButtonActive;
    Texture tutorialButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture playButtonActive;
    Texture playButtonInactive;

    public MainMenuScreen (GameManager gm) {
        this.gm = gm;

        menuBackground = new Texture("menuBackground3.png");

        gameTitle = new Texture("gamename.png");
        GAME_TITLE_X =  Gdx.graphics.getWidth()/2 - GAME_TITLE_WIDTH/2;

	    playButtonActive = new Texture("start_button_active.png");
	    playButtonInactive = new Texture("start_button_inactive.png");
	    PLAY_BUTTON_X = Gdx.graphics.getWidth()/2 - PLAY_BUTTON_WIDTH/2;
	    
	    exitButtonActive = new Texture("exit_button_active.png");
	    exitButtonInactive = new Texture("exit_button_inactive.png");
        EXIT_BUTTON_X = Gdx.graphics.getWidth()/2 - EXIT_BUTTON_WIDTH/2;
        
        tutorialButtonActive = new Texture("tutorial_button_active.png");
	    tutorialButtonInactive = new Texture("tutorial_button_inactive.png");
	    TUTORIAL_BUTTON_X = Gdx.graphics.getWidth()/2 - TUTORIAL_BUTTON_WIDTH/2;
	    
	    batch = new SpriteBatch();	    
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(240/255f, 255/255f, 255/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //original point is from topleft corner
        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();

        //Start drawing...
        batch.begin();
        batch.draw(menuBackground, 0 , 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.draw(gameTitle, GAME_TITLE_X, GAME_TITLE_Y, GAME_TITLE_WIDTH, GAME_TITLE_HEIGHT);

        if (mouseHovering(bType.TUTORIAL, x, y)) {
            batch.draw(tutorialButtonActive, TUTORIAL_BUTTON_X, TUTORIAL_BUTTON_Y, TUTORIAL_BUTTON_WIDTH, TUTORIAL_BUTTON_HEIGHT);
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                gm.setCurrGameState("PRESENTATION");
            }
        } else {
            batch.draw(tutorialButtonInactive, TUTORIAL_BUTTON_X, TUTORIAL_BUTTON_Y, TUTORIAL_BUTTON_WIDTH, TUTORIAL_BUTTON_HEIGHT);
        }

        if (mouseHovering(bType.EXIT, x, y)) {
            batch.draw(exitButtonActive, EXIT_BUTTON_X, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {Gdx.app.exit();}
        } else {
            batch.draw(exitButtonInactive, EXIT_BUTTON_X, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }

        if (mouseHovering(bType.PLAY, x, y)) {
            batch.draw(playButtonActive, PLAY_BUTTON_X, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {                
                gm.setCurrGameState("MAINGAME");
            }
        } else {
            batch.draw(playButtonInactive, PLAY_BUTTON_X, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }
        
		batch.end();
    }

    private boolean mouseHovering(bType button, int x, int y) {
        if (button == bType.EXIT) {
            if (x > EXIT_BUTTON_X && 
    		   x < EXIT_BUTTON_X + EXIT_BUTTON_WIDTH &&
    		   y > EXIT_BUTTON_Y &&
    		   y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT) {
                   return true;
                } else {return false;}
            } else if (button == bType.PLAY) {
                if(x > PLAY_BUTTON_X && 
                x < PLAY_BUTTON_X + PLAY_BUTTON_WIDTH &&
                y > PLAY_BUTTON_Y &&
                y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT) {
                    return true;
                 } else {return false;}
            } else {
                if (x > TUTORIAL_BUTTON_X && 
                x < TUTORIAL_BUTTON_X + TUTORIAL_BUTTON_WIDTH &&
                y > TUTORIAL_BUTTON_Y &&
                y < TUTORIAL_BUTTON_Y + TUTORIAL_BUTTON_HEIGHT) {
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