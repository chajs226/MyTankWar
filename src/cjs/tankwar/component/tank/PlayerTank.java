package cjs.tankwar.component.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

import cjs.tankwar.component.Drawable;
import cjs.tankwar.module.MainWindow;
import static cjs.tankwar.module.PropertiesManager.*;

public class PlayerTank extends Tank{
	
	protected int invisibleTime = 0;
	public int step = 6;
	private int sk = -1;
	protected int energeticTime = 0;
	
	private final Skill skillList[] = {
			new Healing();
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
