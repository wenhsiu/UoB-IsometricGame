package com.isometricgame.core;

import customizedInputProcessor.GameInputProcessor;
import customizedInputProcessor.GameKeys;
import characterManager.Actor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.*;

public class Player extends Actor{

	private Animation walkDown, walkUp, walkRight, walkLeft;
	private float timer;
	private int score;
	private int frameNumber = 1;
	private int frameSizeX;
	private int frameSizeY;

	public Player(float x, float y) {
		super(x, y, (float)0.25);
		speedFactor = 100;
		speedLimit = 300;
		score = 0;
		frameSizeX = 302;
		frameSizeY = 333;
	}
	
	public SpriteBatch getBatch() {return batch;}
	
	@Override
	public void create () {
		characterInit("player_down.png", 0, 0, frameSizeX, frameSizeY);
		animationInit();
		Gdx.input.setInputProcessor(new GameInputProcessor());
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void render () {

		characterUpdate(pos_x, pos_y);
		animationUpdate(Gdx.graphics.getDeltaTime());
		
		if(GameKeys.isDown(GameKeys.UP) || GameKeys.isDown(GameKeys.DOWN) || 
				GameKeys.isDown(GameKeys.RIGHT) || GameKeys.isDown(GameKeys.LEFT)) {
//			speedFactor = Math.min(speedLimit, speedFactor + 1);  --disable accelerate
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
		texture.dispose();
	}

	@Override
	public void animationInit() {
		Array<TextureRegion> frames = new Array<TextureRegion>();

		for(int i = 0; i < frameNumber; i++) {
			frames.add(initTextureReg("player_down.png", 0, 0, frameSizeX, frameSizeY));
		}
		walkDown = new Animation(0.1f,frames);
		frames.clear();

		for(int i = 0; i < frameNumber; i++) {
			frames.add(initTextureReg("player_up.png", 0, 0, frameSizeX, frameSizeY));
		}
		walkUp = new Animation(0.1f,frames);
		frames.clear();

		for(int i = 0; i < frameNumber; i++) {
			frames.add(initTextureReg("player_right.png", 0, 0, frameSizeX, frameSizeY));
		}
		walkRight = new Animation(0.1f,frames);
		frames.clear();

		for(int i = 0; i < frameNumber; i++) {
			frames.add(initTextureReg("player_left.png", 0, 0, frameSizeX, frameSizeY));
		}
		walkLeft = new Animation(0.1f,frames);
		frames.clear();		
	}	

	@Override
	public void animationUpdate(float dt) {
		float speed = dt*speedFactor;
		timer += Gdx.graphics.getDeltaTime();

		if(Gdx.input.isKeyPressed(Keys.UP)) {
			region = walkUp.getKeyFrame(timer, true);
			pos_y += speed;
			pos_x += 2*speed;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			region = walkDown.getKeyFrame(timer, true);
			pos_y -= speed;
			pos_x -= 2*speed;
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			region = walkLeft.getKeyFrame(timer, true);	
			pos_y += speed;
			pos_x -= 2*speed;
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			region = walkRight.getKeyFrame(timer, true);
			pos_y -= speed;
			pos_x += 2*speed;
		}
		
	}

	public void setSpeedFactor(int newSpeed){
		speedFactor = newSpeed;
	}
	
	public void setScore() {score++;}
	
	public int getScore() {return score;}

	@Override
	public boolean isCollision(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

}
