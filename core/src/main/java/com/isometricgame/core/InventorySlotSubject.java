package com.isometricgame.core;

public interface InventorySlotSubject {

    public void addObserver(InventorySlotObserver inventorySlotObserver);
    public void removeObserver(InventorySlotObserver inventorySlotObserver);
    public void removeallAllObservers();
    public void notify(final InventorySlot slot, InventorySlotObserver.SlotEvent event);
    
}