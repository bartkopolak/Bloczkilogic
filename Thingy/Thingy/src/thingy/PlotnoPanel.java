package thingy;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import thingy.*;


public class PlotnoPanel extends JScrollPane implements MouseListener, MouseMotionListener{
	private BufferedImage area;
	Bloczek selectedBlock;
	public Boolean isDragged;
	public Boolean blockDragMode;
	private boolean lineDrawingMode;
	private boolean selectingMode;
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
	boolean isDrawingLine = false;

	Boolean pressed;
	Point point;

	Graphics2D g;
	
	Graphics bufG; 
	Image bufI;
	
	public PlotnoPanel(List<Bloczek> listaBloczkuf, Bloczek selectedBloczek){
		area = new BufferedImage(640,480,BufferedImage.TYPE_INT_RGB);
		
		//pobranie obiektu Graphics z bitmapy i wyczysczenie 
		g = area.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, area.getWidth(), area.getHeight());
		point = new Point(0,0);
		pressed = false;
		setFocusable(true);
		selectedBlock = selectedBloczek;
		this.listaBloczkuf = listaBloczkuf;
		drawMode = DrawingMode.LINE;
		isDragged = false;
		blockDragMode = false;
		
		
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
		
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
		getActionMap().put("delete", DeleteAction);
		
