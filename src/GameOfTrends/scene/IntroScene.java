package GameOfTrends.scene;

import com.cleverfranke.util.FileSystem;

import GameOfTrends.Main;
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
		if (timer > Main.SCENE_BUTTON_TIMEOUT &&  Main.self.buttonDown) {
			Main.triggerNextScene(SceneType.Guide);
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
