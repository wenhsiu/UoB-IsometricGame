package com.isometricgame.core.ui;

public class InventoryItemLocation {

    private int locationIndex;
    private String itemTypeAtLocation;

    public InventoryItemLocation() {
    }

    public InventoryItemLocation(int locationIndex, String itemTypeAtLocation) {
    this.locationIndex = locationIndex;
    this.itemTypeAtLocation = itemTypeAtLocation;
    }

    public String getItemTypeAtLocation() {
        return itemTypeAtLocation;
    }

    public void setItemTypeAtLocation(String itemTypeAtLocation) {
        this.itemTypeAtLocation = itemTypeAtLocation;
    }

    public int getLocationIndex() {
        return locationIndex;
    }

    public void setLocationIndex(int locationIndex) {
        this.locationIndex = locationIndex;
    }
    
}
