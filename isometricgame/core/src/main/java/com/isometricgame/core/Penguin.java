package com.isometricgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.isometricgame.core.charactermanager.People;

public class Penguin extends People{
	
	private Animation spin;
	private float timer;
	private final int frameSizeX = 611;
	private final int frameSizeY = 680;
	private final int frameNumber = 4;

	public Penguin(float x, float y) {
		super(x, y, 0.1f);		
	}

	@Override
	public void create() {
		characterInit("penguin_down.png", 0, 0, frameSizeX, frameSizeY);
		animationInit();
		setBoundary(getSizeY()/2, getSizeY()/2, getSizeX()/2, getSizeX()/2);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		characterUpdate(pos_x, pos_y);
		animationUpdate(Gdx.graphics.getDeltaTime());		
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
		Array<TextureRegion> frames = new Array<TextureRegion>();
		String[] frameName = {"penguin_up.png", "penguin_left.png", "penguin_down.png", "penguin_right.png"};
		for(int i = 0; i < frameNumber; i++) {
			frames.add(initTextureReg(frameName[i], 0, 0, frameSizeX, frameSizeY));
		}
		spin = new Animation(0.1f, frames);
		frames.clear();
	}

	@Override
	public void animationUpdate(float dt) {
		region = spin.getKeyFrame(timer += dt/10, true);	
	}

	@Override
	public void CollisionAction(boolean fire) {
		// TODO Auto-generated method stub
		
	}

}
