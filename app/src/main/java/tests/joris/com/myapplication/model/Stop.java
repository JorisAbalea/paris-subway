package tests.joris.com.myapplication.model;

import java.util.List;
import java.util.Objects;

public class Stop {
	private String name;
	private String description;
	private final double latitude;
	private final double longitude;
	private List<Position> lines;

	public Stop(String name, String description, double latitude, double longitude, List<Position> lines) {
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
		this.lines = lines;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Stop stop = (Stop) o;
		return Double.compare(stop.latitude, latitude) == 0 &&
			Double.compare(stop.longitude, longitude) == 0 &&
			Objects.equals(name, stop.name) &&
			Objects.equals(description, stop.description) &&
			Objects.equals(lines, stop.lines);
	}

	@Override
	public int hashCode() {

		return Objects.hash(name, description, latitude, longitude, lines);
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public List<Position> getLines() {
		return lines;
	}

	public void setLines(List<Position> lines) {
		this.lines = lines;
	}

	@Override
	public String toString() {
		return "Stop{" +
			"name='" + name + '\'' +
			", description='" + description + '\'' +
			", latitude=" + latitude +
			", longitude=" + longitude +
			", lines=" + lines +
			'}';
	}
}
