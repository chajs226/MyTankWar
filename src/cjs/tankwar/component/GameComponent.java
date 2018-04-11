package cjs.tankwar.component;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.Random;

import cjs.tankwar.component.Drawable;

/* Serializable �������̽��� ��ӹ����� ����ȭ�� ���ɴ�. ����ȭ�� �ڹ� class ��ü�� ����Ʈ ��Ʈ��
 * ���·� ����ٴ� ���ε�, JVM�޸𸮿����� ���ֵǾ� �ִ� �����͸� �ܺ� �ڹٽý��ۿ����� �����ֵ��� ����Ʈ 
 * ���·� ��ȯ�ϴ� ���̴�.
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
