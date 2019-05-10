package com.isometricgame.core.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class DialogUI extends Window {

    private int part;
    private Dialog dialog;

    private BitmapFont myFont;

    public DialogUI(Skin skin) {
        super("Dialog", skin);

        dialog = new Dialog("Dialog", skin);
        part = 1;

        /* FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/PressStart2P.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 8;
        parameter.color = new Color(0, 0, 0, 1);
        myFont = generator.generateFont(parameter);
        generator.dispose(); */
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

    public BitmapFont getBitmapFont() {
        return myFont;
    }

}