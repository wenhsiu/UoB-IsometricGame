package com.isometricgame.core.charactermanager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.isometricgame.core.Boss;
import com.isometricgame.core.Penguin;
import com.isometricgame.core.Villager;
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
		
	private boolean triggerred;
	
	private Map<String, Integer> cost; //the properties player has to collect to trigger this game
	private String triggerText;
	
	private Map<String, People> guards;
	
	public TriggerPoint(float x, float y, float scale, GameManager gm, String gameName, String triggerText) {
		posX = x;
		posY = y;
		triggerred = false;
		this.scale = scale;
		this.gm = gm;
		gsName = gameName;
		this.triggerText = triggerText;
		guards = new HashMap<String, People>();
	}
	
	public void initGuards(String pplName, float x, float y) {
		if(!guards.containsKey(pplName)) {
			People p = null;
			String type;
			type = pplName.split("_")[0].toLowerCase();			
			if(type.equals("boss")) {
				p = new Boss(x, y);
			} else if(type.equals("villager")) {
				p = new Villager(x, y);
			} else if(type.equals("penguin")) {
				p = new Penguin(x, y);
			}
			
			p.create();			
			if(p != null) {
				guards.put(pplName, p);
				/**/System.out.println(pplName + " " + gsName);
			}
		}	
	}
	
	public People getGuardByName(String pplName) {
		return guards.get(pplName);
	}
	
	public void removeGuardByName(String pplName) {
		guards.remove(pplName);
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
		if(gm.getGameState(gsName) != null && gm.getGameState(gsName).getPassState()) {
			//if this gameState has been created and passed, remove guards
			
		}
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
		if(gm.getGameState(gsName) != null && gm.getGameState(gsName).getPassState()) {
			//if this gameState has been created and passed, do not trigger
		}else {
			gm.newGameStateByName(gsName);
			gm.setCurrGameState(gsName);
			triggerred = true;
		}				
	}

	public boolean getTriggerred() {
		return triggerred;
	}
	
	public GameState getTriggeredGame() {
		return gm.getGameState(gsName);
	}

	public void dispose() {
		texture.dispose();
	}

	/**
	 * @return the triggerText
	 */
	public String getTriggerText() {
		return triggerText;
	}

	/**
	 * @param triggerText the triggerText to set
	 */
	public void setTriggerText(String triggerText) {
		this.triggerText = triggerText;
	}
	
	public void setTriggerred(boolean triggerred) {
		this.triggerred = triggerred;
	}
	
}
