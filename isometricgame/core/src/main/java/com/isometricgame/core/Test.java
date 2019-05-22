package com.isometricgame.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.isometricgame.core.Villager;
import com.isometricgame.core.charactermanager.People;
import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;
import com.isometricgame.core.clickdrag.Puzzles;
import com.isometricgame.core.GameAvoid;


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


    public static void main(String[] args) {

        rigorousTest(); 
        
        testVillager();

        testCoin(); 

        testGameAvoid();

        System.out.println("All Tests Passed Fine, WooHoo");
    }

    private static void rigorousTest() {

        //A Canary in the coalmine test that can be used to debug testing fw problems. 
        int x = 5; 
        assert(x==5); 

        System.out.println("rigourousTest - Status: Passed");
    }

    private static void testGameAvoid(){

        int number = GameAvoid.RandNum(1, 15);
        assert(number >= 1 && number <= 15);
    }

    private static void testPenguin(){

        Penguin testPenguin = new Penguin(100, 100);

    }

    private static void testCoin(){

        //Testing the coins that appear on the map. 

        Coin testCoin = new Coin(100, 100); 

        System.out.println("coinTest - Status: Passed");

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

    // click and drag
    private static void testClickPuzzles() {
        Puzzles game = new Puzzles();
        
        game.bineryAnswer = game.bineryAddition("01", "100");
        assert(game.bineryAnswer.equals("101"));

        game.checkAnswerLength();
        assert(game.bineryAnswer.equals("0101"));

        assert(game.index(290) == 0);
        assert(game.index(400) == 1);
        assert(game.index(800) == -1);
        assert(game.mouseHovering(950, 250) == true);
        assert(game.mouseHovering(800, 250) == false);
        assert(game.mouseHovering(950, 100) == false);
        assert(game.mouseHovering(800, 100) == false);
    }

    
}