package gameManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.isometricgame.core.PlayerHUD;
import com.isometricgame.core.Player;

public abstract class GameState implements Screen {	

	public final OrthographicCamera cam;
	public final OrthographicCamera hudcam;
	private int width;
	private int height;
	private boolean passed;

	private InputMultiplexer multiplexer;
	private PlayerHUD playerHUD;
	private Player player;
	
	public GameState() {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(width, height);
		cam.translate(width/2, height/2);
		cam.update();
		passed = false;

		player = new Player(0,0);
		hudcam = new OrthographicCamera();
		hudcam.setToOrtho(false);

		playerHUD = new PlayerHUD(hudcam, player);

		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(playerHUD.getStage());
		Gdx.input.setInputProcessor(multiplexer);
	}
	
	public boolean getPassState() {return passed;}
	
	public void setPassState(boolean pass) {passed = pass;}
	
	@Override
	public void render(float delta) {
		
	}

	@Override
	public void resize(int width, int height) {
		cam.update();
		hudcam.update();		
	}

	@Override
	public void show() {
		cam.update();
		hudcam.update();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {		
	}

	
}
