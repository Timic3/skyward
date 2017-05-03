package com.timic.skywardgame.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static Texture background;
	public static TextureRegion backgroundRegion;
	
	public static Texture hero;
	public static TextureRegion heroStand;
	public static TextureRegion heroDuck;
	public static TextureRegion heroJump;
	
	public static Texture tiles;
	public static TextureRegion snowHalfLeft;
	public static TextureRegion snowHalfMid;
	public static TextureRegion snowHalfRight;
	
	public static Texture loadTexture(String file) {
		return new Texture(Gdx.files.internal(file));
	}
	
	public static void load() {
		background = loadTexture("data/images/background.jpg");
		backgroundRegion = new TextureRegion(background, 150, 100, 560, 800);
		hero = loadTexture("data/images/hero_2.png");
		heroStand = new TextureRegion(hero, 67, 190, 66, 92);
		heroDuck = new TextureRegion(hero, 355, 95, 67, 72);
		heroJump = new TextureRegion(hero, 423, 95, 66, 94);
		tiles = loadTexture("data/images/tiles.png");
		snowHalfLeft = new TextureRegion(tiles, 216, 576, 70, 70);
		snowHalfMid = new TextureRegion(tiles, 216, 504, 70, 70);
		snowHalfRight = new TextureRegion(tiles, 216, 432, 70, 70);
		
	}
}
