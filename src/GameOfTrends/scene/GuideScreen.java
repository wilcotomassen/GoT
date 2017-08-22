package GameOfTrends.scene;

import com.cleverfranke.util.FileSystem;

import GameOfTrends.Main;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class GuideScreen extends Scene {
	
	private PImage background;
	private PImage overlay;
	private float timer = 0;
	
	public GuideScreen() {
		background = Main.applet.loadImage(FileSystem.getApplicationPath("gfx/howto-bg.png"));
		overlay = Main.applet.loadImage(FileSystem.getApplicationPath("gfx/howto-fg.png"));
	}
	
	@Override
	public void onEnter() {
		timer = 0;
	}
	
	@Override
	public void update(double delta) {
		timer += delta;
		if (timer > Main.SCENE_BUTTON_TIMEOUT &&  Main.self.buttonDown) {
			Main.triggerNextScene(SceneType.Game);
		}
	}

	@Override
	public void draw(PGraphics g) {
		g.image(background, 0, 0);
		
		g.pushStyle();
		g.blendMode(PConstants.MULTIPLY);
		g.noStroke();
		g.fill(153, 136, 255);
		g.rect(0, Main.applet.height, Main.applet.width, -Main.self.distanceValue * Main.applet.height);
		g.popStyle();
		
		g.image(overlay, 0, 0);
	}
	
	public static SceneType type() {
		return SceneType.Guide;
	}

}
