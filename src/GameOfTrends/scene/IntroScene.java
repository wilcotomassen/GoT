package GameOfTrends.scene;

import com.cleverfranke.util.FileSystem;

import GameOfTrends.Main;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class IntroScene extends Scene {
	
	private PImage background;
	private float timer = 0;
	
	public IntroScene() {
		background = Main.applet.loadImage(FileSystem.getApplicationPath("gfx/intro-bg.png"));
	}
	
	@Override
	public void onEnter() {
		timer = 0;
	}
	
	@Override
	public void update(double delta) {
		
		timer += delta;
		if (timer > 100f &&  Main.self.buttonDown) {
			Main.triggerNextScene(SceneType.Game);
		}
	}

	@Override
	public void draw(PGraphics g) {
		g.image(background, 0, 0);
		
		g.pushStyle();
		g.blendMode(PConstants.MULTIPLY);
		g.noStroke();
		g.fill(255, 0, 0);
		g.rect(0, Main.applet.height, Main.applet.width, -Main.self.distanceValue * Main.applet.height);
		g.popStyle();
	}
	
	public static SceneType type() {
		return SceneType.Intro;
	}

}
