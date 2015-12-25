package com.widesteppe.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.widesteppe.Controller;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		config.title = "Homorium I";
		config.fullscreen = true;
		//config.width = 1280;
		//config.height = 800;
		config.vSyncEnabled = true;
		new LwjglApplication(Controller.getInstance(), config);
	}
}
