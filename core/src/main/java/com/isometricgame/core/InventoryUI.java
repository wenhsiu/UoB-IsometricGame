package com.isometricgame.core;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.isometricgame.core.InventoryItem;
import com.isometricgame.core.OurActor;
import com.isometricgame.core.InventoryItemFactory;
import com.isometricgame.core.InventoryItem.ItemUseType;
import com.isometricgame.core.InventoryItem.ItemTypeID;
import com.isometricgame.core.InventoryItemLocation;
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

        inventorySlotTable = new Table();
        inventorySlotTable.setName("Inventory_Slot_Table");
        inventorySlotTable.setBackground(new Image(new NinePatch(Utility.STATUSUI_TEXTUREATLAS.createPatch("dialog"))).getDrawable());

        for(int i = 0; i < numSlots; i++) {
            InventorySlot inventorySlot = new InventorySlot();
            inventorySlotTable.add(inventorySlot).size(slotWidth, slotHeight);

            inventorySlot.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    if(getTapCount() == 2) {
                        InventorySlot slot = (InventorySlot) event.getListenerActor();
                        if(slot.hasItem()) {
                            InventoryItem item = slot.getTopInventoryItem();
                            if(item.isConsumable()) {
                                String itemInfo = item.getItemUseType() + Integer.toString(item.getItemUseTypeValue());
                                InventoryUI.this.notify(itemInfo, InventoryObserver.InventoryEvent.ITEM_CONSUMED);
                                slot.removeActor(item);
                                slot.remove(item);
                            }
                        }
                    }   
                }
            }
            );

            if(i % lengthSlotRow == 0) {
                inventorySlotTable.row();
            }
        }

        this.add(inventorySlotTable).colspan(2);
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

    public void addActorToInventory(OurActor actor, String itemName) {
        Array<Cell> sourceCells = inventorySlotTable.getCells();
        int index = 0;
        for(; index < sourceCells.size; index++) {
            InventorySlot inventorySlot = (InventorySlot) sourceCells.get(index).getActor();
            if(!(inventorySlot == null)) {
                int numItems = inventorySlot.getNumItems();
                if(numItems == 0) {
                    InventoryItem inventoryItem = InventoryItemFactory.getInstance().getInventoryItem(actor.getItemTypeID());
                    inventoryItem.setName(itemName);
                    inventorySlot.add(inventoryItem);
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