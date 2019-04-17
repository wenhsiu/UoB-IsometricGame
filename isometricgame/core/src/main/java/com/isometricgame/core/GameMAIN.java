package com.isometricgame.core;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import characterManager.People;
import characterManager.Property;
import characterManager.TriggerPoint;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import gameManager.GameManager;
import gameManager.GameState;

public class GameMAIN extends GameState {

	private GameManager gm;
	//map
	private TiledMap map;
	private IsometricTiledMapRenderer mapRenderer;
	// Layers	
	private TiledMapTileLayer blockedLayer;
	private TiledMapTileLayer transparentBlockedLayer;
	//tile
	private float tileEdge;
	private float tileW;
	private float tileH;
	//player
	private Player player;
	//characters
	private ArrayList<People> people;
	private final String[] peopleName = {"Boss_org",
										 "Boss_drop",
										 "Villager_1"			
										};//naming rule: <type>_<alias>
	private final float[] pplX = {500,
								  1954,
								  3000,
							   };
	private final float[] pplY = {500,
								  -38,
								  -1000,
							   };
	//object to collect
	private ArrayList<Property> property;
	private final int coinNumber = 3;
	//mini game trigger point
	private ArrayList<TriggerPoint> tgp;
	private final float[] tgpX = {1170, 
								  //1880, 
								  //1930, 
								  //3030, 
								  //2020, 
								  //3260, 
								  //3900
								  };
	private final float[] tgpY = {50, 
								  //-10, 
								  //840, 
								  //390, 
								  //-755, 
								  //-900, 
								  //380
								  };
	private final String[] allStateName = {"MINIGAME1",									
								  };
	//isometric parameters
	private final double theta = Math.toDegrees(Math.atan(0.5));	

