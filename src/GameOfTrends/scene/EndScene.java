package GameOfTrends.scene;

import java.util.ArrayList;

import com.cleverfranke.util.FileSystem;

import GameOfTrends.Main;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class EndScene extends Scene {
	
	private PImage background;
	private float timer = 0;
	private GameScene gameScene;
	private ArrayList<PVector> playerPoints = new ArrayList<>(500);
	private ArrayList<PVector> sourcePoints = new ArrayList<>();
	
	private final float GRAPH_MARGIN = 100;
	private final float MIN_Y = 300;
	private final float MAX_Y = -400;
	
	public EndScene() {
		background = Main.applet.loadImage(FileSystem.getApplicationPath("gfx/end-bg.png"));
		gameScene = (GameScene) Main.self.scenes.get(GameScene.type());
	}
	
	@Override
	public void onEnter() {
		timer = 0;
		
		// Map source points to screen coords
		sourcePoints.clear();
		ArrayList<Float> sourceValues = gameScene.sourceData.getLastPoints();
		for (int i = 0; i < sourceValues.size(); i++) {
			float x = PApplet.map(i, 0f, (float) (sourceValues.size() - 1), GRAPH_MARGIN, Main.applet.width - GRAPH_MARGIN);
			float y = PApplet.map(sourceValues.get(i), 0f, 1f, MIN_Y, MAX_Y);
			sourcePoints.add(new PVector(x, y));
		}
	
		// Map player points to screen coords
		final float minPlayerX = gameScene.playerScorePoints.get(0).x;
		final float maxPlayerX = gameScene.playerScorePoints.get(gameScene.playerScorePoints.size() - 1).x;
		playerPoints.clear();
		for (PVector p: gameScene.playerScorePoints) {
			float x = PApplet.map(p.x, minPlayerX, maxPlayerX, GRAPH_MARGIN, Main.applet.width - GRAPH_MARGIN);
			float y = PApplet.map(p.y, 0, -50, MIN_Y, MAX_Y);
			playerPoints.add(new PVector(x, y));
		}
		
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
		
		g.translate(0, Main.applet.height / 2);
		
		// Draw source graph
		g.stroke(Main.PALETTE_SOURCEGRAPH);
		PVector prevPoint = null;
		for (PVector p: sourcePoints) {
			if (prevPoint != null) {
				g.line(p.x, p.y, prevPoint.x, prevPoint.y);
			}
			prevPoint = p;
		}
		
		// Draw player graph
		g.stroke(Main.PALETTE_PLAYERGRAPH);
		prevPoint = null;
		for (PVector p: playerPoints) {
			if (prevPoint != null) {
				g.line(p.x, p.y, prevPoint.x, prevPoint.y);
			}
			prevPoint = p;
		}
				
	}
	
	public static SceneType type() {
		return SceneType.End;
	}

}
