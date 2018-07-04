package cjs.tankwar.module.tankgenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cjs.tankwar.component.tank.ComTank;

public abstract class AbstractWaveTankGenerator implements Serializable {

	protected List<ComTank> tankList = new ArrayList<ComTank>();
	protected int pointer = 0;
	
	protected AbstractWaveTankGenerator() {}
	public AbstractWaveTankGenerator(int difficulty) {}
	
	public boolean hasNext() {
		return pointer < tankList.size();
	}
	
	public ComTank next() {
		return tankList.get(pointer++);
	}
}
