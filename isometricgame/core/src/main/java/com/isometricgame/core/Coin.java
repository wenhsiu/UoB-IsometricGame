package com.isometricgame.core;

import characterManager.Property;

import com.badlogic.gdx.Gdx;

public class Coin extends Property {

	private final int range = 30;
	private float orgX;
	private float orgY;
	protected boolean remove;
	
	public Coin(float x, float y) {		
		super(x, y, (float)0.5);
		orgX = x;
		orgY = y;
		speed = 50;
		remove = false;	
	}
	
	
	public void create() {
		initProperty("collectable.png", 0, 0, 500, 500);
	}


	public void render () {	//make coin float				
		float dt = Gdx.graphics.getDeltaTime();
		if(posY >= orgY + range/2 && speed > 0) {speed *= -1;}
		if(posY <= orgY - range/2 && speed < 0) {speed *= -1;}
		posY += speed*dt;
		characterUpdate(posX, posY);
	}

	public void dispose () {
		super.dispose();
	}

	@Override
	public void animationInit() {
	}

	@Override
	public void animationUpdate(float dt) {
	}
}
