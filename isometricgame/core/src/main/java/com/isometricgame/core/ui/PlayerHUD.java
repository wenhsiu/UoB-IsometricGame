package com.isometricgame.core.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.Color;

import com.isometricgame.core.dialog.DialogUI;

import com.isometricgame.core.Utility;

public class PlayerHUD implements Screen, InventoryObserver {

    private Stage stage;
    private Viewport viewport;
    private int initialise = 0;

    private SpriteBatch batch;

    private BitmapFont bmfont;

    private InventoryUI inventoryUI;

    private DialogUI dialogUI;
    private Dialog dialog;
    
    private TextButton next;

    private Label labeltest;
    private BitmapFont myFont;

    private LabelStyle labelstyle;

    private Color black;
    
    private String message = "Testing the dialog.";

    public PlayerHUD(Camera camera) {

        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);

        // TODO: Make inventory resizable?
        // TODO: Make inventory stackable
        // TODO: Test inventory with other items
        // TODO: Resize inventory
        // TODO: In front of actors?

        inventoryUI = new InventoryUI();
        inventoryUI.setVisible(true);
        inventoryUI.setPosition(50, 50);
        inventoryUI.setKeepWithinStage(false);
        inventoryUI.setMovable(true);

        stage.addActor(inventoryUI);

        inventoryUI.validate();

        Array<Actor> actors = inventoryUI.getInventoryActors();
        for(Actor actor: actors) {
            stage.addActor(actor);
        }

        inventoryUI.addObserver(this);

        dialogUI = new DialogUI(Utility.DIALOGUI_SKIN);
        dialog = dialogUI.getDialog();

        // TODO: Set false when not debugging
        dialog.setVisible(false);
        dialog.setPosition(300, 500);
        dialog.setKeepWithinStage(false);
        dialog.setMovable(true);
        dialog.setSize(667, 200);

        //dialog.getContentTable().row().colspan(1).center();
        //dialog.getContentTable().add(message);
        //dialog.getContentTable().setVisible(true);
        //dialog.getContentTable().setPosition(50, 50);

        //dialog.row().colspan(1);

        //next = new TextButton("next", Utility.DIALOGUI_SKIN);
        //dialog.button(next);

        dialog.pack();

        stage.addActor(dialog);

        dialog.validate();
        
    }

    public Stage getStage() {
        return stage;
    }

    public void DialogOn() {
        dialog.setVisible(true);
    }

    public void DialogOff() {
        dialog.setVisible(false);
    }

    public InventoryUI getInventoryUI() {
        return inventoryUI;
    }

    @Override
    public void onNotify(String string, InventoryEvent event) {
        initialise++;
        if(initialise == 1) {
            InventoryUI.clearInventoryItems(inventoryUI.getInventorySlotTable());
        }

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
