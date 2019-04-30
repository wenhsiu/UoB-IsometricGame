package com.isometricgame.core.mainmenu;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;


public class TextScreen implements Screen {
	Texture texture;
    MainMenu game;
    private static final int NEXT_BUTTON_WIDTH = 200;
    

    public TextScreen(MainMenu game) {
        this.game = game;

    }
    
    @Override
    public void show() {
        texture = new Texture("maxresdefault.jpg");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(208/255f, 249/255f, 255/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		game.batch.draw(texture,0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}