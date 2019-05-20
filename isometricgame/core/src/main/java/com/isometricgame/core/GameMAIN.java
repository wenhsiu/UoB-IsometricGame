package com.isometricgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;
import com.isometricgame.core.ui.InventoryUI;
import com.isometricgame.core.ui.InventoryItem.ItemTypeID;
import com.isometricgame.core.charactermanager.People;
import com.isometricgame.core.charactermanager.Property;
import com.isometricgame.core.charactermanager.TriggerPoint;

import com.isometricgame.core.dialogue.GameDialogue;

import java.util.ArrayList;
import java.util.List;

public class GameMAIN extends GameState {

	private GameManager gm;

	//-------- Map --------
	private TiledMap map;
	private IsometricTiledMapRenderer mapRenderer;

	private String noCoins;
	private BitmapFont coinCount; 

	//-------- Layers --------
	private TiledMapTileLayer transparentBlockedLayer;
	private TiledMapTileLayer baseObjLayer;

	//-------- Tiles (transparent) --------
	private float TileEdge;
	private float TileW;
	private float TileH;	

	//-------- Player --------
	private Player player;

	//-------- Characters --------
	private ArrayList<People> people;
	// Naming rule: <type>_<alias>
	private final String[] peopleName = {"Boss_org", //"Boss_drop", 
										 "Villager_1", "Villager_2", "Villager_3", "Villager_4",
										 "Monkey_1", "Monkey_2",
										 };						
	private final float[] pplX = {500, //1954, 
								  3000, 4000, 4065, 4517, 
								  5065, 5398,
								  }; 
	private final float[] pplY = {500, //-38, 
								  -1000, -1500, -1514, 1821, 
								  -2095, -2329,
								  }; 
	
	//-------- Objects-to-collect --------
	private ArrayList<Property> property;

	//-------- Mini-game Trigger Points --------
	private ArrayList<TriggerPoint> tgp;
    private final float[] tgpX = {1170, 5162, 6292, 3030, 
    							  5433, 7971,
    							  3000};
	private final float[] tgpY = {50, -1012, -649, 390,
								  -25, -2065,
								  1100};
	// Naming rule: <type>_<GAMENAME>
	// pb: PhoneBox, fb: FinalBoss
	private final String[] allStateName = {"pb_DRAGGAME1", "pb_DRAGGAME2", "pb_DRAGGAME3", "pb_DROPGAME1", 
										   "pb_AVOIDGAME", "pb_DROPGAME2",
										   "fb_FINALGAME"};
	//Define characters for each trigger point
	private final String[] guards = {"", "", "Penguin_4/Penguin_5/Penguin_6/Penguin_7/Penguin_8","",
									 "Penguin_1/Penguin_2/Penguin_3", "",
									 ""			
	};
	private final String[] grdX = {"", "", "6630/6570/6510/6450/6390", "",
								   "5680/5620/5560", "",								   
								   ""};
	private final String[] grdY = {"", "", "-690/-670/-650/-630/-610", "",
								   "-20/0/20", "",								   
								   ""};
	// Isometric parameters
	private final double theta = Math.toDegrees(Math.atan(0.5));

	//-------- Testing Fonts --------
	private BitmapFont bfont;
	private SpriteBatch textbatch;

	private String triggerText;

	public List<GameDialogue> dialogueList;

	private ShapeRenderer shapeRenderer;

	//Sound Effects 
	private Music coinSound; 
	private Music thud; 
	private Music scream; 
	
