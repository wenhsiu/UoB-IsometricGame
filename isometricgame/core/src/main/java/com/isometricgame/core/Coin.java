package com.isometricgame.core;

import characterManager.Actor;
import com.badlogic.gdx.Gdx;

public class Coin extends Actor {

	private final int range = 30;
	private float org_x, org_y;
	protected boolean remove;
	
	public Coin(float x, float y) {		
		super(x, y, (float)0.5);
		org_x = x;
		org_y = y;
		speedFactor = 50;
		remove = false;	
	}
	
	@Override
	public void create () {
		characterInit("collectable.png", 0, 0, 500, 500);
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void render () {	//make coin float				
		float dt = Gdx.graphics.getDeltaTime();
		if(pos_y >= org_y + range/2 && speedFactor > 0) {speedFactor *= -1;}
		if(pos_y <= org_y - range/2 && speedFactor < 0) {speedFactor *= -1;}
		pos_y += speedFactor*dt;

		characterUpdate(pos_x, pos_y);
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
	}

	@Override
	public void animationUpdate(float dt) {
	}

	@Override
	public boolean isCollision(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}
}