package characterManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Actor implements ApplicationListener{
	protected float pos_x, pos_y;
	protected int size_x, size_y;
	public float bound_top, bound_bottom, bound_right, bound_left;
	protected int speedFactor, speedLimit;	
	protected Texture texture;
	protected SpriteBatch batch;
	protected TextureRegion region;
	
	public Actor(float x, float y) {
		pos_x = x;
		pos_y = y;
	}
	
	public abstract void animationInit();
	public abstract void animationUpdate(float dt);
	
	public abstract boolean isCollision(float x, float y);
	
	public void characterInit(String materails, int sx, int sy, int ex, int ey) {
		texture = new Texture(Gdx.files.internal(materails));
		batch = new SpriteBatch();
		region = new TextureRegion(texture, sx, sy, ex, ey);
		size_x = ex-sx;
		size_y = ey-sy;
	}
	
	public void characterUpdate(float nx, float ny) {
		batch.begin();
		batch.draw(region, nx, ny);
		batch.end();
	}
	
	public float getPositionX() {return pos_x + size_x/2;}
	
	public float getPositionY() {return pos_y + size_y/2;}
	
	public int getSizeX() {return size_x;}
	
	public int getSizeY() {return size_y;}
	
	public void setBound(float top, float bottom, float right, float left) {
		bound_top = top;
		bound_bottom = bottom;
		bound_right = right;
		bound_left = left;
	}
	
	public boolean containPoint(float x, float y) {
		if(x > pos_x + size_x/2 - bound_left &&
		   x < pos_x + size_x/2 + bound_right &&
		   y > pos_y + size_y/2 - bound_bottom &&
		   y < pos_y + size_y/2 + bound_top) {return true;}
		return false;
	}
	
	public TextureRegion initTextureReg(String matName, int startX, int startY, int stopX, int stopY) {
		return new TextureRegion(new Texture(Gdx.files.internal(matName)), startX, startY, stopX, stopY);
	}
}
