package clickAndDragGameManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

public abstract class clickAndDragGameState implements Screen {

	public final OrthographicCamera cam;
	private int width;
	private int height;
	private boolean passed;

	public clickAndDragGameState() {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(width, height);
		cam.translate(width/2, height/2);
		cam.update();
		passed = false;
	}
	
	public boolean getPassState() {return passed;}
	
	public void setPassState(boolean pass) {passed = pass;}
	
	@Override
	public void render(float delta) {
		
	}

	@Override
	public void resize(int width, int height) {
		cam.update();		
	}

	@Override
	public void show() {
		cam.update();
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
