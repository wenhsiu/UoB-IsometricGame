package com.isometricgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import com.isometricgame.core.charactermanager.People;

public class Boss extends People {

	private Animation spin;
	private float timer;
	private int frameNumber = 4;
	private int frameSizeX;
	private int frameSizeY;
	
	
	public Boss(float x, float y) {
		super(x, y, (float) 0.5);
		frameSizeX = 302;
		frameSizeY = 344;
	}

	@Override
	public void create() {
		characterInit("boss_down.png", 0, 0, frameSizeX, frameSizeY);
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
	public void resume() {
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void animationInit() {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		String[] frameName = {"boss_up.png", "boss_left.png", "boss_down.png", "boss_right.png"};
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
	}

	@Override
	public void pause() {
	}

}
