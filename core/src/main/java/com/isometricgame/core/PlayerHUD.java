package com.isometricgame.core;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlayerHUD implements Screen, InventoryObserver {

    private Stage stage;
    private Viewport viewport;
    private int initialise = 0;

    private InventoryUI inventoryUI;

    public PlayerHUD(Camera camera) {

        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);

        inventoryUI = new InventoryUI();
        inventoryUI.setVisible(true);
        inventoryUI.setPosition(600, 600);
        inventoryUI.setKeepWithinStage(false);
        inventoryUI.setMovable(true);

        stage.addActor(inventoryUI);

        inventoryUI.validate();

        Array<Actor> actors = inventoryUI.getInventoryActors();
        for(Actor actor: actors) {
            stage.addActor(actor);
        }

        inventoryUI.addObserver(this);
    }

    public Stage getStage() {
        return stage;
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
