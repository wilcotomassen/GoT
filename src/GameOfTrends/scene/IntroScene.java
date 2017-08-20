package GameOfTrends.scene;

import com.cleverfranke.util.FileSystem;

import GameOfTrends.Main;
import processing.core.PGraphics;
import processing.core.PImage;

public class IntroScene extends Scene {
	
	PImage background;
	
	public IntroScene() {
		background = Main.applet.loadImage(FileSystem.getApplicationPath("gfx/intro-bg.png"));
	}

	@Override
	public void update(double delta) {
		if (Main.applet.keyPressed) {
			Main.triggerNextScene(SceneType.Game);
		}
	}

	@Override
	public void draw(PGraphics g) {
		g.image(background, 0, 0);
	}
	
	public static SceneType type() {
		return SceneType.Intro;
	}

}
