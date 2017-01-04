package thingy;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Bloczek {

	/**
	 * x - pozycja x
	 * y - pozycja y
	 * canvasSize - rozmiar płotna na którym rysowane będa bloczki - ważne po to aby bloczek nie wyszedł poza obszar płotna
	 * name - nazwa bloczka
	 * color - kolor wyswietlany na plotnie, zmieniany np przy zaznaczeniu bloczka
	 * origColor - kolor właściwy bloczka
	 * mouseIn - flaga czy kursor jest najechany na bloczek
	 * dragged - flaga spr czy blocek jest przeciągany
	 * selected - flaga czy bloczek jest zaznaczony
	 * StartPos - pozycja bloczka na poczatku przeciagania
	 * width - szer
	 * height - wysokosc
	 */
	
	
	protected int x;
	protected int y;
	protected Dimension canvasSize;
	protected String name;
	protected Color color;
	protected Color origColor;
	protected boolean mouseIn;
	protected boolean dragged;
	protected boolean selected;
	protected Point StartPos;
	protected Image image;
	static int iloscBloczkuf = 0;
	protected String ID;
	protected int width;
	protected int height;
	public List<Pin> pinList = new ArrayList<Pin>();
	
	
	public Bloczek(Dimension cSize, List<Bloczek> list){
		x = 0;
		y = 0;
		StartPos = new Point(x,y);
		canvasSize = cSize;
		width= 50;
		height = 30;
		name = "Bloczek " + iloscBloczkuf;
		color = Color.RED;
		origColor = color;
		dragged = false;
		mouseIn = false;
		File iconsrc = new File("img/def.png");
		try {
			image = ImageIO.read(iconsrc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			image = new BufferedImage(50,30,BufferedImage.TYPE_INT_RGB);
		}
		ID = "" + iloscBloczkuf;
		iloscBloczkuf++;
		list.add(this);
		
	}
	
	public String getID() {
		return ID;
	}

	public Image getImage() {
		return image;
	}



	public int getWidth() {
		return width;
	}



	public void setWidth(int width) {
		this.width = width;
	}



	public int getHeight() {
		return height;
	}



	public void setHeight(int height) {
		this.height = height;
	}



	public int getX() {
		return x;
	}
	public synchronized void setX(int x) {
		if(x>=0 )
			this.x = x;
		for(Pin p:pinList)
			p.setX(x + p.pinPosX);
			
		
		//if(x > canvasSize.getWidth()-width-1) x = StartPos.x;
	}
	public int getY() {
		return y;
	}
	public synchronized void setY(int y) {
		if(y>=0 )
			this.y = y;
		for(Pin p:pinList)
			p.setY(y + p.pinPosY);
		//if(x > canvasSize.getWidth()-width-1) y = StartPos.y;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}
	
	public void setOrigColor(Color color) {
		origColor = color;
	}
	
	public Color getOrigColor() {
		return origColor;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public boolean isDragged() {
		return dragged;
	}

	public void setDragged(boolean dragged) {
		this.dragged = dragged;
	}
	
	public boolean isMouseIn() {
		return mouseIn;
	}

	public void setMouseIn(boolean mouseIn) {
		this.mouseIn = mouseIn;
	}

	
	
	public Point getStartPos() {
		return StartPos;
	}

	public void setStartPos(int x, int y) {
		StartPos.setLocation(x, y);
	}

	public void Highlight() {
		color = origColor.darker();
	}
	
	public void DeHighlight() {
		if(!selected)
			color = origColor;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	@Override
	public String toString(){
		String s = ID+": " + this.name +", type: " +this.getClass().getName();
		return s;
	}
	
	public static class BloczekListMethods{
		
		BloczekListMethods(){
			
		}
		/**
		 * Zwraca bloczek z listy bloczków, nad którym jest kursor 
		 * @param listaBloczkuf
		 * @return bloczek
		 */
		public static Bloczek getBlockUnderMouse(List<Bloczek> listaBloczkuf){
			//poniewaz piny znajduja sie wewnatz bloczkow, piny sa tworzone po rodzicu, 
			//a funkcja ta zwraca pierwszy bloczek, to najpierw sprawdz, czy pod kursorem nie ma pinu
			Pin pin;
			for(Bloczek b : listaBloczkuf){
				if((pin = Pin.getSelectedPin(b.pinList)) != null)
					return pin;
				if(b.isMouseIn()) return b;
			}
			return null;
		}
		/**
		 * Tworzy liste zaznaczonych bloczków
		 * @param listaBloczkuf
		 * @return lista zaznaczonych bloczków
		 */
		public static List<Bloczek> createSelectedBlocksList(List<Bloczek> listaBloczkuf){
			List<Bloczek> selectedList = new ArrayList<Bloczek>();
			for(Bloczek b : listaBloczkuf){
				if(b.isSelected()) 
					selectedList.add(b);
			}
			return selectedList;
		}
		/**
		 * Tworzy prostokąt obejmujący wszystkie bloczki.
		 * Jeśli jako argument podano pustą listę, metoda ta zwróci prostokąt o zerowym rozmiarze.
		 * @param selected - Lista zaznaczonych bloczków
		 * @return Prostokąt obejmujący wszystkie bloczki.
		 * 
		 */
		public static Rectangle createBoundingBox(List<Bloczek> selected){
			Rectangle r = new Rectangle();
			if(selected.size() > 1){
				Bloczek first = selected.get(0);
				int minx = first.getX();
				int miny = first.getY();
				int maxx = first.getX() + first.getWidth();
				int maxy = first.getY() + first.getHeight();
				for(int i = 1; i < selected.size(); i++){
					Bloczek b = selected.get(i);
					if(b.getX() < minx) minx = b.getX();
					if(b.getY() < miny) miny = b.getY();
					if(maxx < b.getX() + b.getWidth()) maxx = b.getX() + b.getWidth();
					if(maxy < b.getY() + b.getHeight()) maxy = b.getY() + b.getHeight();
				}
				r.setBounds(minx, miny, maxx-minx, maxy-miny);
			}
			else if(selected.size() == 1){
				r.setBounds(selected.get(0).getX(), selected.get(0).getY(), selected.get(0).getWidth(), selected.get(0).getHeight());
			}
			else r.setBounds(0,0,0,0);
			return r;
		}
		/**
		 * Odznacza wszystkie bloczki z listy bloczków
		 * @param lista
		 */
		public static void deselectAll(List<Bloczek> lista){
			for(Bloczek b:lista){
				b.setSelected(false);
				b.DeHighlight();
			}
			
		}
		/**
		 * Odznacza wszystkie bloczki podanej klasy
		 * @param lista
		 * @param className - klasa bloczkow do odznaczenia
		 */
		public static void deselectOnlyOneType(List<Bloczek> lista, Class<?> className){
			for(Bloczek b:lista){
				if(b.getClass() == className){
					b.setSelected(false);
					b.DeHighlight();
				}
			}
		}
		/**
		 * usuwa wszystkie zaznaczone bloczki z listy bloczków
		 * @param listaBloczkuf
		 */
		public static void deleteSelectedBlocks(List<Bloczek> listaBloczkuf){
			List<Bloczek> toRemove = Bloczek.BloczekListMethods.createSelectedBlocksList(listaBloczkuf);
			listaBloczkuf.removeAll(toRemove);
		}
		
		
	}
	
	
	
	
}


