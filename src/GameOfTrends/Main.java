package GameOfTrends;

import java.util.ArrayList;
import java.util.Random;

import com.cleverfranke.util.FileSystem;

import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PShape;

public class Main extends PApplet {
	
	// Constants
	private final int TARGET_FPS = 60;
	private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
	
	// Statics
	public static PApplet applet;
	
	ArrayList<PShape> lines = new ArrayList<>();
	ArrayList<Integer> lineColors = new ArrayList<>();
	
	private long lastLoopTime = System.nanoTime();
	private GameLevelRenderer levelRenderer;
	
	public void settings() {
		size(1920, 1080, P3D);
		smooth(8);
		
		applet = this;
	}
	
	public void setup() {
		
		// Framerate is limited in draw loop, so make sure this is more than TARGET_FPS 
		// to give the machine a fighting change of reaching it
		frameRate(TARGET_FPS * 2); 
		
		// Init Ani library
		Ani.init(this);
		Ani.noAutostart();
		Ani.setDefaultTimeMode("SECONDS");
		
		// Load source data
		SourceDataSeries sourceData = new SourceDataSeries();
		sourceData.loadData(FileSystem.getApplicationPath("data/exampleseries.csv"));
		
		// Setup game renderer
		levelRenderer  = new GameLevelRenderer();
		levelRenderer.setup(sourceData);
		
		// Setup palette
//		lineColors.add(color(25, 250, 50));
//		lineColors.add(color(75, 25, 255));
//		lineColors.add(color(240, 20, 255));
//		lineColors.add(color(245, 255, 15));
//		lineColors.add(color(30, 255, 200));
		
//		float xoff = 0.0f;
//		Random colorRandom = new Random();
//		for (int i = 0; i < 14; i++) {
//			
//			PShape s = createShape();
//			s.beginShape();
//			s.stroke(lineColors.get(colorRandom.nextInt(lineColors.size())));
//			s.strokeWeight(4);
//			s.noFill();
//			for (int j = 0; j < 1000; j++) {
//				
//				xoff = xoff + .01f;
//				float y = noise(xoff) * -50f;
//				s.vertex(j, y, i * .2f); 
//			}
//			s.endShape();
//			lines.add(s);
//			
//		}
		
		// Setup painting device defaults 
		ellipseMode(RADIUS);
	}
	
	public void draw() {
		
		// Game loop bookkeeping
		long now = System.nanoTime();
		long updateLength = now - lastLoopTime;
		lastLoopTime = now;
		double delta = updateLength / ((double) OPTIMAL_TIME);
		
		// Update game logic/physics
		levelRenderer.update(delta);
		
		// Render
		levelRenderer.draw(g);
		
		// Wait until end of frame
		try {
			long timeoutVal = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
			if (timeoutVal > 0) {
				Thread.sleep(timeoutVal);
			}
		} catch (InterruptedException e) {
			System.err.println("Thread sleep issue");
		}
	}
	
	public static void main(String[] args) {
		// Program execution starts here
		PApplet.main(Main.class.getName());
	}
	

}
