package com.isometricgame.core.clickdrag;

import java.util.*;
import java.io.*;
import java.lang.String;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
// import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.ApplicationListener;



// import com.badlogic.gdx.graphics.OrthographicCamera;


public class Puzzles implements ApplicationListener {
	private Stage stage;
	private Image line, addition;
	private Image reset =  new Image(new Texture("clickanddrag/reset_button.png"));
	private Texture Qzero = new Texture("clickanddrag/q0.png");
	private Texture Qone = new Texture("clickanddrag/q1.png");
	private Texture Azero = new Texture("clickanddrag/0.png");
	private Texture Aone = new Texture("clickanddrag/1.png");
	private Texture answer = new Texture("clickanddrag/answer.png");

	private List<Piece> quesOne = new ArrayList<Piece>();
	private List<Piece> quesTwo = new ArrayList<Piece>();
	private List<Piece> puzzles = new ArrayList<Piece>();
	private List<Piece> targets = new ArrayList<Piece>();
	// private List<Drag_drop> drags = new ArrayList<Drag_drop>();

	private String bineryOne = "", bineryTwo = "", bineryAnswer = "";
	private Image testImage;
	private String[] targetAnswer = new String[5];

	private String ans = "";

	@Override
	public void create () {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		setQuestionImages();
		setTargetImages();
		setSourceImages();

		ans = "";
		setReset();
		
	}

	@Override
	public void resize (int width, int height) {
		// stage.getViewport().update(width, height, true);
	}

	@Override
	public void render () {
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
		stage.dispose();
	}

	public boolean checkTagetAnswer() {
		float temp;
		// String ans = "";

		for(int i = 0; i <  8; i++) {
			if(puzzles.get(i).isLocated()) {
				temp = puzzles.get(i).getDropLocation();
				targetAnswer[index(temp)] = puzzles.get(i).getProperty();
			}
		}
		
		ans = String.join("", targetAnswer);
		
		// /**/System.out.println("!!!!!!!!" + ans);
		if(ans.equals(bineryAnswer)) {
			return true;
		}

		return false;
	}

	private void setSourceImages() {
		for(int i = 0; i < 8; i++) {
			if(i < 4) {
				puzzles.add(new Piece(new Image(Azero), "0"));
			} else {
				puzzles.add(new Piece(new Image(Aone), "1"));
			}
			puzzles.get(i).setBounds(180 + i * 110, 50, 100, 100);
			puzzles.get(i).setActor(stage);
			puzzles.get(i).setDragSource();
			for(int j = 0 ; j < 5; j++) {
				puzzles.get(i).setDragTarget(targets.get(j).getImage(), 290 + j * 110);
			}
			
		}
	}

	private void setTargetImages() {
		char[] temp = bineryAnswer.toCharArray();
		int l = temp.length;

		for(int i = 0; i < 5; i++) {
			targets.add(new Piece(new Image(answer), Character.toString(temp[i])));
			targets.get(i).setBounds(290 + i * 110, 300, 100, 100);
			targets.get(i).setActor(stage);
		}
	}

	private void setQuestionImages() {
		line = new Image(new Texture("clickanddrag/line.png"));
		line.setBounds(280, 410, 560, 6);
		stage.addActor(line);

		addition = new Image(new Texture("clickanddrag/addition.png"));
		addition.setBounds(290, 420, 100, 100);
		stage.addActor(addition);

		// for line one
		for(int i = 0; i < 4; i++) {
			if(Math.random() < 0.5) {
				quesOne.add(new Piece(new Image(Qzero), "0"));
			} else {
				quesOne.add(new Piece(new Image(Qone), "1"));
			}

			quesOne.get(i).setBounds(400 + i * 110, 530, 100, 100);
			quesOne.get(i).setActor(stage);
			bineryOne += quesOne.get(i).getProperty();
		}

		// for line two
		for(int i = 0; i < 4; i++) {
			if(Math.random() < 0.5) {
				quesTwo.add(new Piece(new Image(Qzero), "0"));
			} else {
				quesTwo.add(new Piece(new Image(Qone), "1"));
			}

			quesTwo.get(i).setBounds(400 + i * 110, 420, 100, 100);
			quesTwo.get(i).setActor(stage);
			bineryTwo += quesTwo.get(i).getProperty();
		}

		bineryAnswer = bineryAddition(bineryOne, bineryTwo);
		checkAnswerLength();

		System.out.println(bineryOne);
		System.out.println(bineryTwo);
		System.out.println(bineryAnswer);

	}

	private String bineryAddition(String one, String two) {
		int a = Integer.parseInt(one, 2);
		int b = Integer.parseInt(two, 2);
		int sum = a + b;

		return Integer.toBinaryString(sum);
	}

	private void checkAnswerLength() {
		switch (bineryAnswer.length()) {
			case 1:
				bineryAnswer = "0000" + bineryAnswer;
				break;
			case 2:
				bineryAnswer = "000" + bineryAnswer;
				break;
			case 3:
				bineryAnswer = "00" + bineryAnswer;
				break;
			case 4:
				bineryAnswer = "0" + bineryAnswer;
				break;
		}
	}

	private int index(float x) {
		switch((int) x) {
			case 290:
				return 0;
			case 400:
				return 1;
			case 510:
				return 2;
			case 620:
				return 3;
			case 730:
				return 4;
		}

		return -1;
	}

	public void setReset() {
		reset.setBounds(930, 200, 210, 100);
		stage.addActor(reset);

		reset.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				for(Piece p: puzzles) {
					p.removeFromTarget();
				}
				setSourceImages();
				targetAnswer = new String[5];
				// /**/System.out.println("reset: " + String.join(",", targetAnswer));	

			}
		});
	}

	public void clearStage() {
		stage.clear();
	}
	
}

