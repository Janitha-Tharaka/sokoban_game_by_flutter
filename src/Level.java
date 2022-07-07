//Levels are here

public class Level {
		
	public String name;
	public int width;
	public int height;
	public String info = "";
		
	public void setInfo(String s) {
		this.info = s;
	}
	
	public String getInfo() {
		return this.info;
	}
	
	public String replaceTileTesting(String line) {
		line = line.replace("#", "W"); // wall
		line = line.replace(".", "G"); // goal
		line = line.replace("$", "B"); // block
		line = line.replace("@", "A"); // avatar
		line = line.replace("*", "X"); // block + goal
		line = line.replace("+", "Y"); // avatar + goal
		line = line.replace(" ", "S"); // space
		return line;
	}
		
}