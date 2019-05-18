package com.isometricgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.isometricgame.core.dialogue.GameDialogue;
import com.isometricgame.core.dialogue.DialogUI;
import com.isometricgame.core.dialogue.GameDialogue;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;

import java.util.ArrayList;
import java.util.List;

public class GameGUIDE extends GameState {
	
	private Sprite sprite;
	private SpriteBatch batch;
	private Texture mapGuide;
	private GameManager gm;
    
    /*private BitmapFont bfont;
	private static String message = "Welcome to Isometria!";
	private SpriteBatch textbatch;
	private Label labeltest;
	private LabelStyle labelstyle;
	private String triggerText;
    public List<GameDialogue> dialogueList;
    private ShapeRenderer shapeRenderer;*/

	public GameGUIDE(GameManager gm) {
		super();
		this.gm = gm;
		mapGuide = new Texture("mapguide.png");		
		//initDialogueArray();
		batch = new SpriteBatch();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        float x = Gdx.input.getX();
		float y = Gdx.input.getY();
        
        /*shapeRenderer = new ShapeRenderer(); 
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		for (int i = 0; i < dialogueList.size(); i++) {
			if(dialogueTriggerCheck(x, y, i) == true){
				shapeRenderer.setColor(Color.DARK_GRAY);
				shapeRenderer.rect(100, 110, 970, 70);
				shapeRenderer.setColor(Color.LIGHT_GRAY);
				shapeRenderer.rect(110, 120, 950, 50);
			}
		}
        shapeRenderer.end();

		textbatch.begin();
			//dialogueTriggerCheck(testbatch, x, y);
			for (int i = 0; i < dialogueList.size(); i++) {
				if(dialogueTriggerCheck(x, y, i) == true){
					bfont.draw(textbatch, dialogueList.get(i).getTextmessage(), 150, 150); 
				}
			}
        textbatch.end();*/

        batch.begin();
        batch.draw(mapGuide, 0 , 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            gm.setCurrGameState("MENU");
        }
    }
    
    /* private void addDialogue(double maxx, double maxy, double minx, double miny, String message){
		GameDialogue newdialogue = new GameDialogue(maxx, maxy, minx, miny, message);
		dialogueList.add(newdialogue); 
    }
    
    private void initDialogueArray(){
		//add dialogue into the array through this function. 
		addDialogue(300, 300, -300 , -300, "Narrator: Welcome to Isometria!!");
    }

    private Boolean dialogueTriggerCheck(double currentX, double currentY, int i){

        //System.out.println("HELLO TRIGGER CHECK " + dialogueList.get(i).getMinx());

        if(dialogueList.get(i).getMinx() < currentX  && dialogueList.get(i).getMiny() < currentY && dialogueList.get(i).getMaxx() > currentX && dialogueList.get(i).getMaxy() > currentY){
            //System.out.println("true"); 
            return true; 
        }
        return false; 
    }*/

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
        mapGuide.dispose();		
        batch.dispose();
	}

}
