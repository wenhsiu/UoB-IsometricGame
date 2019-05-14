package com.isometricgame.core;

import com.isometricgame.core.charactermanager.TriggerPoint;
import com.isometricgame.core.gamemanager.GameManager;

public class PhoneBox extends TriggerPoint{

	public PhoneBox(float x, float y, float scale, GameManager gm, String gsName) {
		super(x, y, scale, gm, gsName);
		super.initTriggerPoint("Isometria/phoneboxscaled.png", 0, 0, 64, 89, true);
	}

}
