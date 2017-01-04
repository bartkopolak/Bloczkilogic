package thingy;

import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

public class Line extends Bloczek{
	Bloczek inBloczek;
	Bloczek outBloczek;
	int lineSize;
	int x1, y1, x2, y2;
	
	/**
	 * x 
	 * 
	 * @param cSize
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * 
	 */
	public Line(Dimension cSize, int x1, int y1, int x2, int y2, List<Bloczek> list){
		super(cSize, list);
		int temp;
		inBloczek = null;
		outBloczek = null;
		name = null;
		setSelected(false);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.width = Math.abs(x2 - x1);
		this.height = Math.abs(y2 - y1);
		
		if(x1>x2){
			temp= x1;
			x1 = x2;
			x2 = temp;
		}
		this.x = x1;
		if(y1>y2){
			temp = y1;
			y1 = y2;
			y2 = temp;
		}
		this.y = y1;
		
		x -=3;
		y -=3;
		width += 6;
		height += 6;
		StartPos = new Point(x,y);
		Pin pin1 = new Pin(cSize, this, 0,0,Pin.pinType.UNDEF, Pin.pinStyle.LINE, list,1);
		Pin pin2;
		if(y1 == y2)
			pin2 = new Pin(cSize, this, width-6,0,Pin.pinType.UNDEF, Pin.pinStyle.LINE, list,2);
		else if(x1 == x2)
			pin2 = new Pin(cSize, this, 0,height-6,Pin.pinType.UNDEF, Pin.pinStyle.LINE, list,2);
		else pin2=null;
		
		//if(pin1.isInPin()) pin1.connect(pin);
		
		
	}
	@Override
	public void setSelected(boolean selected) {
		if(selected)
			lineSize = 2;
		else
			lineSize = 1;
		this.selected = selected;
	}
	public int getLineSize() {
		return lineSize;
	}
	
	
}
