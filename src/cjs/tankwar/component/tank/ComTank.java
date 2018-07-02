package cjs.tankwar.component.tank;

import static java.awt.Color.*;
import static cjs.tankwar.component.Direction.*;
import java.awt.Color;
import java.awt.Graphics;

import cjs.tankwar.component.*;
import cjs.tankwar.component.Automatic;
import cjs.tankwar.module.MainWindow;

import static cjs.tankwar.component.tank.ComTank.ComTankType.*;
import static cjs.tankwar.module.PropertiesManager.*;


public class ComTank extends Tank implements Automatic {

    static final int[] BOMBER_EXPLOSION_R = 
        { 25, 32, 40, 48, 55, 70, 71, 70, 48, 40 };
	static final int lockSpeed = 30;
	static boolean existOrange = false;
	public final ComTankType tag;
	protected int step = 3;

	//TODO : 액션 전략
	//protected TankActionStrategy strategy = new NormalActionStrategy(this, 60);
	
	//생성자
	public ComTank(int _x, int _y, Color _clr1, Color _clr2, int _fact) {
		x = _x;
		y = _y;
        clr1 = _clr1;
        clr2 = _clr2;
        fact = _fact;
        tag = null;
	}

    public ComTank(Color _clr1, Color _clr2, int _fact, int _HP, int _power,
            int _step) {
        clr1 = _clr1;
        clr2 = _clr2;
        fact = _fact;
        maxHP = HP = _HP;
        power = _power;
        step = _step;
        tag = null;
    }
    
    //ComTanke 생성자. 탱크 타입에 따라 탱크생성에 필요한 초기값을 셋팅
    public ComTank(ComTankType s) {
        this(s, random.nextInt(4));
    }
    
       
    public ComTank(ComTankType s, int d) {
    	//ComTank가 friend, orange가 아니면.. 3/1000 확률로 SOY_SAUCE로 만든다.
    	//ComTank가 friend이고.. orange가 없으면 3/1000확률로 orange로 만든다.
    	 if (random.nextInt(1000) < 3 && !s.equals(FRIEND) && !s.equals(IAMANORANGE))
             s = SOY_SAUCE;
         if (random.nextInt(1000) < 3 && s.equals(FRIEND) && !existOrange)
             s = IAMANORANGE;
         
    	tag = s;
    	
    	//ComTank죽은 수(1500으로 나눈 나머지)를 0.6승을 함. mul값이 커질수록 comtank가 생겨날때 HP값이 커짐
    	//즉 많이 죽일수록 새로 생기는 comtank가 더 강해짐
    	double mul = 1 + Math.pow(((double)MainWindow.getKilled() / 1500.0), 0.6);
    	
        if (s.equals(ENEMY)) {
            clr1 = magenta;
            clr2 = red;
            fact = 1;
            maxHP = HP = (int)(250 * mul);
            power = 40;
            //TODO: strategy
            //strategy = new NormalActionStrategy(this, 60);
            if (random.nextInt(1000) < 3)
                speak("I'm vegetable. T_T ");
        }
        if (s.equals(IAMANORANGE)) {
            clr1 = yellow;
            clr2 = orange;
            clr3 = green;
            fact = 0;
            maxHP = HP = 90;
            step = 7;
            power = 30;
            //strategy = new ChaseActionStrategy(this, 5, false, -1, 10, 120);
            speak("I am an Orange !");
            existOrange = true; 
        }
        if (s.equals(FRIEND)) {
            clr1 = cyan;
            clr2 = blue;
            fact = 0;
            maxHP = HP = (int)(400 * mul);
            power = 70;
            //strategy = new NormalActionStrategy(this, 40);
        }
        if (s.equals(FAKE_PLAYER)) {
            clr1 = green;
            clr2 = grayGreen;
            fact = 0;
            maxHP = HP = 2000;
            //strategy = new NormalActionStrategy(this, 60);
        }
        if (s.equals(SHOOTER)) {
            clr1 = lemon;
            clr2 = blueGray;
            fact = 1;
            maxHP = HP = (int)(400 * mul);
            step = 7;
            power = 30;
            //strategy = new ChaseActionStrategy(this, 7, false, -1, 80, 120);
        }
        if (s.equals(SNIPER)) {
            clr1 = purple;
            clr2 = darkPurple;
            fact = 1;
            power = 350;
            maxHP = HP = (int)(500 * mul);
            step = 1;
            //strategy = new SnipeActionStrategy(this, 30);
        }
        if (s.equals(BOMBER)) {
            clr1 = orange;
            clr2 = lightGray;
            fact = 1;
            maxHP = HP = (int)(1000 * mul);
            step = 2;
            //strategy = new BomberActionStrategy(this, 90, false, -1, 100, 0);
        }
        if (s.equals(ENGINEER)) {
            clr1 = brown;
            clr2 = darkBrown;
            fact = 1;
            step++;
            power = 20;
            maxHP = HP = (int)(500 * mul);
            //strategy = new EngineerActionStrategy(this, 55, false, -1, 70, 120);
            if (random.nextInt(1000) < 100)
                speak("Boom!!!");
        }
        if (s.equals(SOY_SAUCE)) {
            clr1 = pink;
            clr2 = lightGray;
            fact = 1;
            step = 10;
            power = 1;
            maxHP = HP = (int)(80 * mul);
            ignoreMoveLimit = true;
            //strategy = new NormalActionStrategy(this, 999999999, true, 0);
            if (random.nextInt(1000) < 700)
                speak("I just wanna take some soy sauce...");
        }
        //위치값 셋팅 switch 에의해 x, y값이 셋팅됨에 따라 탱크의 시작 위치가 랜덤하게 바뀜.
        //그렇게 안하면 왼쪽 상단의 특정위치에서만 생성됨.
        switch (d % 4) {
	        case 0:
	            x = random.nextInt(760) + 20;
	            y = random.nextInt(30) - 50;
	            setMoveLimit(DOWN, 30);
	            break;
	        case 1:
	            x = random.nextInt(30) - 50;
	            y = random.nextInt(540) + 50;
	            setMoveLimit(RIGHT, 30);
	            break;
	        case 2:
	            x = random.nextInt(760) + 20;
	            y = random.nextInt(30) + 650;
	            setMoveLimit(UP, 30);
	            break;
	        case 3:
	            x = random.nextInt(30) + 850;
	            y = random.nextInt(540) + 50;
	            setMoveLimit(LEFT, 30);
	            break;
	            }
    if (tag == IAMANORANGE)
        moveTimeLimit += 30;
        
    }
    
