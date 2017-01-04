package thingy;

import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

public class Line extends Bloczek{
	int lineSize;	//grubość kabla (uzywane do zaznaczania)
	int x1, y1, x2, y2;
	
	/**
	 * Kabel - łączy piny miedzy sobą
	 * @param cSize - rozmiar obszaru do rysowania
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param list - lista obiektów
	 */
	public Line(Dimension cSize, int x1, int y1, int x2, int y2, List<Bloczek> list){
		super(cSize, list);
		int temp;
		name = null;
		setSelected(false);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.width = Math.abs(x2 - x1);
		this.height = Math.abs(y2 - y1);
		//x1 ma być zawsze mniejsze od x2, zapobiega to problemom przy tworzeniu pinów w każdym kierunku
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
		new Pin(cSize, this, 0,0,Pin.pinType.UNDEF, Pin.pinStyle.LINE, list,1);
		Pin pin2;
		if(y1 == y2)
			pin2 = new Pin(cSize, this, width-6,0,Pin.pinType.UNDEF, Pin.pinStyle.LINE, list,2);
		else if(x1 == x2)
			pin2 = new Pin(cSize, this, 0,height-6,Pin.pinType.UNDEF, Pin.pinStyle.LINE, list,2);
		else pin2=null;
		
		
		
	}
	
	@Override
	/**
	 * zaznaczenie kabla pogrubia go.
	 */
	public void setSelected(boolean selected) {
		if(selected)
			lineSize = 2;
		else
			lineSize = 1;
		this.selected = selected;
	}
	/**
	 * Zaznacza wszystkie połaczone razem kable
	 * @param selected
	 */
	public void selectAllChildLines(boolean selected){
		this.setSelected(selected);
		for(Pin p: pinList){
			if(p.getConnectedPin() != null){
				Bloczek child = p.getConnectedPin().getParent();
				//jesli rodzicem tego pinu jest kabel
				//i jeśli kabel jest połączony na tym pinie z innym kablem(child != null), i:
				//dla zaznaczania(selected=true): ten kabel nie jest zaznaczony (wiec child.isSelected ma być false)
				//dla odznaczania(selected=false): ten kabel jest zaznaczony (wiec child.isSelected ma byc true)
				if(child != null && (selected & !(child.isSelected())) && child instanceof Line)
					((Line) child).selectAllChildLines(selected);
				
			}
		}
	}
	public int getLineSize() {
		return lineSize;
	}
	
	
}
