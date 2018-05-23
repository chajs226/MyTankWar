/*
package cjs.tankwar.component.tank;

import static java.awt.Color.*;
import static xz.tankwar.component.Direction.unitVectorX;
import static xz.tankwar.component.Direction.unitVectorY;
import static xz.tankwar.module.PropertiesManager.whiteGreen;
import static cjs.tankwar.component.Direction.STOP;
import static cjs.tankwar.component.Direction.erase;
import static cjs.tankwar.component.Direction.unitVectorX;
import static cjs.tankwar.component.Direction.unitVectorY;
import static cjs.tankwar.component.tank.ComTank.ComTankType.IAMANORANGE;
import static cjs.tankwar.component.Direction.*;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.io.Serializable;

import cjs.tankwar.component.GameComponent;
import cjs.tankwar.component.tank.Tank;
import cjs.tankwar.module.ConsoleWindow;
import cjs.tankwar.module.MainWindow;
import xz.tankwar.component.supply.Accelerator;
import xz.tankwar.component.supply.HealPack;
import xz.tankwar.component.supply.MagicStone;
import cjs.tankwar.component.tank.Tank.Dialog;
import cjs.tankwar.component.weapon.Missile;
import cjs.tankwar.component.tank.ComTank;
import cjs.tankwar.component.tank.PlayerTank;
import cjs.tankwar.component.weapon.Explosion;

import static cjs.tankwar.module.ProPertiesManager.*;
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
    
    public void setMoveDir(Direction moveDir) {
    	this.moveDir = moveDir;
    }
    
    
    public void setShootDir(Direction dir) {
        if (isLimited()) //movetime이 종료되면 슛방향은 설정 못하도록 함
            return;
        if (dir == null) {
            shootDir = null;
            return;
        }
        this.shootDir = dir;
        if (shootDir == null)
            shootDir = STOP;
        if (shootDir != null && shootDir != STOP)
            this.cannonDir = shootDir;
    }
    
    //캐논 방향 설정
    public void setCannonDir(Direction cannonDir) {
        if (cannonDir != STOP)
            this.cannonDir = cannonDir;
    }
    
    //move 방향 리셋
    public void resetMoveDir(Direction dir) {
        if (isLimited())
            return;
        //move 방향을 리셋시키고
        moveDir = erase(moveDir, dir);
        //Shoot방향이 null이거나 지정되어 있지 않으면 움직임의 방향과 같도록 한다.
        if ((shootDir == null || shootDir == STOP) && moveDir != STOP)
            cannonDir = moveDir;
    }
    
    //Shoot 방향 설정
    public void resetShootDir(Direction dir) {
        //movetime이 종료되면 아무것도 안하고 리턴
    	if (isLimited())
            return;
    	//dir이 null이면 shoot방향은 stop
        if (dir == null) {
            shootDir = STOP;
            return;
        }
        //shoot방향이 null이 아니고 stop상태가 아니면..슛방향을 erase시킨 후, cannon방향도 슛방향과 같이 설정
        if (shootDir != null && shootDir != STOP)
            shootDir = erase(shootDir, dir);
        if (shootDir != null && shootDir != STOP)
            cannonDir = shootDir;
    }

    public Direction getShootDir() {
        return shootDir;
    }

    public Direction getMoveDir() {
        return moveDir;
    }

    public Direction getCannonDir() {
        return cannonDir;
    }
    
    //MoveTime 한계를 지정, MoveDir도 dir방향으로 지정
    public void setMoveLimit(Direction dir, int time) {
        moveDirLimit = dir;
        moveTimeLimit = time;
    }
    
    //moveTime 한계가 남아있으면 true를 리턴
    public boolean isLimited() {
        return (moveTimeLimit != 0);
    }
    
    public int getPower() {
        return power;
    }
    
    //Tank가 윈도우에서 움직일 수 있는 범위를 설정함. 거기를 벗어나면 죽은 것으로 처리
    //TODO : BlockTime은 뭔지 파악이 안되었음
    void moveLimit() {
        if (ignoreMoveLimit) {
            if (x < -25 || x > MainWindow.WINDOW_WIDTH + 25
                    || y < -25 || y > MainWindow.WINDOW_HEIGHT + 25)
                abolish();
            return;
        }
        if (x < 25) {
            x = 25;
            blockTime = 70;
        }
        if (y < 45) {
            y = 45;
            blockTime = 70;
        }
        if (x > MainWindow.WINDOW_WIDTH - 25) {
            x = MainWindow.WINDOW_WIDTH - 25;
            blockTime = 70;
        }
        if (y > MainWindow.WINDOW_HEIGHT - 25) {
            y = MainWindow.WINDOW_HEIGHT - 25;
            blockTime = 70;
        }
    }
    
    
    private boolean block(int x, int y) {
        int dx, dy;
        blockTime = 1;
        //이 객체가  myTank가 아니고, myTank 볼 수 있는 상태라면. (com 객체중 하나라면)
        //myTank 와 거리를 구해서 (Math.abs 절대값 구하는 함수) tank객체크기보다 더 좁혀지면
        //true 를 리턴.  block하겠다는 의미인듯
        if (MainWindow.myTank != this && !MainWindow.myTank.isInvisible()) {
            Tank t = MainWindow.myTank;
            dx = (x - t.x);
            dy = (y - t.y);
            if (Math.abs(dx) < Tank.HALF_WIDTH * 2
                    && Math.abs(dy) < Tank.HALF_WIDTH * 2) {
                return true;
            }
        }

        //2개이상의 스레드가 synchronized method 는 동시에 실행할수없다.
        //tanks 는 Computer tank.. comtank 중에서 본인이라면  continue로 스킵
        //comtank 본인이 아니면, 거리를 계산해서 block처리를함
        synchronized (MainWindow.tanks) {
            for (Tank t : MainWindow.tanks) {
                if (t == this)
                    continue;
                dx = (x - t.x);
                dy = (y - t.y);
                if (Math.abs(dx) < Tank.HALF_WIDTH * 2
                        && Math.abs(dy) < Tank.HALF_WIDTH * 2) {
                    return true;
                }
            }
        }
        synchronized (MainWindow.friends) {
            for (Tank t : MainWindow.friends) {
                if (t == this)
                    continue;
                dx = (x - t.x);
                dy = (y - t.y);
                if (Math.abs(dx) < Tank.HALF_WIDTH * 2
                        && Math.abs(dy) < Tank.HALF_WIDTH * 2) {
                    return true;
                }
            }
        }
        blockTime = 0;
        return false;
    }
    
    //TODO :  Direction 로직확인 해야 이해가능 . 기능을 모르겠음;  
    void shift(Direction dir, int step) {
    x += (int)(step * unitVectorX(dir));
    y += (int)(step * unitVectorY(dir));
    
    }

    //강제로 파라미터값으로 이동시킴. STOP상태이면 움직이지 않고 그냥 리턴함
    public void forceMove(Direction dir, int step) {
        moveDir = dir;
        if (moveDir == STOP)
            return;
        if (shootDir == STOP)
            cannonDir = moveDir;
        x += (int)(step * unitVectorX(dir));
        y += (int)(step * unitVectorY(dir));
    }

    //이동함수. 탱크가 crazy모드이거나 Invisible상태이면 forceMove 시킴.
    public void move(Direction dir, int step) {
        if (this instanceof PlayerTank
                && (((PlayerTank)this).isInvisible()
                || ((PlayerTank)this).isCrazy())) {
            forceMove(dir, step);
            if (moveTimeLimit == 0) {
                moveLimit();
            }
            return;
        }
        if (block(x, y) && !(this instanceof PlayerTank)) {
            HP = 0;
            makeDamage(1);
        }
        moveDir = dir;
        if (moveDir == STOP)
            return;
        if (shootDir == STOP)
            cannonDir = moveDir;
        int tx = x, ty = y;
        tx += (int)(step * unitVectorX(dir));
        ty += (int)(step * unitVectorY(dir));
        if (block(tx, ty))
            return;
        x = tx;
        y = ty;
        if (moveTimeLimit == 0) {
            moveLimit();
        }
    }

    public void move() {
        if (moveTimeLimit > 0) {
            --moveTimeLimit;
            move(moveDirLimit, STEP);
        } else
            move(moveDir, STEP);
    }

    public void move(Direction dir) {
        moveDir = dir;
        move();
    }
    
    //damage 발생.. 죽었으면 true를 리턴
    public void makeDamage(int dmg) {
        if (!alive)
            return;
        modifyHP(-dmg);
        if (HP == 0) {
            abolish(); //alive를 false로 assign
            explode(); 
        }
        energyBarLastTime = 100;
    }
    
    void dropItem(int prob) {
        int rnd = random.nextInt(100);
        if (rnd < prob) {
            synchronized (MainWindow.supplies) {
                MainWindow.supplies.add(new HealPack(x, y));
            }
            return;
        }
        if (rnd < prob * 2) {
            synchronized (MainWindow.supplies) {
                MainWindow.supplies.add(new MagicStone(x, y));
            }
            return;
        }
        if (rnd < prob * 3) {
            synchronized (MainWindow.supplies) {
                MainWindow.supplies.add(new Accelerator(x, y));
            }
            return;
        }

    }
    
    public void modifyHP(int dlt) {
        HP += dlt;
        if (HP > maxHP)
            HP = maxHP;
        if (HP < 0)
            HP = 0;
    }
    
    //kill 처리.. kill count를 증가시킴..
    //TODO : fact 1이 뭔지 확인 필요, explosion 은 weapon에 대한 구현 이해 필요
    void explode() {
        if (fact == 1) {        	
            MainWindow.addKilled();
            if (((ComTank)this).tag == IAMANORANGE)
                ComTank.existOrange = false;
        }
        synchronized (MainWindow.explosions) {
            MainWindow.explosions.add(new Explosion(x, y, EXPLOSION_R, fact));
        }
    }
    
    public int getHP() {
        return HP;
    }
    public int getMaxHP() {
        return maxHP;
    }
    
    public boolean shootBlocked() {
        return false;
    }
    //테스트
    
    public void speak(String s) {
        dialog = new Dialog(s);
    }
    
    //TODO : Missile (weapon 객체 이후 확인) 파라미터받는 메소드와 안받는 메소드 용도 확인    
    public void launch() {
        if (isLimited())
            return;
        if (shootBlocked())  //무조건 false인데, 무슨 의미이지?
            return;
        int nx = x, ny = y, cl = CANNON_LEN + 4;
        
        nx += (int)(cl * unitVectorX(cannonDir));
        ny += (int)(cl * unitVectorY(cannonDir));
        
        
        Missile m = new Missile(nx, ny, cannonDir, power, this.fact);
        synchronized (MainWindow.weapons) {
            MainWindow.weapons.add(m);
        }
        m.explode();
    }
    
    public void launch(Missile m) {
        if (isLimited())
            return;
        int nx = x, ny = y, cl = CANNON_LEN + 4;
        nx += (int)(cl * unitVectorX(cannonDir));
        ny += (int)(cl * unitVectorY(cannonDir));
        m.setX(nx);
        m.setY(ny);
        synchronized (MainWindow.weapons) {
            MainWindow.weapons.add(m);
        }
        m.explode();

    }
    
    //PlayerTank 가 crazy모드이면 색을 변경시킴..
    protected void drawChassis(Graphics g) {
        g.setColor(clr2);
        if (this instanceof PlayerTank && ((PlayerTank)this).getSK() != 0) {
            if (((PlayerTank)this).crazyTime > 0 && random.nextInt(100) < 50)
                g.setColor(whiteGreen);
        }
        g.fillRoundRect(x - HALF_WIDTH, y - HALF_WIDTH, HALF_WIDTH * 2,
                HALF_WIDTH * 2, 10, 10);
    }
    
    //미사일 주둥이를 그리는 함수
    //TODO : UP_RIGHT 등의 Polygon모양으로 그리는 것 확인 필요
    protected void drawCannon(Graphics g) {
        int rnd = random.nextInt(100);
        if (clr3 != null)
            g.setColor(clr3);
        else
            g.setColor(clr1);

        if (this instanceof PlayerTank && ((PlayerTank)this).getSK() != 0) {
            if (((PlayerTank)this).energeticTime > 0 && rnd < 50)
                g.setColor(whiteGreen);
        }

        switch (cannonDir) {
            case UP:
                g.fillRect(x - CANNON_R, y - CANNON_LEN,
                        CANNON_R * 2, CANNON_LEN);
                break;
            case DOWN:
                g.fillRect(x - CANNON_R, y,
                        CANNON_R * 2, CANNON_LEN);
                break;
            case LEFT:
                g.fillRect(x - CANNON_LEN, y - CANNON_R,
                        CANNON_LEN, CANNON_R * 2);
                break;
            case RIGHT:
                g.fillRect(x, y - CANNON_R,
                        CANNON_LEN, CANNON_R * 2);
                break;
            case UP_LEFT: {
                int[] xpoint = { x - 32, x - 25, x, x - 7 }, ypoint = { y - 25,
                        y - 32, y - 7, y };
                g.fillPolygon(xpoint, ypoint, 4);
                break;
            }
            case UP_RIGHT: {
                int[] xpoint = { x + 25, x + 32, x + 7, x }, ypoint = { y - 32,
                        y - 25, y, y - 7 };
                g.fillPolygon(xpoint, ypoint, 4);
                break;
            }
            case DOWN_LEFT: {
                int[] xpoint = { x - 7, x, x - 25, x - 32 }, ypoint = { y,
                        y + 7, y + 32, y + 25 };
                g.fillPolygon(xpoint, ypoint, 4);
                break;
            }
            case DOWN_RIGHT: {
                int[] xpoint = { x, x + 7, x + 32, x + 25 }, ypoint = { y + 7,
                        y, y + 25, y + 32 };
                g.fillPolygon(xpoint, ypoint, 4);
                break;
            }
        }
        g.setColor(clr1);
        if (this instanceof PlayerTank && ((PlayerTank)this).getSK() != 0) {
            if (((PlayerTank)this).energeticTime > 0 && rnd < 50)
                g.setColor(whiteGreen);
        }
        g.fillOval(x - R, y - R, R * 2, R * 2);
    }
    
    public void drawDialog(Graphics g) {
        if (dialog != null)
            dialog.draw(g);
    }

    public void draw(Graphics g) {
        drawChassis(g);
        drawCannon(g);
    }

    public void speak(String s) {
        dialog = new Dialog(s);
    }
    
	///메인윈도우에서 player의 이름 표시를 위한 dialog
    class Dialog implements Serializable {
    	
        protected String[] content = null;
        protected int textHeight;
        protected int textWidth = 0;
        protected Direction dir = DOWN_LEFT;
        protected int lastTime = 2000;

        //_content 파라미터로 PC사용자의 이름이 넘어옴.
        // 폰트 사이즈를 기반으로 dialog의 크기를 구함
        public Dialog(String _content) {
            FontMetrics fm = ConsoleWindow.console.getFontMetrics(BUBBLE_FONT);
            content = _content.split("\n");
            textHeight = content.length * BUBBLE_FONT.getSize()
                    + DIALOG_BORDER_WIDTH * 2;
            for (int i = 0; i < content.length; ++i)
                textWidth = Math.max(textWidth, fm.stringWidth(content[i]));
            textWidth += DIALOG_BORDER_WIDTH * 2;
    
        }

        //dialog의 위치값 산정. 탱크 위치에 따라 dialog의 위치도 변화시킴
        private Point locateRect() {
            int nx = x, ny = y;
            int sumOffsetX = Tank.HALF_WIDTH + DIALOG_ARROW_WIDTH + 3;
            int sumOffsetY = Tank.HALF_WIDTH + DIALOG_ARROW_WIDTH + 3;
            if (dir.includes(UP))
                ny -= sumOffsetY + textHeight;
            if (dir.includes(LEFT))
                nx -= sumOffsetX + textWidth;
            if (dir.includes(DOWN))
                ny += sumOffsetY;
            if (dir.includes(RIGHT))
                nx += sumOffsetX;
            return new Point(nx, ny);
        }

        //dialog의 사각형 박스의 3꼭지점의 위치를 2차원 배열에 넣음. 색을 채우는 draw 영역을 잡는데 사용됨.
        private Polygon getArrow() {
            Polygon pol = new Polygon();
            int px = x, py = y;
            px += unitVectorX(dir) * Math.sqrt(2.0) * (Tank.HALF_WIDTH + 5);
            py += unitVectorY(dir) * Math.sqrt(2.0) * (Tank.HALF_WIDTH + 5);
            pol.addPoint(px, py);
            px += unitVectorX(dir) * Math.sqrt(2.0) * DIALOG_ARROW_WIDTH;
            py += unitVectorY(dir) * Math.sqrt(2.0) * DIALOG_ARROW_WIDTH;
            pol.addPoint(px, py + 3);
            pol.addPoint(px, py - 3);
            return pol;
        }
        
        private void refreshDirection() {
            if (x < textWidth + 40)
                dir = compose(dir, RIGHT);
            if (y < textHeight + 60)
                dir = compose(dir, DOWN);
            if (x > MainWindow.WINDOW_WIDTH - textWidth - 40)
                dir = compose(dir, LEFT);
            if (y > MainWindow.WINDOW_HEIGHT -textHeight - 40)
                dir = compose(dir, UP);
            
        }
        
        //dialog 그리는 함수
        public void draw(Graphics g) {
            if (lastTime > 0 && MainWindow.stat != MainWindow.STAT_PAUSE)
                --lastTime;
            if (lastTime == 0)
                Tank.this.dialog = null;
            refreshDirection();
            g.setColor(DIALOG_BACKGROUND_COLOR);
            Point p = locateRect();
            
          //dialog 박스의 시작위치에서 박스크기만큼 setColor를 적용한다.
            g.fillRoundRect(p.x, p.y, textWidth, textHeight,
                    DIALOG_BORDER_WIDTH, DIALOG_BORDER_WIDTH);
            
          //dialog박스안에 들어갈 글자의 위치와 색을 설정
            g.fillPolygon(getArrow());
            g.setColor(DIALOG_FOREGROUND_COLOR);            
            g.setFont(BUBBLE_FONT);
            
            for (int i = 0; i < content.length; ++i)
                g.drawString(content[i], p.x + DIALOG_BORDER_WIDTH,
                        p.y + DIALOG_BORDER_WIDTH + BUBBLE_FONT.getSize() * (i + 1));
        }
    }
}
*/