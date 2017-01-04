package thingy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

public class Pin extends Bloczek{
	
	enum pinType{
		IN, OUT, INOUT, UNDEF
	}
	enum pinStyle{
		SQUARE, LINE, NOTVISIBLE
	}
	pinType type;
	pinStyle style;
	pinStyle orgStyle;
	Boolean state;
	int pinPosX, pinPosY;
	Bloczek parent;
	Pin connectedTo;
	
	List<Bloczek> BlockList; //uzywany przy kazdorazowej zmianie pozycji pinu w celu sprawdzenia czy 
	/**
	 * Pin - zawiera wartosć logiczną, 
	 * @param cSize - wielkość płótna na którym bloczek jest rysowany
	 * @param parent - rodzic pinu
	 * @param x - pozycja x
	 * @param y - pozycja y
	 * @param type - typ pinu 
	 * @param style - wygląd pinu
	 * @param list - lista bloczków
	 */
	public Pin(Dimension cSize, Bloczek parent, int x, int y, pinType type, pinStyle style, List<Bloczek> list, int id){
		super(cSize, list);
		pinPosX = x;
		pinPosY = y;
		this.parent = parent;
		this.x = this.parent.x + pinPosX;
		this.y = this.parent.y + pinPosY;
		StartPos = new Point(this.x,this.y);
		width = 6;
		height = 6;
		name = "pin";
		color = Color.WHITE;
		origColor = color;
		dragged = false;
		mouseIn = false;
		image = null;
		this.type = type;
		this.style = style;
		orgStyle = this.style;
		state = false;
		BlockList = list;
		setConnectedTo();
		ID = parent.ID + "." + id + "-" + type;
		parent.pinList.add(this);
				
	}
	
	private void setConnectedTo(){
		Pin pin = checkForConnection(BlockList);
		if(pin == null){
			if(connectedTo != null){
				connectedTo.disconnect();
				disconnect();
			}
		}
		else{
			connectedTo = pin;
			connectedTo.connectWith(this);
			
		}
	}
	
	@Override
	public synchronized void setX(int x) {
		if(x>=0 )
			this.x = x;
		for(Pin p:pinList)
			p.setX(x + p.pinPosX);
		setConnectedTo();
	}
	
	@Override
	public synchronized void setY(int y) {
		if(y>=0 )
			this.y = y;
		for(Pin p:pinList)
			p.setY(y + p.pinPosY);
		setConnectedTo();
	}
	
	public Bloczek getParent(){
		return parent;
	}
	
	public pinType getType(){
		return type;
	}
	
	public void setType(pinType t){
		type = t;
	}
	
	public pinStyle getStyle() {
		return style;
	}

	public void setStyle(pinStyle style) {
		this.style = style;
	}
	
	public void connectWith(Pin p){
		connectedTo = p;
		this.x = connectedTo.getX();
		this.y = connectedTo.getY();
		if(orgStyle == Pin.pinStyle.LINE) style = Pin.pinStyle.NOTVISIBLE;		
		else if(orgStyle == Pin.pinStyle.SQUARE) this.setOrigColor(Color.GRAY);
	}
	
	public void disconnect(){
		connectedTo = null;
		if(orgStyle == Pin.pinStyle.LINE) style = orgStyle;		
		else if(orgStyle == Pin.pinStyle.SQUARE) this.setOrigColor(Color.WHITE);
	}
	
	public Pin getConnectedPin(){
		return connectedTo;
	}
	/**
	 * Sprawdza, czy dany pin nie znajduje sie wewnątrz innego pinu. Jesli tak, zwraca pin z którym jest połaczony
	 * @return pin z którym jest połaczony
	 */
	public Pin checkForConnection(List<Bloczek> list){
		
		for(Bloczek b: list){
			if(b instanceof Pin && !b.equals(this)){
				Rectangle area = new Rectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
				Point thisPinPos = new Point(x+width/2,y+height/2);
				if(area.contains(thisPinPos)) {
					connectWith((Pin) b);
					return (Pin) b;
				}
			}
		}
		return null;
	}

	public static Pin getSelectedPin(List<Pin> pinList){
		for(Pin p : pinList){
			if(p.isMouseIn()) return p;
		}
		return null;
	}
	
	public String toString(){
		boolean con = false;
		if(connectedTo != null) con = true;
		String s = ID+": connected: " + Boolean.toString(con);
		return s;
	}
	
	
	
	
	

}