	public GameMAIN(GameManager gm) {
		super();
		this.gm = gm;
		
		initMapAndLayer();
		
		initProperties();
		initPeople();
		initTriggerPoint();
		
		player = gm.getPlayer();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		float x = player.getPositionX();
		float y = player.getPositionY();		
		
		checkPropertyCollisions(x, y); // collect coin		
		checkTriggerGame(x, y); //trigger mini games with phone boxes
		
		cam.position.set(x, y, 0);

		if (checkMapCollision(x, y, tileEdge, tileEdge)) {player.setSpeedFactor(-50);}
		
		getPeopleByName("Villager_1").CollisionAction(
				checkVillagerMapCollision(
						getPeopleByName("Villager_1").getPositionX(),
						getPeopleByName("Villager_1").getPositionY(),
						tileEdge,
						tileEdge));
		
		mapRenderer.setView(cam);
		mapRenderer.render();
		
		player.getBatch().setProjectionMatrix(cam.combined);
		
		combineCameraPeople();
		combineCameraProperty();
		combineCameraTriggerPoint();
		cam.update();

		renderPeople();
		renderProperty();
		renderTriggerPoint();
		player.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void show() {
		super.show();
		mapRenderer.setView(cam);
	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose() {
		mapRenderer.dispose();
		map.dispose();
		player.dispose();
		
		disposeProperty();
		disposeTriggerPoint();
		disposePeople();
	}
	
	private void initMapAndLayer() {
		//Map
		map = new TmxMapLoader().load("./Isometria/isometria.tmx");
		mapRenderer = new IsometricTiledMapRenderer(map);
		mapRenderer.setView(cam);
		// Block represents the "blocked" layer.
		// Later put the TiledMapTileLayers into an array.
		blockedLayer = (TiledMapTileLayer) map.getLayers().get("Block");

		// Blocked edge layer is transparent.
		transparentBlockedLayer = (TiledMapTileLayer) map.getLayers().get("Transparent");
		// blocked layer is blocking but is not visible.
		transparentBlockedLayer.setVisible(false);

		// TODO: Check if initial start position is blocked or not.

		tileW = blockedLayer.getTileWidth();
		tileH = blockedLayer.getTileHeight();
		tileEdge = (float) Math.sqrt(Math.pow(tileH / 2, 2) + Math.pow(tileW / 2, 2));
	}
	

	//Handle People
	private void initPeople() {
		people = new ArrayList<People>();
		People p = null;
		String type;
		
		for(int i = 0; i < peopleName.length; i++) {
			float x = pplX[i];
			float y = pplY[i];
			type = peopleName[i].split("_")[0].toLowerCase();
			
			if(type.equals("boss")) {
				p = new Boss(x, y);
			}else if(type.equals("villager")) {
				p = new Villager(x, y);

			}			
			p.create();			
			if(p != null) {people.add(p);}
		}		
	}
	
	private void combineCameraPeople() {
		for(People ppl : people) {
			ppl.getBatch().setProjectionMatrix(cam.combined);
		}
	}
	
	private void renderPeople() {
		for(People ppl : people) {ppl.render();}
	}
	
	private People getPeopleByName(String name) {
		for(int i = 0; i < peopleName.length; i++) {
			if(peopleName[i].equals(name)) {return people.get(i);}
		}
		return null;
	}
	
	private void disposePeople() {
		for(People ppl : people) {ppl.dispose();}
	}
	
	//Handle TriggerPoint
	private void initTriggerPoint() {
		tgp = new ArrayList<TriggerPoint>();
		for(int i = 0; i < tgpX.length; i++) {
			tgp.add(new PhoneBox(tgpX[i], tgpY[i], 1, gm, allStateName[i]));
		}
	}
	
	private void combineCameraTriggerPoint() {
		for(int i = 0; i < tgp.size(); i++) {
			tgp.get(i).getBatch().setProjectionMatrix(cam.combined);
		}
	}
	
	private void renderTriggerPoint() {
		for(TriggerPoint p : tgp) {
			p.updateTriggerPoint();
		}
	}
	
	private void disposeTriggerPoint() {
		for(TriggerPoint p : tgp) {
			p.dispose();
		}
	}
	
	private void checkTriggerGame(float x, float y) {
		for(TriggerPoint p : tgp) {	
			if(p.containPoint(x, y) && p.getTriggeredGame().getPassState() == false) {
				p.triggerGame();
			}
		}
	}

	//Handle Property
	private void initProperties() {
		property = new ArrayList<Property>();		
		initCoins();
	}
	
	private void initCoins() {		
		for (int i = 0; i < coinNumber; i++) {
			Coin c = new Coin(MathUtils.random(200, Gdx.graphics.getWidth() - 200),
					MathUtils.random(200, Gdx.graphics.getHeight() - 500));

			c.setBoundary(100, 25, 100, 100);
			property.add(c);
			c.create();
		}
	}

	private void renderProperty() {
		for(Property ppt : property){ppt.render();}
	}
	

	private void combineCameraProperty() {
		for (int i = 0; i < property.size(); i++) {
			property.get(i).getBatch().setProjectionMatrix(cam.combined);
		}
	}

	private void disposeProperty() {
		for (Property ppt : property) {ppt.dispose();}
	}

	//collisions
	private void checkPropertyCollisions(float x, float y) {
		for(int i = 0; i < property.size(); i++) {
			if(property.get(i).containPoint(x, y)) {property.remove(i);}
		}
	}

	public boolean checkMapCollision(float x, float y, float tilewidth, float tileheight) {
		int iso_x;
		int iso_y;

		Vector2 v = rotateCoord(x, y);

		iso_x = (int) (v.x / tilewidth);
		iso_y = (int) (v.y / tileheight);		

		return isCellBlocked(iso_x, iso_y);
	}

	private boolean isCellBlocked(int iso_x, int iso_y) {
		Cell cell = blockedLayer.getCell(iso_x, iso_y);
		boolean blocked = (cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey("Blocked"));
		// Check the invisible layer.
		if (blocked == false) {
			Cell transparentCheckCell = transparentBlockedLayer.getCell(iso_x, iso_y);
			blocked = (transparentCheckCell != null && transparentCheckCell.getTile() != null
					&& transparentCheckCell.getTile().getProperties().containsKey("Blocked"));
		}

		return blocked;
	}

	private Vector2 rotateCoord(float x, float y) {
		float tmp_x, tmp_y;
		double len = Math.sqrt(x * x + y * y);
		double alpha = Math.toDegrees(Math.atan(y / x * -1));//map: down is positive

		Vector2 v = new Vector2();
		tmp_x = (float) (Math.cos(Math.PI * (theta - alpha) / 180) * len);
		tmp_y = (float) (Math.cos(Math.PI * (theta + alpha) / 180) * len);
		v.x = (float) (tmp_x
				- len * Math.sin(Math.PI * ((theta - alpha) / 180) / (Math.tan(Math.PI * 2 * theta / 180))));
		v.y = (float) (tmp_y
				- len * Math.sin(Math.PI * ((theta + alpha) / 180) / (Math.tan(Math.PI * 2 * theta / 180))));

		return v;
	}

	public boolean checkVillagerMapCollision(float x, float y, float tilewidth, float tileheight) {
		int iso_x;
		int iso_y;
		Vector2 v = rotateCoord(x, y);

		iso_x = (int) (v.x / tilewidth);
		iso_y = (int) (v.y / tileheight);
		// Changed this from IsCellBlocked
		return isCellBlockedForVillager(iso_x, iso_y);
	}

	// Specifically for isCellBlocked
	private boolean isCellBlockedForVillager(int iso_x, int iso_y) {
		Cell cell = blockedLayer.getCell(iso_x, iso_y);		
		return (cell != null && cell.getTile().getProperties().containsKey("Blocked"));
	}

}
