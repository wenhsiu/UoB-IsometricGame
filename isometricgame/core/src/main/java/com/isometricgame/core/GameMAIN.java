package com.isometricgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.graphics.OrthographicCamera;
// import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;

import com.isometricgame.core.charactermanager.People;
import com.isometricgame.core.charactermanager.Property;
import com.isometricgame.core.charactermanager.TriggerPoint;

// import com.isometricgame.core.dialog.DialogUI;

import com.isometricgame.core.ui.InventoryItem;
import com.isometricgame.core.ui.InventoryItem.ItemTypeID;
import com.isometricgame.core.ui.InventoryItemFactory;
import com.isometricgame.core.ui.InventoryUI;
import com.isometricgame.core.ui.PlayerHUD;

import java.util.ArrayList;

public class GameMAIN extends GameState {

	private GameManager gm;

	// Map
	private TiledMap map;
	private IsometricTiledMapRenderer mapRenderer;

	// Inventory
	public final OrthographicCamera hudcam;
	private PlayerHUD playerHUD;
	private InventoryUI inventoryUI;

	// Layers	
//	private TiledMapTileLayer blockedLayer;
	private TiledMapTileLayer transparentBlockedLayer;
	private TiledMapTileLayer baseObjLayer;

	// Tiles -- transparent
	private float TileEdge;
	private float TileW;
	private float TileH;	

	// Player
	private Player player;

	// Characters
	private ArrayList<People> people;
	// Naming rule: <type>_<alias>
	private final String[] peopleName = {"Boss_org", "Boss_drop", "Villager_1", "Villager_2"};
						
	private final float[] pplX = {500, 1954, 3000, 100};
	private final float[] pplY = {500, -38, -1000, 100};
	
	// Object to collect
	private ArrayList<Property> property;
	private final int coinNumber = 20;

	// Mini-game trigger points
	private ArrayList<TriggerPoint> tgp;
    private final float[] tgpX = {1170,  1880/*, 1930, 3030, 2020, 3260, 3900*/  };
	private final float[] tgpY = {50,  -10/*, 840, 390, -755, -900, 380  */};
	private final String[] allStateName = {"MINIGAME1", "MINIGAME2"};

	// Isometric parameters
	private final double theta = Math.toDegrees(Math.atan(0.5));
	
	public GameMAIN(GameManager gm) {
		super();
		this.gm = gm;
		
		initMapAndLayer();

		// PlayerHUD
		hudcam = new OrthographicCamera(width, height);
		hudcam.translate(width / 2, height / 2);
		hudcam.setToOrtho(true);
				
		playerHUD = new PlayerHUD(hudcam);
				
		inventoryUI = playerHUD.getInventoryUI();
		
		initProperties();
		initPeople();
		initTriggerPoint();
		
		player = gm.getPlayer();
	}

