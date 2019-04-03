package clickAndDragGameManager;

import java.util.HashMap;

import com.badlogic.gdx.Game;

import clickAndDragMiniGame.Complete;
import clickAndDragMiniGame.GameLevel1;
import clickAndDragMiniGame.GameLevel2;
import clickAndDragMiniGame.GameLevel3;
import clickAndDragMiniGame.Start1;
import clickAndDragMiniGame.Start2;
import clickAndDragMiniGame.Start3;

public class clickAndDragGameManager {
	private HashMap<String, clickAndDragGameState> gameStates;
	private String[] stateName = {
			"START1",
			"GAMELEVEL1",
			"START2",
			"GAMELEVEL2",
			"START3",
			"GAMELEVEL3",
			"COMPLETE",
	};
	private clickAndDragGameState currentState;
	private Game game;
	
	public clickAndDragGameManager(Game game) {		
		this.game = game;
		gameStates = new HashMap<String, clickAndDragGameState>();
		currentState = gameStates.get("START1");
		
		// player = new Player(0, 0);
		// player.create();
		initAllState();
	}
	
	public clickAndDragGameState getGameState() {return currentState;}
	
	public clickAndDragGameState getGameState(String gameName) {return gameStates.get(gameName);}
	
	public void setCurrGameState(String gsName) {
		if(gameStates.get(gsName) != null) {
			currentState = gameStates.get(gsName);
			game.setScreen(currentState);
		}
	}
	
	private void initAllState() {
		String state;
		for(int i = 0; i < stateName.length; i++) {
			// state = stateName[i];
			// switch (state) {
			// 	case "START1":
			// 		gameStates.put("START1", new Start1(this));
			// 		break;
			// 	case "START2":
			// 		gameStates.put("START2", new Start2(this));
			// 		break;
			// 	case "START3":
			// 		gameStates.put("START3", new Start3(this));
			// 		break;
			// 	case "GAMELEVEL1":
			// 		gameStates.put("GAMELEVEL1", new GameLevel1(this));
			// 		break;
			// 	case "GAMELEVEL2":
			// 		gameStates.put("GAMELEVEL2", new GameLevel2(this));
			// 		break;
			// 	case "GAMELEVEL3":
			// 		gameStates.put("GAMELEVEL3", new GameLevel3(this));
			// 		break;
			// 	case "COMPLETE":
			// 		gameStates.put("COMPLETE", new Complete(this));
			// 		break;
			// }
			if(stateName[i].equals("START1")) {
				gameStates.put("START1", new Start1(this));
			} else if(stateName[i].equals("GAMELEVEL1")) {
				gameStates.put("GAMELEVEL1", new GameLevel1(this));
			}  else if(stateName[i].equals("START2")) {
				gameStates.put("START2", new Start2(this));
			} else if(stateName[i].equals("START3")) {
				gameStates.put("START3", new Start3(this));
			} else if(stateName[i].equals("COMPLETE")) {
				gameStates.put("COMPLETE", new Complete(this));
			}else if(stateName[i].equals("GAMELEVEL2")) {
				gameStates.put("GAMELEVEL2", new GameLevel2(this));
			} else if(stateName[i].equals("GAMELEVEL3")) {
				gameStates.put("GAMELEVEL3", new GameLevel3(this));
			}
		}
	}
}
