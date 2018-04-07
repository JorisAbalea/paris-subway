package tests.joris.com.myapplication.model;

import java.util.Objects;

public class Position {
	private int pos;
	private String line;

	public Position(int pos, String line) {

		this.pos = pos;
		this.line = line;
	}

	public int getPos() {

		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Position position = (Position) o;
		return pos == position.pos &&
			Objects.equals(line, position.line);
	}

	@Override
	public int hashCode() {

		return Objects.hash(pos, line);
	}

	@Override
	public String toString() {
		return "Position{" +
			"pos=" + pos +
			", line='" + line + '\'' +
			'}';
	}
}
