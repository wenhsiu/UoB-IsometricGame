package com.isometricgame.core.ui;

public interface InventoryObserver {
    public static enum InventoryEvent {
        ITEM_CONSUMED,
        NONE
    }

    void onNotify(final String value, InventoryEvent event);
}