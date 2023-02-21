package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	// Screen Setting
	final int originalTitleSize = 16; // 16*16title
	final int scale = 3;

	public final int titleSize = originalTitleSize * scale; // 48*48
	public final int maxScreenCol = 16; // <- ->
	public final int maxScreenRow = 12; // /|\
	public final int screenWidth = titleSize * maxScreenCol; // 768 pix
	public final int screenHeight = titleSize * maxScreenRow; // 576 pix
	
	//World Setting
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	//public final int worldWidth = titleSize * maxWorldCol;
	//public final int worldHeight = titleSize * maxWorldRow;
	
	// FPS
	int FPS = 60;
	
	//System
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler();
	Sound music = new Sound();
	Sound se = new Sound();
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	Thread gameThread;
	
	//Entity and Obj
	public Player player = new Player(this, keyH);
	public SuperObject obj[] = new SuperObject[10];
	
	

	public GamePanel() {

		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void setupGame() {
		
		aSetter.setObject();
		
		playMusic(0);
	}

	public void startGameThread() {

		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	/*
	 * public void run() {
	 * 
	 * double drawInterval = 1000000000/FPS; // 0.016666 sec double nextDrawTime =
	 * System.nanoTime() + drawInterval;
	 * 
	 * while(gameThread != null) {
	 * 
	 * //long currentTime = System.nanoTime();
	 * //System.out.println("currentTime:"+currentTime );
	 * 
	 * //System.out.println("The game loop is running"); //1 Update information such
	 * as charater poisiton update(); //2 draw the screen with the update
	 * information repaint();
	 * 
	 * 
	 * try {
	 * 
	 * double remainingTime = nextDrawTime - System.nanoTime(); remainingTime =
	 * remainingTime/10000000;
	 * 
	 * if(remainingTime < 0) { remainingTime = 0; }
	 * 
	 * Thread.sleep((long) remainingTime);
	 * 
	 * nextDrawTime += drawInterval;
	 * 
	 * } catch (InterruptedException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } }
	 */

	public void run() {

		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		while (gameThread != null) {

			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}

			if (timer >= 1000000000) {
				//System.out.println("FPS:" + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}

	public void update() {

		player.update();
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		
		//Debug
		long drawStart = 0;
		if(keyH.checkDrawTime == true) {
			 
			drawStart = System.nanoTime();
		}
		
		//Tile
		tileM.draw(g2);
		
		//Oject
		for(int i = 0; i < obj.length; i++) {
			if(obj[i] != null) {
				obj[i].draw(g2, this);
			}
		}
		//Player
		player.draw(g2);
		
		// UI
		ui.draw(g2);
		
		//DeBug
		if(keyH.checkDrawTime == true) {
			
			long drawEnd = System.nanoTime();
			long passed = drawEnd - drawStart;
			g2.setColor(Color.white);
			g2.drawString("Draw Time:" + passed, 10, 400);
			System.out.println("Draw Time:" + passed);
		}
		
		g2.dispose();
	}
	//music
	public void playMusic(int i) {
		
		music.setFile(i);
		music.play();
		music.loop();
	}
	public void stopMusic() {
		
		music.stop();
	}
	public void playSE(int i) {
		
		se.setFile(i);
		se.play();
	}
}
