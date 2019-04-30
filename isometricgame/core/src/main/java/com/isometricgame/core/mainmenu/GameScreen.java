package com.isometricgame.core.mainmenu;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;


public class GameScreen implements Screen {
	//solve issue of delta time
	public static final float speed = 120;
	Texture texture;
	float x;
    float y;
    
    MainMenu game;

    public GameScreen(MainMenu game) {
        this.game = game;
    }
    
    @Override
    public void show() {
        texture = new Texture("libgdx-logo.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(208/255f, 249/255f, 255/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isKeyPressed(Keys.UP)) {
			y += speed * Gdx.graphics.getDeltaTime();
		}

		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			y -= speed * Gdx.graphics.getDeltaTime();
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			x -= speed * Gdx.graphics.getDeltaTime();
		}

		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			x += speed * Gdx.graphics.getDeltaTime();
		}
		game.batch.begin();
		game.batch.draw(texture, x, y);
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