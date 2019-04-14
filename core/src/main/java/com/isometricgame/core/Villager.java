package com.isometricgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class Villager extends OurActor {

	private Animation spin;
	private float timer;
	private int frameNumber = 4;
	private int frameSizeX;
    private int frameSizeY;
    private double move_x, move_y;
	private float init_x, init_y;
	private int direction;

	
	
	public Villager(float x, float y) {
		super(x, y,  (float)0.35);
		frameSizeX = 302;
		frameSizeY = 344;
		direction = 1;
        move_x = 2;
        move_y = 1;
        init_x = x;
        init_y = y;
	}

	@Override
	public void create() {
		characterInit("boss_down.png", 0, 0, frameSizeX, frameSizeY);
		animationInit();
		setBound(getSizeY()/2, getSizeY()/2, getSizeX()/2, getSizeX()/2);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
        moveVillager();
		characterUpdate(pos_x, pos_y);
		animationUpdate(Gdx.graphics.getDeltaTime());		
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
		texture.dispose();
	}

	@Override
	public void animationInit() {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		String[] frameName = {"boss_up.png", "boss_left.png", "boss_down.png", "boss_right.png"};
		for(int i = 0; i < frameNumber; i++) {
			frames.add(initTextureReg(frameName[i], 0, 0, frameSizeX, frameSizeY));
		}
		spin = new Animation(0.1f,frames);
		frames.clear();
	}

	@Override
	public void animationUpdate(float dt) {		
		region = spin.getKeyFrame(timer+=dt/10, true);
	}

	@Override
	public boolean isCollision(float x, float y) {
		return super.containPoint(x, y);
    }
    
    private void moveVillager(){
        pos_x += move_x;
        pos_y += move_y;
    }

    private void randomDirection(){
        double r = Math.random();
		int i = (int) ((r * 3) + 1);
		direction += i;
		direction = direction % 4;
    }

    public float initial_x(){ return init_x; }

    public float initial_y(){ return init_y; }

    public void checkCollision(Cell cell){
		if(cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("Blocked")){
			randomDirection();
			if (direction == 0){
				move_x = 2;
				move_y = 1;
			}
			if (direction == 1){
				move_x = 2;
				move_y = -1;
			}
			if (direction == 2){
				move_x = -2;
				move_y = 1;
			}
			if (direction == 3){
				move_x = -2;
				move_y = -1;
			}
		}
	}


}
