package thingy;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;


public class PlotnoPanel extends JPanel implements MouseListener, MouseMotionListener{
	private BufferedImage area;
	Bloczek selectedBlock;
	private boolean isDragged;
	private boolean editMode;
	private boolean lineDrawingMode;
	public Point mousexy;
	public Point mouseStart;
	List<Bloczek> listaBloczkuf;
	private JPopupMenu popupMenu;
	private JMenuItem mntmDeleteThatThing;
	private JMenuItem mntmProperties;
	
	public enum DrawingMode{
		LINE, SELECT
	}
	public DrawingMode drawMode;
	

	Boolean pressed;
	Point point;

	Graphics bufG; 
	Image bufI;
	
	public PlotnoPanel(BufferedImage image, List<Bloczek> listaBloczkuf, Bloczek selectedBloczek, DrawingMode mode){
		area = image;
		point = new Point(0,0);
		pressed = false;
		selectedBlock = selectedBloczek;
		this.listaBloczkuf = listaBloczkuf;
		drawMode = mode;
		//popup
				popupMenu = new JPopupMenu();
				
				mntmDeleteThatThing = new JMenuItem("Usuń");
				mntmDeleteThatThing.addActionListener(DeleteAction);
				popupMenu.add(mntmDeleteThatThing);
				mntmProperties = new JMenuItem("Właściwości");
				mntmProperties.addActionListener(PropertiesAction);
				popupMenu.add(mntmProperties);
				
		addMouseListener(this);
		addMouseMotionListener(this);
			
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		if( bufI == null )
	 	{
          bufI = createImage( getWidth(), getHeight() );
          bufG = bufI.getGraphics();
	 	}
		
		bufG.drawImage(area, 0, 0, null);
		g.drawImage(bufI, 0, 0, null);
		
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
	
	AbstractAction DeleteAction = new AbstractAction(){
		public synchronized void actionPerformed(ActionEvent arg0) {
			List<Bloczek> toRemove = Bloczek.BloczekListMethods.createSelectedBlocksList(listaBloczkuf);
			listaBloczkuf.removeAll(toRemove);
			
		}
	};
	
	AbstractAction PropertiesAction = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent e){
			new PropertiesWindow(selectedBlock);
		}
	};
	
	
	public void paintBloczki(){
		bufG.setColor(Color.white);
		bufG.fillRect(0, 0, area.getWidth(), area.getHeight());
		
		
		for (Bloczek b: listaBloczkuf){
			bufG.setColor(b.getColor());
			bufG.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
		}
		for(Bloczek b: listaBloczkuf){
			bufG.setColor(Color.BLACK);
			bufG.drawString(b.getName(), b.getX(), b.getY()+b.getHeight()+12);
		}
		
		DrawingHandler();
	}
	
	void DrawingHandler(){
		if(isDragged() && !isEditMode()){
			setLineDrawingMode(true);
			bufG.setColor(Color.black);
			
			if(drawMode == DrawingMode.SELECT){
				Rectangle selection = new Rectangle();
				if(mouseStart.x <= mousexy.x && mouseStart.y <= mousexy.y)
					selection.setBounds(mouseStart.x, mouseStart.y, mousexy.x-mouseStart.x, mousexy.y-mouseStart.y);
				else if(mouseStart.x > mousexy.x && mouseStart.y <= mousexy.y)
					selection.setBounds(mousexy.x, mouseStart.y, mouseStart.x-mousexy.x, mousexy.y-mouseStart.y);
				else if(mouseStart.x <= mousexy.x && mouseStart.y > mousexy.y)
					selection.setBounds(mouseStart.x, mousexy.y, mousexy.x-mouseStart.x, mouseStart.y-mousexy.y);
				else if(mouseStart.x > mousexy.x && mouseStart.y > mousexy.y)
					selection.setBounds(mousexy.x, mousexy.y, mouseStart.x-mousexy.x, mouseStart.y-mousexy.y);
				
				bufG.drawRect(selection.x, selection.y, selection.width, selection.height);
				for(Bloczek b : listaBloczkuf){
					if(selection.contains( new Point((b.getX() + b.getWidth()/2), (b.getY() + b.getHeight()/2)) )){
							
							b.setSelected(true);
							b.Highlight();
					}
					else{
						b.setSelected(false);
						b.DeHighlight();
					}
				}
			}
			else if(drawMode == DrawingMode.LINE){
				bufG.drawLine(mouseStart.x, mouseStart.y, mousexy.x, mousexy.y);
			}
		}
		else ;
	}
	
	
	int pressedButton;
	Rectangle boundingBox = null;
	Point BBoxStartPos = null;

	@Override
	public void mouseDragged(MouseEvent arg0) {
		this.setDragged(true);
		mousexy = arg0.getPoint();
		if(!this.isLineDrawingMode()){
			if(pressedButton == MouseEvent.BUTTON1) this.setDragged(true);
			//stworzenie boundig boxa zaw wszystkie zaznaczone bloczki
				List<Bloczek> selectedList = Bloczek.BloczekListMethods.createSelectedBlocksList(listaBloczkuf);
				if(boundingBox == null) {
					boundingBox = Bloczek.BloczekListMethods.createBoundingBox(selectedList);
					BBoxStartPos = new Point(boundingBox.x, boundingBox.y);
				}
			
			if(boundingBox != null){
				boundingBox.setLocation(BBoxStartPos.x + mousexy.x - mouseStart.x, BBoxStartPos.y + mousexy.y - mouseStart.y);
				Rectangle areaSize = new Rectangle(0,0,area.getWidth(), area.getHeight());
				
				for(Bloczek b : selectedList){
					if(this.isDragged())
						{
								this.setEditMode(true);	//tryb modyfikowania bloczkow
								if(areaSize.contains(boundingBox)){
									synchronized(this){
											//ustaw bloczek w tryb przeciągania
											b.setDragged(true);	
											b.setX(b.getStartPos().x + mousexy.x - mouseStart.x);		//zmień pozycję bloczka
											b.setY(b.getStartPos().y + mousexy.y - mouseStart.y);
										
									}
									//g.setColor(Color.BLACK);
									//g.drawRect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
									//canvas.repaint();
									b.Highlight();
								}
								else{
									b.setDragged(true);	
									boundingBox.setLocation(BBoxStartPos.x + mousexy.x - mouseStart.x, BBoxStartPos.y + mousexy.y - mouseStart.y);
								}
						}
					}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		mousexy = arg0.getPoint();
		
		for(Bloczek b : listaBloczkuf){
		if(mousexy.x > b.getX() && mousexy.y > b.getY() && mousexy.x < b.getX() + b.getWidth() && mousexy.y < b.getY() + b.getHeight() && !this.isLineDrawingMode()){ //jesli kursor jest na bloczku
			b.Highlight();	//podświetl bloczek
			b.setMouseIn(true);
		}
		else {
			b.DeHighlight();			//jesli kursor nie jest na bloczku, wyłącz podświetlenie
			b.setMouseIn(false);
		}
		
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		mouseStart = arg0.getPoint();
		pressedButton = arg0.getButton();
		List<Bloczek> selectedList = Bloczek.BloczekListMethods.createSelectedBlocksList(listaBloczkuf);
		if(Bloczek.BloczekListMethods.getBlockUnderMouse(listaBloczkuf) == null){
			this.setEditMode(false);
			//iloscLabel.setText("Nic nie kliknieto");
		}
		
		for(Bloczek b : listaBloczkuf){
			if(b.isMouseIn() && arg0.getButton() == MouseEvent.BUTTON3)			//kliknieto prawy przycisk myszy na bloczek
			{
				
				if(Bloczek.BloczekListMethods.getBlockUnderMouse(selectedList) == null) Bloczek.BloczekListMethods.deselectAll(selectedList);	//jesli nie nacisnieto na wczesniej zaznaczony bloczek, odznacz wszystkie zaznaczone bloczki
				b.setSelected(true);
				b.Highlight();
				selectedBlock = b;
				popupMenu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
				//iloscLabel.setText("kliknieto prawym na "+ b.getName());
				break;
			}
			else if(b.isMouseIn() && arg0.getButton() == MouseEvent.BUTTON1)	//kliknieto lewy przycisk myszki na bloczek
			{
				if(Bloczek.BloczekListMethods.getBlockUnderMouse(selectedList) == null) Bloczek.BloczekListMethods.deselectAll(selectedList);	
				b.setSelected(true);
				selectedBlock = b;
				//iloscLabel.setText("kliknieto lewy na "+ b.getName());
				break;
			}
			else{					//jesli kliknieto w puste niejsce
				if(Bloczek.BloczekListMethods.getBlockUnderMouse(listaBloczkuf) == null){
					b.setSelected(false);
					b.DeHighlight();
				}
			}
			
		}
		
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(this.isDragged()) this.setDragged(false);
		if(this.isEditMode()) this.setEditMode(false);
		if(this.isLineDrawingMode()) this.setLineDrawingMode(false);
		if(!(boundingBox == null)) boundingBox = null;
		if(!(BBoxStartPos == null)) BBoxStartPos = null;
		for(Bloczek b : listaBloczkuf){
			//if(b.isDragged())
				//b.setSelected(false);
			b.setDragged(false);
			
			b.setStartPos(b.getX(), b.getY());
			
		}
		
	}


}
