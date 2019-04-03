package com.isometricgame.core;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import gameManager.GameManager;
import gameManager.GameState;

public class GameMAIN extends GameState {
	
	private GameManager gm;
	private TiledMap map;
	private IsometricTiledMapRenderer mapRenderer;
	private TiledMapTileLayer blockedLayer;
	private float tileEdge;
	private float tileW;
	private float tileH; 

	private Player player;
    private Boss boss;
    private ArrayList<Coin> coins;
	private int coinNumber = 3;
	private final double theta = Math.toDegrees(Math.atan(0.5));


	//Villager creation. 

	private Villager villager1; 
    
	public GameMAIN(GameManager gm) {
		super();	
		this.gm = gm;
		map = new TmxMapLoader().load("./Isometria/isometria.tmx");
		mapRenderer = new IsometricTiledMapRenderer(map);
		mapRenderer.setView(cam);

		//Block represents the "blocked" layer. 
		//Later put the TiledMapTileLayers into an array. 
		blockedLayer = (TiledMapTileLayer) map.getLayers().get("Block"); 

		//TODO: Check if initial start position is blocked or not. 
		
		tileW = blockedLayer.getTileWidth();
		tileH = blockedLayer.getTileHeight();
		tileEdge = (float)Math.sqrt(Math.pow(tileH/2, 2) + Math.pow(tileW/2, 2));
		
		boss = new Boss(500, 500);
		initCoins();

		//Create a villager 
		villager1 = new Villager (3000, -1000); 
		villager1.create();
		
		boss.create();
		player = gm.getPlayer();
		createCoins();
	}
       

    @Override
	public void render (float delta) {
    	
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f,0xff/255.0f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
	 	
	 	//collect coin
		for(int i = 0; i < coins.size(); i++) {checkCollisions(coins.get(i));}

		//trigger a fight with the boss
		if(boss.isCollision(player.getPositionX(), 
							player.getPositionY())
							&&
							!gm.getGameState("MINIGAME1").getPassState()) {
			gm.setCurrGameState("MINIGAME1");
		}

		if(villager1.isCollision(player.getPositionX(), 
				player.getPositionY())
				&&
				!gm.getGameState("MAINGAME").getPassState()) {
				gm.setCurrGameState("END");
		}
		
		cam.position.set(player.getPositionX(), player.getPositionY(), 0);
		
		if(checkMapCollision(player.getPositionX() - player.getSizeX()/2, 
				  player.getPositionY() - player.getSizeY()/2, 
				  tileEdge, 
				  tileEdge)){		
			player.setSpeedFactor(-100);
		}

		//Check the villager collisions. 

		villager1.checkCollision(checkVillagerMapCollision(villager1.getPositionX() - villager1.getSizeX()/2, 
		villager1.getPositionY() - villager1.getSizeY()/2, tileEdge, tileEdge)); 




		mapRenderer.setView(cam); 
	 	mapRenderer.render();
	 	
	 	player.getBatch().setProjectionMatrix(cam.combined);
	 	boss.getBatch().setProjectionMatrix(cam.combined);
		
		
		// Create the villager batches. 
		villager1.getBatch().setProjectionMatrix(cam.combined);

		
		
		
		combineCameraCoins();
		 
	 	
		cam.update();
		
		renderCoins();
	 	player.render();
		 boss.render();
		 villager1.render();
	}

	@Override
	public void resize (int width, int height) {
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
	public void pause () {
		super.pause();
	}

	@Override
	public void resume () {
		super.resume();
	}

	@Override
	public void dispose () {
		mapRenderer.dispose();
		map.dispose();
		player.dispose();
		boss.dispose();
		disposeCoins();
		villager1.dispose();
	}
	
//Handle coin array
	private void initCoins() {
    	coins = new ArrayList<Coin>();
    	for(int i = 0; i < coinNumber; i++) {
    		Coin c = new Coin(MathUtils.random(200, Gdx.graphics.getWidth()-200), 
    				MathUtils.random(200, Gdx.graphics.getHeight()-500));
    		
    		c.setBound(100, 25, 100, 100);
    		coins.add(c);
    	}
    }
    
    private void renderCoins() {
    	if(coins.isEmpty()) return;
    	for(int i = 0; i < coins.size(); i++) {coins.get(i).render();}
    }
    
    private void createCoins() {
    	for(int i = 0; i < coinNumber; i++) {
    		coins.get(i).create();
    	}
    }
    
    private void combineCameraCoins() {
    	for(int i = 0; i < coins.size(); i++) {
    		coins.get(i).getBatch().setProjectionMatrix(cam.combined);	
    	}
    }
    
    private void disposeCoins() {
    	if(coins.isEmpty()) return;
    	for(int i = 0; i < coins.size(); i++) {coins.get(i).dispose();}
    }
    
//Handle Actors' Collisions
    private void checkCollisions(Coin c) {
    	float x = player.getPositionX();
    	float y = player.getPositionY();
    	if(c.containPoint(x, y)) {
    		coins.remove(c);
    		player.setScore();
    	}    	
	}


	public boolean checkMapCollision(float x, float y, float tilewidth, float tileheight){
		int iso_x;
		int iso_y;
		x -= player.init_x;
		y -= player.init_y;
		Vector2 v = rotateCoord(x, y);

		iso_x = (int)(v.x / tilewidth);
		iso_y = (int)(v.y / tileheight);
		
		return isCellBlocked(iso_x, iso_y);
	}

	private boolean isCellBlocked(int iso_x, int iso_y) {
		Cell cell = blockedLayer.getCell(iso_x, iso_y);
		boolean blocked = (cell!= null && cell.getTile() != null && cell.getTile().getProperties().containsKey("Blocked"));
		return blocked;
	}
	
	private Vector2 rotateCoord(float x, float y) {
		float tmp_x, tmp_y;
		double len = Math.sqrt(x*x + y*y);
		double alpha = Math.toDegrees(Math.atan(y/x * -1));
		
		Vector2 v = new Vector2();
		tmp_x = (float)(Math.cos(Math.PI*(theta - alpha)/180) * len);
		tmp_y = (float)(Math.cos(Math.PI*(theta + alpha)/180) * len);
		v.x = (float) (tmp_x - len*Math.sin(Math.PI*((theta - alpha)/180)/(Math.tan(Math.PI*2*theta/180))));
		v.y = (float) (tmp_y - len*Math.sin(Math.PI*((theta + alpha)/180)/(Math.tan(Math.PI*2*theta/180))));
	
		return v;
	}


	public Cell checkVillagerMapCollision(float x, float y, float tilewidth, float tileheight){
		int iso_x;
		int iso_y;
		Vector2 v = rotateCoord(x, y);

		iso_x = (int)(v.x / tilewidth);
		iso_y = (int)(v.y / tileheight);

		//Changed this from IsCellBlocked
		return isCellBlockedForVillager(iso_x, iso_y);	
	}

	//Specifically for isCellBlocked
	private Cell isCellBlockedForVillager(int iso_x, int iso_y) {
		Cell cell = blockedLayer.getCell(iso_x, iso_y);
		return cell;
	}


}
