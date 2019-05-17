package com.isometricgame.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

	private int testmedal;

	// Map
	private TiledMap map;
	private IsometricTiledMapRenderer mapRenderer;

	// Inventory
	public final OrthographicCamera hudcam;
	private PlayerHUD playerHUD;
	private InventoryUI inventoryUI;

	// Layers	
	private TiledMapTileLayer transparentBlockedLayer;
	private TiledMapTileLayer baseObjLayer;

	// Tiles -- transparent
	private float TileEdge;
	private float TileW;
	private float TileH;	

	// Player
	private Player player;

	// Characters
	private ArrayList<People> people;
	// Naming rule: <type>_<alias>
	private final String[] peopleName = {"Boss_org", "Boss_drop", 
										 "Villager_1", "Villager_2", "Villager_3", "Villager_4", "Villager_5", "Villager_6",
										 "Penguin_1", "Penguin_2", "Penguin_3",
										 "Penguin_4", "Penguin_5", "Penguin_6", "Penguin_7", "Penguin_8"};						
	private final float[] pplX = {500, 1954, 
								  3000, 4000, 4065, 4517, 5065, 5398,
								  5680, 5620, 5560,
								  6630, 6570, 6510, 6450, 6390}; 
	private final float[] pplY = {500, -38, 
								  -1000, -1500, -1514, 1821, -2095, -2329,
								  -20, 0, 20,
								  -690, -670, -650, -630, -610}; 
	
	// Object to collect
	private ArrayList<Property> property;
	private final int coinNumber = 20;

	// Mini-game trigger points
	private ArrayList<TriggerPoint> tgp;
    private final float[] tgpX = {1170, 1880, 1930, 3030, 5463, 6291, 8183};
	private final float[] tgpY = {50, -10, 840, 390, -22, -660, -1887};
	private final String[] allStateName = {"MINIGAME3", "MINIGAME2", "MINIGAME3", "FINALGAME", "MINIGAME2", "MINIGAME1", "MINIGAME3" };

	// Isometric parameters
	private final double theta = Math.toDegrees(Math.atan(0.5));

	// Testing fonts
	private BitmapFont bfont;
	private static String message = "Welcome to Isometria!";
	private SpriteBatch textbatch;
	private Label labeltest;
	private LabelStyle labelstyle;

	private String triggerText;

	public List<GameDialogue> dialogueList;

	private ShapeRenderer shapeRenderer;
  
	
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
				
		inventoryUI = playerHUD.getInventoryUI();
		
		initProperties();
		initPeople();
		initTriggerPoint();
		initDialogueArray();
		
		player = gm.getPlayer();

		bfont = new BitmapFont();
        bfont.setColor(Color.BLACK);
		bfont.setScale(1, 1);

		textbatch = new SpriteBatch();

		labelstyle = new LabelStyle(bfont, Color.BLACK);

        labeltest = new Label("Hi please work", labelstyle);
        labeltest.setPosition(300, 50);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(173/255f, 216/255f, 255/255f, 0xff/255.0f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		mapRenderer.setView(cam);
		mapRenderer.render();	
		
//		GameDrop testdrop = new GameDrop(gm);
		
		float x = player.getPositionX();
		float y = player.getPositionY();
		Vector2 playerNextPosition = player.getNextPosition();

/**/System.out.println(x + "  " + y);		
		
		// Trigger mini games with phone boxes
		checkTriggerGame(x, y); 		
		//show the trigger text if there is any to show. 
		/* showTriggerText(x, y); */

		//System.out.println("Values of X and Y = " + x + "  " + y);

		combineCameraPeople();
		combineCameraProperty();
		combineCameraTriggerPoint();		

		cam.position.set(x, y, 0);

		playerHUD.render(delta);
		hudcam.update();	
		
		// Add coins to inventory
		if(checkPropertyCollisions(x, y)) {
			InventoryItemFactory factory = InventoryItemFactory.getInstance();
			InventoryItem item = factory.getInventoryItem(ItemTypeID.COIN);			
			inventoryUI.addItemToInventory(item, "COIN");
		}		

		// Add medals to inventory
/*		if(testdrop.getMedal()) {
			System.out.println("Is this triggered?");
			if(testmedal == 0) {
				System.out.println("Something is happening");
				putMedalInInventory();
				testmedal++;
			}
		}
*/
		checkVillagerCollisions(x, y);
		
		if(!isOnTheGround(playerNextPosition.x, playerNextPosition.y)) {			
			player.setFrozen(true);
		}else if(checkMapCollision(playerNextPosition.x, playerNextPosition.y)) {
			player.setSpeedFactor(-75);
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
			//dialogueTriggerCheck(testbatch, x, y);
			for (int i = 0; i < dialogueList.size(); i++) {
				if(dialogueTriggerCheck(x, y, i) == true){
					bfont.draw(textbatch, dialogueList.get(i).getTextmessage(), 150, 150); 
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
		playerHUD.dispose();
		
		disposeProperty();
		disposeTriggerPoint();
		disposePeople();
	}
	
	private void initMapAndLayer() {

		//Map
		map = new TmxMapLoader().load("./Isometria/isometria.tmx");
		mapRenderer = new IsometricTiledMapRenderer(map);
		mapRenderer.setView(cam);

		// Blocked edge layer is transparent.
		transparentBlockedLayer = (TiledMapTileLayer) map.getLayers().get("Transparent");

		// Blocked layer is blocking but is not visible.
		transparentBlockedLayer.setVisible(false);

		//BaseObjects
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
		
	private void combineCameraPeople() {
		for(int i = 0; i < people.size(); i++) {
			people.get(i).getBatch().setProjectionMatrix(cam.combined);
		}
	}
	
	private void renderPeople() {
		for(int i = 0; i < people.size(); i++) {
			people.get(i).render();
		}
	}
	
	private People getPeopleByName(String name) {
		for(int i = 0; i < peopleName.length; i++) {
			if(peopleName[i].equals(name)) {return people.get(i);}
		}
		return null;
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
			tgp.add(new PhoneBox(tgpX[i], tgpY[i], 1, gm, allStateName[i], triggerText));
		}
	}
	
	private void combineCameraTriggerPoint() {
		for(int i = 0; i < tgp.size(); i++) {
			tgp.get(i).getBatch().setProjectionMatrix(cam.combined);
		}
	}
	
	private void renderTriggerPoint() {
		for(int i = 0; i < tgp.size(); i++) {
			tgp.get(i).updateTriggerPoint();
		}
	}
	
	private void disposeTriggerPoint() {
		for(int i = 0; i < tgp.size(); i++) {
			tgp.get(i).dispose();
		}
	}
	
	private void checkTriggerGame(float x, float y) {
		for(int i = 0; i < tgp.size(); i++) {	
			if(tgp.get(i).containPoint(x, y)) {
				if(/*tgp.get(i).getTriggeredGame().getPassState() == false && */!tgp.get(i).getTriggerred()) {
					tgp.get(i).triggerGame();
				}
				
			}else {
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
		//add dialogue into the array through this function. 
		addDialogue(300, 300, -300 , -300, "Narrator: Welcome to Isometria!!");

		//Entrance Dragon
		addDialogue(469, 0, 347, -50, "Dragon: You've arrived! Thanks goodness.. 100010001001001, Something is wrong with the people of Isometria...");

		//Entrance Robin
		addDialogue(700, -16, 539, -77, "Robins: 100011000, Tweet tweet! Solve the puzzles, save the our friends!");
		
		//Triple dragons Entrance. 

		addDialogue(892, -3, 705, -92, "Dragons: Use the phoneboxes, solve the puzzles, 100100100101 avoid the penguins... ");


		//Triple Chickens before Forest 

		addDialogue(2773, -478, 2372, -680 , "Chickens: Squawk, squawk! 100010100100101 Be careful! The forest is dark... And full of terrors.");

		


		// Deters player from final boss if not enough badges are collected
		// TODO: only add dialogue if badge count is < 3
		addDialogue(3860, 870, 3430 , 655, "I think we need to collect more badges first...");
		
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
		// addDialogue(maxx, maxy, minx, miny, "Oh no, I think the mayor� forgotten how to access the switch! It� okay, you�e done something similar before. Find your way through the maze and collect the numbers that represent the binary number. Try to do this quickly, we don� have much time!");

		System.out.println("Hello init dialogue array.");
	}

	private Boolean dialogueTriggerCheck(double currentX, double currentY, int i){

			//System.out.println("HELLO TRIGGER CHECK " + dialogueList.get(i).getMinx());

			if(dialogueList.get(i).getMinx() < currentX  && dialogueList.get(i).getMiny() < currentY && dialogueList.get(i).getMaxx() > currentX && dialogueList.get(i).getMaxy() > currentY){
				//System.out.println("true"); 
				return true; 
			}
			return false; 
	}


	//Handle Property
	private void initProperties() {
		property = new ArrayList<Property>();		
		initCoins();
	}
	
	private void initCoins() {
		float x;
		float y;
		
		for(int i = 0; i < coinNumber; i++) {
			//Ensure coins will be on the base layer
			do{
				x = MathUtils.random(0, baseObjLayer.getWidth()*TileW);
				y = MathUtils.random(0, baseObjLayer.getHeight()*TileH);
			}while(!isOnTheGround(x, y) || checkMapCollision(x, y));			
			
			Coin c = new Coin(x, y);

			c.setBoundary(150, 150, 150, 150); //extend the bottom so that George can collect them easily
			property.add(c); 
			c.create();
		}
	}

	private void renderProperty() {
		for(int i = 0; i < property.size(); i++) {
			property.get(i).render();
		}
	}

	private void combineCameraProperty() {
		for(int i = 0; i < property.size(); i++) {
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
				return true;
			}
		}
		return false;
	}

	// Lizzie test collisions with villager
		private void checkVillagerCollisions(float x, float y) {
			for(int i = 0; i < people.size(); i++) {
				if(people.get(i).containPoint(x, y)) {
					playerHUD.DialogOn();
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
	
	//check if on the BaseObjectives layer
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
		
		v.x = tmp_x-dx;
		v.y = tmp_y-dy;
		
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

}











