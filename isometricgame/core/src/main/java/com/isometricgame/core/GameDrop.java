package com.isometricgame.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;

import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

//Example text import - Jack
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GameDrop extends GameState {
	private GameManager gm;
	// load the drop sound effect and the rain background "music"
	private Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
	private Music rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

	private SpriteBatch batch;
	private Rectangle bucket;
	private Array<Rectangle> raindrops0;
	private Array<Rectangle> raindrops1;
	private long lastDropTime;
	private BitmapFont scoreFont;
	private String target;
	private int num;
	private String myDropScore;
	private Player player;
	private int right = 0;
	private int wrong = 0;
	private boolean change = true;
	private boolean medal;
	private boolean start = false;

	// Background images.
	public Texture background = new Texture("window_0011_Vector-Smart-Object.png");
	// drops
	private Texture dropImage0 = new Texture("drop0.png");
	private Texture dropImage1 = new Texture("drop1.png");
	private Texture bucketImage = new Texture("yellowbucket.png");
	private Texture failure = new Texture("failure_image.jpeg");
	// intro
	private Texture intro = new Texture("drop_intro2.png");

	public static List<Integer> Score = new ArrayList<Integer>();

	public GameDrop(GameManager gm) {
		super();
		this.gm = gm;
		player = gm.getPlayer();

		// Set player score = 0

		medal = true;
		myDropScore = "Binary collected";
		target = "Target  " + RandNum(0, 15);

		// Create font to be used for counter.
		scoreFont = new BitmapFont();
		scoreFont.setColor(25 / 255f, 35 / 255f, 76 / 255f, 1f);
		scoreFont.setScale(2);

		// start the playback of the background music immediately
		rainMusic.setLooping(true);

		// create the SpriteBatch
		batch = new SpriteBatch();

		// create a Rectangle to logically represent the bucket
		bucket = new Rectangle();
		// center the bucket horizontally
		bucket.x = 1200 / 2 - 128 / 2;
		// bottom left corner of the bucket is 20 pixels above the bottom screen edge
		bucket.y = 20;
		bucket.width = 50;
		bucket.height = 128;

		// create the raindrops array and spawn the first raindrop
		raindrops0 = new Array<Rectangle>();
		raindrops1 = new Array<Rectangle>();
		spawnRaindrop();
	}

	private int RandNum(double min, double max) {
		double n;
		n = (Math.random() * ((max - min) + 1)) + min;
		num = (int) n;
		return num;
	}

	private void spawnRaindrop() {
		Rectangle raindrop0 = new Rectangle();
		Rectangle raindrop1 = new Rectangle();

		raindrop0.x = MathUtils.random(0, 1200 - 128);
		raindrop1.x = MathUtils.random(0, 1200 - 128);

		while (raindrop1.x - 100 < raindrop0.x && raindrop0.x < raindrop1.x + 100) {
			raindrop0.x = MathUtils.random(0, 1200 - 128);
			raindrop1.x = MathUtils.random(0, 1200 - 128);
		}

		raindrop0.y = 400;
		raindrop0.width = 128;
		raindrop0.height = 128;
		raindrops0.add(raindrop0);

		raindrop1.y = 450;
		raindrop1.width = 128;
		raindrop1.height = 128;
		raindrops1.add(raindrop1);

		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render(float delta) {
	   
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) { 
			start = true;
		}
		
		
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		super.render(delta);

		// begin a new batch and draw the bucket and
		// all drops

		if(right != 3 && wrong !=3) {
			batch.begin();
			batch.draw(background, 0, 0, 1200, 750);
			batch.draw(intro, 30, 500, 1100, 250);

			batch.draw(bucketImage, bucket.x, bucket.y);

			scoreFont.draw(batch, myDropScore, 200, 500);
			scoreFont.draw(batch, target, 0, 500);

			scoreFont.draw(batch, "number correct  " + String.valueOf(right), 650, 500);
			scoreFont.draw(batch, "number incorrect  " + String.valueOf(wrong), 950, 500);
            batch.end();

			if (start) {
			batch.begin();
			for (Rectangle raindrop0 : raindrops0) {
				batch.draw(dropImage0, raindrop0.x, raindrop0.y);
			}

			for (Rectangle raindrop1 : raindrops1) {
				batch.draw(dropImage1, raindrop1.x, raindrop1.y);
			}
			batch.end();
			}
			
		} else if (right == 3) {
			gm.inventoryAddMedals();
			passed = true;
			gm.setCurrGameState("MAINGAME");
		} else if (wrong == 3) {
			dropSound.stop();
			rainMusic.stop(); 
			batch.begin();
            batch.draw(failure, 0, 0, 1200, 750);
            batch.end();
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) { 
                gm.setCurrGameState("MAINGAME");
            }
		}

		// Process user input
		
		if(start) {
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			cam.unproject(touchPos);
			bucket.x = touchPos.x - 128 / 2;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT) && start)
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT) && start)
			bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// Ensure the bucket stays within the screen bounds
		if (bucket.x < 0) {
			bucket.x = 0;
		}

		if (bucket.x > 1200 - 128) {
			bucket.x = 1200 - 128;
		}

		// Check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastDropTime > 100000000 * 100000) {
			spawnRaindrop();
		}

		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the latter case we play back
		// a sound effect as well.
		for (Iterator<Rectangle> iter = raindrops0.iterator(); iter.hasNext();) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y /*+ 128*/ < 0) {
				iter.remove();
			}

			if (raindrop.overlaps(bucket)) {
				Score.add(0);
				myDropScore = "Binary Collected: " + CurrentScore();
				dropSound.play();
				iter.remove();
			}
		}

		for (Iterator<Rectangle> iter = raindrops1.iterator(); iter.hasNext();) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 128 < 0) {
				iter.remove();
			}

			if (raindrop.overlaps(bucket)) {
				Score.add(1);
				myDropScore = "Binary Collected: " + CurrentScore();
				dropSound.play();
				iter.remove();
			}
		}
		}
       
		if (Score.size() == 4) {
			//rainMusic.pause();
			if (CheckScore()) {
				right++;
			} else {
				wrong++;
			}

			Score.clear();
			target = "target  " + RandNum(0, 15);
			change = true;						
		}
		
		change = false;
	  
	}
    
    // Checks to see if user input matches the target
	public boolean CheckScore() {
		double Total = 0;

		if(Score.get(0) == 1) {
			Total += 8;
		}

		if(Score.get(1) == 1) {
			Total += 4;
		}

		if(Score.get(2) == 1) {
			Total += 2;
		}

		if(Score.get(3) == 1) {
			Total += 1;
		}

		if(Total == num) {
			passed = true;
			return true;
		}
		
		passed = false;
		return false;
	}
	
	public static String CurrentScore(){
		String s = " ";
		for(int i =0; i < Score.size(); i++) {
			s += Score.get(i);
		}
		return s;
	}

	@Override
	public void show() {		
		rainMusic.play();		
	}


	@Override
	public void hide() {  
		rainMusic.pause();
	}

	@Override
	public void pause () {
		rainMusic.pause();
	}

	@Override
	public void resume () {
		rainMusic.play();
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		dropImage1.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
		scoreFont.dispose();
		background.dispose();
		// generator.dispose();
	}

	public boolean getMedal() {
		return medal;
	}

	public void setMedal(boolean medal) {
		this.medal = medal;
	}
}