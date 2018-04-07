package tests.joris.com.myapplication.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tests.joris.com.myapplication.model.APIPath;
import tests.joris.com.myapplication.model.Map;

public class MapDataStore implements MapStore {
	private static final String BASE_URL = "http://localhost:3000/maps/";
	private final MapRestService service;
	private final MapListener listener;

	public MapDataStore(MapListener listener) {
		this.listener = listener;
		OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		httpClient.addInterceptor(logging);

		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(httpClient.build())
			.build();
		service = retrofit.create(MapRestService.class);
	}

	@Override
	public void updateMap(String mapId, Map myMap) {
		Call<JsonObject> map = service.updateMap(mapId, myMap);
		map.enqueue(new Callback<JsonObject>() {
			@Override
			public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
				Log.e("TEST", "updateMap SUCCESS " + response);
				if (response.isSuccessful()) {
					Log.e("TEST", "updateMap SUCCESS LA" + response);
				}
			}

			@Override
			public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
				Log.e("TEST", "updateMap FAIL " + t);
			}
		});
	}

	@Override
	public void computeShortestPath(String mapId, String startId, String endId) {
		Call<APIPath> images = service.shortestPath(mapId, startId, endId);
		images.enqueue(new Callback<APIPath>() {
			@Override
			public void onResponse(@NonNull Call<APIPath> call, @NonNull Response<APIPath> response) {
				if (response.isSuccessful()) {
					Log.e("TEST", "getShortestPath SUCCESS LA" + response);
					listener.onPathReceived(response.body());
				}
			}

			@Override
			public void onFailure(@NonNull Call<APIPath> call, @NonNull Throwable t) {
				listener.onPathFailedReceived(t);
			}
		});
	}
}
