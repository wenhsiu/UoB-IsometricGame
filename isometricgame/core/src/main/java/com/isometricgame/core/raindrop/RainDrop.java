package com.isometricgame.core.raindrop;

import com.badlogic.gdx.Gdx;
import com.isometricgame.core.charactermanager.Property;

public class RainDrop extends Property{

	private String imgPath;
	private final int dropW = 256;
	private final int dropH = 256;
	
	private int speed = 50;
	
	public RainDrop(float x, float y, int digit) {
		super(x, y, 0.5f);
		if(digit == 0) {imgPath = "./weather/raindrop0.png";}
		else {imgPath = "./weather/raindrop1.png";}
		
		initProperty(imgPath, 0, 0, dropW, dropH);
	}

	@Override
	public void animationInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void animationUpdate(float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		posY -= Gdx.graphics.getDeltaTime() * speed;
		posY = Math.max(posY, 0);
		characterUpdate(posX, posY);		
	}

}
