package com.unimelb.swen30006.partc.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.unimelb.swen30006.partc.CityOfMelbourneDrivingSimulation;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new CityOfMelbourneDrivingSimulation(), config);
	}
}
