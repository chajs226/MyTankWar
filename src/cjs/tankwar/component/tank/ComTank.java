package cjs.tankwar.component.tank;

import java.awt.Graphics;

import cjs.tankwar.component.Automatic;
import cjs.tankwar.component.tank.ComTank.ComTankType;

public class ComTank extends Tank implements Automatic {

	public final ComTankType tag;
	static boolean existOrange = false;
	
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void autoAct() {
		// TODO Auto-generated method stub
		
	}

	
    public static enum ComTankType {
        ENEMY, FRIEND, FAKE_PLAYER, SHOOTER, IAMANORANGE, SNIPER, BOMBER, SOY_SAUCE, ENGINEER;
    }
}
