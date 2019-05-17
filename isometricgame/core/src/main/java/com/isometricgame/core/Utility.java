package com.isometricgame.core;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Gdx;

public final class Utility {

    private final static String STATUSUI_TEXTURE_ATLAS_PATH = "skins/statusui.atlas";
    private final static String STATUSUI_SKIN_PATH = "skins/statusui.json";
    private final static String ITEMS_TEXTURE_ATLAS_PATH = "skins/inventory_items.atlas";

    public static TextureAtlas STATUSUI_TEXTUREATLAS = new TextureAtlas(STATUSUI_TEXTURE_ATLAS_PATH);
    public static TextureAtlas ITEMS_TEXTUREATLAS = new TextureAtlas(ITEMS_TEXTURE_ATLAS_PATH);
    public static Skin STATUSUI_SKIN = new Skin(Gdx.files.internal(STATUSUI_SKIN_PATH), STATUSUI_TEXTUREATLAS);

    private final static String DIALOGUI_TEXTURE_ATLAS_PATH = "skins/dialogui.atlas";
    private final static String DIALOGUI_SKIN_PATH = "skins/dialogui.json";
    
    public static TextureAtlas DIALOGUI_TEXTUREATLAS = new TextureAtlas(DIALOGUI_TEXTURE_ATLAS_PATH);
    public static Skin DIALOGUI_SKIN = new Skin(Gdx.files.internal(DIALOGUI_SKIN_PATH), DIALOGUI_TEXTUREATLAS);

}