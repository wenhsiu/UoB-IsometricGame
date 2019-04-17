package characterManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Property {
	
	protected float posX;
	protected float posY;
	private float sizeX;
	private float sizeY;
	private float boundTop;
	private float boundBottom;
	private float boundRight;
	private float boundLeft;
	protected int speed;
	private int speedLimit = 300;
	protected Texture texture;
	protected SpriteBatch batch;
	protected TextureRegion region;
	private float scale;
	
	public Property(float x, float y, float scale) {
		posX = x;
		posY = y;
		this.scale = scale;
	}
	
	public abstract void animationInit();
	public abstract void animationUpdate(float dt);
	public abstract void render();
	
	public void initProperty(String materials, int sx, int sy, int ex, int ey) {
		texture = new Texture(Gdx.files.internal(materials));
		batch = new SpriteBatch();
		region = new TextureRegion(texture, sx, sy, ex, ey);
		sizeX = (ex-sx)*scale;
		sizeY = (ey-sy)*scale;
	}
	
	public void characterUpdate(float nx, float ny) {
		batch.begin();
		batch.draw(region, nx, ny, sizeX, sizeY);
		batch.end();
	}
	
	public float getPositionX() {return posX + sizeX/2;}	
	public float getPositionY() {return posY + sizeY/2;}	
	public float getSizeX() {return sizeX;}	
	public float getSizeY() {return sizeY;}
	public int getSpeed() {return speed;}
	public void setSpeed(int newSpeed) {speed = Math.min(newSpeed, speedLimit);}
	public int getSpeedLimit() {return speedLimit;}
	
	public void setBoundary(float top, float bottom, float right, float left) {
		boundTop = top*scale;
		boundBottom = bottom*scale;
		boundRight = right*scale;
		boundLeft = left*scale;
	}
	
	public SpriteBatch getBatch() {return batch;}
	
	public boolean containPoint(float x, float y) {
		if(x > getPositionX() - boundLeft &&
		   x < getPositionX() + boundRight &&
		   y > getPositionY() - boundBottom &&
		   y < getPositionY() + boundTop) {return true;}
		return false;
	}
	
	public TextureRegion initTextureReg(String matName, int startX, int startY, int stopX, int stopY) {
		return new TextureRegion(new Texture(Gdx.files.internal(matName)), startX, startY, stopX, stopY);
	}

	public void dispose() {
		texture.dispose();
	}
}
