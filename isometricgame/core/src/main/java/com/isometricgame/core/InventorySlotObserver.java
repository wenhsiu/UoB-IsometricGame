package com.isometricgame.core;

public interface InventorySlotObserver {
    public static enum SlotEvent {
        ADDED_ITEM, REMOVED_ITEM
    }

    void onNotify(final InventorySlot slot, SlotEvent event);
}