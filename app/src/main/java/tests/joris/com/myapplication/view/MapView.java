package tests.joris.com.myapplication.view;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import tests.joris.com.myapplication.model.Stop;

import java.util.Map;

public interface MapView extends MvpView {
	void drawPath(Map<String, Stop> stops, String strings, String distance);

	void drawPolyLine(PolylineOptions line);

	void addMarker(MarkerOptions icon);

	void centerAndZoom(LatLng loc);

	void resetSelection();
}
