package GameOfTrends;

import java.util.ArrayList;
import java.util.HashMap;

import com.cleverfranke.util.FileSystem;
import com.cleverfranke.util.PColor;

import GameOfTrends.scene.*;
import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PShape;
import processing.data.JSONObject;
import websockets.WebsocketClient;

public class Main extends PApplet {
	
	// Constants
	private final int TARGET_FPS = 60;
	private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
	private final float SENSOR_MIN = 0.1f;
	private final float SENSOR_MAX = 0.5f;
	
	// Statics
	public static PApplet applet;
	public static Main self;
	
	public static int PALETTE_PLAYERGRAPH = PColor.color(255, 0, 255);
	public static int PALETTE_SOURCEGRAPH = PColor.color(255, 255, 0);
	
	ArrayList<PShape> lines = new ArrayList<>();
	ArrayList<Integer> lineColors = new ArrayList<>();
	
	// Sensors
	@SuppressWarnings("unused")
	private WebsocketClient webSocket;
	public float distanceValue;
	public boolean buttonDown;
	
	// Game loop members
	private long lastLoopTime = System.nanoTime();
	private int fpsCounter = 0;
	private long lastFpsTime;
	public static int fps = 0;
	
	// Scene management
	public HashMap<SceneType, Scene> scenes = new HashMap<>();	// All available scenes
	private Scene currentScene;									// Current scene
	private static SceneType nextScene = null;					// Flag for transitioning to the next scene, null if no switch is required
	
	public void settings() {
		size(1920, 1080, P3D);
		smooth(8);
		
		applet = this;
		self = this;
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
		sourceData.loadData(FileSystem.getApplicationPath("data/Data_privacy_2014-today-clean.csv"));
		
		// Setup scenes
		scenes.put(IntroScene.type(), new IntroScene());
		GameScene game = new GameScene();
		game.setup(sourceData);
		scenes.put(GameScene.type(), game);
		scenes.put(EndScene.type(), new EndScene());
		gotoScene(SceneType.Intro);
		
		// Setup painting device defaults 
		ellipseMode(RADIUS);
		
		// Start the websocket
		webSocket = new WebsocketClient(this, "ws://localhost:9010");
		
	}
	
	public void draw() {
		
		// Handle scene switching 
		if (nextScene != null) {
			gotoScene(nextScene);
			nextScene = null;
		}
		
		// Game loop bookkeeping
		long now = System.nanoTime();
		long updateLength = now - lastLoopTime;
		lastLoopTime = now;
		double delta = updateLength / ((double) OPTIMAL_TIME);
		
		// update the frame counter
		lastFpsTime += updateLength;
		fpsCounter++;

		// Update fps if a second has passed since
		// we last recorded
		if (lastFpsTime >= 1000000000) {
			fps = fpsCounter;
			lastFpsTime = 0;
			fpsCounter = 0;
		}
		
		// Update game logic/physics
		currentScene.update(delta);
		
		// Render
		currentScene.draw(g);
		
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
	
	public static void triggerNextScene(SceneType type) {
		nextScene = type;
	}
	
	private void gotoScene(SceneType type) {
		
		// Exit current scene
		if (currentScene != null) {
			currentScene.onExit();
		}
		
		// Enter new scene
		currentScene = scenes.get(type);
		currentScene.onEnter();
		
	}
	
	public static void main(String[] args) {
		// Program execution starts here
		PApplet.main(Main.class.getName());
	}
	
	public void webSocketEvent(String msg){
		JSONObject json = parseJSONObject(msg);
		distanceValue = json.getFloat("sensor_0"); // Raw value
		distanceValue = map(distanceValue, SENSOR_MIN, SENSOR_MAX, 1f, 0f); // Map to [0, 1]
		distanceValue = min(1f, max(0f, distanceValue)); // Limit to [0, 1]
		buttonDown = (json.getFloat("sensor_1") >= .5f);
	}

}
