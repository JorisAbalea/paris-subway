package tests.joris.com.myapplication.model;

public class APIPath {
	private String distance;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private String path;

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public APIPath(String distance, String path) {

		this.distance = distance;
		this.path = path;
	}

	@Override
	public String toString() {
		return "APIPath{" +
			"distance='" + distance + '\'' +
			", path=" + path +
			'}';
	}
}
