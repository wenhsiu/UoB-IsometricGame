package com.isometricgame.core;

import com.badlogic.gdx.graphics.Texture;
import com.isometricgame.core.Villager;


/* 
To compile this, use the following command. 

javac -d . -cp ~/.m2/repository/com/badlogicgames/gdx/gdx/1.9.9/gdx-1.9.9.jar Test.java 

java -ea com.isometricgame.core.Test 

*/


/**
 * Test
 */
public class Test {

    public static Texture backgroundTest; 


    public static void main(String[] args) {

        int x; 

        x = 5; 
        //System.out.println("Hello World");

        System.out.println("Another message.");

        assert x == 6 ;  

        System.out.println("Reached the botton.");
        
        testVillager(); 
    }


    private static void testVillager(){
        Villager testVillager = new Villager(10, 10); 

        assert(testVillager.getPositionX() == 10); 
    }


    
}