package com.isometricgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapProperties;



public class Character extends Sprite /*implements InputProcessor*/ {

    //private Vector2 velocity = new Vector2();
    

    private float speed = 60 * 4, increment;

    private TiledMapTileLayer collisionLayer;

    private boolean cnt1 = true, cnt2 = true, cnt3 = true, cnt4 = true;


    public Character(Sprite sprite, TiledMapTileLayer collisionLayer){
        super(sprite);
        this.collisionLayer = collisionLayer;
    }

    public void draw(SpriteBatch spriteBatch){
        update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
    }

    public void update(float delta){ 

        //save old position
        
        float oldX = getX(), oldY = getY();
        boolean collisionX = false, collisionY = false;

        if(Gdx.input.isKeyPressed(Keys.LEFT)) setX(getX() - speed * Gdx.graphics.getDeltaTime());
        if(Gdx.input.isKeyPressed(Keys.RIGHT)) setX(getX() + speed * Gdx.graphics.getDeltaTime());
        if(Gdx.input.isKeyPressed(Keys.DOWN)) setY(getY() - speed * Gdx.graphics.getDeltaTime());
        if(Gdx.input.isKeyPressed(Keys.UP)) setY(getY() + speed * Gdx.graphics.getDeltaTime());



		// calculate the increment for step in #collidesLeft() and #collidesRight()
		increment = (float) 10.625;

		if(Gdx.input.isKeyPressed(Keys.LEFT)) // going left
			collisionX = collidesLeft();
		else if(Gdx.input.isKeyPressed(Keys.RIGHT)) // going right
			collisionX = collidesRight();

		// react to x collision
		if(collisionX) {
            setX(oldX);
		}

		// move on y
        //setY(getY() + velocity.y * delta * 5f);
        
		if(Gdx.input.isKeyPressed(Keys.DOWN)) // going down
			collisionY = collidesBottom();
		else if(Gdx.input.isKeyPressed(Keys.UP)) // going up
			collisionY = collidesTop();

		// react to y collision
		if(collisionY) {
            setY(oldY);
        }


    }

	private boolean isCellBlocked(float x, float y) {
        Cell cell = collisionLayer.getCell((int) ((x - 253) / 10.625), (int) ((y - 30) / 10.625));
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("Block");
    }

    public boolean collidesRight() {
			if(isCellBlocked(getX() + increment, getY()))
				return true;
		return false;
	}

	public boolean collidesLeft() {
			if(isCellBlocked(getX() - increment, getY()))
				return true;
		return false;
	}

	public boolean collidesTop() {
			if(isCellBlocked(getX(), getY() + increment))
				return true;
		return false;

	}

	public boolean collidesBottom() {
			if(isCellBlocked(getX(), getY() - increment))
				return true;
		return false;
    }

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
    }

    public float getXPostion() {
        return getX();
    }

    public float getYPostion() {
        return getY();
    }

}