package com.isometricgame.core;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.isometricgame.core.InventoryItem;
import com.isometricgame.core.Utility;

public class InventorySlot extends Stack implements InventorySlotSubject {

    // default image
    private Stack defaultBackground;
    private Image customBackgroundDecal;
    private Label numItemsLabel;
    private int numItemsVal = 0;
    private int filterItemType;

    private Array<InventorySlotObserver> observers;

    public InventorySlot() {

        filterItemType = 0;
        defaultBackground = new Stack();
        customBackgroundDecal = new Image();
        observers = new Array<InventorySlotObserver>();
        Image image = new Image(new NinePatch(Utility.STATUSUI_TEXTUREATLAS.createPatch("dialog")));

        numItemsLabel = new Label(String.valueOf(numItemsVal), Utility.STATUSUI_SKIN, "inventory-item-count");
        numItemsLabel.setAlignment(0); // look into
        numItemsLabel.setVisible(true);

        defaultBackground.add(image);
        defaultBackground.setName("background");
        numItemsLabel.setName("numitems");

        this.add(defaultBackground);
        this.add(numItemsLabel);

    }

    public void decrementItemCount(boolean sendRemoveNotification) {
        numItemsVal--;
        numItemsLabel.setText(String.valueOf(numItemsVal));
        if(defaultBackground.getChildren().size == 1) {
            defaultBackground.add(customBackgroundDecal);
        }
        if(sendRemoveNotification) {
            notify(this, InventorySlotObserver.SlotEvent.REMOVED_ITEM);
        }

    }

    public void incrementItemCount(boolean sendAddNotification) {
        numItemsVal++;
        numItemsLabel.setText(String.valueOf(numItemsVal));
        if(defaultBackground.getChildren().size > 1) {
            defaultBackground.getChildren().pop();
        }
        if(sendAddNotification) {
            notify(this, InventorySlotObserver.SlotEvent.ADDED_ITEM);
        }

    }

    @Override
    public void addObserver(InventorySlotObserver slotObserver) {
        observers.add(slotObserver);
    }

    @Override
    public void removeObserver(InventorySlotObserver slotObserver) {
        observers.removeValue(slotObserver, true);
    }

    @Override
    public void removeAllObservers() {
        for(InventorySlotObserver observer: observers) {
            observers.removeValue(observer, true);
        }
    }

    @Override
    public void notify(final InventorySlot slot, InventorySlotObserver.SlotEvent event) {
        for(InventorySlotObserver observer: observers) {
            observer.onNotify(slot, event);
        }
    }

}
