package tests.joris.com.myapplication.presenter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tests.joris.com.myapplication.model.APIPath;
import tests.joris.com.myapplication.model.Line;
import tests.joris.com.myapplication.model.Map;
import tests.joris.com.myapplication.model.Position;
import tests.joris.com.myapplication.model.Stop;
import tests.joris.com.myapplication.network.MapDataStore;
import tests.joris.com.myapplication.network.MapListener;
import tests.joris.com.myapplication.network.MapStore;
import tests.joris.com.myapplication.view.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapPresenter extends MvpBasePresenter<MapView> implements MapListener {
	private static final String TAG = MapPresenter.class.getName();
	private java.util.Map<String, Line> lines = new HashMap<>();
	private java.util.Map<String, Stop> stops = new HashMap<>();
	private String jsonLines;
	private String jsonStops;
	private MapStore store;
	private Map map;

	public MapPresenter() {
		store = new MapDataStore(this);
	}

	public java.util.Map<String, Line> getLines() {
		return lines;
	}

	public java.util.Map<String, Stop> getStops() {
		return stops;
	}

	@Override
	public void onPathFailedReceived(Throwable t) {
		Log.e(TAG, "onPathFailedReceived ");
	}

	@Override
	public void onPathReceived(final APIPath path) {
		ifViewAttached(new ViewAction<MapView>() {
			@Override
			public void run(@NonNull MapView view) {
				if (path != null) {
					view.drawPath(stops, path.getPath(), path.getDistance());
				}
			}
		});

	}

	public void findShortestPath(String mapId, String startId, String endId) {
		store.computeShortestPath(mapId, startId, endId);
		ifViewAttached(new ViewAction<MapView>() {
			@Override
			public void run(@NonNull MapView view) {
				view.resetSelection();
			}
		});
	}

	public void updateMap(String mapId) {
		store.updateMap(mapId, this.map);
	}

	public void parseLines(String jsonLines) {
		this.jsonLines = jsonLines;
		new GetLines().execute();
	}

	public void parseStops(String jsonStops) {
		this.jsonStops = jsonStops;
		new GetStops().execute();
	}

	public void createMapJSON() {
		java.util.Map<String, java.util.Map<String, Double>> tempMap;
		tempMap = new HashMap<>();
		java.util.Map<String, Double> subTempMap = new HashMap<>();

		for (java.util.Map.Entry<String, Stop> e : stops.entrySet()) {

			for (Position pos : e.getValue().getLines()) {
				int prev = pos.getPos() - 1;
				int succ = pos.getPos() + 1;
				Line tempLine = lines.get(pos.getLine());
				if (!tempMap.containsKey(e.getKey())) {
					subTempMap = new HashMap<>();
					tempMap.put(e.getKey(), subTempMap);
				}
				subTempMap = tempMap.get(e.getKey());
				if (tempLine != null && tempLine.getStations().containsKey(prev)) {

					subTempMap.put(tempLine.getStations().get(prev),
						computeDistance(tempLine.getStations().get(prev), tempLine.getStations().get(prev + 1)));
				}
				if (tempLine != null && tempLine.getStations().containsKey(succ)) {
					subTempMap.put(tempLine.getStations().get(succ),
						computeDistance(tempLine.getStations().get(succ - 1), tempLine.getStations().get(succ)));
				}
			}
		}
		this.map = new Map(tempMap);
	}

	private double computeDistance(String nodeStart, String nodeEnd) {
		float[] results = new float[1];
		Stop sNode = stops.get(nodeStart);
		Stop eNode = stops.get(nodeEnd);
		Location.distanceBetween(sNode.getLatitude(), sNode.getLongitude(),
			eNode.getLatitude(), eNode.getLongitude(), results);
		return results[0];
	}

	public void drawStationLines() {
		for (java.util.Map.Entry<String, Line> e : lines.entrySet()) {
			List<LatLng> latStations = new ArrayList<>();
			java.util.Map<Integer, String> stationsTemp = e.getValue().getStations();
			if (stationsTemp != null) {
				int i = 1;
				while (stationsTemp.containsKey(i)) {
					String tempStat = stationsTemp.get(i);
					Stop tempStop = stops.get(tempStat);
					if (tempStop != null) {
						latStations.add(new LatLng(tempStop.getLatitude(), tempStop.getLongitude()));
					}
					i++;
				}
				PolylineOptions line =
					new PolylineOptions()
						.addAll(latStations)
						.width(10)
						.color(Color.parseColor(e.getValue().getColor()));
				getView().drawPolyLine(line);
			} else {
				Log.e(TAG, "Error");
			}
		}
	}

	public void drawNodeColors() {
		LatLng loc = null;
		for (java.util.Map.Entry<String, Stop> e : stops.entrySet()) {
			loc = new LatLng(e.getValue().getLatitude(), e.getValue().getLongitude());
			String color;
			if (e.getValue().getLines().isEmpty() || e.getValue().getLines().size() > 1) {
				color = "#000000";
			} else {
				try {
					color = lines.get(e.getValue().getLines().get(0).getLine()).getColor();
				} catch (Exception ee) {
					color = "#000000";
				}
			}
			getView().addMarker(new MarkerOptions().position(loc)
				.title(e.getValue().getName())
				.icon(getMarkerIcon(color)));
		}
		getView().centerAndZoom(loc);
	}

	private BitmapDescriptor getMarkerIcon(String color) {
		if (color == null || color.length() < 6) {
			return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
		}
		float[] hsv = new float[3];
		Color.colorToHSV(Color.parseColor(color), hsv);
		return BitmapDescriptorFactory.defaultMarker(hsv[0]);
	}

	private class GetLines extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			if (jsonLines != null) {
				try {
					JSONArray jsonArr = new JSONArray(jsonLines);
					for (int i = 0; i < jsonArr.length(); i++) {
						JSONObject line = jsonArr.getJSONObject(i);
						String name = line.getString("name");
						String color = line.getString("color");
						String number = line.getString("number");
						String routeName = line.getString("route_name");
						lines.put(name, new Line(name, color, number, routeName));
					}
				} catch (final JSONException e) {
					logJSONError(e);
				}
			}
			return null;
		}
	}

	private class GetStops extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			if (jsonStops != null) {
				try {
					parseStops();
					constructLineStations();
				} catch (final JSONException e) {
					logJSONError(e);
				}
			}
			return null;
		}

		private void parseStops() throws JSONException {
			JSONArray jsonArr = new JSONArray(jsonStops);
			JSONArray lineArr;
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject line = jsonArr.getJSONObject(i);
				String name = line.getString("name");
				String description = line.getString("description");
				double latitude = line.getDouble("latitude");
				double longitude = line.getDouble("longitude");
				lineArr = line.getJSONArray("lines");
				List<Position> myList = new ArrayList<>();
				for (int j = 0; j < lineArr.length(); j++) {
					JSONObject line2 = lineArr.getJSONObject(j);
					String lineMetro = line2.getString("line");
					int position = line2.getInt("position");
					myList.add(new Position(position, lineMetro));
				}
				stops.put(name, new Stop(name, description, latitude, longitude, myList));
			}
		}

		@SuppressLint("UseSparseArrays")
		private void constructLineStations() {
			for (java.util.Map.Entry<String, Stop> e : stops.entrySet()) {
				List<Position> tempPositions = e.getValue().getLines();
				for (Position pos : tempPositions) {
					Line tempLine = lines.get(pos.getLine());
					java.util.Map<Integer, String> stationsTemp;
					if (tempLine != null) {
						if (tempLine.getStations() == null) {
							stationsTemp = new HashMap<>();
						} else {
							stationsTemp = tempLine.getStations();
						}
						stationsTemp.put(pos.getPos(), e.getValue().getName());
						tempLine.setStations(stationsTemp);
					}
				}
			}
		}
	}

	private void logJSONError(JSONException e) {
		Log.e(TAG, "Json parsing error: " + e.getMessage());
	}

}
