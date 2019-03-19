package com.isometricgame.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.isometricgame.core.Player;

public class Play2nd implements Screen{
	
	private OrthographicCamera cam;
	private Player player;
	private Game game;
	
	public Play2nd(Game game0) {
		game = game0;	
		player = new Player(100, 100);
	}
	
	public Play2nd(Game game0, Player player0) {
		game = game0;	
		player = player0;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {}	
	 	player.render();		
	}

	@Override
	public void resize (int width, int height) {
		cam.viewportWidth = width; 
		cam.viewportHeight = height; 
		cam.update();
    }
    
    @Override
    public void show() {   
    	cam = new OrthographicCamera();
    	player.create();
    }

    @Override
    public void hide() {

    }

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
		player.dispose();
	}
}
