
package cjs.tankwar.component.tank;

public class PlayerTank extends Tank {
    
	public int step = 6;
	
    public static final String[] SKILL_KEY =
    { "~", "1", "2", "3", "E", "F", "X", "R", "C", "Z", "V", " " };
    //public static final int SKILL_LIMIT[];
    private final int lineShootUpdate1 = 1100;
    private final int lineShootUpdate2 = 2000;
    private final int dashUpdate = 1500;
    public long skillUse[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private int sk = -1;
    protected int MP;
    protected int maxMP;
    protected int invisibleTime = 0;
    protected int energeticTime = 0;
    protected int crazyTime = 0;
    protected int accTime = 0;
    
    public boolean isInvisible() {
        return (invisibleTime != 0);
    }
}


/*
package cjs.tankwar.component.tank;


import java.awt.*;
import java.awt.Graphics;
import java.io.Serializable;

import static java.awt.Color.*;

import cjs.tankwar.component.Drawable;
import cjs.tankwar.module.MainWindow;
import cjs.tankwar.component.tank.Tank.Dialog;

import static cjs.tankwar.component.Direction.UP;
import static cjs.tankwar.module.PropertiesManager.*;

public class PlayerTank extends Tank{
	
	protected int invisibleTime = 0;
	public int step = 6;
	private int sk = -1;
	protected int energeticTime = 0;
	
	private final Skill skillList[] = {
			new Healing(),
            new SummonEnemy(),
            new SetMine(),
            new SummonFriend(),
            new OctopusCannon(),
            new Dash(),
            new Stealth(),
            new LineShoot(),
            new BigExplosion(),
            new IceAge(),
            new Earthquake(),
            new LongDistanceShoot(),
	};
	

    public PlayerTank() {
        super();
        clr1 = green;
        clr2 = grayGreen;
        maxHP = HP = 1000;
        maxMP = MP = 1000;
        x = 400;
        y = 633;
        setMoveLimit(UP, 30);
        dialog = new Dialog("Hello,\nI'm " + playerName + ".");

        if (MainWindow.stat == MainWindow.STAT_START) {
            x = -x;
            y = -y;
        }
        resetSkillUse();
    }

    public boolean isInvisible() {
        return (invisibleTime != 0);
    }
	
    public void hintNewSkill(int killed) {
        for (int i = 2; i < skillUse.length; ++i) {
            if (killed == SKILL_LIMIT[i])
                this.speak("I've learned a new skill:\n"
                        + skillList[i].toString()
                        + "!  Key <" + SKILL_KEY[i] + ">");
        }
    }
    
    public int getSK() {
        return sk;
    }
    
    void stopSkill() {
    	sk = -1;
    }
    
    
    public boolean isEnergetic() {
    	return (energeticTime != 0);
    }
    
    protected abstract class Skill implements Serializable {
    	abstract int getCost();
    	
    	abstract boolean use();
    }
    
    protected class Healing extends Skill implements Drawable {
    	
    	public String toString() {
    		return "Healing";    				
    	}
    	
    	int getCost() {
    		return 50;
    	}
    	
    	//ergeticTime이 있으면,, stop스킬을 하고.. false를 리턴 
    	boolean use() {
    		if (isEnergetic()) {
    			stopSkill();
    			return false;
    		}
    			
    		//HP를 수정 : 최대 50까지 더함 
    		modifyHP(10 * (int)Math.min(5, MainWindow.getKilled() / 200 + 1));
            return true;
    	}
    	
        public void draw(Graphics g) {
            Color c1 = clr1, c2 = clr2;
            clr1 = darkWhiteGreen;
            clr2 = lightGrayGreen;
            drawSample(g);
            clr1 = c1;
            clr2 = c2;
        }
    	
    }
}
*/