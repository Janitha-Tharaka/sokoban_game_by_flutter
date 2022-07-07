import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.File;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * Main Window Class.
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

	private static GameMain main;
	
	private Model model;
	private Level level;
	private static char[][] levelMap;
	private String levelString2;
	private BufferedImage images[] = new BufferedImage[5];
	private BufferedImage bg;
	private JLabel status;
	private boolean playing = true;
	private static ScoreManager sm = new ScoreManager();
	
	//To Check the entered values
	private static String levelString;
	boolean isInt = false;
	
    public static final int INTERVAL = 25;
	
    public GameBoard(JLabel status) {
        this.status = status;
        
        // Main Window
    	JOptionPane.showMessageDialog(this, "This is Done By\n"
		+ "Tharaka Gunawardhana\n"
		+ "21S07001\n");
        
    	levelString = "1";
    	level = Levels.get(levelString);
    	model = new Model(level);
    	
    	setFocusable(true);
    	
    	// reads graphics from file and returns buffered image
    	try {
    		images[0] = ImageIO.read(new File("graphics/Avatar.png"));
    		images[1] = ImageIO.read(new File("graphics/Block.png"));
    		images[2] = ImageIO.read(new File("graphics/GoalTile.png"));
    		images[3] = ImageIO.read(new File("graphics/NormalTile.png"));
    		images[4] = ImageIO.read(new File("graphics/Wall.png"));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}	
    	bg = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_ARGB);
    	
    	// Game Board Border
    	setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	
    	// Timer to check the game is finished
    	Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start();
        
        // Key movements
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
               if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            	   	moveLeft();
            	   	GameMain.moved();
               } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            	   	moveRight();
            	   	GameMain.moved();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                	moveDown();
                	GameMain.moved();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                	moveUp();
                	GameMain.moved();
                }
            }
        });
    }
    
	// CChecking the completion
    void tick() {
    	if (playing) {
    		if (isCompleted()) {
    			
    			JOptionPane.showMessageDialog(this, "Congratulations! Level cleared!");
    			playing = false;
    			status.setText("Level Completed!");
    			
    			String name = JOptionPane.showInputDialog("Let us know your Name!");
    			sm.addScore(name, GameMain.getNumMoves());
    			JOptionPane.showMessageDialog(this, "Highscores: \n" + sm.getHighscoreString());
    			
    			
    			//Automatically level up
    			
    			//I have developed this. But cannot reset the number of moves. So commented
    			
//    			int levelInt =Integer.parseInt(levelString);
//    			
//    			if(levelInt > 5) {
//    				JOptionPane.showMessageDialog(this, "You Have Successfully Completed All the levels!\n"
//    						+ "Thank You!\n"
//					);
//    			}else {
//     
//    				levelInt += 1;
//        			levelString = String.valueOf(levelInt);
//        	    	level = Levels.get(levelString);
//        	    	model = new Model(level);
//        	    	status.setText("Running...");
//        	    	repaint(); // redraw
//        	    	requestFocusInWindow();
//        	    	playing = true;
//        			setFocusable(true);
//        			main.numMoves = 0;
//    			}
    			
    			JOptionPane.showMessageDialog(this, "Please select a New Level");
    			
    		}
    	}
    }
    
    // reset the game to its initial state
    public void reset() {
    	model = new Model(level);
    	repaint();
    	status.setText("Running...");
    	requestFocusInWindow();
    	playing = true;
    }

	// Choose a new game
	public void newGame() throws IOException {
    	// Getting level from user and validating it
    	levelString2 = JOptionPane.showInputDialog("Enter Level (1-5):");
    	
        int number1Int = 0;
        try{
        	number1Int = Integer.parseInt(levelString2);
        	
        	if(number1Int > 5 || number1Int < 1) {
            	JOptionPane.showMessageDialog(this, "Please insert levels in between 1 and 5");
            	newGame();
            }else {
            	levelString = String.valueOf(number1Int);
            	level = Levels.get(levelString);
            	model = new Model(level);
            	status.setText("Running...");
            	repaint(); // redraw
            	requestFocusInWindow();
            	playing = true;
            	setFocusable(true);
            }
            
        }catch(NumberFormatException e){
        	JOptionPane.showMessageDialog(this, "Please insert a numeric value for levels");
        	newGame();
        }
	}
    
    // repaint calls this every time
    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	
    	draw(bg.createGraphics());
    	g.drawImage(bg, 0, 0, null);
    }
    
    public void draw(Graphics2D g) {
    	g.setBackground(Color.GRAY);
    	g.clearRect(0, 0, getWidth(), getHeight());
    	g.translate(70, 1);
    	
    	levelMap = model.getLevelMap();
    	int tileSize = 64;
    	
    	for (int x = 0; x < levelMap.length; x++) {
    		for (int y = 0; y < levelMap[0].length; y++) {
    			g.drawImage(images[3], x*tileSize, y*tileSize, null); // normal tiles
    			if (levelMap[x][y] == 'S') { // normal tiles
        			g.drawImage(images[3], x*tileSize, y*tileSize, null); 
    			}
    			if (levelMap[x][y] == 'W') { // wall
    				g.drawImage(images[4], x*tileSize, y*tileSize, null);
    			}
    			if (levelMap[x][y] == 'G') { // goal tiles
    				g.drawImage(images[2], x*tileSize, y*tileSize, null);
    			}
    			if (levelMap[x][y] == 'B') { // block
    				g.drawImage(images[1], x*tileSize, y*tileSize, null);
    			}
    			if (levelMap[x][y] == 'A') { // avatar
    		    	g.drawImage(images[0], x*tileSize, y*tileSize, null); 
    			}
    			if (levelMap[x][y] == 'X') { // block + goal (basically just block)
    				g.drawImage(images[1], x*tileSize, y*tileSize, null);
    			}
    			if (levelMap[x][y] == 'Y') { // avatar + goal (draws goal then avatar)
    				g.drawImage(images[2], x*tileSize, y*tileSize, null);
    		    	g.drawImage(images[0], x*tileSize, y*tileSize, null); 
    			}
    		}
    	}
    }
    

    public void moveUp() { 
    	for (int x = 0; x < levelMap.length; x++) {
    		for (int y = 0; y < levelMap[0].length; y++) {
    			// if avatar is trying to move into a space
    			if (levelMap[x][y] == 'A' && levelMap[x][y-1] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y-1] = 'A';
    				repaint();
    			}
    			// if avatar is trying to push a block into a space
    			if (levelMap[x][y] == 'A' && levelMap[x][y-1] == 'B' && levelMap[x][y-2] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y-1] = 'A';
    				levelMap[x][y-2] = 'B';
    				repaint();
    			}
    			// if avatar is trying to push a block into a goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x][y-1] == 'B' && levelMap[x][y-2] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y-1] = 'A';
    				levelMap[x][y-2] = 'X';
    				repaint();
    			}
    			// if avatar is trying to push a block onto another goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x][y-1] == 'X' && levelMap[x][y-2] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y-1] = 'Y';
    				levelMap[x][y-2] = 'X';
    				repaint();
    			}
    			// if avatar is trying to move to a goal tile 
    			if (levelMap[x][y] == 'A' && levelMap[x][y-1] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y-1] = 'Y';
    				repaint();
    			}
    			// if avatar is trying to move from a goal tile to another goal tile
    			if (levelMap[x][y] == 'Y' && levelMap[x][y-1] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y-1] = 'Y';
    				repaint();
    			}
    			// if avatar is trying to move from a goal tile to an empty space
    			if (levelMap[x][y] == 'Y' && levelMap[x][y-1] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y-1] = 'A';
    				repaint();
    			}
    			// if avatar is on a goal tile and trying to push a block onto another goal
    			if (levelMap[x][y] == 'Y' && levelMap[x][y-1] == 'X' && levelMap[x][y-2] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y-1] = 'Y';
    				levelMap[x][y-2] = 'X';
    				repaint();
    			}
    			// if avatar is on a goal and trying to push a block on a goal into an empty space 
    			if (levelMap[x][y] == 'Y' && levelMap[x][y-1] == 'X' && levelMap[x][y-2] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y-1] = 'Y';
    				levelMap[x][y-2] = 'B';
    				repaint();
    			}
    		}
    	}
    }
    
    public void moveDown() {
    	for (int x = 0; x < levelMap.length; x++) {
    		for (int y = 0; y < levelMap[0].length; y++) {
    			// if avatar is trying to move into a space
    			if (levelMap[x][y] == 'A' && levelMap[x][y+1] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y+1] = 'A';
    				repaint();
    				break;
    			}
    			// if avatar is trying to push a block into a space
    			if (levelMap[x][y] == 'A' && levelMap[x][y+1] == 'B' && levelMap[x][y+2] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y+1] = 'A';
    				levelMap[x][y+2] = 'B';
    				repaint();
    				break;
    			}
    			// if avatar is trying to push a block into a goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x][y+1] == 'B' && levelMap[x][y+2] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y+1] = 'A';
    				levelMap[x][y+2] = 'X';
    				repaint();
    				break;
    			}
    			// if avatar is trying to push a block onto another goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x][y+1] == 'X' && levelMap[x][y+2] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y+1] = 'Y';
    				levelMap[x][y+2] = 'X';
    				repaint();
    				break;
    			}
    			// if avatar is trying to move to a goal tile 
    			if (levelMap[x][y] == 'A' && levelMap[x][y+1] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y+1] = 'Y';
    				repaint();
    				break;
    			}
    			// if avatar is trying to move from a goal tile to another goal tile
    			if (levelMap[x][y] == 'Y' && levelMap[x][y+1] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y+1] = 'Y';
    				repaint();
    				break;
    			}
    			// if avatar is trying to move from a goal tile to an empty space
    			if (levelMap[x][y] == 'Y' && levelMap[x][y+1] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y+1] = 'A';
    				repaint();
    				break;
    			}
    			// if avatar is on a goal tile and trying to push a block onto another goal
    			if (levelMap[x][y] == 'Y' && levelMap[x][y+1] == 'X' && levelMap[x][y+2] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y+1] = 'Y';
    				levelMap[x][y+2] = 'X';
    				repaint();
    				break;
    			}
    			// if avatar is on a goal and trying to push a block on a goal into an empty space 
    			if (levelMap[x][y] == 'Y' && levelMap[x][y+1] == 'X' && levelMap[x][y+2] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y+1] = 'Y';
    				levelMap[x][y+2] = 'B';
    				repaint();
    				break;
    			}
    		}
    	}
    }
    
    public void moveRight() {
    	for (int x = 0; x < levelMap.length; x++) {
    		for (int y = 0; y < levelMap[0].length; y++) {
    			// if avatar is trying to move into a space
    			if (levelMap[x][y] == 'A' && levelMap[x+1][y] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x+1][y] = 'A';
    				repaint();
    				return;
    			} 
    			// if avatar is trying to push a block into a space
    			if (levelMap[x][y] == 'A' && levelMap[x+1][y] == 'B' && levelMap[x+2][y] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x+1][y] = 'A';
    				levelMap[x+2][y] = 'B';
    				repaint();
    				return;
    			}
    			// if avatar is trying to push a block into a goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x+1][y] == 'B' && levelMap[x+2][y] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x+1][y] = 'A';
    				levelMap[x+2][y] = 'X';
    				repaint();
    				return;
    			}
    			// if avatar is trying to push a block onto another goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x+1][y] == 'X' && levelMap[x+2][y] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x+1][y] = 'Y';
    				levelMap[x+2][y] = 'X';
    				repaint();
    				return;
    			}
    			// if avatar is trying to move to a goal tile 
    			if (levelMap[x][y] == 'A' && levelMap[x+1][y] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x+1][y] = 'Y';
    				repaint();
    				return;
    			}
    			// if avatar is trying to move from a goal tile to another goal tile
    			if (levelMap[x][y] == 'Y' && levelMap[x+1][y] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x+1][y] = 'Y';
    				repaint();
    				return;
    			}
    			// if avatar is trying to move from a goal tile to an empty space
    			if (levelMap[x][y] == 'Y' && levelMap[x+1][y] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x+1][y] = 'A';
    				repaint();
    				return;
    			}
    			// if avatar is on a goal tile and trying to push a block onto another goal
    			if (levelMap[x][y] == 'Y' && levelMap[x+1][y] == 'X' && levelMap[x+2][y] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x+1][y] = 'Y';
    				levelMap[x+2][y] = 'X';
    				repaint();
    				return;
    			}
    			// if avatar is on a goal and trying to push a block on a goal into an empty space 
    			if (levelMap[x][y] == 'Y' && levelMap[x+1][y] == 'X' && levelMap[x+2][y] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x+1][y] = 'Y';
    				levelMap[x+2][y] = 'B';
    				repaint();
    				return;
    			}
    		}
    	}
    }
    
    public void moveLeft() {
    	for (int x = 0; x < levelMap.length; x++) {
    		for (int y = 0; y < levelMap[0].length; y++) {
    			// if avatar is trying to move into a space
    			if (levelMap[x][y] == 'A' && levelMap[x-1][y] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x-1][y] = 'A';
    				repaint();
    			}
    			// if avatar is trying to push a block into a space
    			if (levelMap[x][y] == 'A' && levelMap[x-1][y] == 'B' && levelMap[x-2][y] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x-1][y] = 'A';
    				levelMap[x-2][y] = 'B';
    				repaint();
    			}
    			// if avatar is trying to push a block into a goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x-1][y] == 'B' && levelMap[x-2][y] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x-1][y] = 'A';
    				levelMap[x-2][y] = 'X';
    				repaint();
    			}
    			// if avatar is trying to push a block onto another goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x-1][y] == 'X' && levelMap[x-2][y] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x-1][y] = 'Y';
    				levelMap[x-2][y] = 'X';
    				repaint();
    			}
    			// if avatar is trying to move to a goal tile 
    			if (levelMap[x][y] == 'A' && levelMap[x-1][y] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x-1][y] = 'Y';
    				repaint();
    			}
    			// if avatar is trying to move from a goal tile to another goal tile
    			if (levelMap[x][y] == 'Y' && levelMap[x-1][y] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x-1][y] = 'Y';
    				repaint();
    			}
    			// if avatar is trying to move from a goal tile to an empty space
    			if (levelMap[x][y] == 'Y' && levelMap[x-1][y] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x-1][y] = 'A';
    				repaint();
    			}
    			// if avatar is on a goal tile and trying to push a block onto another goal
    			if (levelMap[x][y] == 'Y' && levelMap[x-1][y] == 'X' && levelMap[x-2][y] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x-1][y] = 'Y';
    				levelMap[x-2][y] = 'X';
    				repaint();
    				return;
    			}
    			// if avatar is on a goal and trying to push a block on a goal into an empty space 
    			if (levelMap[x][y] == 'Y' && levelMap[x-1][y] == 'X' && levelMap[x-2][y] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x-1][y] = 'Y';
    				levelMap[x-2][y] = 'B';
    				repaint();
    				return;
    			}
    		}
    	}
    }
    
    // checks if all blocks are pushed to goal tiles
    public boolean isCompleted() {
    	int emptyGoalCount = 0;
    	int avatarAndGoalCount = 0;
    	for (int x = 0; x < levelMap.length; x++) {
    		for (int y = 0; y < levelMap[0].length; y++) {
    			// if there is an empty goal, increment. you don't want empty goals!
                if (levelMap[x][y] == 'G') {
                	emptyGoalCount++;
                }
                // if the avatar is on a goal tile
                if (levelMap[x][y] == 'Y') {
                	avatarAndGoalCount++;
                }
            }
        }
    	if (emptyGoalCount == 0 && avatarAndGoalCount == 0) {
    		return true;
    	}
        return false;
    } 

    // gets level that is being played (used in ScoreManager.java)
	public static String getLevelString() {
		return levelString;
	}
    
}
