package com.timic.skywardgame.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
	public static Texture rocket;
	
	public static Texture loadTexture(String file) {
		return new Texture(Gdx.files.internal(file));
	}
	
	public static void load() {
		rocket = loadTexture("data/images/rocket.png");
	}
}
