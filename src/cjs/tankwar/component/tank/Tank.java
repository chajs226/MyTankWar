package cjs.tankwar.component.tank;

import static java.awt.Color.*;
import static xz.tankwar.component.Direction.STOP;
import static xz.tankwar.component.Direction.erase;
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
    
    //�����ӹ���, ���ù��� ������ �ʱⰪ enum STOP���� ������
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
        if (isLimited()) //movetime�� ����Ǹ� �������� ���� ���ϵ��� ��
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
    
    public boolean isLimited() {
        return (moveTimeLimit != 0);
    }
    
    public void setCannonDir(Direction cannonDir) {
        if (cannonDir != STOP)
            this.cannonDir = cannonDir;
    } 

    public void resetMoveDir(Direction dir) {
        if (isLimited())
            return;
        moveDir = erase(moveDir, dir);
        if ((shootDir == null || shootDir == STOP) && moveDir != STOP)
            cannonDir = moveDir;
    }
    
    public void resetShootDir(Direction dir) {
        if (isLimited())
            return;
        if (dir == null) {
            shootDir = STOP;
            return;
        }
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
    
    public void setMoveLimit(Direction dir, int time) {
        moveDirLimit = dir;
        moveTimeLimit = time;
    }
    
    public int getPower() {
        return power;
    }
    
    //Tank�� �����쿡�� ������ �� �ִ� ������ ������. �ű⸦ ����� ���� ������ ó��
    //TODO : BlockTime�� ���� �ľ��� �ȵǾ���
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
    
	//���������쿡�� player�� �̸� ǥ�ø� ���� dialog
    class Dialog implements Serializable {
    	
        protected String[] content = null;
        protected int textHeight;
        protected int textWidth = 0;
        protected Direction dir = DOWN_LEFT;
        protected int lastTime = 2000;

        //_content �Ķ���ͷ� PC������� �̸��� �Ѿ��.
        // ��Ʈ ����� ������� dialog�� ũ�⸦ ����
        public Dialog(String _content) {
            FontMetrics fm = ConsoleWindow.console.getFontMetrics(BUBBLE_FONT);
            content = _content.split("\n");
            textHeight = content.length * BUBBLE_FONT.getSize()
                    + DIALOG_BORDER_WIDTH * 2;
            for (int i = 0; i < content.length; ++i)
                textWidth = Math.max(textWidth, fm.stringWidth(content[i]));
            textWidth += DIALOG_BORDER_WIDTH * 2;
    
        }

        //dialog�� ��ġ�� ����. ��ũ ��ġ�� ���� dialog�� ��ġ�� ��ȭ��Ŵ
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

        //dialog�� �簢�� �ڽ��� 3�������� ��ġ�� 2���� �迭�� ����. ���� ä��� draw ������ ��µ� ����.
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
        
        //dialog �׸��� �Լ�
        public void draw(Graphics g) {
            if (lastTime > 0 && MainWindow.stat != MainWindow.STAT_PAUSE)
                --lastTime;
            if (lastTime == 0)
                Tank.this.dialog = null;
            refreshDirection();
            g.setColor(DIALOG_BACKGROUND_COLOR);
            Point p = locateRect();
            
            //dialog �ڽ��� ������ġ���� �ڽ�ũ�⸸ŭ setColor�� �����Ѵ�.
            g.fillRoundRect(p.x, p.y, textWidth, textHeight,
                    DIALOG_BORDER_WIDTH, DIALOG_BORDER_WIDTH);
            
            //dialog�ڽ��ȿ� �� ������ ��ġ�� ���� ����
            g.fillPolygon(getArrow());
            g.setColor(DIALOG_FOREGROUND_COLOR);            
            g.setFont(BUBBLE_FONT);
            
            for (int i = 0; i < content.length; ++i)
                g.drawString(content[i], p.x + DIALOG_BORDER_WIDTH,
                        p.y + DIALOG_BORDER_WIDTH + BUBBLE_FONT.getSize() * (i + 1));
        }
    }
}
