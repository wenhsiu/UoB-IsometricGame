package com.isometricgame.core;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
// import com.badlogic.gdx.math.DelaunayTriangulator;
// import com.badlogic.gdx.math.Vector2;
// import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
// import com.badlogic.gdx.scenes.scene2d.actions.Actions;
// import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
// import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
// import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// import com.isometricgame.core.InventoryItem;
// import com.isometricgame.core.InventoryItem.ItemTypeID;
// import com.isometricgame.core.Utility;
import com.isometricgame.core.Player;
// import com.isometricgame.core.GameMAIN;

public class PlayerHUD implements Screen, InventoryObserver {

    // private static final String TAG = PlayerHUD.class.getSimpleName();

    private Stage stage;
    private Viewport viewport;
    // private Camera camera;

    private InventoryUI inventoryUI;

    // private Json json;

    // private static final String INVENTORY_FULL = "Your inventory is full.";

    public PlayerHUD(Camera camera, Player player) {
        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);

        inventoryUI = new InventoryUI();
        inventoryUI.setVisible(true);
        inventoryUI.setPosition(0, 0);
        inventoryUI.setKeepWithinStage(false);
        inventoryUI.setMovable(false);

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

    @Override
    public void onNotify(String string, InventoryEvent event) {
        // TODO: Implement
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
