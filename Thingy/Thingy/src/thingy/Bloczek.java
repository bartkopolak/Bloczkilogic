package thingy;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

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
	
	
	private int x;
	private int y;
	private Dimension canvasSize;
	private String name;
	private Color color;
	private Color origColor;
	private boolean mouseIn;
	private boolean dragged;
	private boolean selected;
	private Point StartPos;
	static int iloscBloczkuf = 0;
	int width;
	int height;
	
	public Bloczek(Dimension cSize){
		x = 0;
		y = 0;
		StartPos = new Point(x,y);
		canvasSize = cSize;
		width= 50;
		height = 30;
		name = "Bloczek_jakis_tam nr " + iloscBloczkuf;
		color = Color.RED;
		origColor = color;
		dragged = false;
		mouseIn = false;
		iloscBloczkuf++;
		
		
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
		if(x>=0 && x <= canvasSize.getWidth()-width-1)
			this.x = x;
		if(x > canvasSize.getWidth()-width-1) x = StartPos.x;
	}
	public int getY() {
		return y;
	}
	public synchronized void setY(int y) {
		if(y>=0 && y<=canvasSize.getHeight()-height-1)
			this.y = y;
		if(x > canvasSize.getWidth()-width-1) y = StartPos.y;
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
	
	public static class BloczekListMethods{
		
		BloczekListMethods(){
			
		}
		/**
		 * Zwraca bloczek z listy bloczków, nad którym jest kursor 
		 * @param listaBloczkuf
		 * @return bloczek
		 */
		public static Bloczek getBlockUnderMouse(List<Bloczek> listaBloczkuf){
			for(Bloczek b : listaBloczkuf){
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
		
		
	}
	
	
	
	
}


