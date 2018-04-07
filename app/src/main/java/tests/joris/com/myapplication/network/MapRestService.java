package tests.joris.com.myapplication.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import tests.joris.com.myapplication.model.APIPath;
import tests.joris.com.myapplication.model.Map;

interface MapRestService {
	@PUT("{mapId}")
	Call<JsonObject> updateMap(@Path("mapId") String mapId, @Body Map map);

	@GET("{mapId}/path/{startId}/{endId}")
	Call<APIPath> shortestPath(@Path("mapId") String mapId,
		@Path("startId") String startId,
		@Path("endId") String endId);
}
