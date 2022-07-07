import java.io.Serializable;

// Store Highest Score Objects

@SuppressWarnings("serial")
public class HighScores implements Serializable {

	private int score;
	private String name;
	
	public int getScore() {
		return score;
	}
	
	public String getName() {
		return name;
	}
	
	public HighScores(String name, int score) {
		this.score = score;
		this.name = name;
	}
	
}
