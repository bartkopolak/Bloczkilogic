package thingy;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public class FuncBlock extends Bloczek {

	protected int inCount;
	protected int outCount;
	protected List<Line> in;
	protected List<Line> out;
	static int MINSIZE = 16;
	
	public FuncBlock(Dimension cSize, List<Bloczek> list, int inCount, int outCount) {
		super(cSize, list);
		
		this.inCount = inCount;
		this.outCount = outCount;
		setPins(list);
		
	}

	public int getInCount() {
		return inCount;
	}

	public void setInCount(int inCount) {
		this.inCount = inCount;
	}

	public int getOutCount() {
		return outCount;
	}

	public void setOutCount(int outCount) {
		this.outCount = outCount;
	}
	
	protected void setPins(List<Bloczek> list){
		if(!pinList.isEmpty())
			pinList.clear();
		
		for(int i = 0; i < inCount; i++){
			new Pin(canvasSize, this, 0 ,10+i*10, Pin.pinType.IN, Pin.pinStyle.SQUARE, list,i+1);
			checkSize(i+1);
			
		}
		for(int i = 0; i < outCount; i++){
			new Pin(canvasSize, this, this.width-6 ,10+i*10, Pin.pinType.OUT, Pin.pinStyle.SQUARE, list,i+1);
			checkSize(i+1);
		}
		
		
	}
/**
 * Zmienia wysokość bloczka odpowiednio do ilości pinów.
 * @param i - ilosc pinów
 */
	private void checkSize(int i){
		if((this.height + (i*10)) > MINSIZE) 
			this.height = MINSIZE + (i)*10;
		else 
			this.height = MINSIZE;
	}
	
	
}
