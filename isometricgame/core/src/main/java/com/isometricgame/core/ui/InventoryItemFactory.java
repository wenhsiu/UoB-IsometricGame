package com.isometricgame.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Scaling;

import com.isometricgame.core.ui.InventoryItem.ItemTypeID;
import com.isometricgame.core.Utility;

import java.util.*;

public class InventoryItemFactory {

    private Json json = new Json();
    private final String INVENTORY_ITEM = "skins/inventory_items.json";

    private static InventoryItemFactory instance = null;
    private Hashtable<ItemTypeID, InventoryItem> inventoryItemList;

    public static InventoryItemFactory getInstance() {
        if(instance == null) {
            instance = new InventoryItemFactory();
        }

        return instance;
    }

    private InventoryItemFactory() {
        ArrayList<JsonValue> list = json.fromJson(ArrayList.class, Gdx.files.internal(INVENTORY_ITEM));
        inventoryItemList = new Hashtable<ItemTypeID, InventoryItem>();

        for(int i = 0; i < list.size(); i++) {
            InventoryItem inventoryItem = json.readValue(InventoryItem.class, list.get(i));
            inventoryItemList.put(inventoryItem.getItemTypeID(), inventoryItem);
        }
    }

    public InventoryItem getInventoryItem(ItemTypeID inventoryItemType) {
        InventoryItem item = new InventoryItem(inventoryItemList.get(inventoryItemType));
        item.setDrawable(new TextureRegionDrawable(Utility.ITEMS_TEXTUREATLAS.findRegion(item.getItemTypeID().toString())));
        item.setScaling(Scaling.none);
        return item;
    }

}
