package com.isometricgame.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.isometricgame.core.Villager;
import com.isometricgame.core.charactermanager.People;
import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;
import com.isometricgame.core.clickdrag.Puzzles;
import com.isometricgame.core.GameAvoid;
import com.isometricgame.core.maze.GameMaze;
import com.isometricgame.core.ui.InventoryUI;



import java.util.ArrayList; 
import java.util.List;


/* 

To compile this set of testing, use the following. 

javac -d . -cp ~/.m2/repository/com/badlogicgames/gdx/gdx/1.9.9/gdx-1.9.9.jar:/home/dev1/GitProjects/IsometricGame/isometricgame/core/target/isometricgame-core-1.0-SNAPSHOT.jar Test.java

java -ea -cp ~/.m2/repository/com/badlogicgames/gdx/gdx/1.9.9/gdx-1.9.9.jar:/home/dev1/GitProjects/IsometricGame/isometricgame/core/target/isometricgame-core-1.0-SNAPSHOT.jar com.isometricgame.core.Test

javac -d . -cp ~/.m2/repository/com/badlogicgames/gdx/gdx/1.9.9/gdx-1.9.9.jar:/Users/wenhsiuhsu/Desktop/UoB\ computer\ science/Group\ project/IsometricGame/isometricgame/core/target/isometricgame-core-1.0-SNAPSHOT.jar Test.java

java -ea -cp ~/.m2/repository/com/badlogicgames/gdx/gdx/1.9.9/gdx-1.9.9.jar:/Users/wenhsiuhsu/Desktop/UoB\ computer\ science/Group\ project/IsometricGame/isometricgame/core/target/isometricgame-core-1.0-SNAPSHOT.jar com/isometricgame/core/Test

*/


/**
 * Test
 */
public class Test {

    public static Texture backgroundTest; 
    private static GameManager gm;
    private static int mazeCnt = 0;
    private static 	List<Integer> mazeScore = new ArrayList<>();



    public static void main(String[] args) {

  
        
        testVillager();

        TestRandNum();

        Testgeneratebinarynumber();

        Testgeneratetargets();

        TestCurrentScore();

        testMazeGameCollection();

        testCheckScore();

        testCheckScore();

        testInventoryTime();

    
        System.out.println("All Tests Passed Successfully");
    }

   

    //Villager inhereits all of the functionality of people 
    private static void testVillager(){
        Villager testVillager = new Villager(10, 10); 

        //Check initial setting of the villagers positions. 
        assert(testVillager.initial_x() == 10); 
        assert(testVillager.initial_y() == 10); 
        assert(testVillager.getPositionX() == 10); 
        assert(testVillager.getPositionY() == 10); 
        System.out.println("villagerTest - Status: Passed");

    }


    /* Testing avoid game */

   private static void Testgeneratebinarynumber()  {
    GameAvoid.generatebinarynumber();
    assert(GameAvoid.target.length() == 4);

    assert(GameAvoid.target.charAt(0) == '1' || GameAvoid.target.charAt(0) == '0');
    assert(GameAvoid.target.charAt(1) == '1' || GameAvoid.target.charAt(1) == '0');
    assert(GameAvoid.target.charAt(2) == '1' || GameAvoid.target.charAt(2) == '0');
    assert(GameAvoid.target.charAt(3) == '1' || GameAvoid.target.charAt(3) == '0');

    System.out.println("Testgeneratebinarynumber - Status: Passed");

  }

  private static void TestRandNum()  {
   GameAvoid.RandNum(0, 15);
     assert(GameAvoid.num >=0 && GameAvoid.num <= 15);
     
     System.out.println("TestRandNum - Status: Passed");
  }

