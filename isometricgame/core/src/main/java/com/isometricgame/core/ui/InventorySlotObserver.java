package com.isometricgame.core.ui;

public interface InventorySlotObserver {
    public static enum SlotEvent {
        ADDED_ITEM, REMOVED_ITEM
    }

    void onNotify(final InventorySlot slot, SlotEvent event);
}