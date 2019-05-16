package com.isometricgame.core.raindrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.isometricgame.core.charactermanager.Property;

public class Cloud extends Property {
	
	private final int frameW = 256;
	private final int frameH = 256;
	
	private Animation change;
	
	private float timer;

	public Cloud(float x, float y) {
		super(x, y, 1f);
		animationInit();
	}
	
	public void create() {
		initProperty("./weather/cloud.png", 0, 0, 256, 256);
	}

	@Override
	public void animationInit() {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		String[] frameName = {"./weather/cloud.png", 
							  "./weather/cloud.png", 
							  "./weather/cloud.png", 
							  "./weather/storm.png"};
		for(int i = 0; i < frameName.length; i++) {
			frames.add(initTextureReg(frameName[i], 0, 0, frameW, frameH));
		}
		change = new Animation(0.1f, frames);
		frames.clear();					
	}

	@Override
	public void animationUpdate(float dt) {
		region = change.getKeyFrame(timer += dt/10, true);
	}

	@Override
	public void render() {		
		animationUpdate(Gdx.graphics.getDeltaTime());
		characterUpdate(posX, posY);
	}

}
