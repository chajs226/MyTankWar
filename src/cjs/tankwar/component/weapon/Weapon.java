package cjs.tankwar.component.weapon;

import java.awt.*;

import cjs.tankwar.component.Automatic;
import cjs.tankwar.component.GameComponent;

public abstract class Weapon extends GameComponent implements Automatic {

	public abstract int getAttackRadius();
	public abstract boolean effect(Tank t);
	
	public Weapon() {
		
	}
}
