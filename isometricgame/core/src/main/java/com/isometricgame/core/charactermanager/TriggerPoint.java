package com.isometricgame.core.charactermanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;

public class TriggerPoint {
	
	protected float posX;
	protected float posY;
	protected float sizeX;
	protected float sizeY;
	protected float boundTop;
	protected float boundBottom;
	protected float boundRight;
	protected float boundLeft;
	protected Texture texture;
	protected SpriteBatch batch;
	protected TextureRegion region;
	private float scale;
	private GameManager gm;
	private String gsName;
	
	public TriggerPoint(float x, float y, float scale, GameManager gm, String gameName) {
		posX = x;
		posY = y;
		this.scale = scale;
		this.gm = gm;
		gsName = gameName;
	}
	
	public void initTriggerPoint(String materials, int sx, int sy, int ex, int ey) {
		texture = new Texture(Gdx.files.internal(materials));
		batch = new SpriteBatch();
		region = new TextureRegion(texture, sx, sy, ex, ey);
		sizeX = (ex - sx) * scale;
		sizeY = (ey - sy) * scale;
		setBoundary(sizeY / 2, sizeY / 2, sizeX / 2, sizeX / 2);
	}

	public void setBoundary(float top, float bottom, float right, float left) {
		boundTop = top * scale;
		boundBottom = bottom * scale;
		boundRight = right * scale;
		boundLeft = left * scale;
	}
	
	public void updateTriggerPoint() {
		batch.begin();
		batch.draw(region, posX, posY, sizeX, sizeY);
		batch.end();
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}
	
	public float getPositionX() {
		return posX + sizeX / 2;
	}

	public float getPositionY() {
		return posY + sizeY / 2;
	}
	
	public boolean containPoint(float x, float y) {
		if(x > getPositionX() - boundLeft &&
		   x < getPositionX() + boundRight &&
		   y > getPositionY() - boundBottom &&
		   y < getPositionY() + boundTop) {
				return true;
			}
		return false;
	}
	
	public void triggerGame() {
		System.out.println(gsName);
		gm.setCurrGameState(gsName);
	}

	public GameState getTriggeredGame() {
		return gm.getGameState(gsName);
	}

	public void dispose() {
		texture.dispose();
	}
	
}
