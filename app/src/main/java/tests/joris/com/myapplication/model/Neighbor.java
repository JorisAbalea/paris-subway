package tests.joris.com.myapplication.model;

public class Neighbor {
	private String name;
	private double distance;

	public Neighbor(String name, double distance) {
		this.name = name;
		this.distance = distance;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "Neighbor{" +
			"name='" + name + '\'' +
			", distance=" + distance +
			'}';
	}

}
