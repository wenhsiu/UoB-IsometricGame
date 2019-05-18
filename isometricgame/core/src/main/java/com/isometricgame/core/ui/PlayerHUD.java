package com.isometricgame.core.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
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

    public PlayerHUD(Camera camera) {

        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);

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

        //dialogUI = new DialogUI(Utility.DIALOGUI_SKIN);
        //dialog = dialogUI.getDialog();

        //dialog.setVisible(false);
        //dialog.setPosition(300, 500);
        //dialog.setKeepWithinStage(false);
        // dialog.setMovable(true);
        // dialog.setSize(667, 200);

        //dialog.getContentTable().row().colspan(1).center();
        //dialog.getContentTable().add(message);
        //dialog.getContentTable().setVisible(true);
        //dialog.getContentTable().setPosition(50, 50);

        //dialog.row().colspan(1);

        //next = new TextButton("next", Utility.DIALOGUI_SKIN);
        //dialog.button(next);

        // dialog.pack();

        // stage.addActor(dialog);

        // dialog.validate();
        
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
