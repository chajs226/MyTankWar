package cjs.tankwar.component;

import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.util.Random;

import cjs.tankwar.component.Drawable;
import cjs.tankwar.module.MainWindow;

/* Serializable 인터페이스를 상속받으면 직렬화가 가능다. 직렬화란 자바 class 객체를 바이트 스트림
 * 형태로 만든다는 것인데, JVM메모리에서만 상주되어 있는 데이터를 외부 자바시스템에서도 쓸수있도록 바이트 
 * 형태로 변환하는 것이다.
 */

public abstract class GameComponent implements Drawable, Serializable {

	protected static final Random random = new Random();
	
	protected int x = 130;
	protected int y = 130;
	protected int fact;
	protected boolean alive = true;
	
	public int getFact() {
		return fact;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void abolish() {
		alive = false;
	}
	
	protected GameComponent() {
		
	}
	
	protected GameComponent(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	//컴포넌트사이의 거리를 측정 (피타고라스의 정리를 이용)	
	//제곱을 안하고 실행했을 때, 컴포넌트간에 이상하게 움직이는 현상이 없었음 왜일까..
	public double distance(GameComponent g) {
		if (g== null)
			return 1000000000.0;
		return Math.sqrt(Math.pow(g.x - x, 2) + Math.pow(g.y - y, 2));
	}
	
    public double distance(Point g) {
        if (g == null)
            return 100000000000.0;
        return Math.sqrt(Math.pow(g.x - x, 2) + Math.pow(g.y - y, 2));
    }
	
    public boolean inScreen() {
    	return !(x < -10 || x > MainWindow.WINDOW_WIDTH + 10 || y < -10 || y > MainWindow.WINDOW_HEIGHT + 10);
    	
    }

}
