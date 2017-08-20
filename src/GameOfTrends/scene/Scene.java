package GameOfTrends.scene;

import processing.core.PGraphics;

public abstract class Scene {
	
	public abstract void update(double delta); 
	
	public abstract void draw(PGraphics g);
	
	/**
	 * Called when the scene is entered (just before the first render of the scene)
	 */
	public void onEnter() {}
	
	/**
	 * Called when the scene is exited
	 */
	public void onExit() {}
	
	
	public static SceneType type() {
		return SceneType.Invalid;
	}
	
}
