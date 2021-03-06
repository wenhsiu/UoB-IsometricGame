package com.isometricgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import com.isometricgame.core.charactermanager.People;

public class Monkey extends People {

	private Animation spin;
	private float timer;
	private int frameNumber = 4;
	private int frameSizeX;
    private int frameSizeY;
    private double move_x, move_y;
	private float init_x, init_y;
	private int direction;

	public Monkey(float x, float y) {
		super(x, y, (float) 0.35);
		frameSizeX = 309;
		frameSizeY = 399;
		direction = 1;
        move_x = 2;
        move_y = 1;
        init_x = x;
        init_y = y;
	}

	@Override
	public void create() {
		characterInit("monkey_down.png", 0, 0, frameSizeX, frameSizeY);
		animationInit();
		setBoundary(getSizeY() / 2, getSizeY() / 2, getSizeX() / 2, getSizeX() / 2);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void render() {
        moveVillager();
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
	public void dispose() {
		super.dispose();
	}

	@Override
	public void animationInit() {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		String[] frameName = {"monkey_up.png", "monkey_left.png", "monkey_down.png", "monkey_right.png"};
		for(int i = 0; i < frameNumber; i++) {
			frames.add(initTextureReg(frameName[i], 0, 0, frameSizeX, frameSizeY));
		}
		spin = new Animation(0.1f, frames);
		frames.clear();
	}

	@Override
	public void animationUpdate(float dt) {		
		region = spin.getKeyFrame(timer += dt / 10, true);
	}

	@Override
	public void CollisionAction(boolean fire) {
		if(fire) {
			randomDirection();
			if(direction == 0) {
				move_x = 2;
				move_y = 1;
			}
			else if(direction == 1) {
				move_x = 2;
				move_y = -1;
			}
			else if(direction == 2) {
				move_x = -2;
				move_y = 1;
			}
			else {
				move_x = -2;
				move_y = -1;
			}
		}		
	}
    
    private void moveVillager() {
        pos_x += move_x;
        pos_y += move_y;
    }

    private void randomDirection() {
        double r = Math.random();
		int i = (int) ((r * 3) + 1);
		direction += i;
		direction = direction % 4;
    }

    public float initial_x() {
		return init_x;
	}

    public float initial_y() {
		return init_y;
	}

}
