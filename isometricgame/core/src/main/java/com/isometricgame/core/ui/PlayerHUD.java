package com.isometricgame.core.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

public class PlayerHUD implements Screen {

    private Stage stage;
    private Viewport viewport;

    private InventoryUI inventoryUI;

    private Dialog dialog;
    
    private Vector2 UIPosition;

    public PlayerHUD(Camera camera) {

        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);
        UIPosition = new Vector2();
        UIPosition.x = 50;
        UIPosition.y = 50;
        inventoryUI = new InventoryUI();
        inventoryUI.setVisible(true);
        inventoryUI.setPosition(UIPosition.x, UIPosition.y);
        inventoryUI.setKeepWithinStage(false);
        inventoryUI.setMovable(true);

        stage.addActor(inventoryUI);

        inventoryUI.validate();

        Array<Actor> actors = inventoryUI.getInventoryActors();
        for(Actor actor: actors) {
            stage.addActor(actor);
        }        
    }
    
    public Vector2 getInventoryUIPosition() {
    	return UIPosition;
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