  private static void Testgeneratetargets()  {
     GameAvoid.generatetargets();
     assert(GameAvoid.t1 != GameAvoid.t2 && GameAvoid.t1 != GameAvoid.t3);

     GameAvoid.generatetargets();
     assert(GameAvoid.t1 != GameAvoid.t2 && GameAvoid.t1 != GameAvoid.t3);

     GameAvoid.generatetargets();
     assert(GameAvoid.t1 != GameAvoid.t2 && GameAvoid.t1 != GameAvoid.t3);

     GameAvoid.generatetargets();
     assert(GameAvoid.t1 != GameAvoid.t2 && GameAvoid.t1 != GameAvoid.t3);

     System.out.println("Testgeneratetargets - Status: Passed");

  }
   /* Testing avoid game */
   
      
   /* Testing Drop Game */
   private static void TestCurrentScore() {
       GameDrop.Score.clear();

       GameDrop.Score.add(1);
       GameDrop.Score.add(0);
       GameDrop.Score.add(0);
       GameDrop.Score.add(1);

       assert(GameDrop.CurrentScore().equals(" 1001") == true);

       System.out.println("TestCurrentScore - Status: Passed");

   }

   /* Testing Drop Game */

   /* Testing Maze Game */

   private static void testMazeGameCollection() {
       assert(mazeScore.size() == 0);

       //check if player is in correct position the 1 is added to the score
       digitHit(300, 90);
       assert(mazeScore.size() == 1);
       assert(mazeScore.get(0) == 1);

       //check if this digit has been collected, it isn't collected again
       digitHit(300, 90);
       assert(mazeScore.size() == 1);
       assert(mazeScore.get(0) == 1);

       //set the cnt to 0 so the digit is picked up again
       mazeCnt = 0;
       digitHit(300, 90);
       assert(mazeScore.size() == 2);
       assert(mazeScore.get(0) == 1);
       assert(mazeScore.get(1) == 1);

       //reset the score and check if the position is wrong then a coin isnt collected
       //check incorrect x coordinate 
       mazeScore.clear();
       digitHit(200, 90);
       assert(mazeScore.size() == 0);

       //check y coordinate
       digitHit(300, 200);
       assert(mazeScore.size() == 0);

       System.out.println("testMazeGame - Status: Passed");
   }

   //The final game checks if the itmes have been collected using almost identical methids to this. 
   //We have adpated this method so we input the players x and y postion.
   public static void digitHit(int x, int y){
       if((x >= 309 - 22) && (x < 309)){
           if((y >= 93 - 22) && (y < 93)){
               if(mazeCnt == 0){
                   mazeScore.add(1);
               }
               mazeCnt = 1;
           }
       }
   }

   private static void testCheckScore() {
       GameMaze.score.clear();
       GameMaze.score.add(1);
       GameMaze.score.add(0);
       GameMaze.score.add(0);
       GameMaze.score.add(0);
       GameMaze.num = 8;
       assert(GameMaze.checkScore() == true);

       GameMaze.score.clear();
       GameMaze.score.add(1);
       GameMaze.score.add(1);
       GameMaze.score.add(1);
       GameMaze.score.add(1);
       GameMaze.num = 15;
       assert(GameMaze.checkScore() == true);

       GameMaze.score.clear();
       GameMaze.score.add(0);
       GameMaze.score.add(1);
       GameMaze.score.add(0);
       GameMaze.score.add(1);
       GameMaze.num = 5;
       assert(GameMaze.checkScore() == true);

       GameMaze.score.clear();
       GameMaze.score.add(0);
       GameMaze.score.add(0);
       GameMaze.score.add(0);
       GameMaze.score.add(0);
       GameMaze.num = 1;
       assert(GameMaze.checkScore() == false);
       GameMaze.num = 0;
       assert(GameMaze.checkScore() == true);


       System.out.println("testCheckScore - Status: Passed");
   }

      /* Testing Maze Game */

     /* Testing Inventory */

   private static void testInventoryTime(){
       InventoryUI.noCoins = 2;
       InventoryUI.noMedals = 2;
       assert(InventoryUI.getInventoryTime() == 44);

       InventoryUI.noCoins = 0;
       InventoryUI.noMedals = 4;
       assert(InventoryUI.getInventoryTime() == 80);

       InventoryUI.noCoins = 10;
       InventoryUI.noMedals = 0;
       assert(InventoryUI.getInventoryTime() == 20);

       InventoryUI.noCoins = 5;
       InventoryUI.noMedals = 5;
       assert(InventoryUI.getInventoryTime() == 110);

       System.out.println("testInventroyTime - Status: Passed");
   }

         /* Testing Inventory */

}