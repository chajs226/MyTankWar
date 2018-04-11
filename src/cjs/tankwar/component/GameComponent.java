package cjs.tankwar.component;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.Random;

import cjs.tankwar.component.Drawable;

/* Serializable 인터페이스를 상속받으면 직렬화가 가능다. 직렬화란 자바 class 객체를 바이트 스트림
 * 형태로 만든다는 것인데, JVM메모리에서만 상주되어 있는 데이터를 외부 자바시스템에서도 쓸수있도록 바이트 
 * 형태로 변환하는 것이다.
 */

public class GameComponent implements Drawable, Serializable {

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
	
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}

}
