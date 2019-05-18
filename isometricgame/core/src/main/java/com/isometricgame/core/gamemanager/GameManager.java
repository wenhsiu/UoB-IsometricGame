package com.isometricgame.core.gamemanager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.isometricgame.core.ui.InventoryItem;
import com.isometricgame.core.ui.InventoryItemFactory;
import com.isometricgame.core.ui.InventoryUI;
import com.isometricgame.core.ui.InventoryItem.ItemTypeID;
import com.isometricgame.core.GameEND;
import com.isometricgame.core.GameMAIN;
import com.isometricgame.core.GameGUIDE;
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
//			"GAMELEVEL1",
//			"START2",
//			"GAMELEVEL2",
//			"START3",
//			"GAMELEVEL3",
//			"COMPLETE",
	};
	private GameState currentState;
	private Game game;
	private Player player;
	
	public GameManager(Game game) {		
		this.game = game;
		
		gameStates = new HashMap<String, GameState>();
		currentState = gameStates.get("MAINGAME");
		
		player = new Player(200, -50);
		player.create();
		initAllState();
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

	//Pass in the game inventory. 
	public void checkPassedState(InventoryUI currentInventory){
		for(String key: gameStates.keySet()){
			//System.out.println("Key == " + key);
			//System.out.println("Passed? == " + gameStates.get(key).passed);

			if(gameStates.get(key).passed == true){
				/* System.out.println("HELLO WORLD"); 
				System.out.println("HELLO WORLD"); 
				System.out.println("HELLO WORLD"); 
				System.out.println("HELLO WORLD"); 
				System.out.println("HELLO WORLD"); 
				System.out.println("HELLO WORLD"); 
				System.out.println("HELLO WORLD"); 
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
		
		//Added new gameState or replaced the failed state with a newly-created one
		if(gsName.equals("DROPGAME1")) {
			newGS = new GameDropEasy(this);
		}else if(gsName.equals("DROPGAME2")) {
			newGS = new GameDrop(this);
		}else if(gsName.equals("DRAGGAME1")) {
			newGS = new GameLevel1(this);
		}else if(gsName.equals("DRAGGAME2")) {
			newGS = new GameLevel2(this);
		}else if(gsName.equals("DRAGGAME3")) {
			newGS = new GameLevel3(this);
		}else if(gsName.equals("AVOIDGAME")) {
			newGS = new GameAvoid(this);
		}else if(gsName.equals("FINALGAME")) {
			newGS = new GameMaze(this);
		} else if(gsName.equals("PRESENTATION")) {
			newGS = new Presentation(this);
		};  
		
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
				gameStates.put("GUIDE", new GameGUIDE(this));
			}
			 /*else if(stateName[i].equals("MINIGAME1")) {
				gameStates.put("MINIGAME1", new GameDrop(this));
			} else if(stateName[i].equals("MINIGAME2")){
				gameStates.put("MINIGAME2", new Start1(this));
			} else if(stateName[i].equals("START2")) {
				gameStates.put("START2", new Start2(this));
			} else if(stateName[i].equals("START3")) {
				gameStates.put("START3", new Start3(this));
			} else if(stateName[i].equals("COMPLETE")) {
				gameStates.put("COMPLETE", new Complete(this));
			} else if(stateName[i].equals("GAMELEVEL1")) {
				gameStates.put("GAMELEVEL1", new GameLevel1(this));
			} else if(stateName[i].equals("GAMELEVEL2")) {
				gameStates.put("GAMELEVEL2", new GameLevel2(this));
			} else if(stateName[i].equals("GAMELEVEL3")) {
				gameStates.put("GAMELEVEL3", new GameLevel3(this));
			}*/
		}
	}
}
