package gameManager;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.isometricgame.core.GameDrop;
import com.isometricgame.core.GameEND;
import com.isometricgame.core.GameMAIN;
import com.isometricgame.core.Player;


public class GameManager {
	private HashMap<String, GameState> gameStates;
	private String[] stateName = {
//			"MENU",
			"MAINGAME",
			"MINIGAME1",
//			"MINIGAME2",
			"END"
	};
	private GameState currentState;
	private Game game;
	private Player player;
	
	public GameManager(Game game) {		
		this.game = game;
		gameStates = new HashMap<String, GameState>();
		currentState = gameStates.get("MAINGAME");
		
		player = new Player(0, 0);
		player.create();
		initAllState();
	}
	
	public GameState getGameState() {return currentState;}
	
	public GameState getGameState(String gameName) {return gameStates.get(gameName);}
	
	public void setCurrGameState(String gsName) {
		if(gameStates.get(gsName) != null) {
			currentState = gameStates.get(gsName);
			game.setScreen(currentState);
		}
	}

	
	public Player getPlayer() {return player;}
	
	private void initAllState() {
		for(int i = 0; i < stateName.length; i++) {
			if(stateName[i].equals("MAINGAME")) {
				gameStates.put("MAINGAME", new GameMAIN(this));
			}else if(stateName[i].equals("END")) {
				gameStates.put("END", new GameEND(this));
			}else if(stateName[i].equals("MINIGAME1")) {
				gameStates.put("MINIGAME1", new GameDrop(this));
			}/* else if(stateName[i].equals("MINIGAME2")) {
				gameStates.put("MINIGAME2", new clickAndDragGameManager());
			} */ 
		}
	}
}
