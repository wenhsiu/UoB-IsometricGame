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
        super("Inventory", Utility.STATUSUI_SKIN, "solidbackground");

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
                                String itemInfo = item.getItemUseType() + Component.MESSAGE_TOKEN + item.getItemUseTypeValue();
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

    }



}