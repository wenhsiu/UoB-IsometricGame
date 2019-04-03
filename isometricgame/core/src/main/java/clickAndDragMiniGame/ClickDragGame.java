package clickAndDragMiniGame;

import com.badlogic.gdx.Game;

import clickAndDragGameManager.clickAndDragGameManager;
import gameManager.GameManager;



public class ClickDragGame extends Game {
	private clickAndDragGameManager gm;

	@Override
	public void create () {
		gm = new clickAndDragGameManager(this);
		gm.setCurrGameState("START1");
	}

	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void render () {
		super.render();
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
		super.dispose();
	}
}
