package tests.joris.com.myapplication.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import org.json.JSONArray;
import org.json.JSONException;

import tests.joris.com.myapplication.R;
import tests.joris.com.myapplication.model.Stop;
import tests.joris.com.myapplication.presenter.MapPresenter;
import tests.joris.com.myapplication.util.ConverterUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapActivity extends MvpActivity<MapView, MapPresenter> implements
	MapView, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

	private static final String TAG = "COUCOU";
	public static final String PARIS_SUBWAY = "Paris Subway";
	private Marker startNode = null;
	private Marker endNode = null;
	private GoogleMap map;
	@BindView(R.id.textView) TextView tv1;
	@BindView(R.id.textView2) TextView tv2;
	@BindView(R.id.textView3) TextView tv3;
	private Polyline shortestPath;

	@OnClick(R.id.buttonCreate)
	protected void onClickCreate() {
		getPresenter().updateMap(PARIS_SUBWAY);
	}

	@OnClick(R.id.buttonClear)
	protected void onClickClear() {
		resetSelection();
		tv1.setText("");
		tv2.setText("");
		tv3.setText("");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
			.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		String jsonLines = ConverterUtil.readFileFromRawDirectory(R.raw.lines, MapActivity.this);
		String jsonStops = ConverterUtil.readFileFromRawDirectory(R.raw.stops, MapActivity.this);
		getPresenter().parseLines(jsonLines);
		getPresenter().parseStops(jsonStops);
	}

	@NonNull
	@Override
	public MapPresenter createPresenter() {
		return new MapPresenter();
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.map = googleMap;
		getPresenter().drawStationLines();
		getPresenter().createMapJSON();
		googleMap.setOnMarkerClickListener(this);
		getPresenter().drawNodeColors();
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (startNode == null) {
			startNode = marker;
			tv1.setText("From: " + marker.getTitle());
		} else if (startNode == marker) {
			startNode = null;

		} else {
			endNode = marker;
			tv2.setText("To: " + marker.getTitle());
			getPresenter().findShortestPath(PARIS_SUBWAY, startNode.getTitle(), endNode.getTitle());
		}
		return false;
	}

	@Override
	public void drawPath(Map<String, Stop> stops, String listOfStations, String distance) {
		Log.e("TEST", "drawPathdrawPathdrawPathdrawPathdrawPath " + listOfStations);
		try {
			JSONArray jsonArray = new JSONArray(listOfStations);
			int len = jsonArray.length();
			List<LatLng> latStations = new ArrayList<>();

			for (int i = 0; i < len; ++i) {
				String stationName = (String) jsonArray.get(i);
				latStations.add(new LatLng(stops.get(stationName).getLatitude(), stops.get(stationName).getLongitude()));
			}
			PolylineOptions line = new PolylineOptions()
				.addAll(latStations)
				.width(40)
				.color(Color.YELLOW);
			shortestPath = map.addPolyline(line.zIndex(5));
			tv3.setText("Distance: " + String.valueOf(distance));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void drawPolyLine(PolylineOptions line) {
		if (map != null) {
			map.addPolyline(line.zIndex(10));
		}
	}

	@Override
	public void addMarker(MarkerOptions marker) {
		if (map != null) {
			map.addMarker(marker);
		}

	}

	@Override
	public void centerAndZoom(LatLng loc) {
		if (map != null) {
			map.moveCamera(CameraUpdateFactory.newLatLng(loc));
			map.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
		}
	}

	@Override
	public void resetSelection() {
		this.startNode = null;
		this.endNode = null;
		if (shortestPath != null) {
			shortestPath.remove();
		}
	}
}