		JLabel x = new JLabel();
		x.setIcon(new ImageIcon(area));
		x.setBounds(0, 0,1, 1);
		setViewportView(x);	
		
		
	}

	@Override
	public void paintComponent(Graphics g){
		
		if( bufI == null )
	 	{
          bufI = createImage( getWidth(), getHeight() );
          bufG = bufI.getGraphics();
	 	}
		paintBloczki();
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
	
	public boolean isBlockDragMode() {
		return blockDragMode;
	}

	public void setBlockDragMode(boolean Mode) {
		this.blockDragMode = Mode;
	}

	public boolean isInSelectingMode() {
		return selectingMode;
	}

	public void setSelectingMode(boolean selectingMode) {
		this.selectingMode = selectingMode;
	}

	public boolean isLineDrawingMode() {
		return lineDrawingMode;
	}

	public void setLineDrawingMode(boolean lineDrawingMode) {
		this.lineDrawingMode = lineDrawingMode;
	}
	
	
	
	AbstractAction DeleteAction = new AbstractAction(){
		public synchronized void actionPerformed(ActionEvent arg0) {
			Bloczek.BloczekListMethods.deleteSelectedBlocks(listaBloczkuf);
		}
	};
	
	AbstractAction PropertiesAction = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent e){
			new PropertiesWindow(selectedBlock);
		}
	};
	
	
	public void paintBloczki(){
		g.setColor(Color.white);
		g.fillRect(0, 0, area.getWidth(), area.getHeight());
		
		//bloczki
		for (Bloczek b: listaBloczkuf){
			g.setStroke(new BasicStroke(1));
			if(b instanceof Line){
				g.setColor(Color.BLACK);
				g.setStroke(new BasicStroke(((Line) b).getLineSize()));
				g.drawLine(b.getX()+3, b.getY()+3, b.getX()+b.getWidth()-3, b.getY()+b.getHeight()-3);

			}
			else if (b instanceof TestBloczek){
				g.setColor(b.getColor());
				g.drawImage(b.getImage(), b.getX(), b.getY(), null);
				g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			}
			g.setStroke(new BasicStroke(1));
			for(Pin p: b.pinList){
				if(p.getStyle() == Pin.pinStyle.SQUARE){
					g.setColor(p.getColor());
					g.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
					g.setColor(Color.BLACK);
					g.drawRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
				}
				else if (p.getStyle() == Pin.pinStyle.LINE){
					g.setColor(Color.BLACK);
					g.drawLine(p.getX(), p.getY(), p.getX()+p.getWidth(), p.getY()+p.getHeight());
					g.drawLine(p.getX(), p.getY()+p.getHeight(), p.getX()+p.getWidth(), p.getY());
				}
			}
		}
		
		
		//podpisy
		for(Bloczek b: listaBloczkuf){
			if((b instanceof FuncBlock)){
				
				g.setColor(Color.BLACK);
				g.drawString(b.getName(), b.getX(), b.getY()+b.getHeight()+12);
			}
		}
		
		DrawingHandler();
	}
	
	void DrawingHandler(){
		g.setStroke(new BasicStroke(1));
		if(isDragged() && isInSelectingMode()){
			g.setColor(Color.black);
			
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
				
				g.drawRect(selection.x, selection.y, selection.width, selection.height);
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
		}
		else if (drawMode == DrawingMode.LINE && isLineDrawingMode()){
			if(Math.abs(mouseStart.y - mousexy.y) < Math.abs(mouseStart.x - mousexy.x))
				g.drawLine(mouseStart.x, mouseStart.y, mousexy.x, mouseStart.y);
			else
				g.drawLine(mouseStart.x, mouseStart.y, mouseStart.x, mousexy.y);
		}
	}
	
	
	int pressedButton;
	Rectangle boundingBox = null;
	Point BBoxStartPos = null;

	@Override
	public void mouseDragged(MouseEvent arg0) {
		this.setDragged(true);
		mousexy = arg0.getPoint();
		Bloczek checkBlok = Bloczek.BloczekListMethods.getBlockUnderMouse(listaBloczkuf);
		if(checkBlok == null && drawMode == DrawingMode.SELECT) setSelectingMode(true);
		if(!this.isLineDrawingMode() && !this.isInSelectingMode()){
			if(pressedButton == MouseEvent.BUTTON1) this.setDragged(true);
			//stworzenie bounding boxa zaw wszystkie zaznaczone bloczki
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
								this.setBlockDragMode(true);	//tryb modyfikowania bloczkow
								if(areaSize.contains(boundingBox)){
									synchronized(this){
											//ustaw bloczek w tryb przeciągania i zmien pozycje bloczka
											b.setDragged(true);	
											b.setX(b.getStartPos().x + mousexy.x - mouseStart.x);		
											b.setY(b.getStartPos().y + mousexy.y - mouseStart.y);
										
									}
									//g.setColor(Color.BLACK);
									//g.drawRect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
									//canvas.repaint(); (wysw bboxa)
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
		else{
			if(this.isLineDrawingMode()) isDrawingLine = true;
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		mousexy = arg0.getPoint();
		
		//ust. flagi MouseIn dla kazdego bloczka
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
		List<Bloczek> selectedList = Bloczek.BloczekListMethods.createSelectedBlocksList(listaBloczkuf);
		Bloczek.BloczekListMethods.deselectAll(listaBloczkuf);	
		for(Bloczek b : listaBloczkuf){
			
			if(b.isMouseIn()){
				b.setSelected(true);
				b.Highlight();
				break;
			}
			//iloscLabel.setText("kliknieto lewy na "+ b.getName());
			
		}
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
		if( Bloczek.BloczekListMethods.getBlockUnderMouse(listaBloczkuf) instanceof Pin){
			this.setBlockDragMode(false);
			setLineDrawingMode(true);
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
			else{																//jesli kliknieto w puste niejsce
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
		if(this.isBlockDragMode()) this.setBlockDragMode(false);
		if(this.isInSelectingMode()) this.setSelectingMode(false);
		if(!(boundingBox == null)) boundingBox = null;
		if(!(BBoxStartPos == null)) BBoxStartPos = null;
		
		for(Bloczek b : listaBloczkuf){
			//if(b.isDragged())
				//b.setSelected(false);
			b.setDragged(false);
			
			b.setStartPos(b.getX(), b.getY());
			
		}
		if(drawMode == DrawingMode.LINE && this.isDrawingLine && this.isLineDrawingMode()){
			Bloczek.BloczekListMethods.deselectAll(listaBloczkuf);
			
				if(Math.abs(mouseStart.y - mousexy.y) < Math.abs(mouseStart.x - mousexy.x))
					new Line(this.getSize(),mouseStart.x, mouseStart.y, mousexy.x, mouseStart.y, listaBloczkuf).setSelected(true);
				else
					new Line(this.getSize(),mouseStart.x, mouseStart.y, mouseStart.x, mousexy.y, listaBloczkuf).setSelected(true);
			
			
			
		}
		if(this.isLineDrawingMode()) this.setLineDrawingMode(false);
		if(this.isDrawingLine) isDrawingLine = false;
		
	}



}
