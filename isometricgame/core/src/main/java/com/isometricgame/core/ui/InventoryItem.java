package com.isometricgame.core.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class InventoryItem extends Image {

    public enum ItemTypeID {
        COIN, MEDAL, NONE;
    }

    private ItemTypeID itemTypeID;

    public InventoryItem() {
        super();
    }

    public InventoryItem(InventoryItem inventoryItem) {
        super();
        this.itemTypeID = inventoryItem.getItemTypeID();
    }

    public ItemTypeID getItemTypeID() {
        return itemTypeID;
    }

    public void setItemTypeID(ItemTypeID itemTypeID) {
        this.itemTypeID = itemTypeID;
    }

    public boolean isSameItemType(InventoryItem possibleInventoryItem) {
        return itemTypeID == possibleInventoryItem.getItemTypeID();
    }
}
