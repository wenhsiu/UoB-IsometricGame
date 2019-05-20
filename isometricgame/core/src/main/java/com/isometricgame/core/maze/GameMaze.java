
package com.isometricgame.core.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input;
import com.isometricgame.core.ui.InventoryUI;


import java.util.ArrayList; 
import java.util.List;


import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;

public class GameMaze extends GameState {

	private GameManager gm; 

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMapTileLayer collisionLayer;

	private Character player; 

	private SpriteBatch batch;

	private Texture digit0 = new Texture("0 (1).png"); 
	private Texture digit1 = new Texture("1 (1).png");
	private Texture noexit = new Texture("entry_blue.png");
	private Texture exit = new Texture("entry_yellow.png");
	private Texture failure = new Texture("failure_image.jpeg");


	private BitmapFont scoreFont; 
	private String myScore; 
	private String target; 

	private int[] options = new int[]{3,5,6,9,10,12};


	private int cnt1 = 0, cnt2 = 0, cnt3 = 0, cnt4 = 0;
	
	private List<Integer> score = new ArrayList<Integer>();
	private int num;

	private BitmapFont timer; 
	private long startTime = 0;
	private int timeReamining = 0;


	public GameMaze(GameManager gm){
		super();
		this.gm = gm;

		map = new TmxMapLoader().load("./maze.tmx");
		//renderer = new IsometricTiledMapRenderer(map);
		renderer = new OrthogonalTiledMapRenderer(map);
		renderer.setView(cam);

		startTime = System.currentTimeMillis();
  
		timer = new BitmapFont(); 
		timer.setColor(Color.GREEN);
		timer.setScale(5,5);

		myScore = "COLLECTED"; 
		num = options[RandNum()];
		target = "TARGET  " + num;

		scoreFont = new BitmapFont(); 
		scoreFont.setColor(Color.GREEN);
		scoreFont.setScale(2);

		batch = new SpriteBatch();

		player = new Character(new Sprite(new Texture("reddot.png")), (TiledMapTileLayer) map.getLayers().get("Walls"));
		 //maybe change cordinates as they may start from diff origin
		player.setPosition(300, 60);
		player.setScale((float)0.3);
	}

	@Override
	public void resize (int width, int height) {
		cam.viewportHeight = height;
		cam.viewportWidth = width;
		cam.zoom += 2;
		cam.translate(400, 600);
		cam.update();
		//super.resize(width, height);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 13);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		long elapsedTime = System.currentTimeMillis() - startTime;
		setTimeRemaining(elapsedTime);

		renderer.setView(cam);
			
		renderer.render();

		if(score.size() != 4 && timeReamining > 0) {
			batch.begin();

			timer.draw(batch,String.valueOf(timeReamining), 1000 , 700);
			player.draw(batch);

			checkDigitsHit();

			if(cnt1 == 0){
				batch.draw(digit1, 309, 93, 22, 22);
			}
			if(cnt2 == 0){
				batch.draw(digit0, 309, 487, 22, 22);
			}
			if(cnt3 == 0){
				batch.draw(digit0, 544, 551, 22, 22);
			}
			if(cnt4 == 0){
				batch.draw(digit1, 885, 381, 22, 22);
			}

			if(score.size() == 4 && checkScore()){
				batch.draw(exit, 890, 650, 50, 50);
			}
			else{
				batch.draw(noexit, 890, 650, 50, 50);
			}

			scoreFont.draw(batch, myScore, 0, 700); 
			scoreFont.draw(batch, target, 0, 500);

			batch.end();
		} else if(score.size() == 4) {
			if(checkScore()){
				//set the first game state passed to true so that the trigger point can detect correctly
				gm.inventoryAddMedals();
				passed = true;
				gm.setCurrGameState("MAINGAME");
			} else {
				batch.begin();
	            batch.draw(failure, 0, 0, 1200, 750);
	            batch.end();
	            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) { 
	                gm.setCurrGameState("MAINGAME");
	            }
			}
		} else if(timeReamining <= 0){
			batch.begin();
            batch.draw(failure, 0, 0, 1200, 750);
            batch.end();
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) { 
                gm.setCurrGameState("MAINGAME");
            }
		}

		//System.out.println(player.getX());
		//System.out.println(player.getY());

		System.out.println(myScore);
		if(score.size() > 0){
			System.out.println(score.get(0));
		}

		cam.update();
	}

	@Override
	public void show() {
		super.show();
		renderer.setView(cam);
	}

	@Override
	public void hide() {  
		dispose();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		player.getTexture().dispose();
	}

	private void setTimeRemaining(long elaspedTime){
		int time = gm.getMazeTime();
		timeReamining = time - (int)elaspedTime / 1000;
	}

	private int RandNum(){
		double n;
		n = Math.random()*6;
		return (int)n;
	}

	private String collectedDigits(){
		String s = "";
		for(int i = 0; i < score.size(); i++){
			s += score.get(i);
		}
		return s;
	}

	public void checkDigitsHit(){
		firstDigitHit();
		secondDigitHit();
		thirdDigitHit();
		fourthDigitHit();
	}

	public void firstDigitHit(){
		if((player.getX() >= 309 - 22) && (player.getX() < 309)){
			if((player.getY() >= 93 - 22) && (player.getY() < 93)){
				if(cnt1 == 0){
					score.add(1);
					myScore = "COLLECTED " + collectedDigits();
				}
				cnt1 = 1;
			}
		}
	}

	public void secondDigitHit(){
		if((player.getX() >= 309 - 22) && (player.getX() < 309)){
			if((player.getY() >= 487 - 22) && (player.getY() < 487)){
				if(cnt2 == 0){
					score.add(0);
					myScore = "COLLECTED " + collectedDigits();
				}
				cnt2 = 1;
			}
		}
	}

	public void thirdDigitHit(){
		if((player.getX() >= 544 - 22) && (player.getX() < 544)){
			if((player.getY() >= 551 - 22) && (player.getY() < 551)){
				if(cnt3 == 0){
					score.add(0);
					myScore = "COLLECTED " + collectedDigits();
				}
				cnt3 = 1;
			}
		}
	}

	public void fourthDigitHit(){
		if((player.getX() >= 885 - 22) && (player.getX() < 885)){
			if((player.getY() >= 381 - 22) && (player.getY() < 381)){
				if(cnt4 == 0){
					score.add(1);
					myScore = "COLLECTED " + collectedDigits();
				}
				cnt4 = 1;
			}
		}
	}

	public boolean checkScore() {
		double Total = 0;

		if(score.get(0) == 1) {
			Total += 8;
		}

		if(score.get(1) == 1) {
			Total += 4;
		}

		if(score.get(2) == 1) {
			Total += 2;
		}

		if(score.get(3) == 1) {
			Total += 1;
		}

		if(Total == num) {
			return true;
		}

		return false;
	}
	
	public String Currentscore(){
		String s = " ";
		for(int i =0; i < score.size(); i++) {
			s += score.get(i);
		}
		return s;
	}
}

