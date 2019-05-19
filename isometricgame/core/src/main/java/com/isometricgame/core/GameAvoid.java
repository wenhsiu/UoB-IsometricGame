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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;


public class GameAvoid extends GameState {

    private final int BOX_TOP = -32;
    private final int BOX_BOTTOM = -205;

    private final int BOX1_LEFT = 5010;
    private final int BOX1_RIGHT = 5307;

    private final int BOX2_LEFT = 5356;
    private final int BOX2_RIGHT = 5632;

    private final int BOX3_LEFT = 5708;
    private final int BOX3_RIGHT = 6012;

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
    private int timeremaining = 0;
    private int pos1;
    private int times = 0;
    private int cnt = 0;
    private Music backgroundMusic; 
    long startTime = 0;
    int scorearray[] = new int[3];
    int pos = 0;
    double oldx, oldy;
    long time= 0;

    // public static Texture backgroundTexture;
    private Texture backgroundTexture = new Texture("window_0011_Vector-Smart-Object.png");
    private Texture box = new Texture("avoid_button.png");
    private Texture failure = new Texture("failure_image.jpeg");
    private Texture intro = new Texture("avoid_intro.png");

    public GameAvoid(GameManager gm){
        super();
        this.gm = gm;

        startTime = System.currentTimeMillis();
        time = System.currentTimeMillis();

        scoreFont = new BitmapFont(); 
        scoreFont.setColor(25/255f, 35/255f, 76/255f, 1f);
        scoreFont.setScale(2,2);


        timer = new BitmapFont(); 
        timer.setColor(25/255f, 35/255f, 76/255f, 1f);
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
        if(pos == 0) {
            oldx = player.getPositionX();
            oldy = player.getPositionY();
        }
        pos++;
        times++;
        //creating a time that counts down
        long elapsedTime = System.currentTimeMillis() - startTime; 
        long timeleft = System.currentTimeMillis() - time;

        if(times == 1) {
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("10secondcountdown.mp3")); 
            backgroundMusic.setLooping(true);
            backgroundMusic.play();
        }
       
        if(times == 1 || elapsedSeconds == 0) {
            if(correctposition() && times != 0) {
                cnt++;
                /**/System.out.println("conut: " + cnt);       
            } else {
                cnt = 0;
            }       
            target = "";
            target = generatebinarynumber();
            targetstring = "What is " + target;
            generatetargets();
            startTime = System.currentTimeMillis();
        }

        settimeremaining(timeleft);

        Gdx.gl.glClearColor(255/255f, 234/255f, 188/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 

        super.render(delta);

        // begin a new batch and draw the background and box
        if(cnt != 3 && timeremaining > 0) {
            batch.begin();
            batch.draw(intro, 30, 550, 1000, 150);
            batch.draw(box, 110, 160, 300, 200);
            batch.draw(box, 460, 160, 300, 200);
            batch.draw(box, 810, 160, 300, 200);
            batch.end();  

            setTimer(elapsedTime); 

            batch.begin();
            timer.draw(batch,String.valueOf(elapsedSeconds), 600 , 470);
            scoreFont.draw(batch,targetstring, 520 , 550);
            scoreFont.draw(batch,"current score  " + String.valueOf(cnt),950 , 550);
            scoreFont.draw(batch,"time remaining  " + String.valueOf(timeremaining),50 , 550);
            scoreFont.draw(batch,String.valueOf(scorearray[0]), 250 , 300); 
            scoreFont.draw(batch,String.valueOf(scorearray[1]), 600 , 300); 
            scoreFont.draw(batch,String.valueOf(scorearray[2]), 950 , 300);
            batch.end();

            player.render(); 
        }

        if(cnt == 3) {
            backgroundMusic.stop();
            player.setPositionX((int)oldx);
            player.setPositionY((int)oldy);
            //set the first game state passed to true so that the trigger point can detect correctly
            gm.inventoryAddMedals();
            passed = true;
            // back to main game
            gm.setCurrGameState("MAINGAME");
        } else if (timeremaining <= 0) {
            backgroundMusic.stop();
            batch.begin();
            batch.draw(failure, 0, 0, 1200, 750);
            batch.end();
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) { 
                player.setPositionX((int)oldx);
                player.setPositionY((int)oldy);
                gm.setCurrGameState("MAINGAME");
            }
        }
  
        // System.out.println("X"+player.getPositionX());
        // System.out.println("Y"+player.getPositionY());
    }
     
    // setting timer reducing time as the players score increases making it harder.
    private void setTimer(long elapsedTime) {
        if (cnt == 0) {
            elapsedSeconds = 10 - (int)elapsedTime / 1000; 
        } else if ( cnt == 1) {
            elapsedSeconds = 7 - (int)elapsedTime / 1000; 
        } else if ( cnt == 2) {
            elapsedSeconds = 5 - (int)elapsedTime / 1000; 
        } 
    }

    private void settimeremaining(long elapsedTime) {
        timeremaining = 60 - (int)elapsedTime / 1000;
    }

    //checking to see if the player is in the correct position when the timer reaches zero
    private boolean correctposition() {
        if(pos1 == 0) {
            if( BOX1_LEFT <= player.getPositionX() && player.getPositionX() <= BOX1_RIGHT) {
                if( BOX_BOTTOM <= player.getPositionY() && player.getPositionY() <= BOX_TOP) {
                    return true;
                }
            }
        } else if(pos1 == 1) {
            if( BOX2_LEFT <= player.getPositionX() && player.getPositionX() <= BOX2_RIGHT) {
                if( BOX_BOTTOM <= player.getPositionY() && player.getPositionY() <= BOX_TOP) {
                    return true;
                }
            }
        } else if(pos1 == 2) {
            if(BOX3_LEFT <= player.getPositionX() && player.getPositionX() <= BOX3_RIGHT) {
                if( BOX_BOTTOM <= player.getPositionY() && player.getPositionY() <= BOX_TOP) {
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
            } else {
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
    
    // generates the target and 2 random numbers then inserts these 3 numbers 
    // into an array randomly so that the position of
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
    
    // this function checks to see if the binary matches the target 
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
        backgroundTexture.dispose();
	}
}