	public GameMAIN(GameManager gm) {
		super();
		this.gm = gm;
		dialogueList =  new ArrayList<>();
		
		initMapAndLayer();
		
		initProperties();
		initPeople();
		initTriggerPoint();
		initDialogueArray();
		
		player = gm.getPlayer();

		bfont = new BitmapFont();
		bfont.setScale(1, 1);
		bfont.setColor(Color.BLACK);

		textbatch = new SpriteBatch(); 

		coinSound = Gdx.audio.newMusic(Gdx.files.internal("coinSound.mp3")); 
		thud = Gdx.audio.newMusic(Gdx.files.internal("thud.mp3")); 
		scream = Gdx.audio.newMusic(Gdx.files.internal("scream1.mp3")); 

		coinCount = new BitmapFont(); 
		coinCount.setColor(Color.BLACK);
		coinCount.setScale((float) 1.2);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(173/255f, 216/255f, 255/255f, 0xff/255.0f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		mapRenderer.setView(cam);
		mapRenderer.render();	
		
		float x = player.getPositionX();
		float y = player.getPositionY();
		Vector2 playerNextPosition = player.getNextPosition();
		
		// Trigger mini games with phone boxes
		checkTriggerGame(x, y); 		

		System.out.println("Values of X and Y = " + x + " , " + y);

		cam.position.set(x, y, 0);

		gm.renderInventory(delta);

		// Add coins to inventory
		if(checkCoinCollisions(x, y)) {
			gm.inventoryAddCoin();
		}		

		checkVillagerCollisions(x, y);
		
		if(!isOnTheGround(playerNextPosition.x, playerNextPosition.y)) {			
			player.setFrozen(true);
		} else if (checkMapCollision(playerNextPosition.x, playerNextPosition.y)) {
			player.setSpeedFactor(-75);
			thud.play();
		}
		
		checkPeopleCollision();

		player.getBatch().setProjectionMatrix(cam.combined);
		
		renderPeople();
		renderProperty();
		renderTriggerPoint();

		player.render();

		shapeRenderer = new ShapeRenderer();

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
		for (int i = 0; i < dialogueList.size(); i++) {
			if(dialogueTriggerCheck(x, y, i) == true){
				bfont.draw(textbatch, dialogueList.get(i).getTextmessage(), 150, 150); 
			}
		}

			// TODO: Fix slot problem
			noCoins = "" + gm.inventoryGetItemNumber(ItemTypeID.COIN);
			if(!noCoins.equals("0") && !noCoins.equals("1")){
				if(gm.inventoryGetItemNumber(ItemTypeID.COIN) < 10){
					coinCount.draw(textbatch, noCoins, 80, 700 ); 
				}
				else {
					coinCount.draw(textbatch, noCoins, 61, 643); 
				}
			}

		textbatch.end();
		
		cam.update();
		player.setFrozen(false);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void show() {
		super.show();
		mapRenderer.setView(cam);
	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose() {
		mapRenderer.dispose();
		map.dispose();
		player.dispose();
		
		disposeProperty();
		disposeTriggerPoint();
		disposePeople();
	}
	
	
	private void initMapAndLayer() {

		// Map
		map = new TmxMapLoader().load("./Isometria/isometria.tmx");
		mapRenderer = new IsometricTiledMapRenderer(map);
		mapRenderer.setView(cam);

		// Transparent edge blocking layer
		transparentBlockedLayer = (TiledMapTileLayer) map.getLayers().get("Transparent");

		// Edge blocking later is not visible
		transparentBlockedLayer.setVisible(false);

		// BaseObjects
		baseObjLayer = (TiledMapTileLayer)map.getLayers().get("BaseObjects");

		TileW = transparentBlockedLayer.getTileWidth();
		TileH = transparentBlockedLayer.getTileHeight();
		TileEdge = (float) Math.sqrt(Math.pow(TileH / 2, 2) + Math.pow(TileW / 2, 2));			
	}
	
	// Handle People
	private void initPeople() {
		people = new ArrayList<People>();
		People p = null;
		String type;
		
		for(int i = 0; i < peopleName.length; i++) {
			float x = pplX[i];
			float y = pplY[i];
			type = peopleName[i].split("_")[0].toLowerCase();
			
			if(type.equals("boss")) {
				p = new Boss(x, y);
			} else if(type.equals("villager")) {
				p = new Villager(x, y);
			} else if(type.equals("penguin")) {
				p = new Penguin(x, y);
			} else if(type.equals("monkey")){
				p = new Monkey(x, y); 
			}
			
			p.create();			
			if(p != null) {
				people.add(p);
			}
		}		
	}
	
	// TODO: Do we need this?
	private void playerBounce(Player player) {
		player.setSpeedFactor(-50);
		Vector2 nextPos = player.getNextPosition();
		if(!checkMapCollision(nextPos.x, nextPos.y) && isOnTheGround(nextPos.x, nextPos.y) ) {
			player.animationUpdate(Gdx.graphics.getDeltaTime());
			return;
		}else {
			playerBounce(player);
		}
	}
			
	private void renderPeople() {
		for(int i = 0; i < people.size(); i++) {
			people.get(i).render();
			people.get(i).getBatch().setProjectionMatrix(cam.combined);
		}
	}
	
	private People getPeopleByName(String name) {
		for(int i = 0; i < people.size(); i++) {
			if(peopleName[i].equals(name)) {return people.get(i);}
		}
		return null;
	}
	
	/* private void checkPeopleCollision() {
		for(People p : people) {
			p.CollisionAction(checkVillagerMapCollision(p.getPositionX(), p.getPositionY(), 
						TileEdge, TileEdge)
					);
		}
	} */
	
	private void removePeopleByName(String name) {
		for(int i = 0; i < people.size(); i++) {
			if(peopleName[i].equals(name)) {
				people.remove(i);				
			}
		}
	}
	
	private void disposePeople() {
		for(int i = 0; i < people.size(); i++) {
			people.get(i).dispose();
		}
	}
	
	//Handle TriggerPoint
	private void initTriggerPoint() {
		tgp = new ArrayList<TriggerPoint>();
		for(int i = 0; i < tgpX.length; i++) {
			String type = allStateName[i].split("_")[0].toLowerCase();
			String name = allStateName[i].split("_")[1];			
			String[] grds = null;
			String[] x = null;
			String[] y = null;
			int grdNumber = 0;
			
			if(!guards[i].isEmpty() && !grdX[i].isEmpty() && !grdY[i].isEmpty()) {
				grds = guards[i].split("/");
				x = grdX[i].split("/");
				y = grdY[i].split("/");			
				grdNumber = Math.min(grds.length, Math.min(x.length, y.length));
			}
			
			if(type.equals("pb")) {
				PhoneBox pb = new PhoneBox(tgpX[i], tgpY[i], 1, gm, name, triggerText);
				for(int j = 0; j < grdNumber; j++) {
					pb.initGuards(grds[j].trim(), Float.parseFloat(x[j].trim()), Float.parseFloat(y[j].trim()));
				}
				tgp.add(pb);
			}else if(type.equals("fb")) {
				FinalBoss fb = new FinalBoss(tgpX[i], tgpY[i], 1, gm, name, triggerText);
				for(int j = 0; j < grdNumber; j++) {
					fb.initGuards(grds[j].trim(), Float.parseFloat(x[j].trim()), Float.parseFloat(y[j].trim()));
				}				
				tgp.add(fb);
			}
		}
	}
	
	private void renderTriggerPoint() {
		for(int i = 0; i < tgp.size(); i++) {
			tgp.get(i).updateTriggerPoint();
			tgp.get(i).getBatch().setProjectionMatrix(cam.combined);
			if(tgp.get(i).getTriggeredGame() == null || !tgp.get(i).getTriggeredGame().getPassState()) {
				renderTGPGuards(i); // Don't render guards if player passed this game
			}
		}
	}
	
	private void disposeTriggerPoint() {
		for(int i = 0; i < tgp.size(); i++) {
			tgp.get(i).dispose();
		}
	}
	
	private void renderTGPGuards(int index) {
		if(!guards[index].isEmpty()) {
			String[] gdNames = guards[index].split("/");
			for(int i = 0; i < gdNames.length; i++) {
				tgp.get(index).getGuardByName(gdNames[i]).render();
				tgp.get(index).getGuardByName(gdNames[i]).getBatch().setProjectionMatrix(cam.combined);
			}
		}
	}
	
	private void checkTriggerGame(float x, float y) {
		for(int i = 0; i < tgp.size(); i++) {	
			if(tgp.get(i).containPoint(x, y)) {
				if(!tgp.get(i).getTriggerred()) {
					tgp.get(i).triggerGame();
				}
			} else {
				tgp.get(i).setTriggerred(false);
			}
		}
	}

	// TODO: Do we need this?
	private void showTriggerText(float x, float y){
		for (int i = 0; i < tgp.size(); i++) {
			if(tgp.get(i).containPoint(x, y)){
				SpriteBatch textBatch; 
				BitmapFont screenText; 
				screenText = new BitmapFont(); 
				textBatch = new SpriteBatch(); 

				screenText.setColor(Color.BLACK);
				screenText.setScale(2, 2);

				textBatch.begin(); 
				screenText.draw(textBatch, tgp.get(i).getTriggerText(), x, y); 
				textBatch.end();
			}
		}
	}

	private void addDialogue(double maxx, double maxy, double minx, double miny, String message){
		GameDialogue newdialogue = new GameDialogue(maxx, maxy, minx, miny, message);
		dialogueList.add(newdialogue); 
	}

	private void initDialogueArray(){
		//-------- INTRO --------
		addDialogue(373, 20, 230 , -50, "Welcome to Isometria! And just in time, because the residents of the island are in trouble...");

		addDialogue(485, 21, 374, -95, "For some reason, they can't stop speaking in 1s and 0s - nobody can understand each other!");

		addDialogue(702, -9, 519 , -109, "But this has happened before, remember? At that time, all the mayor had to do was flip the town's switch on and off...");

		addDialogue(901, -10, 703 , -148, "The town was reset and everything went back to normal. Unfortunately, the mayor's away on a trip... you're our only hope!");

		addDialogue(1040, -33, 902 , -182, "Hmm, but since you're not an elected official, you're going to need to gain special access to the Town Hall...");

		addDialogue(1375, 33, 1058 , -222, "This means you'll need to make some calls to officials for access badges... hey, use the town's red telephone boxes!");

		addDialogue(1479, -55, 1376 , -249, "You might need to prove you know your binary, but I know you can do it! Get to the Town Hall, flip the switch and save Isometria!");

		//-------- ENTRANCE: One Dragon --------
		addDialogue(1718, -229, 1578, -320, "TOWNSPERSON: Help us! We... 100010001001001... can't... 1101010111...");

		//-------- ENTRANCE: Two Robins --------
		addDialogue(2327, -264, 2177, -361, "TOWNSPEOPLE: 100011000... collect the coins... 10101100111... money can... 00011111... buy you extra time...");

		//-------- NEAR PHONEBOX: Two Chickens --------
		addDialogue(2688, 188, 2423, 41, "11101101... keep going... 10101010101... save us... 1111... get to the Town Hall...");

		//-------- BEFORE FOREST: Three Chickens --------
		addDialogue(2773, -478, 2372, -680 , "TOWNSPEOPLE: 100101001... avoid the roaming guards... 101110111... hurry, save Isometria!");

		//--------  --------
		addDialogue(6237, -2237, 5667, -2537, "1110101010010111!!!!");

		//--------  --------
		addDialogue(7071, -2290, 6606, -2595, "11101010101??!");

		//--------  --------
		addDialogue(6527, -1638, 6162, -1813, "1110101001011!?");

		//-------- NEAR BIG CAMPFIRE: Two Dragons --------
		addDialogue(5886, -1327, 5569, -1474, "11101110?! Phonebox? 00100001... over there!");

		//--------  --------
		addDialogue(5775, -751, 5563, -886, "Go... 110110110... left... 0000... right... 1111... straight...");

		//--------  --------
		addDialogue(6559, -650, 6234, -822, "Hey you! 110110110! Are you authorised? 11110000!");

		//--------  --------
		addDialogue(6925, -1112, 6644, -1237, "Phone box? 00010000... I think... 1110! Inside the maze! 11111111!");

		//--------  --------
		addDialogue(7428, -1351, 7221, -1471, "Be careful! 11010101... maze,,,0001!");

		//--------  --------
		addDialogue(5558, -395, 5242, -561, "Watch out... 101010111... spikes... 11010011...");

		//--------  --------
		addDialogue(5580, -54, 5508, -126, "0011? 11110000? 101010100!");

		//--------  --------
		addDialogue(4340, -43, 4012, -205, "0000! 1111! Town Square? 0011011... it's up ahead... 11101111?");

		//--------  --------
		addDialogue(4225, 148, 4007, 24, "00101010... I couldn't get access... 11101011?!");

		//--------  --------
		addDialogue(3967, 496, 3712, 373, "111010010101111? 11110111!");

		//-------- Pre-Boss Check --------
		addDialogue(3860, 870, 3430, 655, "Are you sure you have enough badges to access the Town Hall? Make sure you do!");

		//--------  --------
		addDialogue(3530, 1109, 3276, 978, "YA'LL READY FOR THIS");

	}

	private Boolean dialogueTriggerCheck(double currentX, double currentY, int i) {
		if(dialogueList.get(i).getMinx() < currentX  && dialogueList.get(i).getMiny() < currentY && dialogueList.get(i).getMaxx() > currentX && dialogueList.get(i).getMaxy() > currentY){
			return true; 
		}
		return false; 
	}

	// Handle Property
	private void initProperties() {
		property = new ArrayList<Property>();		
		initCoins();
	}
	
	private void initCoins(){
		
		// Add the X and Y Coordinates to the following arrays. The coin will then be created. 
		int[] xCoordCoins = new int[] {813, 1337, 1702, 2250, 2582, 2630, 2822, 2664, 2988, 3012, 3760, 4497, 5251, 5579, 3836, 4311, 3977, 4750, 5699, 6232, 6401, 7031, 6647, 5781, 5514, 7449, 8209, 8699, 7849, 7868, 5334, 4943, 5082}; 
		int[] yCooordCoins = new int[] {-94, -152, -335, -356, -130, 87, 294, -667, -881, -1245, -1619, -1769, -1832, -2239, 421, 600, 15, -344, -596, -2531, -2615, -2300, -1887, -1454, -917, -1445, -1418, -1846, -1681, -1975, -481, 0, 568}; 

		for (int i = 0; i < xCoordCoins.length; i++) {
			Coin coin = new Coin(xCoordCoins[i], yCooordCoins[i]); 
			coin.setBoundary(150, 150, 150, 150);
			property.add(coin); 
			coin.create();
		}
	}

	private void renderProperty() {
		for(int i = 0; i < property.size(); i++) {
			property.get(i).render();
			property.get(i).getBatch().setProjectionMatrix(cam.combined);
		}
	}

	private void disposeProperty() {
		for(int i = 0; i < property.size(); i++) {
			property.get(i).dispose();
		}
	} 

	// Collisions with inventory, boolean for later checking
	private boolean checkCoinCollisions(float x, float y) {
		for(int i = 0; i < property.size(); i++) {
			if(property.get(i).containPoint(x, y)) {
				property.remove(i);
				coinSound.play();
				return true;
			}
		}
		return false;
	}

	// Remove the coins if the villagers are collided with. 
	private void checkVillagerCollisions(float x, float y) {
		for(int i = 0; i < people.size(); i++) {
			if(people.get(i).containPoint(x, y)) {
				scream.play();
				scream.setVolume(50);
				if(people.get(i).isBumpingInto()) {
					gm.inventoryRemoveOneCoin();
				}
			}
		}
	}

	public boolean checkMapCollision(float x, float y) {		
		Vector2 v = rotateCoord(x, y);
		int iso_x = (int) (v.x / TileEdge);
		int iso_y = (int) (v.y / TileEdge);

		Cell blockedCell = transparentBlockedLayer.getCell(iso_x, iso_y);	
		return (blockedCell != null && blockedCell.getTile() != null);
	}
	
	//check if on the BaseObjects layer
	private boolean isOnTheGround(float x, float y) {
		if(x < 0) return false;
		
		Vector2 v = rotateCoord(x, y);
		int iso_x = (int)(v.x/ TileEdge);
		int iso_y = (int)(v.y/ TileEdge);

		Cell c = baseObjLayer.getCell(iso_x, iso_y);
		return (c != null && c.getTile() != null);
	}

	private Vector2 rotateCoord(float x, float y) {
		float tmp_x ;
		float tmp_y;
		float dx;
		float dy;
		
		double len = Math.sqrt(x * x + y * y);
		double alpha = Math.toDegrees(Math.atan(Math.abs(y) / x)); // Map: down is positive
		if(y > 0) {alpha *= -1;}
		double beta = 90 - 2*theta;
		
		Vector2 v = new Vector2();
		tmp_x = (float) (Math.cos(Math.PI * (theta - alpha) / 180) * len);
		tmp_y = (float) (Math.cos(Math.PI * (theta + alpha) / 180) * len);
		
		dx = (float)(len*Math.sin(Math.PI*(theta-alpha)/180)*Math.tan(Math.PI*beta/180));
		dy = (float)(len*Math.sin(Math.PI*(theta+alpha)/180)*Math.tan(Math.PI*beta/180));
		
		v.x = tmp_x - dx;
		v.y = tmp_y - dy;
		
		return v;
	}

	public boolean checkVillagerMapCollision(float x, float y, float tilewidth, float tileheight) {
		int iso_x;
		int iso_y;
		Vector2 v = rotateCoord(x, y);

		iso_x = (int) (v.x / tilewidth);
		iso_y = (int) (v.y / tileheight);
		
		// Changed this from IsCellBlocked
		return isCellBlockedForVillager(iso_x, iso_y);
	}

	// Specifically for isCellBlocked
	private boolean isCellBlockedForVillager(int iso_x, int iso_y) {
		Cell cell = transparentBlockedLayer.getCell(iso_x, iso_y);		
		return (cell != null && cell.getTile() != null);
	}

	private void checkPeopleCollision(){
		for (int i = 0; i < people.size(); i++) {
			People p = people.get(i); 
			p.CollisionAction(checkVillagerMapCollision(p.getPositionX(), p.getPositionY(), TileEdge, TileEdge));	
		}
	}
}
