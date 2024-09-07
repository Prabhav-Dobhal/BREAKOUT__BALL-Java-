package demogame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Bricks_maker {
	public int map[][];
	public int brickWidth,brickHeight;
	
	//constructor 
	public Bricks_maker(int row,int col) {
		map=new int[row][col];
		
		for(int i=0;i<row;i++) {
			for(int j=0;j<col;j++) {
				map[i][j]=1;
			}
		}
		brickWidth=540/col;
		brickHeight=150/row;
		
	}
	//SOUNDs
		public static void music(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
			File file=new File(filepath);
			AudioInputStream audioStream=AudioSystem.getAudioInputStream(file);
			//create clip object
			Clip clip =AudioSystem.getClip();
			clip.open(audioStream);
			clip.start();
		}
	public int setBrick(int value,int r,int c) {
		//erase brick by setting m[r][c] to 0
				map[r][c]=value;
		//if yellow brick
		if((r==2&&c==5)||(r==1&&c==3)||(r==4&&c==2)) {
			try {
				music("smb3_power-up.wav");
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 9;
		}return 0;
	}
	//draw bricks on screen
	public void draw(Graphics2D g) {
		for(int i=0;i<map.length;i++) {
			for(int j=0;j<map[0].length;j++) {
				//brick value should be greater than 0
				if(map[i][j]>0) {
					//yellow bricks
					if(i==1&&j==3||i==2&&j==5||i==4&&j==2) {
						g.setColor(Color.yellow);
						g.fillRect(j*brickWidth+80, i*brickHeight+50, brickWidth, brickHeight);
						g.setColor(Color.gray);
						g.setStroke(new BasicStroke(3));
						g.drawRect(j*brickWidth+80, i*brickHeight+50, brickWidth, brickHeight);
					}
					else {
					g.setColor(Color.RED);
					g.fillRect(j*brickWidth+80, i*brickHeight+50, brickWidth, brickHeight);
					g.setColor(Color.gray);
					g.setStroke(new BasicStroke(3));
					g.drawRect(j*brickWidth+80, i*brickHeight+50, brickWidth, brickHeight);
					}
				}
			}
		}
	}
	
}
