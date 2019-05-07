package com.isometricgame.core;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;
import com.badlogic.gdx.audio.Music;


public class Avoid extends GameState {

   private GameManager gm; 
   private SpriteBatch batch;
   private BitmapFont scoreFont; 
   private BitmapFont timer; 
   private String target = ""; 
   private String targetstring = ""; 
   private int num;
   private ShapeRenderer shapeRenderer;
   private Player player;
   private int elapsedSeconds = 0;
   private int pos1;
   private int times = 0;
   private int cnt = 0;
   private Music backgroundMusic; 
   long startTime = 0;
   int scorearray[] = new int[3];


   public Avoid(GameManager gm){
       
	  super();
      this.gm = gm;
      
	  startTime = System.currentTimeMillis();
      
      scoreFont = new BitmapFont(); 
      scoreFont.setColor(Color.GREEN);
      scoreFont.setScale(2,2);

      timer = new BitmapFont(); 
      timer.setColor(Color.GREEN);
      timer.setScale(4,4);
    
      target =generatebinarynumber();
      targetstring = "What is " + target;
      generatetargets();
          
      batch = new SpriteBatch();
      player = gm.getPlayer();
      shapeRenderer = new ShapeRenderer();
      
   }
      
	@Override
	public void render(float delta) {
        times++;
        long elapsedTime = System.currentTimeMillis() - startTime; //creating a time that counts down
        
        if(times == 1) {
          backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("10secondcountdown.mp3")); 
          backgroundMusic.setLooping(true);
          backgroundMusic.play();
        }
       
        if(times == 1 || elapsedSeconds == 0) {
            if(correctposition() && times != 0) {
                cnt++;        
              }
              else {
                cnt = 0;
              } 
          
          
          
          
          target = "";
          target =generatebinarynumber();
          targetstring = "What is " + target;
          generatetargets();
          startTime = System.currentTimeMillis();
        }
        

	   Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	   Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 
     
	   super.render(delta);
	   
	   // begin a new batch and draw the bucket and
	   // all drops
       
       drawRectangles();
      
       batch.begin();
             
       setTimer(elapsedTime); 
       
       timer.draw(batch,String.valueOf(elapsedSeconds), 600 , 500);
       
       scoreFont.draw(batch,targetstring, 520 , 400);
       scoreFont.draw(batch,"current score  " + String.valueOf(cnt),950 , 660);
       
       scoreFont.draw(batch,String.valueOf(scorearray[0]), 260 , 260); 
       scoreFont.draw(batch,String.valueOf(scorearray[1]), 610 , 260); 
       scoreFont.draw(batch,String.valueOf(scorearray[2]), 960 , 260);
       batch.end();

       player.render(); 

         if(cnt == 3) {
            backgroundMusic.stop();
            gm.setCurrGameState("MAINGAME");
         }
  
     }
     
     private void drawRectangles() {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(110, 160, 300, 200);
        shapeRenderer.rect(460, 160, 300, 200);
        shapeRenderer.rect(810, 160, 300, 200);
        shapeRenderer.end(); 
     }
     
     // setting timer reducing time as the players score increases making it harder.
     private void setTimer(long elapsedTime) {
        if ( cnt == 0) {
            elapsedSeconds = 10 - (int)elapsedTime / 1000; 
            }
         else if ( cnt == 1) {
            elapsedSeconds = 7 - (int)elapsedTime / 1000; 
            }   
         else if ( cnt == 2) {
            elapsedSeconds = 5 - (int)elapsedTime / 1000; 
            } 
     }
   
     //checking to see if the player is in the correct position when the timer reaches zero
     private boolean correctposition() {
        System.out.println(player.getPositionX());
        System.out.println(player.getPositionY());

        if(pos1 == 0) {
         if( 676 <= player.getPositionX() && player.getPositionX() <= 976) {
           if( -156 <= player.getPositionY() && player.getPositionY() <= 27) {
               return true;
                }
              }
            }
    
        else if(pos1 == 1) {
               if( 1030 <= player.getPositionX() && player.getPositionX() <= 1327) {
                 if( -156 <= player.getPositionY() && player.getPositionY() <= 27) {
                  return true;
                    }
                  }
                }
        else if(pos1 == 2) {
               if(1395 <= player.getPositionX() && player.getPositionX() <= 1677) {
                 if( -156 <= player.getPositionY() && player.getPositionY() <= 27) {
                   return true;
                   }
                  }
                }
        return false;

     }


     private String generatebinarynumber() {
        
        for(int i=0; i < 4; i++) {
             if(RandNum(1,100)%2 == 0) {
                 target += "1";
             } 
             else {
                 target += "0";
             }
            }     
        
        return target ;
     }

        
 private int RandNum(double min, double max){
	double n;
	n = (Math.random()*((max-min)+1))+min;
	num = (int)n;
	return num;
    }
    
 // generates the target and 2 random numbers then inserts these 3 numbers into an array randomly so that the position of
 // the correct number moves between the 3 places

 private void generatetargets() {
    int t1, t2, t3, pos2=0;
    int  pos3 = 0;
    t1 = CheckScore();
    t2 = t1;
    t3 = t1;
    
    while( t1 == t2) {
      t2 = RandNum(1, 15);
      }
    
    while( t1 == t3) {
      t3 = RandNum(1, 15);
      }
    
    t3 = RandNum(1, 15);

     pos1 = RandNum(0,2);
     System.out.println(pos1);
     
     while(pos1 == pos2) {
        pos2 = RandNum(0,2);
     }

     while(pos3 == pos1 || pos3 == pos2) {
         pos3 = RandNum(0,2); 

     }

    scorearray[pos1] = t1;
    scorearray[pos2] = t2;
    scorearray[pos3] = t3;
    }
    
     /*this function checks to see if the binary matches the target */
	public int CheckScore() {
		int Total = 0;

		if(target.charAt(0) == '1') {
			Total += 8;
		} 
		
		if(target.charAt(1) == '1') {
			Total += 4;
		}

		if(target.charAt(2) == '1') {
			Total += 2;
		}

		if(target.charAt(3) == '1') {
			Total += 1;
		}
	   		
		return Total;
 	}


	@Override
	public void dispose() {
	   // dispose of all the native resources
	   batch.dispose();
       scoreFont.dispose();
       timer.dispose();
       shapeRenderer.dispose();
	}
}






