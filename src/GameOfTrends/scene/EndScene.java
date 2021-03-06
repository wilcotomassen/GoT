package GameOfTrends.scene;

import java.util.ArrayList;

import com.cleverfranke.util.FileSystem;

import GameOfTrends.Main;
import de.looksgood.ani.Ani;
import de.looksgood.ani.AniSequence;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class EndScene extends Scene {
	
	// Consts
	private final float GRAPH_MARGIN = 0;
	private final float MIN_Y = 797;
	private final float MAX_Y = 326;
	
	private PImage background;
	private float timer = 0;
	private GameScene gameScene;
	private ArrayList<PVector> playerPoints = new ArrayList<>(500);
	private ArrayList<PVector> sourcePoints = new ArrayList<>();
	
<<<<<<< HEAD
	
	private ArrayList<ArrayList<PVector>> historicPoints = new ArrayList<ArrayList<PVector>>();
	
	private PFont topFont;
	private PFont percFont;
	private String percString;
	
=======
	private ArrayList<ArrayList<PVector>> historicPoints = new ArrayList<ArrayList<PVector>>();
	
>>>>>>> 6fb1689a6adb31d0ee2e02b3d710422135016202
	// Animation props
	private AniSequence sequence;
	private float sourceAnimProgress;
	private float playerAnimProgress;
	private float foregroundAnimProgress;
	private float historyAnimProgress;
	
	public EndScene() {
		background = Main.applet.loadImage(FileSystem.getApplicationPath("gfx/end-bg.png"));
		topFont = Main.applet.createFont(FileSystem.getApplicationPath("gfx/W Foundry - Sonny Gothic Condensed Regular.otf"), 120f);
		percFont = Main.applet.createFont(FileSystem.getApplicationPath("gfx/W Foundry - Sonny Gothic Condensed Regular.otf"), 480f);
		
		gameScene = (GameScene) Main.self.scenes.get(GameScene.type());
		
		sequence = new AniSequence(Main.applet);
		sequence.beginSequence();
		sequence.add(Ani.to(this, 1, 1, "sourceAnimProgress", 1));
		sequence.add(Ani.to(this, 1, .3f, "playerAnimProgress", 1));
		sequence.add(Ani.to(this, .5f, .01f, "foregroundAnimProgress", 1));
		sequence.add(Ani.to(this, 1f, 1f, "historyAnimProgress", 1));
		sequence.endSequence();
		
	}
	
	@Override
	public void onEnter() {
		timer = 0;
		sourceAnimProgress = 0;
		playerAnimProgress = 0;
		historyAnimProgress = 0;
<<<<<<< HEAD
		
		percString = String.valueOf(50 + Math.round(Math.random() * 44)) + "%";
=======
>>>>>>> 6fb1689a6adb31d0ee2e02b3d710422135016202
		
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
		
		sequence.start();
		
	}
	
	@SuppressWarnings("unchecked")
	public void onExit() {
		historicPoints.add((ArrayList<PVector>) playerPoints.clone());
	}

	@Override
	public void update(double delta) {
		timer += delta;
		if (timer > Main.SCENE_BUTTON_TIMEOUT &&  Main.self.buttonDown) {
			Main.triggerNextScene(SceneType.Intro);
		}
	}

	@Override
	public void draw(PGraphics g) {
		g.image(background, 0, 0);
		
		// Draw source graph
		g.stroke(Main.PALETTE_SOURCEGRAPH);
		PVector prevPoint = null;
		g.strokeWeight(5);
		for (int i = 0; i < Math.round(sourceAnimProgress * sourcePoints.size()) ; i++) {
			PVector p = sourcePoints.get(i);
			if (prevPoint != null) {
				g.line(p.x, p.y, prevPoint.x, prevPoint.y);
			}
			prevPoint = p;
		}
		
		// Draw player graph
		g.stroke(Main.PALETTE_PLAYERGRAPH);
		prevPoint = null;
		for (int i = 0; i < Math.round(playerAnimProgress * playerPoints.size()) ; i++) {
			PVector p = playerPoints.get(i);
			if (prevPoint != null) {
				g.line(p.x, p.y, prevPoint.x, prevPoint.y);
			}
			prevPoint = p;
		}
		
		// Draw history
		g.stroke(255, 70);
		g.strokeWeight(3);
		for (int j = 0; j < historicPoints.size(); j++) {
			ArrayList<PVector> hList = historicPoints.get(j);
			prevPoint = null;
			for (int i = 0; i < Math.round(historyAnimProgress * hList.size()) ; i++) {
				PVector p = hList.get(i);
				if (prevPoint != null) {
					g.line(p.x, p.y, prevPoint.x, prevPoint.y);
				}
				prevPoint = p;
			}
		}
		
		if (foregroundAnimProgress > .5f) {
<<<<<<< HEAD
			
			g.textAlign(PConstants.CENTER, PConstants.TOP);
			g.fill(255);
			g.textFont(topFont);
			g.text("Nice try!", 0, 100, Main.applet.width, 500);
			
			g.fill(255);
			g.textFont(percFont);
			g.text(percString, 0, 200, Main.applet.width, 300);
			
=======
			g.pushMatrix();
			g.pushStyle();
			g.blendMode(PConstants.SCREEN);
			g.translate(0,  0, 10);
			g.image(foreground, 0, 0);
			g.popStyle();
			g.popMatrix();
>>>>>>> 6fb1689a6adb31d0ee2e02b3d710422135016202
		}
				
	}
	
	public static SceneType type() {
		return SceneType.End;
	}

}
