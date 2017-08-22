package GameOfTrends.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.cleverfranke.util.FileSystem;

import GameOfTrends.Main;
import GameOfTrends.SourceDataSeries;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

public class GameScene extends Scene {
	
	private final float DATAPOINT_WIDTH = 5;
	private final float WALL_Z = 0;
	private final float SOURCE_Z = 1;
	private final float PLAYER_Z = 2;
	private final float LINE_Z = 5;
	
	public SourceDataSeries sourceData;
	private PShape sourceDataGraph;
	private HashMap<Float, String> sourceDataGraphKeys;
	private ArrayList<PVector> playerPoints = new ArrayList<>(500);
	public ArrayList<PVector> playerScorePoints = new ArrayList<>(500);
	public ArrayList<Particle> particles = new ArrayList<>();
	
	private PFont labelFont;
	private PImage overlay;
	
	private float scoreStartX;
	private float endX;	
	private float currentX = 0;
	private boolean sourceExploded;
	
	public GameScene() {
		labelFont = Main.applet.createFont(FileSystem.getApplicationPath("gfx/W Foundry - Sonny Gothic Condensed Book.otf"), 1.4f);
	}
	
	public void setup(SourceDataSeries sourceData) {

		this.sourceData = sourceData;
		sourceDataGraph = sourceData.getGameGraph(
				0, sourceData.getRangeMax(), 
				0, -50, 
				SOURCE_Z, 
				DATAPOINT_WIDTH, 
				Main.PALETTE_SOURCEGRAPH);
		
		sourceDataGraphKeys = new HashMap<>();
		HashMap<Integer, String> sourceDataKeys = sourceData.getKeys();
		for (Entry<Integer, String> e: sourceDataKeys.entrySet()) {
			float x = (float) e.getKey() * DATAPOINT_WIDTH;
			sourceDataGraphKeys.put(x, e.getValue());
		}
		
		overlay = Main.applet.loadImage(FileSystem.getApplicationPath("gfx/gameplay-fg.png"));
		
		scoreStartX = (((float) sourceData.getDataPointCount() - 1f) * Main.SOURCEDATA_PERC) * DATAPOINT_WIDTH;
		endX = sourceData.getDataPointCount() * DATAPOINT_WIDTH;
		
	}
	
	@Override
	public void onEnter() {
		currentX = 0;
		playerPoints.clear();
		playerScorePoints.clear();
		particles.clear();
		sourceExploded = false;
	}
	
	public void update(double delta) {
		// Update x
		currentX += .4f * delta;
		
		// Update player graph
		float y = Main.self.distanceValue  * -50f;
		playerPoints.add(new PVector(currentX, y, PLAYER_Z));
		
		if (currentX >= scoreStartX) {
			playerScorePoints.add(new PVector(currentX, y, PLAYER_Z));
			
			if (!sourceExploded) {
				for (int i = 0; i < 100; i++) {
					particles.add(new Particle(new PVector(currentX, y, PLAYER_Z)));
				}
				sourceExploded = true;
			}
		}
		
		if (currentX >= endX) {
			Main.triggerNextScene(SceneType.End);
		}
		
		// Update particles
		for (Particle p: particles) {
			p.update(delta);
		}
		
		// Remove dead particles
		for (int i = particles.size()-1; i >= 0; i--) {
			Particle p = particles.get(i);
			p.update(delta);
			if (p.isDead()) {
				particles.remove(i);
			}
		}
		
	}
	
