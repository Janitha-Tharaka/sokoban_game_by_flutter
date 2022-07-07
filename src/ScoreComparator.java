import java.util.Comparator;

/*
 * This class creates a comparator that helps sort the list of highscores in ScoreManager
 */
public class ScoreComparator implements Comparator<HighScores> {
	
	public int compare(HighScores score1, HighScores score2) {
		
		int sc1 = score1.getScore();
        int sc2 = score2.getScore();
        
        // the least pushes the better, high scores will be sorted from least to greatest
        if (sc1 > sc2){ 
        	return +1;
        } else if (sc1 < sc2){
            return -1;
        } else {
            return 0;
        }
    }
}