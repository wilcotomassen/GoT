package GameOfTrends;

import java.util.HashMap;
import processing.data.Table;
import processing.data.TableRow;

public class SourceDataSeries extends DataSeries {
	
	private float rangeMin, rangeMax;							// y-axis range
	private HashMap<Integer, String> keys = new HashMap<>(); 	// Index->Label for x-axis
	
	public void loadData(String filename) {
		
		// Reset any current keys/values
		values.clear();
		keys.clear();
		rangeMin = Float.MAX_VALUE;
		rangeMax = Float.MIN_VALUE;
		
		// Load new keys/values
		int index = 0;
		Table data = Main.applet.loadTable(filename, "header");
		for (TableRow row: data.rows()) {
			String key = row.getString("key");
			float value = row.getFloat("value");

			// Add key/value
			if (!key.isEmpty()) {
				keys.put(index, key);
			}
			values.add(value);
			
			// Update range of series
			if (value > rangeMax) {
				rangeMax = value;
			} 
			if (value < rangeMin) {
				rangeMin = value;
			}
			
			index++;
		}
		
	}

	public HashMap<Integer, String> getKeys() {
		return keys;
	}
	
	public float getRangeMin() {
		return rangeMin;
	}
	
	public float getRangeMax() {
		return rangeMax;
	}

}
