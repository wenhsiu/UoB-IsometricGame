package com.isometricgame.core;

import com.badlogic.gdx.Input.Keys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.*;

import com.isometricgame.core.custominputprocessor.GameInputProcessor;
import com.isometricgame.core.custominputprocessor.GameKeys;

import com.isometricgame.core.charactermanager.People;

public class Player extends People{

	private Animation walkDown, walkUp, walkRight, walkLeft;
	private float timer;
	private int score;
	private int frameNumber = 1;
	private int frameSizeX;
	private int frameSizeY;
	private float speed;
	private int direction = Keys.RIGHT;
	
	private Vector2 nextPosition;
	private final int nextSpeedParam = 10; //used to calculation next position
	private boolean frozen;

	public Player(float x, float y) {
		super(x, y, (float) 0.2); // was 0.5
		speedFactor = 100;
		speedLimit = 100;
		score = 0;
		frameSizeX = 302;
		frameSizeY = 333;
		speed = 0;
		nextPosition = new Vector2(pos_x, pos_y);
		frozen = false;
	}
	
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
		GameKeys.update();
		
		if(Gdx.input.isKeyPressed(Keys.UP)) {
			direction = Keys.UP;
			speedFactor = Math.min(speedLimit, speedFactor + 10);
		}else if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			direction = Keys.DOWN;
			speedFactor = Math.min(speedLimit, speedFactor + 10);
		}else if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			direction = Keys.RIGHT;
			speedFactor = Math.min(speedLimit, speedFactor + 10);
		}else if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			direction = Keys.LEFT;
			speedFactor = Math.min(speedLimit, speedFactor + 10);
		}else {
			speedFactor = 0;
		}
	
		characterUpdate(pos_x, pos_y);
		if(!frozen) {			
			animationUpdate(Gdx.graphics.getDeltaTime());
		}		
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
		super.dispose();
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
		speed = dt * speedFactor;
		timer += Gdx.graphics.getDeltaTime();		
		
		if(direction == Keys.UP) {
			region = walkUp.getKeyFrame(timer, true);
			pos_y += speed;
			pos_x += 2 * speed;
		}
		if(direction == Keys.DOWN) {
			region = walkDown.getKeyFrame(timer, true);
			pos_y -= speed;
			pos_x -= 2 * speed;
		}
		if(direction == Keys.LEFT) {
			region = walkLeft.getKeyFrame(timer, true);	
			pos_y += speed;
			pos_x -= 2 * speed;
		}
		if(direction == Keys.RIGHT) {
			region = walkRight.getKeyFrame(timer, true);
			pos_y -= speed;
			pos_x += 2 * speed;
		}
		
	}
	
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public int getDirection() {return direction;}
	
	public Vector2 getNextPosition() {
		speed  = Gdx.graphics.getDeltaTime() * speedFactor * nextSpeedParam;
		
		if(direction == Keys.UP) {
			nextPosition.x = (getPositionX() + 2*speed); 
			nextPosition.y = (getPositionY() + speed);
		}else if(direction == Keys.DOWN) {
			nextPosition.x = (getPositionX() - 2*speed); 
			nextPosition.y = (getPositionY() - speed);
		}else if(direction == Keys.LEFT) {
			nextPosition.x = (getPositionX() - 2*speed); 
			nextPosition.y = (getPositionY() + speed);
		}else if(direction == Keys.RIGHT) {
			nextPosition.x = (getPositionX() + 2*speed); 
			nextPosition.y = (getPositionY() - speed);
		}
		return nextPosition;
	} 
	
	public void setSpeedFactor(int newSpeed){
		speedFactor = Math.min(speedLimit, newSpeed);
	}
	
	public void setScore() {
		score++;
	}
	
	public int getScore() {
		return score;
	}

	@Override
	public void CollisionAction(boolean fire) {
		// TODO Auto-generated method stub
	}
	
}
