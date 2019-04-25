package com.isometricgame.core;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Gdx;

// N.B. Utility is used for various things besides inventory.
// Please edit me if you need to use me.

public final class Utility {

    private final static String STATUSUI_TEXTURE_ATLAS_PATH = "skins/statusui.atlas";
    private final static String STATUSUI_SKIN_PATH = "skins/statusui.json";
    private final static String ITEMS_TEXTURE_ATLAS_PATH = "skins/inventory_items.atlas";
    private final static String ITEMS_SKIN_PATH = "skins/inventory_items.json";

    public static TextureAtlas STATUSUI_TEXTUREATLAS = new TextureAtlas(STATUSUI_TEXTURE_ATLAS_PATH);
    public static TextureAtlas ITEMS_TEXTUREATLAS = new TextureAtlas(ITEMS_TEXTURE_ATLAS_PATH);
    public static Skin STATUSUI_SKIN = new Skin(Gdx.files.internal(STATUSUI_SKIN_PATH), STATUSUI_TEXTUREATLAS);
    // public static Skin ITEMS_SKIN = new Skin(Gdx.files.internal(ITEMS_SKIN_PATH), ITEMS_TEXTUREATLAS);

}