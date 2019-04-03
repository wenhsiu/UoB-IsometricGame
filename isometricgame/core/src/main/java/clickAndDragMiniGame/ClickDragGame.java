package clickAndDragMiniGame;

import com.badlogic.gdx.Game;

import gameManager.GameManager;



public class ClickDragGame extends Game {
	private GameManager gm;

	@Override
	public void create () {
		gm = new GameManager(this);
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
