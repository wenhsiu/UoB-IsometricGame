package com.isometricgame.core.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import com.isometricgame.core.ui.InventoryItem;
import com.isometricgame.core.ui.InventoryItem.ItemTypeID;
import com.isometricgame.core.ui.InventoryItemLocation;
import com.isometricgame.core.Utility;

public class InventoryUI extends Window {

    public final static int numSlots = 8;
    
    private int lengthSlotRow = 4;
    private Table inventorySlotTable;
    private Array<Actor> inventoryActors;

    private final int slotWidth = 48;
    private final int slotHeight = 48;

    public static int noCoins;
    public static int noMedals;

    public InventoryUI() {
        super("Inventory", Utility.STATUSUI_SKIN, "default");

        inventoryActors = new Array<Actor>();

        inventorySlotTable = new Table(Utility.STATUSUI_SKIN);

        inventorySlotTable.row();

        for(int i = 1; i <= numSlots; i++) {
            InventorySlot inventorySlot = new InventorySlot();
            inventorySlot.setNumber(i); 
            inventorySlotTable.add(inventorySlot).size(slotWidth, slotHeight);

            if(i % lengthSlotRow == 0 && i < numSlots) {
                inventorySlotTable.row();
            }
        }      
        this.add(inventorySlotTable);
        this.pack();
    }

    public Table getInventorySlotTable() {
        return inventorySlotTable;
    }
    
    public static Array<InventoryItemLocation> removeInventoryItems(String name, Table inventoryTable) {
        Array<Cell> cells = inventoryTable.getCells();
        Array<InventoryItemLocation> items = new Array<InventoryItemLocation>();
        
        for(int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = (InventorySlot) cells.get(i).getActor();
            if(!(inventorySlot == null)) {
                inventorySlot.removeAllInventoryItemsWithName(name);
            }
        }
        return items;
    }
    
    // N.B. Only works when noCoins or noMedals is greater than 0
    // Otherwise use addItemstoInventory
    public void addBulkItems(ItemTypeID itemID, int amount) {
    	switch(itemID) {
    	case COIN:
    		if(noCoins > 0) {
    			noCoins += amount;
    		}
    		break;
    	case MEDAL:
    		if(noMedals > 0) {
                noMedals += amount;
    		}
    		break;
    	default:
    	}
    }

    public void addItemToInventory(InventoryItem item, String itemName) {
        Array<Cell> sourceCells = inventorySlotTable.getCells();

        if(itemName.equals("COIN") && noCoins == 0) {
            for(int index = 0; index < sourceCells.size; index++) {
                InventorySlot inventorySlot = (InventorySlot) sourceCells.get(index).getActor();
                if(!(inventorySlot == null) && inventorySlot.getNumber() == 5) {
                    int numItems = inventorySlot.getNumItems();
                    if(numItems == 0) {
                        item.setName(itemName);
                        inventorySlot.add(item);
                        noCoins++;
                        break;
                    }
                }
            }
        }
        else if(itemName.equals("MEDAL") /*&& noMedals == 0*/) {
            for(int index = 0; index < sourceCells.size; index++) {
                InventorySlot inventorySlot = (InventorySlot) sourceCells.get(index).getActor();
                if(!(inventorySlot == null) /*&& inventorySlot.getNumber() == 6*/) {
                    int numItems = inventorySlot.getNumItems();
                    if(numItems == 0) {
                        item.setName(itemName);
                        inventorySlot.add(item);
                        noMedals++;
                        break;
                    }
                }
            }
        }
        else if(itemName.equals("COIN") && noCoins > 0) {
            noCoins++;
            System.out.println("Number of coins is " + noCoins);
        }
        else if(itemName.equals("MEDAL") && noMedals > 0) {
            noMedals++;
            System.out.println("Number of medals is " + noMedals);
        }
    }

    public int getItemNumber(ItemTypeID itemID) {
    	if(itemID == ItemTypeID.COIN) {
            return noCoins;
        }
    	else if(itemID == ItemTypeID.MEDAL) {
            return noMedals;
        }
    	return -1;
    }

    public Array<Actor> getInventoryActors() {
        return inventoryActors;
    }

    public static int getInventoryTime() {
        int time = (noCoins * 2) + (noMedals * 20);
        return time;
    }

    public void removeBulkItems(ItemTypeID itemID, int amount) {
    	switch(itemID) {
    	case COIN:
    		if(noCoins > 0 && noCoins - amount > 0) {
    			noCoins -= amount;
            }
            else if(noCoins > 0 && noCoins - amount == 0) {
                noCoins -= amount;
                removeInventoryItems("COIN", inventorySlotTable);
            }
    		break;
    	case MEDAL:
    		if(noMedals > 0 && noMedals - amount > 0) {
                noMedals -= amount;
                System.out.println("Number of medals is " + noMedals);
            }
            else if(noMedals > 0 && noMedals - amount == 0) {
                noMedals -= amount;
                removeInventoryItems("MEDAL", inventorySlotTable);
            }
    		break;
    	}	
    }

}
