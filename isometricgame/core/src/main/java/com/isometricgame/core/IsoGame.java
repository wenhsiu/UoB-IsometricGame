package com.isometricgame.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import com.isometricgame.core.gamemanager.GameManager;

public class IsoGame extends Game {

	private GameManager gm;
	private Music backgroundMusic; 
	// https://soundimage.org/fantasy-2/
	
	@Override
	public void create () {
		gm = new GameManager(this);
		gm.setCurrGameState("MENU");		

		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Hypnotic-Puzzle3.mp3")); 
		backgroundMusic.setLooping(true);
		backgroundMusic.play(); 
	}

	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void render () {
		super.render();		
	}

	@Override
	public void pause () {
		super.pause();
	}

	@Override
	public void resume () {
		super.resume();
	}

	@Override
	public void dispose () {
		super.dispose();
	}
	
}
