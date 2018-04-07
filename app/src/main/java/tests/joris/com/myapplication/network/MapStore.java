package tests.joris.com.myapplication.network;

import tests.joris.com.myapplication.model.Map;

public interface MapStore {

	void updateMap(String mapId, Map myMap);

	void computeShortestPath(String mapId, String startId, String endId);
}
