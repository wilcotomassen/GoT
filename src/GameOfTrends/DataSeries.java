package GameOfTrends;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PShape;

public class DataSeries {
	
	protected ArrayList<Float> values = new ArrayList<>();
	
	public Iterator<Float> iterator() {
		return values.iterator();
	}
	
	public PShape getGraph(float yDataMin, float yDataMax, float yPosMin, float yPosMax, float z, float xWidth, int color) {
		
		PShape s = Main.applet.createShape();
		s.beginShape();
		s.stroke(color);
		s.strokeWeight(4);
		s.noFill();
		
		for (int i = 0; i < values.size(); i++) {
			float x = (float) i * xWidth;
			float y = PApplet.map(values.get(i), yDataMin, yDataMax, yPosMin, yPosMax);
			s.vertex(x, y, z);
		}

		s.endShape();
		
		return s;
	}
	
	public PShape getGameGraph(float yDataMin, float yDataMax, float yPosMin, float yPosMax, float z, float xWidth, int color) {
		
		PShape s = Main.applet.createShape();
		s.beginShape();
		s.stroke(color);
		s.strokeWeight(4);
		s.noFill();
		
		int endIndex = (int) (.8f * (float) values.size()); 
		for (int i = 0; i < endIndex; i++) {
			float x = (float) i * xWidth;
			float y = PApplet.map(values.get(i), yDataMin, yDataMax, yPosMin, yPosMax);
			s.vertex(x, y, z);
		}
		s.endShape();
		
		return s;
	}
	
	public int getDataPointCount() {
		return values.size();
	}
	
	
}
