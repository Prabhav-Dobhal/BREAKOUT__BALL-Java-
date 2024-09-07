package demogame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;
public class Gamework extends JPanel implements ActionListener, KeyListener{
	//THE GAME CODING 
	//?? GAME PHYSICS
	private boolean play=false;
	private int score=0;
	private int totalBricks=35;
	private Timer timer;
	private int delay =8;
	private int ballposX=120;
	private int ballposY=350;
	private int ballXdir=-1;
	private int ballYdir=-2;
	private int playerX=350;
	private Bricks_maker map;
	public int paddle_increment=0;
	
	//Constructor
	public Gamework() {
		//responds to key events
		addKeyListener(this);
		//receive the input
		setFocusable(true);
		//used to schedule tasks
		timer=new Timer(delay,this);
		timer.start();
		map= new Bricks_maker(5,7);
	}
	
	//inbuilt paint method
	public void paint(Graphics g) {
		
		//background color
		g.setColor(Color.black);
		g.fillRect( 1, 1, 692, 592);
		//border
		g.setColor(Color.cyan);
		//top border
		g.fillRect(0, 0, 692, 3);
		//left border
		g.fillRect(0, 0, 3, 592);
		//right border
		g.fillRect(691, 0, 3, 592);
		
		//ball
		g.setColor(Color.green);
		g.fillOval(ballposX, ballposY, 20, 20);
		//paddle
		g.setColor(Color.orange);
		g.fillRect(playerX, 550, 100+paddle_increment, 8);
		
		//bricks
		map.draw((Graphics2D) g);
		
		//score
		g.setColor(Color.white);
		g.setFont(new Font("serif",Font.BOLD,20));
		g.drawString("Your Score is:"+score,450,30);
		
		//gameover
		if(ballposY>=570) {
			if(play) {
				try {
					music("smb_gameover.wav");
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			play=false;
			g.setColor(Color.RED);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("GAME OVER!!!, Score: "+score,200,300);
			
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter To Restart..."+score,230,350);
		}
		//win
		if(totalBricks==0) {
			if(play) {
				try {
					music("smb_world_clear.wav");
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
            play=false;
			g.setColor(Color.GREEN);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("YOU WIN!!!, Score: "+score,200,300);
			
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter To Restart..."+score,230,350);
		}
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
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	private void moveLeft() {
		playerX-=20;
	}
	private void moveRight() {
		playerX+=20;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			//game starts
			play=true;
			if(playerX<=8) {
				playerX=8;
			}
			else
			moveLeft();
		}
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			play=true;
			if(playerX>=594) {
				playerX=594;
			}
			else
			moveRight();
		}
		if(e.getKeyCode()==KeyEvent.VK_ENTER) {
			if(!play) {
				score=0;
				totalBricks=35;
				ballposX=120;
				ballposY=350;
				ballXdir=-1;
				ballYdir=-2;
				playerX=320;
				map=new Bricks_maker(5,7);
			}
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	//Ball physics
	public void actionPerformed(ActionEvent e) {
		if(play) {
			
			if(ballposX<=0) {
				ballXdir=-ballXdir;
			}
			if(ballposX>=660) {
				ballXdir=-ballXdir;
			}
			if(ballposY<=0) {
				ballYdir=-ballYdir;
			}
			//Imaginary rectangle for ball
			Rectangle ballRect=new Rectangle (ballposX,ballposY,20,20);
			//Imaginary rectangle for paddle
			Rectangle paddleRect=new Rectangle (playerX,550,100,8);
			
			//does imaginary rect of ball intersect with img rect of paddle???
			if(ballRect.intersects(paddleRect)) {
				try {
					music("smb_bump.wav");
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ballYdir=-ballYdir;
			}
			
			//check if ball collides with bricks
			A:for(int i=0;i<5;i++) {
				for(int j=0;j<7;j++) {
					if(map.map[i][j]>0) {
						
						int width=map.brickWidth;
						int height=map.brickHeight;
						int brickXpos=80+j*width;
						int brickYpos=50+i*height;
						
						//make img rect for all bricks
						Rectangle brickRect=new Rectangle(brickXpos,brickYpos,width,height);
						
						if(ballRect.intersects(brickRect)) {
							//sound
							try {
								music("smb_breakblock.wav");
							} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							//ball collided with brick
							paddle_increment+=map.setBrick(0, i, j);
							totalBricks--;
							//score will increase
							score+=50;
							//ball should go in opp dir when collided with brick
							//with left or right
							if(ballposX+19<=brickXpos||ballposX+1>=brickXpos+width) {
								ballXdir=-ballXdir;
							}
							else {
								//down se ya up se takrayi hogi
								ballYdir=-ballYdir;
							}
							break A;
						}
					}
				}
			}
			
			ballposX+=ballXdir;
			ballposY+=ballYdir;
		}
		repaint();
	}
}
