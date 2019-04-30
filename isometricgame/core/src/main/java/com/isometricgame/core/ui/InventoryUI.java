package com.isometricgame.core.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import com.isometricgame.core.ui.InventoryItem;
import com.isometricgame.core.ui.InventoryItemFactory;
import com.isometricgame.core.ui.InventoryItem.ItemTypeID;
import com.isometricgame.core.ui.InventoryItemLocation;
import com.isometricgame.core.Utility;

public class InventoryUI extends Window implements InventorySubject, InventorySlotObserver {

    public final static int numSlots = 10;
    public static final String PLAYER_INVENTORY = "Player_Inventory";
    
    private int lengthSlotRow = 5;
    private Table inventorySlotTable;
    private Array<Actor> inventoryActors;

    private final int slotWidth = 48;
    private final int slotHeight = 48;

    private Array<InventoryObserver> observers;

    public InventoryUI() {
        super("Inventory", Utility.STATUSUI_SKIN, "default");

        observers = new Array<InventoryObserver>();
        inventoryActors = new Array<Actor>();

        inventorySlotTable = new Table(Utility.STATUSUI_SKIN);

        inventorySlotTable.setName("Inventory_Slot_Table");
        inventorySlotTable.row();

        for(int i = 1; i <= numSlots; i++) {
            InventorySlot inventorySlot = new InventorySlot();
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

    public static void clearInventoryItems(Table targetTable) {
        Array<Cell> cells = targetTable.getCells();
        for(int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = (InventorySlot) cells.get(i).getActor();
            if(!(inventorySlot == null)) {
                inventorySlot.clearAllInventoryItems(false);
            }
        }

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

    public static void populateInventory(Table targetTable, Array<InventoryItemLocation> inventoryItems,
                                         String defaultName, boolean disableNonDefaultItems) {
        clearInventoryItems(targetTable);
        Array<Cell> cells = targetTable.getCells();

        for(int i = 0; i < inventoryItems.size; i++) {
            InventoryItemLocation itemLocation = inventoryItems.get(i);
            ItemTypeID itemTypeID = ItemTypeID.valueOf(itemLocation.getItemTypeAtLocation());
            InventorySlot inventorySlot = (InventorySlot) cells.get(itemLocation.getLocationIndex()).getActor();
            
            for(int index = 0; index < itemLocation.getNumberItemsAtLocation(); index++) {
                InventoryItem item = InventoryItemFactory.getInstance().getInventoryItem(itemTypeID);
                String itemName = itemLocation.getItemNameProperty();
                if(itemName == null || itemName.isEmpty()) {
                    item.setName(defaultName);
                }
                else {
                    item.setName(itemName);
                }
                inventorySlot.add(item);
            }
        }
    }

    public static Array<InventoryItemLocation> getInventory(Table targetTable) {
        Array<Cell> cells = targetTable.getCells();
        Array<InventoryItemLocation> items = new Array<InventoryItemLocation>();
        for(int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = (InventorySlot) cells.get(i).getActor();
            if(!(inventorySlot == null)) {
                int numItems = inventorySlot.getNumItems();
                if(numItems > 0) {
                    items.add(new InventoryItemLocation(i, inventorySlot.getTopInventoryItem().toString(),
                                                numItems, inventorySlot.getTopInventoryItem().getName()));
                }
            }
        }
        return items;
    }

    public static void setInventoryItemNames(Table targetTable, String name) {
        Array<Cell> cells = targetTable.getCells();
        for(int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = (InventorySlot) cells.get(i).getActor();
            if(!(inventorySlot == null)) {
                inventorySlot.updateAllInventoryItemNames(name);
            }
        }
    }

    public boolean doesInventoryHaveSpace() {
        Array<Cell> sourceCells = inventorySlotTable.getCells();
        int index = 0;
        for(; index < sourceCells.size; index++) {
            InventorySlot inventorySlot = (InventorySlot) sourceCells.get(index).getActor();
            if(!(inventorySlot == null)) {
                int numItems = inventorySlot.getNumItems();
                if(numItems == 0) {
                    return true;
                }
                else {
                    index++;
                }
            }
        }
        return false;
    }

    public void addItemToInventory(InventoryItem item, String itemName) {
        Array<Cell> sourceCells = inventorySlotTable.getCells();

        for(int index = 0; index < sourceCells.size; index++) {
            InventorySlot inventorySlot = (InventorySlot) sourceCells.get(index).getActor();
            if(!(inventorySlot == null)) {
                int numItems = inventorySlot.getNumItems();
                if(numItems == 0) {
                    item.setName(itemName);
                    inventorySlot.add(item);
                    break;
                }
            }
        }
    }

    public Array<Actor> getInventoryActors() {
        return inventoryActors;
    }

    @Override
    public void onNotify(InventorySlot slot, SlotEvent event) {
    }

    @Override
    public void addObserver(InventoryObserver inventoryObserver) {
        observers.add(inventoryObserver);
    }

    @Override
    public void removeObserver(InventoryObserver inventoryObserver) {
        observers.removeValue(inventoryObserver, true);
    }

    @Override
    public void removeAllObservers() {
        for(InventoryObserver observer: observers) {
            observers.removeValue(observer, true);
        }
    }

    @Override
    public void notify(String value, InventoryObserver.InventoryEvent event) {
        for(InventoryObserver observer: observers) {
            observer.onNotify(value, event);
        }
    }

}