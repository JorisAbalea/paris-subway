package tests.joris.com.myapplication.model;

import java.util.Map;
import java.util.Objects;

public class Line {
	private String name;
	private String color;
	private String number;
	private String routeName;
	private Map<Integer, String> stations;

	public Line(String name, String color, String number, String routeName) {
		this.name = name;
		this.color = color;
		this.number = number;
		this.routeName = routeName;
	}

	public Map<Integer, String> getStations() {
		return stations;
	}

	public void setStations(Map<Integer, String> stations) {
		this.stations = stations;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Line line = (Line) o;
		return Objects.equals(name, line.name) &&
			Objects.equals(color, line.color) &&
			Objects.equals(number, line.number) &&
			Objects.equals(routeName, line.routeName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, color, number, routeName);
	}

	@Override
	public String toString() {
		return "Line{" +
			"name='" + name + '\'' +
			", color='" + color + '\'' +
			", number='" + number + '\'' +
			", routeName='" + routeName + '\'' +
			'}';
	}
}
