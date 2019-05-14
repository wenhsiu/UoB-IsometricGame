package com.isometricgame.core.clickdrag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

// this extends is from libgdx
public class Piece /*extends Actor */{
	private String property;
	private Image image;
	private Stage stage;
	private boolean located = false;
	private float boundry_top, boundry_buttom, boundry_left, boundry_right;
	private float posX, posY, width, height, locationX;
	private String[] answer = new String[5];
	private DragAndDrop dragAndDrop = new DragAndDrop();

	public Piece(Image image, String property) {
		this.image = image;
		this.property = property;
		
	}

	public void setBounds(float posX, float posY, float width, float height) {
		image.setBounds(posX, posY, width, height);
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		// boundry_top = posY;
		// boundry_buttom = posY + height;
		// boundry_left = posX;
		// boundry_right = posX + width;
	}

	public void setActor(Stage stage) {
		this.stage = stage;
		stage.addActor(image);
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String string) {
		property = string;
	}

	public Image getImage() {
		return image;
	}

	public void setDragSource() {
		dragAndDrop.setDragActorPosition(-(width/2), height/2);

		dragAndDrop.addSource(new Source(image) {
			public Payload dragStart (InputEvent event, float x, float y, int pointer) {
				Payload payload = new Payload();
				payload.setObject(image);
				payload.setDragActor(image);

				return payload;
			}

			public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
				stage.addActor(image);
			}
		});
	}

	public void setDragTarget(Actor t, float pos) {
		// System.out.println(posX);
		final float posX = pos;
		dragAndDrop.addTarget(new Target(t) {
			public boolean drag (Source source, Payload payload, float x, float y, int pointer) {

				return true;
			}

			public void drop (Source source, Payload payload, float x, float y, int pointer) {
				image.setPosition(posX, 300);
				located = true;
				locationX = posX;
				dragAndDrop.removeSource(source);
			}
		});		
	}

	public float getDropLocation() {
		return locationX;
	}

	public boolean isLocated() {
		return located;
	}
}