	@Override
	public void render(float delta) {
		//Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
		Gdx.gl.glClearColor(173/255f, 216/255f, 255/255f, 0xff/255.0f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		mapRenderer.setView(cam);
		mapRenderer.render();		
		
		float x = player.getPositionX();
		float y = player.getPositionY();
		Vector2 playerNextPosition = player.getNextPosition();
		
		// Trigger mini games with phone boxes
		checkTriggerGame(x, y); 		

		combineCameraPeople();
		combineCameraProperty();
		combineCameraTriggerPoint();		

		cam.position.set(x, y, 0);

		playerHUD.render(delta);
		hudcam.update();	
		
		// Add coins to inventory
		if(checkPropertyCollisions(x, y)) {
			InventoryItemFactory factory = InventoryItemFactory.getInstance();
			InventoryItem item = factory.getInventoryItem(ItemTypeID.COIN);			
			inventoryUI.addItemToInventory(item, "COIN");
			// TODO: Generalise this for more than 1 item
		}		

		checkVillagerCollisions(x, y);
		
		if(!isOnTheGround(playerNextPosition.x, playerNextPosition.y)) {			
			player.setFrozen(true);
		}else if(checkMapCollision(playerNextPosition.x, playerNextPosition.y)) {
			player.setSpeedFactor(-75);
		}		
		
		getPeopleByName("Villager_1").CollisionAction(
			checkVillagerMapCollision(
				getPeopleByName("Villager_1").getPositionX(),
				getPeopleByName("Villager_1").getPositionY(),
				TileEdge, TileEdge));
		
		player.getBatch().setProjectionMatrix(cam.combined);
		
		renderPeople();
		renderProperty();
		renderTriggerPoint();					
		player.render();
		
		cam.update();
		player.setFrozen(false);
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
		playerHUD.dispose();
		
		disposeProperty();
		disposeTriggerPoint();
		disposePeople();
	}
	
	private void initMapAndLayer() {

		//Map
		map = new TmxMapLoader().load("./Isometria/isometria.tmx");
		mapRenderer = new IsometricTiledMapRenderer(map);
		mapRenderer.setView(cam);

		// Blocked edge layer is transparent.
		transparentBlockedLayer = (TiledMapTileLayer) map.getLayers().get("Transparent");

		// Blocked layer is blocking but is not visible.
		transparentBlockedLayer.setVisible(false);

		//BaseObjects
		baseObjLayer = (TiledMapTileLayer)map.getLayers().get("BaseObjects");
		// TODO: Check if initial start position is blocked or not.

		TileW = transparentBlockedLayer.getTileWidth();
		TileH = transparentBlockedLayer.getTileHeight();
		TileEdge = (float) Math.sqrt(Math.pow(TileH / 2, 2) + Math.pow(TileW / 2, 2));			
	}
	
	// Handle People
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
			} else if(type.equals("villager")) {
				p = new Villager(x, y);
			}			
			p.create();			
			if(p != null) {
				people.add(p);
			}
		}		
	}
	
	private void playerBounce(Player player) {
		player.setSpeedFactor(-50);
		Vector2 nextPos = player.getNextPosition();
		if(!checkMapCollision(nextPos.x, nextPos.y) && isOnTheGround(nextPos.x, nextPos.y) ) {
			player.animationUpdate(Gdx.graphics.getDeltaTime());
			return;
		}else {
			playerBounce(player);
		}
	}
		
	private void combineCameraPeople() {
		for(int i = 0; i < people.size(); i++) {
			people.get(i).getBatch().setProjectionMatrix(cam.combined);
		}
	}
	
	private void renderPeople() {
		for(int i = 0; i < people.size(); i++) {
			people.get(i).render();
		}
	}
	
	private People getPeopleByName(String name) {
		for(int i = 0; i < peopleName.length; i++) {
			if(peopleName[i].equals(name)) {return people.get(i);}
		}
		return null;
	}
	
	private void disposePeople() {
		for(int i = 0; i < people.size(); i++) {
			people.get(i).dispose();
		}
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
		for(int i = 0; i < tgp.size(); i++) {
			tgp.get(i).updateTriggerPoint();
		}
	}
	
	private void disposeTriggerPoint() {
		for(int i = 0; i < tgp.size(); i++) {
			tgp.get(i).dispose();
		}
	}
	
	private void checkTriggerGame(float x, float y) {
		for(int i = 0; i < tgp.size(); i++) {	
			if(tgp.get(i).containPoint(x, y) && tgp.get(i).getTriggeredGame().getPassState() == false) {
				tgp.get(i).triggerGame();
				/**/System.out.println("Collision happen");
			}
		}
	}

	//Handle Property
	private void initProperties() {
		property = new ArrayList<Property>();		
		initCoins();
	}
	
	private void initCoins() {
		float x;
		float y;
		
		for(int i = 0; i < coinNumber; i++) {
			//Ensure coins will be on the base layer
			do{
				x = MathUtils.random(0, baseObjLayer.getWidth()*TileW);
				y = MathUtils.random(0, baseObjLayer.getHeight()*TileH);
			}while(!isOnTheGround(x, y) || checkMapCollision(x, y));			
			
			Coin c = new Coin(x, y);

			c.setBoundary(100, 100, 100, 100); //extend the bottom so that George can collect them easily
			property.add(c); 
			c.create();
		}
	}

	private void renderProperty() {
		for(int i = 0; i < property.size(); i++) {
			property.get(i).render();
		}
	}

	private void combineCameraProperty() {
		for(int i = 0; i < property.size(); i++) {
			property.get(i).getBatch().setProjectionMatrix(cam.combined);
		}
	}

	private void disposeProperty() {
		for(int i = 0; i < property.size(); i++) {
			property.get(i).dispose();
		}
	} 

	// Collisions with inventory, boolean for later checking
	private boolean checkPropertyCollisions(float x, float y) {
		for(int i = 0; i < property.size(); i++) {
			if(property.get(i).containPoint(x, y)) {
				property.remove(i);
				return true;
			}
		}
		return false;
	}

	// Lizzie test collisions with villager
		private void checkVillagerCollisions(float x, float y) {
			for(int i = 0; i < people.size(); i++) {
				if(people.get(i).containPoint(x, y)) {
					playerHUD.DialogOn();
				}
			}
	}

	public boolean checkMapCollision(float x, float y) {		
		Vector2 v = rotateCoord(x, y);
		int iso_x = (int) (v.x / TileEdge);
		int iso_y = (int) (v.y / TileEdge);

		Cell blockedCell = transparentBlockedLayer.getCell(iso_x, iso_y);	
		return (blockedCell != null && blockedCell.getTile() != null);
	}
	
	//check if on the BaseObjectives layer
		private boolean isOnTheGround(float x, float y) {
			if(x < 0) return false;
			
			Vector2 v = rotateCoord(x, y);
			int iso_x = (int)(v.x/ TileEdge);
			int iso_y = (int)(v.y/ TileEdge);

			Cell c = baseObjLayer.getCell(iso_x, iso_y);
			return (c != null && c.getTile() != null);
		}

	private Vector2 rotateCoord(float x, float y) {
		float tmp_x ;
		float tmp_y;
		float dx;
		float dy;
		
		double len = Math.sqrt(x * x + y * y);
		double alpha = Math.toDegrees(Math.atan(Math.abs(y) / x)); // Map: down is positive
		if(y > 0) {alpha *= -1;}
		double beta = 90 - 2*theta;
		
		Vector2 v = new Vector2();
		tmp_x = (float) (Math.cos(Math.PI * (theta - alpha) / 180) * len);
		tmp_y = (float) (Math.cos(Math.PI * (theta + alpha) / 180) * len);
		
		dx = (float)(len*Math.sin(Math.PI*(theta-alpha)/180)*Math.tan(Math.PI*beta/180));
		dy = (float)(len*Math.sin(Math.PI*(theta+alpha)/180)*Math.tan(Math.PI*beta/180));
		
		v.x = tmp_x-dx;
		v.y = tmp_y-dy;
		
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
		Cell cell = transparentBlockedLayer.getCell(iso_x, iso_y);		
		return (cell != null && cell.getTile() != null);
	}
}
