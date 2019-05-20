package com.isometricgame.core.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.isometricgame.core.ui.InventoryItem;
import com.isometricgame.core.Utility;

public class InventorySlot extends Stack {

    // default image
    private Stack defaultBackground;
    private Image customBackgroundDecal;
    private Label numItemsLabel;
    private int numItemsVal = 0;
    private int number; 

    public InventorySlot() {

        defaultBackground = new Stack();
        customBackgroundDecal = new Image();

        Image image = new Image(new NinePatch(Utility.STATUSUI_TEXTUREATLAS.createPatch("statusui")));

        // TODO: USE THIS TO DISPLAY NUM OF ITEMS GOT FROM INVENTORYUI???
        numItemsLabel = new Label(String.valueOf(numItemsVal), Utility.STATUSUI_SKIN, "default");
        numItemsLabel.setAlignment(0); 
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
    }

    public void incrementItemCount(boolean sendAddNotification) {
        numItemsVal++;
        numItemsLabel.setText(String.valueOf(numItemsVal));
        if(defaultBackground.getChildren().size > 1) {
            defaultBackground.getChildren().pop();
        }
    }

    public boolean hasItem() {
        if(hasChildren()) {
            SnapshotArray<Actor> items = this.getChildren();
            if(items.size > 2) {
                return true;
            }
        }
        return false;
    }

    public int getNumItems() {
        if(hasChildren()) {
            SnapshotArray<Actor> items = this.getChildren();
            return items.size - 2;
        }
        return 0;
    }

    public int getNumItems(String name) {
        if(hasChildren()) {
            SnapshotArray<Actor> items = this.getChildren();
            int totalFilteredSize = 0;
            for(Actor actor: items) {
                if(actor.getName().equalsIgnoreCase(name)) {
                    totalFilteredSize++;
                }
            }
            return totalFilteredSize;
        }
        return 0;
    }

    public void checkVisibilityOfItemCount() {
        if(numItemsVal < 2) {
            numItemsLabel.setVisible(false);
        }
        else {
            numItemsLabel.setVisible(true);
        }
    }

    public InventoryItem getTopInventoryItem() {
        InventoryItem actor = null;
        if(hasChildren()) {
            SnapshotArray<Actor> items = this.getChildren();
            if(items.size > 2) {
                actor = (InventoryItem) items.peek();
            }
        }
        return actor;
    }

    @Override
    public void add(Actor actor) {
        super.add(actor);

        if(numItemsLabel == null) {
            return;
        }

        if(!actor.equals(defaultBackground) &&
           !actor.equals(numItemsLabel)) {
               incrementItemCount(true);
           }
    }

    public void remove(Actor actor) {
        super.removeActor(actor);

        if(numItemsLabel == null) {
            return;
        }

        if(!actor.equals(defaultBackground) &&
           !actor.equals(numItemsLabel)) {
               decrementItemCount(true);
           }
    }

    public void Add(Array<Actor> array) {
        for(Actor actor: array) {
            super.add(actor);

            if(numItemsLabel == null) {
                return;
            }

            if(!actor.equals(defaultBackground) && 
               !actor.equals(numItemsLabel)) {
                incrementItemCount(true);
            }
        }
    }

    public Array<Actor> getAllInventoryItems() {
        Array<Actor> items = new Array<Actor>();
        if(hasItem()) {
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            int numInventoryItems = arrayChildren.size - 2;
            for(int i = 0; i < numInventoryItems; i++) {
                decrementItemCount(true);
                items.add(arrayChildren.pop());
            }
        }
        return items;
    }

    public void removeAllInventoryItemsWithName(String name) {
        if(hasItem()) {
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            for(int i = arrayChildren.size - 1; i > 1; i--) {
                String itemName = arrayChildren.get(i).getName();
                if(itemName.equalsIgnoreCase(name)) {
                    decrementItemCount(true);
                    arrayChildren.removeIndex(i);
                }
            }
        }
    }

    public void clearAllInventoryItems(boolean sendRemoveNotifications) {
        if(hasItem()) {
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            int numInventoryItems = getNumItems();
            for(int i = 0; i < numInventoryItems; i++) {
                decrementItemCount(sendRemoveNotifications);
                arrayChildren.pop();
            }
        }
    }

    public int getNumber(){
        return number; 
    }

    public void setNumber(int number){
        this.number = number; 
    }



}
