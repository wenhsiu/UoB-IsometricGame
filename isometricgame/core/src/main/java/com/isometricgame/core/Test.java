package com.isometricgame.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.isometricgame.core.Villager;
import com.isometricgame.core.charactermanager.People;
import com.isometricgame.core.gamemanager.GameManager;
import com.isometricgame.core.gamemanager.GameState;


/* 

To compile this set of testing, use the following. 

javac -d . -cp ~/.m2/repository/com/badlogicgames/gdx/gdx/1.9.9/gdx-1.9.9.jar:/home/dev1/GitProjects/IsometricGame/isometricgame/core/target/isometricgame-core-1.0-SNAPSHOT.jar Test.java

java -ea -cp ~/.m2/repository/com/badlogicgames/gdx/gdx/1.9.9/gdx-1.9.9.jar:/home/dev1/GitProjects/IsometricGame/isometricgame/core/target/isometricgame-core-1.0-SNAPSHOT.jar com.isometricgame.core.Test


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

        Game testGame = new Game(){
        
            @Override
            public void create() {

                GameState currentState; 

                GameAvoid testGameAvoid = new GameAvoid(gm);
                gm.newGameStateByName("AVOIDGAME");
                gm.setCurrGameState("AVOIDGAME");

                currentState = gm.getGameState(); 
            }
        }; 
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



    
}