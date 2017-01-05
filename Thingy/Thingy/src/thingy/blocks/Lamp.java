package thingy.blocks;

import java.awt.Dimension;
import java.util.List;

import thingy.Bloczek;
import thingy.FuncBlock;

public class Lamp extends FuncBlock {

	public Lamp(Dimension cSize, List<Bloczek> list) {
		super(cSize, list, 1, 0);
		setName("Lamp");
		// TODO Auto-generated constructor stub
		func();
	}
	
	public void func(){
		Boolean isOn = in.get(0).getState();
		if(isOn){
			setImage("img/on.png");
		}
		else{
			setImage("img/off.png");
		}
	}

}
