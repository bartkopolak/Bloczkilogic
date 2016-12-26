package thingy;

import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Thing extends JFrame  {

	private JPanel contentPane;
	Plotno canvas;
	Rectangle r1;
	Rectangle r2;
	Rectangle r3;
	Rectangle r4;
	Point point;
	BufferedImage bi;
	Point mouse;
	int PointDir = 2;
	int licznik;
	int distL, distR, distU, distD;
	File plik;
	BufferedImage zpliku;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Thing frame = new Thing();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Thing() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 617, 621);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		mouse = new Point(200,200);
		bi = new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
		plik = new File("test.png");
	       try{
	    	   zpliku = ImageIO.read(plik);
			}catch(IOException e){e.printStackTrace();}
		canvas = new Plotno(bi);
		r1 = new Rectangle(100,20);
		r1.setLocation(100, 100);
		r2 = new Rectangle(10,200);
		r2.setLocation(100, 120);
		r3 = new Rectangle(150,30);
		r3.setLocation(100, 320);
		r3 = new Rectangle(200,30);
		r3.setLocation(100, 320);
		r4 = new Rectangle(20,100);
		r4.setLocation(300, 220);
		point = new Point(10,10);
		contentPane.add(canvas, BorderLayout.CENTER);
		licznik = 0;
		distL = 0;
		distR = 0;
		distU = 0;
		distD = 0;
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				Dimension biSize = new Dimension(bi.getWidth(), bi.getHeight());
				Graphics g = bi.createGraphics();
				while(true){
					drawThings(g, biSize);
					//mouse.x = (int) canvas.mousexy.getX();
					//mouse.y = (int) canvas.mousexy.getY();
					updateCounter();
					g.drawImage(bi, 0, 0, null);
					canvas.repaint();
					try {
						Thread.sleep(5);
					} 
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		};
		Thread thr = new Thread(r);
		thr.start();
	}

	public void movePoint(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(point.x, point.y, 5, 5);
		int dist = Integer.MAX_VALUE;
		Dimension biSize = new Dimension(bi.getWidth(), bi.getHeight());
		if(point.getX() > 0 && point.getY() > 0 && point.getX() < biSize.getWidth()-1 && point.getY() < biSize.getHeight()-1){
			if(PointDir == 0){
				dist = scaner(g, 0, biSize, point);
				if(dist > 10){
					point.x--;
				}
				else PointDir = (int)(System.nanoTime()%4);
				
			}
			if(PointDir == 1){
				dist = scaner(g, 1, biSize, point);
				if(dist > 10){
					point.x++;
				}
				else PointDir = (int)(System.nanoTime()%4);
			}
			if(PointDir == 2){
				dist = scaner(g, 2, biSize, point);
				if(dist > 10){
					point.y--;
				}
				else
				PointDir = (int)(System.nanoTime()%4);
			}
			if(PointDir == 3){
				dist = scaner(g, 3, biSize, point);
				if(dist > 10){
					point.y++;
				}
				else PointDir = (int)(System.nanoTime()%4);
			}
		}
			
	}
	
	public void updateCounter(){
		
		this.setTitle("COUNTER: " + licznik + ", paint count: " + canvas.licznik2 + ", distL: " + distL +", distR: " + distR +", distU: " + distU +", distD: " + distD);
	}
	public void drawThings(Graphics g, Dimension scanSize){
		g.drawImage(zpliku, 0,0,null);
		g.setColor(Color.RED);
		
		//distL = scaner(g, 0, scanSize, mouse);
		//distR = scaner(g, 1, scanSize, mouse);
		//distU = scaner(g, 2, scanSize, mouse);
		//distD = scaner(g, 3, scanSize, mouse);
		g.fillRect(mouse.x, mouse.y, 20, 20);
		movePoint(g);
		
	}
	
	//TODO: zrobic ta funkcje tak, aby przyjmowala za argument kąt nachylenia czujnika, a nastepnie zwracała punkt wykrycia piksela oraz odległość od tego piksela
	//wtedy mozna wywolac tą funkcje kilka razy i mamy czujnik dookola 
	public int scaner(Graphics g, int mode, Dimension maxsize, Point origin){ //mode 0 - left, 1 - right, 2 - down, 3 - up, else - left
		Boolean wykryto = false;
		int maxPixel = 100;
		int currPixel = 0;
		int dist = Integer.MAX_VALUE;
		Color scan = null;
		Point pixel = new Point((int)origin.getX(), (int)origin.getY());
		while (!wykryto && currPixel < maxPixel && pixel.getX() > 0 && pixel.getY() > 0 && pixel.getX() < maxsize.getWidth()-1 && pixel.getY() < maxsize.getHeight()-1){
			scan = new Color(bi.getRGB((int)pixel.getX(), (int)pixel.getY()));
		
			if(scan.equals(Color.RED)){
				wykryto = true;
				dist = currPixel;
				break;
			}
			bi.setRGB((int)pixel.getX(), (int)pixel.getY(), Color.black.getRGB());
			currPixel++;
			if(mode == 0) pixel.x--;
			else if(mode == 1) pixel.x++;
			else if(mode == 2) pixel.y--;
			else if(mode == 3) pixel.y++;
			else pixel.x--;
		}
		return dist;
	}
	
	

}
