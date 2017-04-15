package com.timic.skywardgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.timic.skywardgame.SkywardGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
		config.width = 1280;
		config.height = 720;
		config.resizable = false;
		config.fullscreen = false;
		config.vSyncEnabled = false;
		config.backgroundFPS = 0;
		config.foregroundFPS = 0;
		config.title = "Skyward";
		new LwjglApplication(new SkywardGame(), config);
	}
}
