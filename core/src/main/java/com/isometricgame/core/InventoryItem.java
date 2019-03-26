package com.isometricgame.core;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class InventoryItem extends Image {

    public enum ItemAttribute {
        CONSUMABLE(1),
        EQUIPPABLE(2),
        STACKABLE(4);

        private int attribute;

        ItemAttribute(int attribute) {
            this.attribute = attribute;
        }

        public int getValue() {
            return attribute;
        }

    }

    public enum ItemUseType {
        STANDARD(1);

        private int itemUseType;

        ItemUseType(int itemUseType) {
            this.itemUseType = itemUseType;
        }

        public int getValue() {
            return itemUseType;
        }

    }

    public enum ItemTypeID {
        BLUEBOX, GREENBOX, BROWNBOX, REDBOX, NONE;
    }

    private int itemAttributes;
    private int itemUseType;
    private int itemUseTypeValue;
    private ItemTypeID itemTypeID;
    private String itemShortDescription;
    private int itemValue;

    public InventoryItem(TextureRegion textureRegion, int itemAttributes, 
                        ItemTypeID itemTypeID, int itemUseType, 
                        int itemUseTypeValue, int itemValue) {
        super(textureRegion);
        
        this.itemTypeID = itemTypeID;
        this.itemAttributes = itemAttributes;
        this.itemUseType = itemUseType;
        this.itemUseTypeValue = itemUseTypeValue;
        this.itemValue = itemValue;

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

    public boolean isConsumable() {
        // uses powers of 2 to check whether the consumable
        // bit (1) is present in the item attributes

        return ((itemAttributes & ItemAttribute.CONSUMABLE.getValue())
                == ItemAttribute.CONSUMABLE.getValue());
    }

    public boolean isEquippable() {
        // uses powers of 2 to check whether the equippable
        // bit (1x) is present in the item attributes

        return ((itemAttributes & ItemAttribute.EQUIPPABLE.getValue())
                == ItemAttribute.EQUIPPABLE.getValue());
    }

    public boolean isStackable() {
        // uses powers of 2 to check whether the stackable
        // bit (1xx) is present in the item attributes

        return ((itemAttributes & ItemAttribute.STACKABLE.getValue())
                == ItemAttribute.STACKABLE.getValue());
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