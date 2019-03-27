package characterManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Actor implements ApplicationListener{
	protected float pos_x, pos_y;
	public final float init_x, init_y;
	protected float size_x, size_y;
	public float bound_top, bound_bottom, bound_right, bound_left;
	protected int speedFactor, speedLimit;	
	protected Texture texture;
	protected SpriteBatch batch;
	protected TextureRegion region;
	private float scale;
	
	public Actor(float x, float y, float scale) {
		pos_x = x;
		pos_y = y;
		init_x = x;
		init_y = y;
		this.scale = scale;
	}
	
	public abstract void animationInit();
	public abstract void animationUpdate(float dt);
	
	public abstract boolean isCollision(float x, float y);
	
	public void characterInit(String materails, int sx, int sy, int ex, int ey) {
		texture = new Texture(Gdx.files.internal(materails));
		batch = new SpriteBatch();
		region = new TextureRegion(texture, sx, sy, ex, ey);
		size_x = (ex-sx)*scale;
		size_y = (ey-sy)*scale;
	}
	
	public void characterUpdate(float nx, float ny) {
		batch.begin();
		batch.draw(region, nx, ny, size_x*scale, size_y*scale);
		batch.end();
	}
	
	public float getPositionX() {return pos_x + size_x/2;}
	
	public float getPositionY() {return pos_y + size_y/2;}
	
	public float getSizeX() {return size_x;}
	
	public float getSizeY() {return size_y;}
	
	public void setBound(float top, float bottom, float right, float left) {
		bound_top = top*scale;
		bound_bottom = bottom*scale;
		bound_right = right*scale;
		bound_left = left*scale;
	}
	
	public SpriteBatch getBatch() {return batch;}
	
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
