package com.isometricgame.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;


import java.util.Iterator;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import gameManager.GameManager;
import gameManager.GameState;

import java.util.List;
import java.util.ArrayList; 


public class GameDrop extends GameState {

   private GameManager gm; 

   private Texture dropImage0;
   private Texture dropImage1;
   private Texture bucketImage;
   private Sound dropSound;
   private Music rainMusic;
	 private SpriteBatch batch;
	 private Sprite background; 
   private Rectangle bucket;
   private Array<Rectangle> raindrops0;
   private Array<Rectangle> raindrops1;
   private long lastDropTime;
   private BitmapFont scoreFont; 
   private String target; 
   private int num;
	 private String myDropScore; 
	 
	 //Background images. 
	 public static Texture backgroundTexture;
	 public static Sprite backgroundSprite;


   private List<Integer> Score = new ArrayList<Integer>();

   public GameDrop(GameManager gm){
	  super();
      this.gm = gm;

      // Set player score = 0
	  
	  myDropScore = "Binary collected"; 
	  target = "target  " + RandNum(1,16);

	  // Create font to be used for counter. 
	  scoreFont = new BitmapFont(); 
		scoreFont.setColor(Color.MAROON);
		scoreFont.setScale(3);
		
		backgroundTexture = new Texture("background_0001_Vector-Smart-Object.png");
     
    // load the images for the droplet and the bucket, 64x64 pixels each
	  
	  dropImage0 = new Texture(Gdx.files.internal("0 (1).png")); 
	  dropImage1 = new Texture(Gdx.files.internal("1 (1).png"));
	  bucketImage = new Texture(Gdx.files.internal("bucket.png"));
      
      // load the drop sound effect and the rain background "music"
      dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
      rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
      
      // start the playback of the background music immediately
      rainMusic.setLooping(true);
      
      // create the SpriteBatch
      batch = new SpriteBatch();
      
      // create a Rectangle to logically represent the bucket
      bucket = new Rectangle();
      bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
      bucket.y = 20; // bottom left corner of the bucket is 20 pixels above the bottom screen edge
      bucket.width = 64;
      bucket.height = 64;
      
      // create the raindrops array and spawn the first raindrop
	  raindrops0 = new Array<Rectangle>();
	  raindrops1 = new Array<Rectangle>();
      spawnRaindrop();
   }

   
 private int RandNum(double min, double max){
	double n;
	n = (Math.random()*((max-min)+1))+min;
	num = (int)n;
	return num;
}
   
 
private void spawnRaindrop() {
	Rectangle raindrop0 = new Rectangle();
	Rectangle raindrop1 = new Rectangle();
	
	raindrop0.x = MathUtils.random(0, 800-64);
	raindrop0.y = 480;
	raindrop0.width = 64;
	raindrop0.height = 64;
	raindrops0.add(raindrop0);

	raindrop1.x = MathUtils.random(0, 800-64);
	raindrop1.y = 480;
	raindrop1.width = 64;
	raindrop1.height = 64;
	raindrops1.add(raindrop1);

	lastDropTime = TimeUtils.nanoTime();
 }

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void render(float delta) {
	   // clear the screen with a dark blue color. The
	   // arguments to glClearColor are the red, green
	   // blue and alpha component in the range [0,1]
	   // of the color to be used to clear the screen.
		 
		 Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		 Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 
     
	   
	   // tell the camera to update its matrices.
	   super.render(delta);
	   
	   // begin a new batch and draw the bucket and
		 // all drops
		 
		
		 batch.begin();
			 //batch.draw(backgroundTexture, 0, 0);
			 
			batch.draw(backgroundTexture, 0, 0);
			

    	batch.draw(bucketImage, bucket.x, bucket.y);
	   
	 
	   scoreFont.draw(batch, myDropScore, 1100 , 700); 
	   scoreFont.draw(batch, target, 0 , 700); 
	   
	   
	        for(Rectangle raindrop0: raindrops0) {
				batch.draw(dropImage0, raindrop0.x, raindrop0.y);
				 }
		

			for(Rectangle raindrop1: raindrops1) {
				batch.draw(dropImage1, raindrop1.x, raindrop1.y);
				}		
	   
       batch.end();  
	   
	   // process user input
	   if(Gdx.input.isTouched()) {
		  Vector3 touchPos = new Vector3();
		  touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		  cam.unproject(touchPos);
		  bucket.x = touchPos.x - 64 / 2;
	   }
	   if(Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
	   if(Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();
	   
	   /*
	   if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
		   rainMusic.pause();
		   gm.setCurrGameState("END");
	   } */
	   
	   // make sure the bucket stays within the screen bounds
	   if(bucket.x < 0) bucket.x = 0;
	   if(bucket.x > 800 - 64) bucket.x = 800 - 64;
	   
	   // check if we need to create a new raindrop
	   if(TimeUtils.nanoTime() - lastDropTime > 100000000*100000) spawnRaindrop();
	   
	   // move the raindrops, remove any that are beneath the bottom edge of
	   // the screen or that hit the bucket. In the latter case we play back
	   // a sound effect as well.
	   for (Iterator<Rectangle> iter = raindrops0.iterator(); iter.hasNext(); ) {
		  Rectangle raindrop = iter.next();
		  raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
		  if(raindrop.y + 64 < 0) iter.remove();
		  if(raindrop.overlaps(bucket)) {
          Score.add(0); 
          myDropScore = "Binary Collected: " + CurrentScore(); 
			 dropSound.play();
			 iter.remove();
		  }
	   }
   
	   for (Iterator<Rectangle> iter = raindrops1.iterator(); iter.hasNext(); ) {
		Rectangle raindrop = iter.next();
		raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
		if(raindrop.y + 64 < 0) iter.remove();
		if(raindrop.overlaps(bucket)) {
		Score.add(1); 
		myDropScore = "Binary Collected: " + CurrentScore(); 
		   dropSound.play();
		   iter.remove();
		}
	 }
	
	   if(Score.size() == 4) {
		  rainMusic.pause();
		  if(CheckScore()) {			
            gm.setCurrGameState("MAINGAME");
		   }
	       else {
			gm.setCurrGameState("END");
		   }
		}
	
	
	}
    /*this function checks to see if the binary matches the target */
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
			return true;
		}

		return false;
 	}
	
	
	
	public String CurrentScore(){
		String s = " ";
		for(int i =0; i < Score.size(); i++){
			s += Score.get(i);
		}
		return s;
	}
   
   @Override
   public void show() {
	   rainMusic.play();
	   setPassState(true);
   }

   @Override
   public void hide() {  
   }

	@Override
	public void pause () {
		
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
		 backgroundTexture.dispose();
	}




}