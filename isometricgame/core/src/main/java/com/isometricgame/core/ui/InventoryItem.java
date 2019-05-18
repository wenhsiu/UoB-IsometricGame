package com.isometricgame.core.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class InventoryItem extends Image {

    // TAKE OUT WHAT ISN'T BEING USED AND/OR REFACTOR

    public enum ItemTypeID {
        COIN, MEDAL, NONE;
    }

    private int itemAttributes;
    private int itemUseType;
    private int itemUseTypeValue;
    private ItemTypeID itemTypeID;
    private String itemShortDescription;
    private int itemValue;

    public InventoryItem(TextureRegion textureRegion, int itemAttributes, 
                        ItemTypeID itemTypeID, int itemUseType) {
        super(textureRegion);
        
        this.itemTypeID = itemTypeID;
        this.itemAttributes = itemAttributes;
        this.itemUseType = itemUseType;

    }

    public InventoryItem() {
        super();
    }

    public InventoryItem(InventoryItem inventoryItem) {
        super();
        this.itemTypeID = inventoryItem.getItemTypeID();
        this.itemAttributes = inventoryItem.getItemAttributes();
        this.itemUseType = inventoryItem.getItemUseType();
        this.itemUseTypeValue = inventoryItem.getItemUseTypeValue();
        this.itemShortDescription = inventoryItem.getItemShortDescription();
        this.itemValue = inventoryItem.getItemValue();

    }

    public int getItemUseTypeValue() {
        return itemUseTypeValue;
    }

    public void setItemUseTypeValue(int itemUseTypeValue) {
        this.itemUseTypeValue = itemUseTypeValue;
    }

    public int getItemValue() {
        return itemValue;
    }

    public void setItemValue(int itemValue) {
        this.itemValue = itemValue;
    }

    public ItemTypeID getItemTypeID() {
        return itemTypeID;
    }

    public void setItemTypeID(ItemTypeID itemTypeID) {
        this.itemTypeID = itemTypeID;
    }

    public int getItemAttributes() {
        return itemAttributes;
    }

    public void setItemAttributes(int itemAttributes) {
        this.itemAttributes = itemAttributes;
    }

    public int getItemUseType() {
        return itemUseType;
    }

    public void setItemUseType(int itemUseType) {
        this.itemUseType = itemUseType;
    }

    public String getItemShortDescription() {
        return itemShortDescription;
    }

    public void setItemShortDescription(String itemShortDescription) {
        this.itemShortDescription = itemShortDescription;
    }

    public boolean isSameItemType(InventoryItem possibleInventoryItem) {
        return itemTypeID == possibleInventoryItem.getItemTypeID();
    }

    public int getTradeValue() {
        // can sell items for half their original price
        if(itemValue >= 0) {
            return MathUtils.floor(itemValue * .5f);
        }
        else {
            return 0;
        }
    }

    




}