package com.undervon.betelgeuse.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.undervon.betelgeuse.GameMain;
import helpers.GameInfo;

/**
 * \class DesktopLauncher
 * \brief Clasa ce o sa lanseze in executie functia main.
 * */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		///seteaza latimea ferestrei jocului de 480px
		config.width = GameInfo.WIDTH;
		///seteaza inaltimea ferestrei jocului de 800px
		config.height = GameInfo.HEIGHT;
		new LwjglApplication(new GameMain(), config);
	}
}
