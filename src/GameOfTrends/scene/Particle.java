package GameOfTrends.scene;

import GameOfTrends.Main;
import processing.core.PGraphics;
import processing.core.PVector;

public class Particle {
	
	float life;
	PVector pos,speed,grav;
	float splash = 5;
	
	Particle (PVector pos) {
		this.pos = pos;
		this.life = 10000f;
		
		float startx = 8f + Main.applet.random(-splash,splash);
		float starty = 8f + Main.applet.random(-splash,splash);
		float xspeed = Main.applet.random(-3,3);
		float yspeed = Main.applet.random(-3,3);
		
		pos = new PVector(startx,starty);
		speed = new PVector(xspeed,yspeed);
		grav = new PVector(0,0.02f);
	    
	}
	
	void update(double delta) {
		pos.add(speed);
		speed.add(grav);
		
		life -= delta * 0.001f;
	}
	
	void draw(PGraphics g) {
		g.pushMatrix();
		g.translate(pos.x, pos.y, pos.z);
		g.noStroke();
		g.fill(Main.PALETTE_PLAYERGRAPH);
		g.ellipse(0, 0, .3f, .3f);
		g.popMatrix();
	}

	public boolean isDead() {
		return life < 0;
	}

}