    //폭파처리
    public void explode() {    
    	super.explode();
    	//SOY_SAUCE는 죽을 떄, 33아이템을 떨어뜨림
    	if (tag == SOY_SAUCE)
    		dropItem(33);
    	else
    		dropItem(2);
    	
    	//TODO : BOMBER가 뭔지 ?
    	if (tag == BOMBER) {
    		//TODO : 메인윈도우의 explosions는 weapon 리스트. 메인윈도우의 해당리스트에 
    		// add 작업이 일어나기 떄문에.. sync처리를 함.
    		/*synchronized (MainWindow.explosions) {
                MainWindow.explosions.add(new Explosion(x, y,
                        BOMBER_EXPLOSION_R, fact));
            }*/
    		//Comtank가 BOMBER일 때,, 가까이 myTank가 있으면 HP를 감소시킴
    		if (distance(MainWindow.myTank) < 90) {
                MainWindow.myTank.HP -= (int)(maxHP * 0.3);
                if (MainWindow.myTank.HP < 0)
                    MainWindow.myTank.HP = 0;
                MainWindow.myTank.makeDamage((int)(maxHP * 0.3));
            }
    		//Comtank 들도 가까이 붙어있으면 HP가 깎인다.
    		Tank t = null;
            synchronized (MainWindow.tanks) {
                for (int i = 0; i < MainWindow.tanks.size(); ++i) {
                    t = MainWindow.tanks.get(i);
                    if (t != this && distance(t) < 90)
                        t.makeDamage((int)(maxHP * 0.6));
                }
            }
            synchronized (MainWindow.friends) {
                for (int i = 0; i < MainWindow.friends.size(); ++i) {
                    t = MainWindow.friends.get(i);
                    if (t != this && distance(t) < 90)
                        t.makeDamage((int)(maxHP * 0.6));
                }
            }
    	}
    }
    
    public void move() {
    	if (moveTimeLimit > 0) {
    		--moveTimeLimit;
    		move(moveDirLimit, 4);
    	} else {
    		//step = 3
    		move(moveDir, step);
    	}
    }
    
	public void autoAct() {
		//moveTimeLimit 가 0이 아니면 true
		if (isLimited()) {
			move();
			return;
		}
		//TODO : strategy 구현
		//strategy.act();
		}

	//에너지바를 그림
    public void drawEnergyBar(Graphics g) {
        g.setColor(red);
        if (HP > 0)
            g.fillRect(x - Tank.HALF_WIDTH, y + Tank.HALF_WIDTH + 25,
                    (int)((double)Tank.HALF_WIDTH * 2 * ((double)HP / maxHP)),
                    3);
        g.setColor(black);
        g.drawRect(x - Tank.HALF_WIDTH, y + Tank.HALF_WIDTH + 25,
                Tank.HALF_WIDTH * 2, 3);
    }

    public void draw(Graphics g) {
        super.draw(g);
        //탱크가 살아있는 상태이고 에너지바가 남아있으면 에너지바를 그림
        //게임이 멈춤상태가 아니면 에너지바를 조금씩 감소시킴
        if (alive && energyBarLastTime > 0) {
            if (MainWindow.stat != MainWindow.STAT_PAUSE)
                --energyBarLastTime;
            drawEnergyBar(g);
        }
        //TODO: strategy. 구현
        //if (strategy instanceof Drawable) {
        //	((Drawable)strategy).draw(g);
        //}
    }

    /* Getters & Setters */
    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

	
	public static enum ComTankType {
		ENEMY, FRIEND, FAKE_PLAYER, SHOOTER, IAMANORANGE, SNIPER, BOMBER, SOY_SAUCE, ENGINEER;
    }
	
	
}