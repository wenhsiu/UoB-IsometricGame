package com.isometricgame.core;
import com.isometricgame.core.GameKeys;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.*;

public class Player implements ApplicationListener {
	private Texture texture;
	private SpriteBatch batch;
	private Sprite sprite;

	private TextureRegion region;

	private Animation walkDown, walkUp, walkRight, walkLeft;
	private TextureRegion[] framesUp, framesDown, framesRight, framesLeft;
	private float timer;
	
	private int speedFactor, speedMax = 300;

	int x, y;

	@Override
	public void create () {
		texture = new Texture(Gdx.files.internal("george.png"));
		batch = new SpriteBatch();
		// sprite = new Sprite(texture, 0, 0, 48, 48);
		region = new TextureRegion(texture, 0, 0, 48, 48);

		createFrames();

		x = 0;
		y = 0;
		speedFactor = 100;
		
		Gdx.input.setInputProcessor(new GameInputProcessor());
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void render () {
		this.update(Gdx.graphics.getDeltaTime());		

		batch.begin();
		batch.draw(region, x, y);
		batch.end();
		
		if(GameKeys.isDown(GameKeys.UP) || GameKeys.isDown(GameKeys.DOWN) || 
				GameKeys.isDown(GameKeys.RIGHT) || GameKeys.isDown(GameKeys.LEFT)) {
			speedFactor = Math.min(speedMax, speedFactor + 1);
		}else {speedFactor = 100;}
		
		GameKeys.update();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}

	public void update(float dt) {
		float speed = dt*speedFactor;
		timer += Gdx.graphics.getDeltaTime();

		if(Gdx.input.isKeyPressed(Keys.UP)) {
			// region.setRegion(96, 0, 48, 48);
			region = walkUp.getKeyFrame(timer,true);
			y += speed;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			// region.setRegion(0, 0, 48, 48);
			region = walkDown.getKeyFrame(timer,true);
			y -= speed;
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			// region.setRegion(48, 0, 48, 48);
			region = walkLeft.getKeyFrame(timer,true);			
			x -= speed;
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			// region.setRegion(144, 0, 48, 48);
			region = walkRight.getKeyFrame(timer,true);
			x += speed;
		}
	}

	public void createFrames() {
		Array<TextureRegion> frames = new Array<TextureRegion>();

		for(int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(texture, 0, i * 48, 48, 48));
		}
		walkDown = new Animation(0.1f,frames);
		frames.clear();

		for(int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(texture, 96, i * 48, 48, 48));
		}
		walkUp = new Animation(0.1f,frames);
		frames.clear();

		for(int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(texture, 144, i * 48, 48, 48));
		}
		walkRight = new Animation(0.1f,frames);
		frames.clear();

		for(int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(texture, 48, i * 48, 48, 48));
		}
		walkLeft = new Animation(0.1f,frames);
		frames.clear();
	}
}
