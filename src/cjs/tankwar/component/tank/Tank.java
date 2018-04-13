package cjs.tankwar.component.tank;

import static java.awt.Color.black;
import static java.awt.Color.gray;
import static cjs.tankwar.component.Direction.STOP;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

import cjs.tankwar.component.GameComponent;
import cjs.tankwar.component.Direction;


public class Tank extends GameComponent {

	public static final int R = 18, HALF_WIDTH = 22, STEP = 4, CANNON_R = 5,
            CANNON_LEN = 35;
    public static final int SHOOT_CD = 70;
    static final int EXPLOSION_R[] = { 15, 25, 30, 45, 35 };

    public static final int DIALOG_BORDER_WIDTH = 5;
    public static final int DIALOG_ARROW_WIDTH = 7;
    public static final Color DIALOG_BACKGROUND_COLOR = new Color(255, 255,
            255, 150);
    public static final Color DIALOG_FOREGROUND_COLOR = black;
    
    //움직임방향, 슈팅방향 등으로 초기값 enum STOP으로 설정함
    protected Direction moveDir = STOP;
    protected Direction shootDir = STOP;
    protected Direction cannonDir = STOP;
    protected int power = 50;
    protected int HP;
    protected int maxHP;
    public long blockTime = 0;
    protected boolean ignoreMoveLimit = false;
    protected int energyBarLastTime = 0;
    protected Color clr1 = gray;
    protected Color clr2 = black;
    protected Color clr3 = null;
    protected Dialog dialog = null;

    protected Direction moveDirLimit = STOP;
    protected int moveTimeLimit = 0;

	
    class Dialog implements Serializable {
    	
    }
}
