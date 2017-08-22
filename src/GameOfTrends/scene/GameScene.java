package GameOfTrends.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import GameOfTrends.Main;
import GameOfTrends.SourceDataSeries;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;

public class GameScene extends Scene {
	
	private final float DATAPOINT_WIDTH = 5;
	private final float WALL_Z = 0;
	private final float SOURCE_Z = 10;
	private final float PLAYER_Z = 20;
	
	public SourceDataSeries sourceData;
	private PShape sourceDataGraph;
	private HashMap<Float, String> sourceDataGraphKeys;
	private ArrayList<PVector> playerPoints = new ArrayList<>(500);
	public ArrayList<PVector> playerScorePoints = new ArrayList<>(500);
	
	private float scoreStartX;
	private float endX;	
	private float currentX = 0;
	
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
		
		scoreStartX = (int) ((float) sourceData.getDataPointCount() * .8f) * DATAPOINT_WIDTH;
		endX = sourceData.getDataPointCount() * DATAPOINT_WIDTH;
		
	}
	
	@Override
	public void onEnter() {
		currentX = 0;
		playerPoints.clear();
		playerScorePoints.clear();
	}
	
	public void update(double delta) {
		// Update x
		currentX += .4f * delta;
		
		// Update player graph
		float y = Main.self.distanceValue  * -50f;
		playerPoints.add(new PVector(currentX, y, PLAYER_Z));
		
		if (currentX >= scoreStartX) {
			playerScorePoints.add(new PVector(currentX, y, PLAYER_Z));
		}
		
		if (currentX >= endX) {
			Main.triggerNextScene(SceneType.End);
		}
		
	}
	
	public void draw(PGraphics g) {
		g.background(35);
		
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
		g.translate(scoreStartX,  0, 10);
		g.noFill();
		g.strokeWeight(4);
		g.stroke(255);
		g.line(0, 0, 0, -50);
		g.popMatrix();
		g.popStyle();
		
		// Draw UI
		drawUI(g);
		
	}
	
	private void drawWall(PGraphics g) {
		g.pushStyle();
		
		g.stroke(15, 100, 90);
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
		g.textSize(1);
		g.fill(255);
		g.textAlign(PConstants.CENTER, PConstants.TOP);
		for (Entry<Float, String> e: sourceDataGraphKeys.entrySet()) {
			if (e.getKey() > currentX - 200 && e.getKey() < currentX) {
				g.text(e.getValue(), e.getKey(), 2);
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
		while (playerPoints.size() > 300) {
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
		
		g.textMode(PConstants.SHAPE);
		g.textSize(12);
		g.fill(255);
		g.text(Main.fps, 100, 100);
		
		// Re-enable 3D
		g.hint(PConstants.ENABLE_DEPTH_TEST);
		
		g.popStyle();
	}
	
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
