package GameOfTrends;

import java.util.ArrayList;
import java.util.Random;

import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PShape;

public class Main extends PApplet {
	
	public static PApplet applet;
	
	float cameraDeltaX = 0;
	
	ArrayList<PShape> lines = new ArrayList<>();
	
	ArrayList<Integer> lineColors = new ArrayList<>();
	
	public void settings() {
		size(1920, 1080, P3D);
		smooth(8);
		applet = this;
	}
	
	public void setup() {
		// Init Ani library
		Ani.init(this);
		Ani.noAutostart();
		Ani.setDefaultTimeMode("SECONDS");
		
		// Setup palette
		lineColors.add(color(25, 250, 50));
		lineColors.add(color(75, 25, 255));
		lineColors.add(color(240, 20, 255));
		lineColors.add(color(245, 255, 15));
		lineColors.add(color(30, 255, 200));
		
		float xoff = 0.0f;
		Random colorRandom = new Random();
		for (int i = 0; i < 14; i++) {
			
			PShape s = createShape();
			s.beginShape();
			s.stroke(lineColors.get(colorRandom.nextInt(lineColors.size())));
			s.strokeWeight(4);
			s.noFill();
			for (int j = 0; j < 1000; j++) {
				
				xoff = xoff + .01f;
				float y = noise(xoff) * -50f;
				s.vertex(j, y, i * .2f); 
			}
			s.endShape();
			lines.add(s);
			
		}
		
		
		ellipseMode(RADIUS);
	}
	
	public void draw() {
		background(35);

		// Setup camera
		camera(
				50.0f + cameraDeltaX, 0, 80,	// Eye position
				cameraDeltaX, -25f, 0f, 			// Look at position
				0.0f, 1.0f, 0.0f);				// Rotation
		float fov = PI/4.0f;
		perspective(fov, (float) width / (float) height, 1, 50f * 10.0f);
		
		
		
		// 3D Gizmo
		
		// X: Green
		stroke(0, 255, 0);
		line(0, 0, 0, 10, 0, 0);
		// Y: red
		stroke(255, 0, 0);
		line(0, 0, 0, 0, 10, 0);
		// Z: blue
		stroke(0, 0, 255);
		line(0, 0, 0, 0, 0, 10);
		
		// Wall
		stroke(15, 100, 90);
		for (int y = 0; y > -50; y -= 5) {
			line(0, y, -1, 1000, y, -1);
		}
		
		// Draw lines
		for (PShape s: lines) {
			shape(s);
		}
		
		
		hint(DISABLE_DEPTH_TEST);
		camera();
		ortho();
		noLights();
		fill(255, 0, 0);
		text(frameRate, 100, 100);
		hint(ENABLE_DEPTH_TEST);
		
			

		// Move camera
		cameraDeltaX += .1f;

	}
	
	public static void main(String[] args) {
		// Program execution starts here
		PApplet.main(Main.class.getName());
	}

}
