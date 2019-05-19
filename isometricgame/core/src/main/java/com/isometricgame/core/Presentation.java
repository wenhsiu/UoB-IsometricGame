package com.isometricgame.core;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.isometricgame.core.custominputprocessor.GameInputProcessor;
import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

public class Presentation extends GameState{

   private GameManager gm; 
   private SpriteBatch batch;
   private BitmapFont scoreFont; 
   private int cnt = 0;
   private Texture slide;
   private Music slide1;


   private Texture[] slides = new Texture[24];
   private Music[] talking = new Music[24];

   public static Texture backgroundTexture;
   public static Texture background1Texture;
   public static Texture background2Texture;
   public static Texture background3Texture;
   public static Texture background4Texture;
   public static Texture background5Texture;

   private boolean sPressed = false, bPressed = false;


   public Presentation(GameManager gm){
       
	  super();
      this.gm = gm;
      
     
      for(int i =0; i < 24; i++) {
          int m = i + 1;
          slides[i] = new Texture(Gdx.files.internal("slide" + m + "-min.png"));
          talking[i] = Gdx.audio.newMusic(Gdx.files.internal("slide" + m + "-sound.mp3"));
      } 


    

      
    //  slide = new Texture(Gdx.files.internal("slide1.png"));
 
    //  slide1 = Gdx.audio.newMusic(Gdx.files.internal("slide1-sins.mp3"));
      scoreFont = new BitmapFont(); 
      scoreFont.setColor(25/255f, 35/255f, 76/255f, 1f);
		  scoreFont.setScale(2,2);
          
    /*  backgroundTexture = new Texture("slide1-min.png");
      background1Texture = new Texture("slide2-min.png");
      background2Texture = new Texture("slide3-min.png");
      background3Texture = new Texture("slide4-min.png");
      background4Texture = new Texture("slide5-min.png");
      background5Texture = new Texture("slide6-min.png");*/

          
      batch = new SpriteBatch();

      
   }
      
	@Override
	public void render(float delta) {
    
        /*if(GameInputProcessor.KeyUp(Input.keys.RIGHT/) {
            batch.draw(backgroundTexture , 0, 0, 1200, 750);
            
            //cnt++;
        }
        if(Gdx.input.isKeyJustPressed(Keys.LEFT)) {
            batch.draw(background1Texture , 0, 0, 1200, 750);
           
            //cnt++;
        }*/

        boolean sPrev = sPressed, bPrev = bPressed;

        if(Gdx.input.isKeyPressed(Keys.SPACE)) {
            sPressed = true; 
        }
        else {
            sPressed = false;
        }

        if(sPrev == true && sPressed == false){
            cnt++;
            show();
        }

        if(Gdx.input.isKeyPressed(Keys.B)) {
            bPressed = true; 
        }
        else {
            bPressed = false;
        }

        if(bPrev == true && bPressed == false){
            cnt--;
            show();
        }

        if(cnt < 0){
            cnt = 0;
        }

        if(cnt > 23){
            cnt = 0; 
            gm.setCurrGameState("MENU");
        }

      
      
	   Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	   Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 
     
	   super.render(delta);
	   
	   // begin a new batch and draw the bucket and
	   // all drops
       
 
       batch.begin();

       System.out.println(cnt);

       for(int j = 0; j < 24; j++){
        if(cnt == j) {
            batch.draw(slides[j] , 0, 0, 1200, 750);
           }
       }

       
       batch.end();  

    
      
   

  
     //  System.out.println("X"+player.getPositionX());
     //  System.out.println("Y"+player.getPositionY());
     }
     
  
    @Override
    public void show() {
        if(cnt > 23 || cnt < 0){
            talking[23].stop();
            talking[0].stop();
            return;
        }
            talking[cnt].play();
            if(cnt < 23){
                talking[cnt+1].stop();

            }
            if(cnt > 0){
                talking[cnt-1].stop();
            }  
        }
    

	@Override
	public void dispose() {
	   // dispose of all the native resources
	   batch.dispose();
       scoreFont.dispose();
       backgroundTexture.dispose();
	}
}






