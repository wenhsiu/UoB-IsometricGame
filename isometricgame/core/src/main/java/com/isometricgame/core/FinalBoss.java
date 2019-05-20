package com.isometricgame.core;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.isometricgame.core.charactermanager.TriggerPoint;
import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.ui.InventoryItem.ItemTypeID;

public class FinalBoss extends TriggerPoint{
	
	private float scale;
	private float increment = 0.001f;
	private final float minScale = 0.95f;
	private final float maxScale = 1.05f;
	private float dt;
	private float updatedSizeX;
	private float updatedSizeY;
	
	
//	private Animation breath;

	public FinalBoss(float x, float y, float scale, GameManager gm, String gameName, String triggerText) {
		super(x, y, scale, gm, gameName, triggerText);
		super.initTriggerPoint("finalBoss_right.png", 0, 0, 333, 394);
		this.scale = scale;
		dt = Gdx.graphics.getDeltaTime();
		cost = new HashMap<ItemTypeID, Integer>();
		cost.put(ItemTypeID.COIN, 5);
	}
	
	@Override
	public void updateTriggerPoint() {
		
		if(scale >= maxScale || scale <= minScale) {increment *= -1f;}
		scale += increment;
		
		updatedSizeX = sizeX*scale;
		updatedSizeY = sizeY*scale;		
		
		batch.begin();
		batch.draw(region, posX, posY, updatedSizeX, updatedSizeY);
		batch.end();
	}

	@Override
	public void triggerGame() {
		if(gm.getNumCoins() >= cost.get(ItemTypeID.COIN)) {
			super.triggerGame();
		}
	}
}
