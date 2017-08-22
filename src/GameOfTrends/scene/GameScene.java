package GameOfTrends.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.cleverfranke.util.PColor;

import GameOfTrends.Main;
import GameOfTrends.SourceDataSeries;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;

public class GameScene extends Scene {
	
	private final float DATAPOINT_WIDTH = 5;
	
	private SourceDataSeries sourceData;
	private PShape sourceDataGraph;
	private HashMap<Float, String> sourceDataGraphKeys;
	
	
	private float currentX = 0;
	private ArrayList<PVector> playerPoints = new ArrayList<>(500);
	
	public void setup(SourceDataSeries sourceData) {

		this.sourceData = sourceData;
		sourceDataGraph = sourceData.getGraph(
				0, sourceData.getRangeMax(), 
				0, -50, 
				-1f, 
				DATAPOINT_WIDTH, 
				PColor.color(255, 255, 0));
		
		sourceDataGraphKeys = new HashMap<>();
		HashMap<Integer, String> sourceDataKeys = sourceData.getKeys();
		for (Entry<Integer, String> e: sourceDataKeys.entrySet()) {
			float x = (float) e.getKey() * DATAPOINT_WIDTH;
			sourceDataGraphKeys.put(x, e.getValue());
		}
		
	}
	
	@Override
	public void onEnter() {
		currentX = 0;
		playerPoints.clear();
	}
	
	public void update(double delta) {
		// Update x
		currentX += .4f * delta;
		
		// Update player graph
		float y = Main.self.distanceValue  * -50f;
		playerPoints.add(new PVector(currentX, y, -1));
		
	}
	
	public void draw(PGraphics g) {
		g.background(35);
		
		// Setup camera
		g.camera(
				50.0f + currentX, 0, 80,	// Eye position
				currentX, -25f, 0f, 			// Look at position
				0.0f, 1.0f, 0.0f);				// Rotation
		g.perspective(
			PConstants.PI / 4.0f, // FOV
			(float) Main.applet.width / (float) Main.applet.height, // Aspect
			1, 					// Near clipping plane
			50f * 10.0f 		// Far clipping plane
		);
		
		// Draw gizmo
		drawGizmo(g);
		
		// Draw wall and source graph
		drawWall(g);
		drawSourceGraph(g);
		drawPlayerGraph(g);
		
		// Mask
		g.noStroke();
		g.fill(35);
		g.rect(currentX, 1, 80, -80);
		
		// Draw UI
		drawUI(g);
		
	}
	
	private void drawWall(PGraphics g) {
		g.pushStyle();
		
		g.stroke(15, 100, 90);
		for (int y = 0; y > -50; y -= 5) {
			g.line(0, y, -3f, currentX, y, -3f);
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
		g.pushStyle();
		
		// Draw player graph
		g.stroke(255, 0, 255);
		g.strokeWeight(4);
		PVector prevPoint = null;
		for (PVector p: playerPoints) {
			if (prevPoint != null) {
				g.line(prevPoint.x, prevPoint.y, prevPoint.z, 
						p.x, p.y, p.z);
			}
			prevPoint = p;
		}
		
		//@todo: we can clear the first part of the points
		// list, to save performance on drawing: just keep n
		// points in the list
		
		g.popStyle();
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
		g.text(String.valueOf(1f - (float) Main.applet.mouseY / (float) Main.applet.height), 100, 120);
		
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
