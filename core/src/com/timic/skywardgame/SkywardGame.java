package com.timic.skywardgame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.timic.skywardgame.logic.Assets;
import com.timic.skywardgame.screens.LoginScreen;

public class SkywardGame extends Game {
	public static Skin SKIN_VISION;
	private final boolean DEBUG = false;

	@Override
	public void create() {
		if(DEBUG)
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		SKIN_VISION = new Skin(Gdx.files.internal("data/skins/uiskin.json"));
		Assets.load();
		this.setScreen(new LoginScreen(this));
	}

	@Override
	public void dispose() {
		Gdx.app.debug("Skyward", "Dispose of all");
	}
}
