package com.isometricgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;

public class Play implements Screen {

  private TiledMap map; 
	private IsometricTiledMapRenderer renderer; 
	private OrthographicCamera camera; 

    private Player player = new Player();

    @Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		renderer.setView(camera); 
	 	renderer.render();

	 	player.render();
	}

	@Override
	public void resize (int width, int height) {
		camera.viewportWidth = width; 
		camera.viewportHeight = height; 
		camera.update();
    }
    
    @Override
    public void show() {   
		map = new TmxMapLoader().load("map.tmx");
		// TmxMapLoader loader = new TmxMapLoader();
  //       map = loader.load("map.tmx");
		renderer = new IsometricTiledMapRenderer(map); 
		camera = new OrthographicCamera(); 

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
		map.dispose(); 
		renderer.dispose();
	}
}
