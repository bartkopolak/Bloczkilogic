package thingy.blocks;

import java.awt.Dimension;
import java.util.List;

import thingy.Bloczek;
import thingy.FuncBlock;
import thingy.Pin;

public class TestBloczek extends FuncBlock {

	public TestBloczek(Dimension cSize, List<Bloczek> list, int inCount) {
		super(cSize, list, inCount, 1);
		setName("test blok");
		setImage("img/def.png");
		
	}
	//and
	public void func(){
		Pin wyj = out.get(0);
		for(Pin wej: in){
			wyj.setState(wyj.getState() & wej.getState());
		}
	}
	
	

}
