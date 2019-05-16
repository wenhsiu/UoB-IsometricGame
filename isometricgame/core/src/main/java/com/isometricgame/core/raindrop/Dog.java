package com.isometricgame.core.raindrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.isometricgame.core.charactermanager.People;

public class Dog extends People{

	private float timer;
	private final int speed = 200;
	private int direction;
	private final int frameSizeX = 585;
	private final int frameSizeY = 670;
	private Animation right, left;
	private final int windowW;
	
	public Dog(float x, float y) {
		super(x, y, 0.2f);
		timer = 0;
		windowW = Gdx.graphics.getWidth();
	}

	@Override
	public void create() {
		animationInit();
		characterInit("dog_down.png", 0, 0, frameSizeX, frameSizeY);			
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		animationUpdate(Gdx.graphics.getDeltaTime());
		characterUpdate(pos_x, pos_y);
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
	public void animationInit() {
		Array<TextureRegion> rightFrames = new Array<TextureRegion>();
		Array<TextureRegion> leftFrames = new Array<TextureRegion>();

		rightFrames.add(initTextureReg("dog_right.png", 0, 0, frameSizeX, frameSizeY));
		leftFrames.add(initTextureReg("dog_down.png", 0, 0, frameSizeX, frameSizeY));
		
		right = new Animation(0.1f, rightFrames);
		left = new Animation(0.1f, leftFrames);
		
		rightFrames.clear();
		leftFrames.clear();
	}

	@Override
	public void animationUpdate(float dt) {
		timer += Gdx.graphics.getDeltaTime();
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			region = right.getKeyFrame(timer, true);
			pos_x += speed * dt;
			pos_x = Math.min(pos_x, windowW);
		}else if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			region = left.getKeyFrame(timer, true);
			pos_x -= speed * dt;
			pos_x = Math.max(pos_x, 0);
		}
		
	}

	@Override
	public void CollisionAction(boolean fire) {
		// TODO Auto-generated method stub
		
	}

}