	public void draw(PGraphics g) {
		g.background(34, 0, 51);
		
		// Setup camera
		g.camera(
				50.0f + currentX, 0, 80,		// Eye position
				currentX, -25f, 0f, 			// Look at position
				0.0f, 1.0f, 0.0f);				// Rotation
		g.perspective(
			PConstants.PI / 4.0f, // FOV
			(float) Main.applet.width / (float) Main.applet.height, // Aspect
			1, 					// Near clipping plane
			50f * 10.0f 		// Far clipping plane
		);
		
		// Draw gizmo
//		drawGizmo(g);
		
		// Draw wall and source graph
		drawWall(g);
		drawSourceGraph(g);
		drawPlayerGraph(g);
		
		// Draw guess moment
		g.pushMatrix();
		g.pushStyle();
		g.blendMode(PConstants.ADD);
		g.translate(scoreStartX,  20, LINE_Z); 
		g.rotateY(PConstants.HALF_PI);
		g.fill(255, 255, 255, 50);
		g.noStroke();
		g.rect(-2, 0, 10, -90);
		g.popMatrix();
		g.popStyle();
		
		// Draw particles
		for (Particle p: particles) {
			p.draw(g);
		}
		
		// Draw UI
		drawUI(g);
		
	}
	
	private void drawWall(PGraphics g) {
		g.pushStyle();
		
		g.strokeWeight(1);
		g.stroke(153, 136, 255);
		for (int y = 0; y > -55; y -= 5) {
			g.line(0, y, WALL_Z, currentX + 100f, y, WALL_Z);
		}
		
		g.popStyle();
	}

	private void drawSourceGraph(PGraphics g) {
		g.pushStyle();
		
		// Draw graph
		g.shape(sourceDataGraph);
		
		// Draw keys
		g.textMode(PConstants.SHAPE);
		g.textFont(labelFont);
		g.textLeading(1.8f);
		g.fill(255);
		g.textAlign(PConstants.CENTER, PConstants.TOP);
		for (Entry<Float, String> e: sourceDataGraphKeys.entrySet()) {
			if (e.getKey() > currentX - 200 && e.getKey() < currentX + 100) {
				String value = e.getValue();
				value = value.replaceAll("-", "\n");
				g.text(value, e.getKey(), 2);
			}
		}
		
		g.popStyle();
	}
	
	private void drawPlayerGraph(PGraphics g) {
		g.pushMatrix();
		g.pushStyle();
		
		// Draw player graph
		g.stroke(Main.PALETTE_PLAYERGRAPH);
		g.strokeWeight(4);
		PVector prevPoint = null;
		for (PVector p: playerPoints) {
			if (prevPoint != null) {
				g.line(prevPoint.x, prevPoint.y, prevPoint.z,  p.x, p.y, p.z);
			}
			prevPoint = p;
		}
		
		g.translate(0,  0, PLAYER_Z);
		g.fill(Main.PALETTE_PLAYERGRAPH);
		g.ellipse(prevPoint.x, prevPoint.y, .3f, .3f);
		
		//@todo: we can clear the first part of the points
		// list, to save performance on drawing: just keep n
		// points in the list
		while (playerPoints.size() > 400) {
			playerPoints.remove(0);
		}
		
		g.popStyle();
		g.popMatrix();
	}
	
	private void drawUI(PGraphics g) {
		g.pushStyle();
		
		// Disable 3D
		g.hint(PConstants.DISABLE_DEPTH_TEST);
		g.camera();
		g.ortho();
		g.noLights();
		
		g.image(overlay, 0, 0);
		
//		g.textMode(PConstants.SHAPE);
//		g.textSize(12);
//		g.fill(255);
//		g.text(Main.fps, 100, 100);
		
		// Re-enable 3D
		g.hint(PConstants.ENABLE_DEPTH_TEST);
		
		g.popStyle();
	}
	
	
	@SuppressWarnings("unused")
	private void drawGizmo(PGraphics g) {
		g.pushStyle();
		
		// X: Green
		g.stroke(0, 255, 0);
		g.line(0, 0, 0, 10, 0, 0);
		
		// Y: red
		g.stroke(255, 0, 0);
		g.line(0, 0, 0, 0, 10, 0);
		
		// Z: blue
		g.stroke(0, 0, 255);
		g.line(0, 0, 0, 0, 0, 10);
		
		g.popStyle();
	}

	public static SceneType type() {
		return SceneType.Game;
	}

}
