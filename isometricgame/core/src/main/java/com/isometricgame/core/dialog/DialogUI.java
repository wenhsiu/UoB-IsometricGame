package com.isometricgame.core.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

public class DialogUI extends Window {

    private int part;
    private Dialog dialog;

    public DialogUI(Skin skin) {
        super("Dialog", skin);

        dialog = new Dialog("Dialog", skin);
        part = 1;

    }

    public int getPart() {
        return part;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void addPart() {
        part++;
    }

}