package characterManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class People implements ApplicationListener{
	protected float pos_x;
	protected float pos_y;
	protected float size_x;
	protected float size_y;
	protected float bound_top;
	protected float bound_bottom;
	protected float bound_right;
	protected float bound_left;
	protected int speedFactor, speedLimit;	
	protected Texture texture;
	protected SpriteBatch batch;
	protected TextureRegion region;
	private float scale;
	
	public People(float x, float y, float scale) {
		pos_x = x;
		pos_y = y;
		this.scale = scale;
	}
	
	public abstract void animationInit();
	public abstract void animationUpdate(float dt);	
	public abstract void CollisionAction(boolean fire);
	
	public void characterInit(String materials, int sx, int sy, int ex, int ey) {
		texture = new Texture(Gdx.files.internal(materials));
		batch = new SpriteBatch();
		region = new TextureRegion(texture, sx, sy, ex, ey);
		size_x = (ex-sx)*scale;
		size_y = (ey-sy)*scale;
		setBoundary(size_y/2, size_y/2, size_x/2, size_x/2);
	}
	
	public void characterUpdate(float nx, float ny) {
		batch.begin();
		batch.draw(region, nx, ny, size_x, size_y);
		batch.end();
	}
	
	public float getPositionX() {return pos_x + size_x/2;}
	
	public float getPositionY() {return pos_y + size_y/2;}
	
	public float getSizeX() {return size_x;}
	
	public float getSizeY() {return size_y;}
	
	public void setBoundary(float top, float bottom, float right, float left) {
		bound_top = top;
		bound_bottom = bottom;
		bound_right = right;
		bound_left = left;
	}
	
	public SpriteBatch getBatch() {return batch;}
	
	public boolean containPoint(float x, float y) {
		if(x > getPositionX() - bound_left &&
		   x < getPositionX() + bound_right &&
		   y > getPositionY() - bound_bottom &&
		   y < getPositionY() + bound_top) {return true;}
		return false;
	}
	
	public TextureRegion initTextureReg(String matName, int startX, int startY, int stopX, int stopY) {
		return new TextureRegion(new Texture(Gdx.files.internal(matName)), startX, startY, stopX, stopY);
	}

	public void dispose() {
		texture.dispose();
	}
}
