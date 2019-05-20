package com.isometricgame.core.gamemanager;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.isometricgame.core.ui.InventoryItem;
import com.isometricgame.core.ui.InventoryItemFactory;
import com.isometricgame.core.ui.InventoryUI;
import com.isometricgame.core.ui.PlayerHUD;
import com.isometricgame.core.ui.InventoryItem.ItemTypeID;
import com.isometricgame.core.GameEND;
import com.isometricgame.core.GameGuide;
import com.isometricgame.core.GameMAIN;
import com.isometricgame.core.Player;
import com.isometricgame.core.Presentation;
import com.isometricgame.core.mainmenu.MainMenuScreen;
import com.isometricgame.core.raindrop.GameDropEasy;
import com.isometricgame.core.clickdrag.GameLevel1;
import com.isometricgame.core.clickdrag.GameLevel2;
import com.isometricgame.core.clickdrag.GameLevel3;
import com.isometricgame.core.GameAvoid;
import com.isometricgame.core.GameDrop;
import com.isometricgame.core.maze.GameMaze;

public class GameManager {
	private HashMap<String, GameState> gameStates;
	private String[] stateName = {
			"MENU",
			"MAINGAME",
			"END",
			"PRESENTATION",
			"GUIDE",
	};
	private GameState currentState;
	private Game game;
	private Player player;

	public OrthographicCamera hudcam;
	private PlayerHUD playerHUD;
	private InventoryUI inventoryUI;
	private InventoryItemFactory factory;
	
	public GameManager(Game game) {		
		this.game = game;
		
		gameStates = new HashMap<String, GameState>();
		currentState = gameStates.get("MAINGAME");
		
		player = new Player(200, -50);
		player.create();
		
		hudcam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hudcam.translate(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		hudcam.setToOrtho(true);		
		playerHUD = new PlayerHUD(hudcam);
		inventoryUI = playerHUD.getInventoryUI();
		factory = InventoryItemFactory.getInstance();		
		
		initAllState();
	}
	
	public void inventoryAddCoin() {
		InventoryItem item = factory.getInventoryItem(ItemTypeID.COIN);			
		inventoryUI.addItemToInventory(item, "COIN");
	}
	
	public void inventoryAddMedals() {
		InventoryItem item = factory.getInventoryItem(ItemTypeID.MEDAL);			
		inventoryUI.addItemToInventory(item, "MEDAL");
	}
	
	public void renderInventory(float delta) {
		playerHUD.render(delta);
		hudcam.update();
	}
	
	public void inventoryRemoveOneCoin() {
		inventoryUI.removeBulkItems(ItemTypeID.COIN, 1);
	}
	
	public int inventoryGetItemNumber(ItemTypeID typeID) {
		return inventoryUI.getItemNumber(typeID);
	}

	public int getNumCoins() {
		return inventoryUI.getItemNumber(ItemTypeID.COIN);
	}

	public int getNumMedals() {
		return inventoryUI.getItemNumber(ItemTypeID.MEDAL);
	}
	
	public String[] getAllGameNames() {
		return stateName;
	}
	
	public GameState getGameState() {
		return currentState;
	}
	
	public GameState getGameState(String gameName) {
		return gameStates.get(gameName);
	}
	
	public void setCurrGameState(String gsName) {
		if(gameStates.get(gsName) != null) {
			currentState = gameStates.get(gsName);
			game.setScreen(currentState);
		}
	}

	// Add a medal to the inventory if the pass condition is true
	public void checkPassedState(InventoryUI currentInventory){
		// TODO: LibGDX doesn't like the use of iterators, doesn't always compile
		for(String key: gameStates.keySet()){
			if(gameStates.get(key).passed == true){
				InventoryItemFactory factory = InventoryItemFactory.getInstance();
				InventoryItem item = factory.getInventoryItem(ItemTypeID.MEDAL);	
				currentInventory.addItemToInventory(item, "MEDAL");
			}
		}
	}

	public Player getPlayer() {
		return player;
	}
	
	public void newGameStateByName(String gsName) {
		GameState newGS = null;
		
		// Add new GameState or replace the failed state with a newly-created one
		if(gsName.contains("DROPGAME1")) {
			newGS = new GameDropEasy(this);
		}else if(gsName.contains("DROPGAME2")) {
			newGS = new GameDrop(this);
		}else if(gsName.contains("DRAGGAME1")) {
			newGS = new GameLevel1(this);
		}else if(gsName.contains("DRAGGAME2")) {
			newGS = new GameLevel2(this);
		}else if(gsName.contains("DRAGGAME3")) {
			newGS = new GameLevel3(this);
		}else if(gsName.contains("AVOIDGAME")) {
			newGS = new GameAvoid(this);
		}else if(gsName.contains("FINALGAME")) {
			newGS = new GameMaze(this);
		} /* else if(gsName.contains("PRESENTATION")) {//why new Presentation here?
			newGS = new Presentation(this);
		} */;  
		
		if(newGS != null) {
			gameStates.put(gsName, newGS);
		}
	}

	private void initAllState() {
		for(int i = 0; i < stateName.length; i++) {
			if(stateName[i].equals("MENU")) {
				gameStates.put("MENU", new MainMenuScreen(this));
			} else if(stateName[i].equals("MAINGAME")) {
				gameStates.put("MAINGAME", new GameMAIN(this));
			} else if(stateName[i].equals("END")) {
				gameStates.put("END", new GameEND(this));
			} else if(stateName[i].equals("PRESENTATION")) {
				gameStates.put("PRESENTATION", new Presentation(this));
			} else if(stateName[i].equals("GUIDE")) {
				gameStates.put("GUIDE", new GameGuide(this));
			}
		}
	}

	public int getMazeTime() {
		return inventoryUI.getInventoryTime();
	}

	public InventoryUI getInventoryUI() {
		return inventoryUI;
	}

	public void setInventoryUI(InventoryUI inventoryUI) {
		this.inventoryUI = inventoryUI;
	}

	
}
