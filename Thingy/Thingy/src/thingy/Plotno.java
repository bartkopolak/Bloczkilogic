package thingy;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;


public class Plotno extends Canvas {
	private BufferedImage img;
	public int licznik2;
	private boolean isDragged;
	private boolean editMode;
	private boolean lineDrawingMode;
	
	public enum DrawingMode{
		LINE, SELECT
	}
	public DrawingMode drawMode;
	

	Boolean pressed;
	Point point;

	Graphics bufG; 
	Image BufI;
	
	public Plotno(BufferedImage image){
		img = image;
		licznik2 = 0;
		
		point = new Point(0,0);
		pressed = false;
			
	}
	
	@Override
	public void update(Graphics g){
		
		if( BufI == null )
	 	{
          BufI = createImage( getWidth(), getHeight() );
          bufG = BufI.getGraphics();
	 	}
		
		bufG.drawImage(img, 0, 0, null);
		g.drawImage(BufI, 0, 0, null);
		licznik2++;
		
	}
	
	
	
	public Boolean getPressed() {
		return pressed;
	}

	public void setPressed(Boolean pressed) {
		this.pressed = pressed;
	}

	public void setDragged(boolean isDragged) {
		this.isDragged = isDragged;
	}

	public boolean isDragged() {
		return isDragged;
	}
	
	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
	
	public boolean isLineDrawingMode() {
		return lineDrawingMode;
	}

	public void setLineDrawingMode(boolean lineDrawingMode) {
		this.lineDrawingMode = lineDrawingMode;
	}


}
