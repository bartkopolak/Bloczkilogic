package thingy.blocks;

import java.awt.Dimension;
import java.util.List;

import thingy.Bloczek;
import thingy.FuncBlock;

public class VCC extends FuncBlock {

	public VCC(Dimension cSize, List<Bloczek> list) {
		super(cSize, list, 0, 1);
		setName("VCC");
		// TODO Auto-generated constructor stub
		func();
	}
	
	public void func(){
		out.get(0).setState(true);
	}

}
