
//Model per level

public class Model{
	
	//private fields
	private String level = "";
	private char[][] levelMap;
	
	// used in testing
	public Model(Level level, boolean b) {
		this.level = level.info;
	}
	
	public Model(Level level) {
		this.level = level.info;
		levelMap = new char[level.width][level.height];
		fillMap();
	}
	
	public Model (Level level, String s) {
		this.level = level.info;
		levelMap = new char[level.width][level.height];
		// fillLoadMap();
	}
	
	public char[][] getLevelMap() {
		return levelMap;
	}
	
	public String getLevelInfo() {
		return level;
	}
	
	private void fillMap() {
		String[] lines = level.split("\\n");
		int y = 0;
		for (String line : lines) {
			for (int x = 0; x < line.length(); x++) {
				char c = line.charAt(x);
				levelMap[x][y] = c;
			}
			y++;
		}
	}
	
	
}
