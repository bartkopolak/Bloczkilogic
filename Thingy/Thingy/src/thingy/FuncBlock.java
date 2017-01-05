package thingy;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class FuncBlock extends Bloczek {

	protected int inCount;
	protected int outCount;
	protected List<Pin> in;
	protected List<Pin> out;
	static int MINSIZE = 20;
	protected Image image;
	public FuncBlock(Dimension cSize, List<Bloczek> list, int inCount, int outCount) {
		super(cSize, list);
		in = new ArrayList<Pin>();
		out = new ArrayList<Pin>();
		this.inCount = inCount;
		this.outCount = outCount;
		setPins(list);
		
		
	}

	public Image getImage() {
		return image;
	}
	
	public void setImage(String dest) {
		File iconsrc = new File(dest);
		try {
			image = ImageIO.read(iconsrc);
		} catch (IOException e) {
			image = new BufferedImage(50,30,BufferedImage.TYPE_INT_RGB);
		}
	}
	
	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
		for(Pin p: pinList){
			p.setSelected(selected);
			if(p.getConnectedPin() != null){
				Bloczek parent = p.getConnectedPin().getParent();
				if(parent instanceof Line)
					((Line) parent).selectAllChildLines(selected);
			}
			
		}
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
		checkSize(Math.max(inCount, outCount));
		for(int i = 0; i < inCount; i++){
			in.add(new Pin(canvasSize, this, 0 ,10+i*10, Pin.pinType.IN, Pin.pinStyle.SQUARE, list,i+1));
			
		}
		for(int i = 0; i < outCount; i++){
			out.add(new Pin(canvasSize, this, this.width-6 ,10+i*10, Pin.pinType.OUT, Pin.pinStyle.SQUARE, list,i+1));
		}
		
		
	}
/**
 * Zmienia wysokość bloczka odpowiednio do ilości pinów.
 * @param i - ilosc pinów
 */
	private void checkSize(int i){
		if((this.height + (i*10) - 3) > MINSIZE) 
			this.height = MINSIZE + (i)*10 - 3;
		else 
			this.height = MINSIZE;
	}
	
	
	
}
