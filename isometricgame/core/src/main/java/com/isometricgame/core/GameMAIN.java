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

import com.isometricgame.core.charactermanager.People;
import com.isometricgame.core.charactermanager.Property;
import com.isometricgame.core.charactermanager.TriggerPoint;

import com.isometricgame.core.dialogue.GameDialogue;

import com.isometricgame.core.ui.InventoryItem;
import com.isometricgame.core.ui.InventoryItem.ItemTypeID;
import com.isometricgame.core.ui.InventoryItemFactory;
import com.isometricgame.core.ui.InventoryUI;
import com.isometricgame.core.ui.PlayerHUD;

import java.util.ArrayList;
import java.util.List;

public class GameMAIN extends GameState {

	private GameManager gm;

	// private int testmedal;

	//-------- Map --------
	private TiledMap map;
	private IsometricTiledMapRenderer mapRenderer;

	//-------- Inventory --------
	public final OrthographicCamera hudcam;
	private PlayerHUD playerHUD;
	private InventoryUI inventoryUI;

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
	private final String[] peopleName = {"Boss_org", "Boss_drop", 
										 "Villager_1", "Villager_2", "Villager_3", "Villager_4", "Villager_5", "Villager_6",
										 };						
	private final float[] pplX = {500, 1954, 
								  3000, 4000, 4065, 4517, 5065, 5398,
								  }; 
	private final float[] pplY = {500, -38, 
								  -1000, -1500, -1514, 1821, -2095, -2329,
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
										   "pb_AVOIDGAME", "pb_DROPGAME2-2",
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
	private Label labeltest;
	private LabelStyle labelstyle;

	private String triggerText;

	public List<GameDialogue> dialogueList;

	private ShapeRenderer shapeRenderer;
	private ShapeRenderer helpRenderer;

	//Sound Effects 
	private Music coinSound; 
	private Music thud; 
	private Music scream; 
	
	public GameMAIN(GameManager gm) {
		super();
		this.gm = gm;
		dialogueList =  new ArrayList<GameDialogue>();
		
		initMapAndLayer();

		// PlayerHUD
		hudcam = new OrthographicCamera(width, height);
		hudcam.translate(width / 2, height / 2);
		hudcam.setToOrtho(true);
		playerHUD = new PlayerHUD(hudcam);
		
		// Import inventory
		inventoryUI = playerHUD.getInventoryUI();
		
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
		//show the trigger text if there is any to show. 
		/* showTriggerText(x, y); */

		System.out.println("Values of X and Y = " + x + " , " + y);

		cam.position.set(x, y, 0);

		playerHUD.render(delta);
		hudcam.update();	
		
		// Add coins to inventory
		if(checkPropertyCollisions(x, y)) {
			InventoryItemFactory factory = InventoryItemFactory.getInstance();
			InventoryItem item = factory.getInventoryItem(ItemTypeID.COIN);			
			inventoryUI.addItemToInventory(item, "COIN");
			System.out.println("Got here COIN.");
		}		

		checkVillagerCollisions(x, y);
		
		if(!isOnTheGround(playerNextPosition.x, playerNextPosition.y)) {			
			player.setFrozen(true);
		}else if(checkMapCollision(playerNextPosition.x, playerNextPosition.y)) {
			player.setSpeedFactor(-75);
			thud.play();
		}		

		//Probably should iterate through this. 
		getPeopleByName("Villager_1").CollisionAction(
			checkVillagerMapCollision(
				getPeopleByName("Villager_1").getPositionX(),
				getPeopleByName("Villager_1").getPositionY(), 
				TileEdge, TileEdge));
		
		getPeopleByName("Villager_2").CollisionAction(
			checkVillagerMapCollision(
				getPeopleByName("Villager_2").getPositionX(),
				getPeopleByName("Villager_2").getPositionY(), 
				TileEdge, TileEdge));

		getPeopleByName("Villager_3").CollisionAction(
			checkVillagerMapCollision(
				getPeopleByName("Villager_3").getPositionX(),
				getPeopleByName("Villager_3").getPositionY(), 
				TileEdge, TileEdge));

		getPeopleByName("Villager_4").CollisionAction(
			checkVillagerMapCollision(
				getPeopleByName("Villager_4").getPositionX(),
				getPeopleByName("Villager_4").getPositionY(), 
				TileEdge, TileEdge));

		getPeopleByName("Villager_5").CollisionAction(
			checkVillagerMapCollision(
				getPeopleByName("Villager_5").getPositionX(),
				getPeopleByName("Villager_5").getPositionY(), 
				TileEdge, TileEdge));

		getPeopleByName("Villager_6").CollisionAction(
			checkVillagerMapCollision(
				getPeopleByName("Villager_6").getPositionX(),
				getPeopleByName("Villager_6").getPositionY(), 
				TileEdge, TileEdge));


		player.getBatch().setProjectionMatrix(cam.combined);
		
		renderPeople();
		renderProperty();
		renderTriggerPoint();

		player.render();

		//Repeat this infront of all of the objects. 
		/* if(x < 230 && x > 228 && y < -35){
			testbatch.begin();
			bfont.draw(testbatch, message, 300, 300);
			labeltest.draw(testbatch, 100);
			testbatch.end();
		} */

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
			// dialogueTriggerCheck(testbatch, x, y);
			for (int i = 0; i < dialogueList.size(); i++) {
				if(dialogueTriggerCheck(x, y, i) == true){
					bfont.draw(textbatch, dialogueList.get(i).getTextmessage(), 150, 150); 
				}
			}
		textbatch.end();
		
		cam.update();
		player.setFrozen(false);

		// Check the passed state of everything in the game state manager, if its true, add a coin.
		gm.checkPassedState(inventoryUI);
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
		playerHUD.dispose();
		
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
			}
			
			p.create();			
			if(p != null) {
				people.add(p);
			}
		}		
	}
	
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
				renderTGPGuards(i);//Don't render guards if player passed this game
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
		// Add dialogue into the array through this function. 
		addDialogue(300, 300, -300 , -300, "Narrator: Welcome to Isometria!!");

		//Entrance Dragon
		addDialogue(469, 0, 347, -50, "Dragon: You've arrived! Thanks goodness.. 100010001001001, Something is wrong with the people of Isometria...");

		//Entrance Robin
		addDialogue(700, -16, 539, -77, "Robins: 100011000, Tweet tweet! Solve the puzzles, save the our friends!");
		
		// Triple Dragons Entrance. 

		addDialogue(892, -3, 705, -92, "Dragons: Use the phoneboxes, solve the puzzles, 100100100101 avoid the penguins... ");

		//Triple Chickens before Forest 

		addDialogue(2773, -478, 2372, -680 , "Chickens: Squawk, squawk! 100010100100101 Be careful! The forest is dark... And full of terrors.");


		addDialogue(2041, -461, -1826, -616, "HELLO WORLD");

		// Deters player from final boss if not enough badges are collected
		// TODO: only add dialogue if badge count is < 3
		// TODO: Lizzie will do this
		addDialogue(3860, 870, 3430 , 655, "Narrator: The boss ahead is hard... Make sure you're prepared!!! ");
		
		// NPC #1
		// Unpassed
		//addDialogue(maxx, maxy, minx, miny, "Groundskeeper Nystrom�� 0011 0101�� tile puzzle��1111 1101�� anyone� solved it yet�� 1010�� reward�� 0101 0010�� Wilhelm Place.");
		// Passed
		//addDialogue(maxx, maxy, minx, miny, "1100 1010! 0000 0010 1010 1101!");

		// Pre-Mini Game #1 Dialogue (should appear once the player has triggered NPC #1 dialogue)
		// addDialogue(maxx, maxy, minx, miny, "All right, so it looks like you have to solve tile puzzles at Wilhelm Place. The groundskeeper, Mr. Nystrom, has been known to quiz people for entrance to the grounds. Now that everyone� speaking in binary, maybe his infamous tile puzzles will be easier to solve�� All you have to do is drag the tiles to the correct boxes that form the solution to the puzzles. There are three questions, so take your time and try to answer them correctly on the first go. Give it a go!");

		// NPC #2
		// Unpassed
		//addDialogue(maxx, maxy, minx, miny, "1101 0101 1011 1111�� special items at Gottfried Gardens�� 0000 1011 1010 1011�� keep an eye out for rain drops��");
		// Passed
		//addDialogue(maxx, maxy, minx, miny, "0010 1010 0100 0000! 1111 0100 0101 0100!");

		// Pre-Mini Game #2 Dialogue (should appear once the player has triggered NPC #2 dialogue)
		// addDialogue(maxx, maxy, minx, miny, "Hmm, so this one might be a little trickier�� I think we need collect raindrops that represent the chosen binary number. Then we�� water the plants? I� not really a gardener so I don� fully understand it, but let� try it out. Be careful, though, it looks like there� a certain time limit for how long it rains. Try not to make too many mistakes, three strikes and you�e out!");

		// NPC #3
		// Unpassed
		//addDialogue(maxx, maxy, minx, miny, "1111 0011 1100�� watch out for the guards�� 1110 1011�� save the chickens�� 0101 0001 1000... Leibniz Square.");
		// Passed
		//addDialogue(maxx, maxy, minx, miny, "0001 0000 0001!");

		// Pre-Mini Game #3 Dialogue (should appear once the player has triggered NPC #3 dialogue)
		// addDialogue(maxx, maxy, minx, miny, "Oh no, it� getting worse�� people are becoming more and more incomprehensible! We have to hurry to Leibniz Square. Okay, so there are guards we need to watch out for�� and a treasure�� It� risky, but let� just try avoiding the guards for now and see if we can find this so-called treasure. It also looks like there� a certain time limit for how long the guards patrol a certain aisle. I�l keep a look-out and let you know which aisle they�l check next, so just avoid that aisle so they can� see you.");

		// Post-Mini Game #3 Dialogue (should appear once the player has successfully finished Mini Game #3 and returned to the map)
		// addDialogue(maxx, maxy, minx, miny, "Amazing! We�e collected all the Binary Badges we need. Now we can go to the Town Square and flip the switch. Let� go!");

		// Pre-Final Boss Dialogue #1 (should appear once the player hits the bounds of the entrance of the last area)
		// addDialogue(maxx, maxy, minx, miny, "The mayor� here! But why hasn� he flipped the switch? I think you should go up and speak to him��");

		// Boss
		//addDialogue(maxx, maxy, minx, miny, "0001! 1101 0101 1011 1111? 1010 1111 1110 0011��");

		// Pre-Final Boss Battle Dialogue #2 (should appear after the Boss dialogue)
		// TODO: Decide if we want this to be in a dialogue box or a separate screen that ties up the story - either works
		// addDialogue(maxx,maxx maxy, minx, miny, "Oh no, I think the mayor� forgotten how to access the switch! It� okay, you�e done something similar before. Find your way through the maze and collect the numbers that represent the binary number. Try to do this quickly, we don� have much time!");

	}

	private Boolean dialogueTriggerCheck(double currentX, double currentY, int i) {

			// System.out.println("HELLO TRIGGER CHECK " + dialogueList.get(i).getMinx());

			if(dialogueList.get(i).getMinx() < currentX  && dialogueList.get(i).getMiny() < currentY && dialogueList.get(i).getMaxx() > currentX && dialogueList.get(i).getMaxy() > currentY){
				// System.out.println("true"); 
				return true; 
			}
			return false; 
	}

	//Handle Property
	private void initProperties() {
		property = new ArrayList<Property>();		
		initCoins();
	}
	
	private void initCoins(){
		
		// Add the X and Y Coordinates to the following arrays. The coin will then be created. 
		int[] xCoordCoins = new int[]{813, 1337, 1702, 2250, 2582, 2630, 2822, 2664, 2988, 3012, 3760, 4497, 5251, 5579, 3836, 4311, 3977, 4750, 5699, 6232, 6401, 7031, 6647, 5781, 5514, 7449, 8209, 8699, 7849, 7868, 5334, 4943, 5082}; 
		int[] yCooordCoins = new int[]{-94, -152, -335, -356, -130, 87, 294, -667, -881, -1245, -1619, -1769, -1832, -2239, 421, 600, 15, -344, -596, -2531, -2615, -2300, -1887, -1454, -917, -1445, -1418, -1846, -1681, -1975, -481, 0, 568}; 

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
	private boolean checkPropertyCollisions(float x, float y) {
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
	// TODO: Ask what this means???
	private void checkVillagerCollisions(float x, float y) {
		for(int i = 0; i < people.size(); i++) {
			if(people.get(i).containPoint(x, y)) {
				scream.play();
				scream.setVolume(50);
				removeItemInInventory();
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

	public void putMedalInInventory() {
		InventoryItemFactory factory = InventoryItemFactory.getInstance();
		InventoryItem item = factory.getInventoryItem(ItemTypeID.MEDAL);			
		inventoryUI.addItemToInventory(item, "MEDAL");
	}

	// TODO: Lizzie look at this
	public void removeItemInInventory(){

		//InventoryItemFactory factory = InventoryItemFactory.getInstance();

		Table inventoryTable  =  inventoryUI.getInventorySlotTable(); 
		inventoryUI.removeInventoryItems("COIN", inventoryTable); 

	}

}











