package com.isometricgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;

public class GameSUCCESS extends GameState{
	
	private Sprite sprite;
	private SpriteBatch batch;
	private Texture gameSuccess;
	private GameManager gm;
	
	/*TextureRegion img1, img2, img3, img4, img5, img6, img6, img7, img8;
	
	Animation imgAnimation; = new Animation(0.1f, tex1, tex2, tex3, tex4);*/

	public GameSUCCESS(GameManager gm) {
		super();
		this.gm = gm;
		gameSuccess = new Texture(Gdx.files.internal("gameWin.png"));
		/*img1 = new TextureRegion(new Texture("play_anim_1"));		
		img2 = new TextureRegion(new Texture("play_anim_1"));
		img3 = new TextureRegion(new Texture("play_anim_1"));
		img4 = new TextureRegion(new Texture("play_anim_1"));
		img5 = new TextureRegion(new Texture("play_anim_1"));
		img6 = new TextureRegion(new Texture("play_anim_1"));
		img7 = new TextureRegion(new Texture("play_anim_1"));
		img8 = new TextureRegion(new Texture("play_anim_1"));
		imgAnimation = new new Animation(0.1f, img1, img2, img3, img4, img5, img6, img7, img8);*/
		// sprite = new Sprite(bkgTexture);
		batch = new SpriteBatch();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		batch.begin();
		//imgAnimation.setPlayMode(Animation.PlayMode.LOOP);

		// sprite.draw(batch);
		batch.draw(gameSuccess, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
	 		gm.setCurrGameState("MENU");
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
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		gameSuccess.dispose();		
		batch.dispose();
	}

}
