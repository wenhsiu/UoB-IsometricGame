package com.isometricgame.core.raindrop;

import com.isometricgame.core.charactermanager.Property;

public class Sun extends Property{
	
	private boolean visible;

	public Sun(float x, float y) {
		super(x, y, 1.0f);
		visible = false;
		// TODO Auto-generated constructor stub
	}

	public void create() {
		initProperty("./weather/sunny.png", 0, 0, 256, 256);
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
		if(visible) {
			characterUpdate(posX, posY);
		}		
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
