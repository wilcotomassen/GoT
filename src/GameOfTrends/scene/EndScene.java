package GameOfTrends.scene;

import com.cleverfranke.util.FileSystem;

import GameOfTrends.Main;
import processing.core.PGraphics;
import processing.core.PImage;

public class EndScene extends Scene {
	
	private PImage background;
	private float timer = 0;
	
	public EndScene() {
		background = Main.applet.loadImage(FileSystem.getApplicationPath("gfx/end-bg.png"));
	}
	
	@Override
	public void onEnter() {
		timer = 0;
	}

	@Override
	public void update(double delta) {
		timer += delta;
		if (timer > 100f &&  Main.self.buttonDown) {
			Main.triggerNextScene(SceneType.Intro);
		}
	}

	@Override
	public void draw(PGraphics g) {
		g.image(background, 0, 0);
	}
	
	public static SceneType type() {
		return SceneType.End;
	}

}
