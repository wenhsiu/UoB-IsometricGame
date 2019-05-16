package com.isometricgame.core.raindrop;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;

public class GameDrop extends GameState{
	
	private GameManager gm;
	
	private int windowW;
	private int windowH;
	private final int lineW = 300;
	private final int lineH = 75;
	
	private Texture bkgTexture;
	private Cloud cloud0;
	private Cloud cloud1;
	private float cloudW = 256*1f;
	private float cloudH = 256*1f;
	private float dogW = 585*0.2f;
	private float dogH = 670*0.2f;
	
	private ArrayList<RainDrop> drop0;
	private ArrayList<RainDrop> drop1;
	//Use rectangles to detect if rain drop and characters are overlap.
	private Rectangle rectDrop0;
	private Rectangle rectDrop1;
	private Rectangle rectdog;
	
	private SpriteBatch batch;
	
	private Dog dog;
	private ArrayList<Integer> collectedScore;
	
	private int target;
	
	private BitmapFont scoreFont;
	private String info;
	private String score;

	public GameDrop(GameManager gm) {
		this.gm = gm;
		
		windowW = Gdx.graphics.getWidth();
		windowH = Gdx.graphics.getHeight();
		
		//init background
		bkgTexture = new Texture(Gdx.files.internal("background_pink.png"));
		//init clouds
		cloud0 = new Cloud(windowW/4 - cloudW/2, windowH*3/4 - cloudH/2);
		cloud0.create();
		cloud1 = new Cloud(windowW*3/4 - cloudW/2, windowH*3/4 - cloudH/2);
		cloud1.create();
		//init raindrops
		drop0 = new ArrayList<RainDrop>();
		drop1 = new ArrayList<RainDrop>();		
		rectDrop0 = new Rectangle();
		rectDrop1 = new Rectangle();
		
		batch = new SpriteBatch();
		
		dog = new Dog(windowW/2-dogW/2, 25);
		dog.create();
		rectdog = new Rectangle();
		rectdog.x = windowW/2-dogW/2;
		rectdog.y = 20;
		rectdog.width = dogW;
		rectdog.height = dogH;
		
		collectedScore = new ArrayList<Integer>();
		
		target = MathUtils.random(0, 15);
		info = "TARGET: " + Integer.toString(target);
		score = "COLLECTED: ";
		
		//set Font
		scoreFont = new BitmapFont();
		scoreFont.setColor(25 / 255f, 35 / 255f, 76 / 255f, 1f);
		scoreFont.setScale(2);
	}
	
	private void generateRainDrop() {
		if(drop0.size() == 0) {
			float x0 = MathUtils.random(windowW/4-cloudW/2, windowW/4+cloudW/2);
			float y0 = windowH*3/4 - cloudH/2;			
			RainDrop d0 = new RainDrop(x0, y0, 0);
			rectDrop0.x = x0;
			rectDrop0.y = y0;
			rectDrop0.height = d0.getSizeY();
			rectDrop0.width = d0.getSizeX();
			
			if(d0 != null) {drop0.add(d0);}
		}
		
		if(drop1.size() == 0) {
			float x1 = MathUtils.random(windowW*3/4-cloudW/2, windowW*3/4+cloudW/2);
			float y1 = windowH*3/4 - cloudH/2;
			RainDrop d1 = new RainDrop(x1, y1, 1);
			rectDrop1.x = x1;
			rectDrop1.y = y1;
			rectDrop1.height = d1.getSizeY();
			rectDrop1.width = d1.getSizeX();
			if(d1 != null) {drop1.add(d1);}
		}
	}
	
	private void removeRainDrop() {
		if(getRainDrop() == null) {
			//check if the raindrop touches the floor, remove it if yes.
			if(drop0.size() != 0 && rectDrop0.y <= 100) {
				RainDrop d0 = drop0.remove(0);
				d0.dispose();
			} 
			if(drop1.size() != 0 && rectDrop1.y <= 100) {
				RainDrop d1 = drop1.remove(0);
				d1.dispose();
			}
		}
	}
	
	private void updateRainDropPosY() {
		if(drop0.size() != 0) {
			rectDrop0.y = drop0.get(0).getPositionY();
		}
		if(drop1.size() != 0) {
			rectDrop1.y = drop1.get(0).getPositionY();
		}
	}
	
	private void updatedogPosX() {
		rectdog.x = dog.getPositionX();
	}
	
	private void renderRainDrop() {
		for(RainDrop d0 : drop0) {
			d0.render();
		}
		for(RainDrop d1 : drop1) {
			d1.render();
		}
	}
	
	private void renderCloud() {
		cloud0.render();
		cloud1.render();
	}
	
	private RainDrop getRainDrop() {
		if(rectDrop0.overlaps(rectdog)) {
			collectedScore.add(0);
			score += Integer.toString(0);
			return drop0.remove(0);
		}else if(rectDrop1.overlaps(rectdog)) {
			collectedScore.add(1);
			score += Integer.toString(1);
			return drop1.remove(0);
		}
		return null;
	}
	
	private boolean isCompleted() {
		return (collectedScore.size() == 4);
	}
	
	private int checkScore() {
		int score = 0;
		for(Integer i : collectedScore) {
			score = score*2 + i;
		}		
		return score;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(192/255f, 192/255f, 192/255f, 0xff/255.0f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		generateRainDrop();
		removeRainDrop();
		updateRainDropPosY();
		updatedogPosX();
		
		//render background
		batch.begin();		
		batch.draw(bkgTexture, 0, 0, 1200, 2133);
		scoreFont.draw(batch, info, windowW/2-lineW/2, windowH*3/4);
		scoreFont.draw(batch, score, windowW/2-lineW/2, windowH*3/4 + lineH);
		scoreFont.draw(batch, score, windowW/2 - lineW/2, windowH*3/4 + lineH);
		batch.end();
		
		renderCloud();
		renderRainDrop();
		dog.render();
		
		getRainDrop();
		if(isCompleted()) {
			passed = (checkScore() == target);
			//TODO: show fail/pass image.
			gm.setCurrGameState("MAINGAME");
		}
	}

	@Override
	public void resize(int width, int height) {		
	}

	@Override
	public void show() {
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
		bkgTexture.dispose();
		scoreFont.dispose();
	}

}
