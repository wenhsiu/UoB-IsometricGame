package com.isometricgame.core;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import gameManager.GameManager;
import gameManager.GameState;

import com.isometricgame.core.InventoryItem.*;

public class GameMAIN extends GameState {
	
	private GameManager gm;
	private TiledMap map;
	private IsometricTiledMapRenderer mapRenderer;
	private TiledMapTileLayer blockedLayer; 

	private Player player;
    private Boss boss;
    private ArrayList<Coin> coins;
	private int coinNumber = 3;
    
	public GameMAIN(GameManager gm) {
		super();	
		this.gm = gm;
		map = new TmxMapLoader().load("test_map.tmx");
		mapRenderer = new IsometricTiledMapRenderer(map);
		mapRenderer.setView(cam);

		//Block represents the "blocked" layer. 
		//Later put the TiledMapTileLayers into an array. 
		blockedLayer = (TiledMapTileLayer) map.getLayers().get("Block"); 

		// TODO: Check if initial start position is blocked or not. 
		
		boss = new Boss(800, 800);
		initCoins();
		
		boss.create();
		player = gm.getPlayer();
		createCoins();
	}
       

    @Override
	public void render (float delta) {
    	
    	Cell cell = checkMapCollision(player.getPositionX(), player.getPositionY(), 
				 blockedLayer.getTileWidth(), blockedLayer.getTileHeight());
    	
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
	 	
	 	//collect coin
		for(int i = 0; i < coins.size(); i++) {checkCollisions(coins.get(i));}

		//trigger a fight with the boss
		if(boss.isCollision(player.getPositionX(), 
							player.getPositionY())
							&&
							!gm.getGameState("MINIGAME1").getPassState()) {gm.setCurrGameState("MINIGAME1");}
		
		cam.position.set(player.getPositionX(), player.getPositionY(), 0);
		
		if(cell!= null && cell.getTile() != null && cell.getTile().getProperties().containsKey("Blocked")){		
			player.setSpeedFactor(-100);
		}

		mapRenderer.setView(cam); 
	 	mapRenderer.render();
	 	
//	 	player.getBatch().setProjectionMatrix(cam.combined); dig deep
	 	
		cam.update();
		
		renderCoins();
	 	player.render();
	 	boss.render();
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
	}
	
//Handle coin array
	private void initCoins() {
    	coins = new ArrayList<Coin>();
    	for(int i = 0; i < coinNumber; i++) {
    		Coin c = new Coin(MathUtils.random(200, Gdx.graphics.getWidth()-200), 
    				MathUtils.random(200, Gdx.graphics.getHeight()-500), ItemTypeID.NONE);
    		
    		c.setBound(100, 25, 100, 100);
    		coins.add(c);
    	}
    }
    
    private void renderCoins() {
    	if(coins.isEmpty()) return;
    	for(int i = 0; i < coins.size(); i++) {coins.get(i).render();}
    }
    
    private void createCoins() {
    	for(int i = 0; i < coinNumber; i++) {coins.get(i).create();}
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


	public Cell checkMapCollision(float x, float y, float tilewidth, float tileheight){
		int iso_x;
		int iso_y;
		
		x -= (gm.init_x + player.getSizeX()/2);
		y -= (gm.init_y + player.getSizeY()/2);
	
		//differ from Henry's machine. Need two times movement to map to tile map properly
		x = x*2;
		y = y*2;
			
		x /= tilewidth;
		y = (y - tileheight) / tileheight + x;
		x -= y - x;

		iso_x = (int) x;
		iso_y = (int) y;		

		return isCellBlocked(iso_x, iso_y);		
	}

	private Cell isCellBlocked(int iso_x, int iso_y) {
		Cell cell = blockedLayer.getCell(iso_x, iso_y);
		return cell;
	}
}
