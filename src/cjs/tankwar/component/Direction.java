package cjs.tankwar.component;

import java.io.Serializable;
import java.util.Random;

import cjs.tankwar.component.Direction;


/* enum�� ����.. 1�� UP, 3�� UP_LEFT...
 * 
 */
public enum Direction implements Serializable{
    UP(1),
    UP_LEFT(3),
    LEFT(2),
    DOWN_LEFT(6),
    DOWN(4),
    DOWN_RIGHT(12),
    RIGHT(8),
    UP_RIGHT(9),
    STOP(0);
	
	private static final Direction DIRECTIONS[] =
        { UP, UP_LEFT, LEFT, DOWN_LEFT, DOWN, DOWN_RIGHT, RIGHT, UP_RIGHT };
    private static final Direction DIRECTIONS_WITH_STOP[] =
        { UP, UP_LEFT, LEFT, DOWN_LEFT, DOWN, DOWN_RIGHT, RIGHT, UP_RIGHT, STOP };
    
    int value;
    
    private Direction(int value) {
        this.value = value;
    } 
    
    //TODO : �뵵��???
    public static Direction forDirection(int dirValue) {
        for (Direction dir : DIRECTIONS_WITH_STOP)
            if (dirValue == dir.value)
                return dir;
        return null;
    }
    
    //TODO : ȸ��.. x���� ����.. ��ȭ�Ǵ� direction�� �����ϴµ�.. ��Ȯ�� ������ ���Ŀ� Ȯ���ʿ�
    public static Direction rotate(Direction dir, int x) {
        if (dir == STOP)
            return STOP;
        int ord;
        for (ord = 0; ord < DIRECTIONS.length; ++ord)
            if (dir == DIRECTIONS[ord])
                break;
        return DIRECTIONS[(ord + x) % DIRECTIONS.length];
    }

    public static Direction rotate(Direction dir) {
        return rotate(dir, 1);
    }
    
    //TODO : ������ ���� ���� �ɱ�?
    public static Direction randomDirection() {
        return DIRECTIONS[new Random().nextInt(DIRECTIONS.length)];
    }
    
    public static Direction randomDirectionIncludingStop() {
        return DIRECTIONS_WITH_STOP[new Random().nextInt(DIRECTIONS_WITH_STOP.length)];
    }
    
    /*
     * dir.value�� ���� 1��Ʈ ������ 2���̸� len�� 1/��Ʈ2 �� ��
     * dir.value�� UP.value
     */
    //TODO : �Ʒ��� ���⿡ ���� ������ ���� Ȯ��. ��ƴ�;;
    public static double unitVectorX(Direction dir) {
        double len = 1.0;
        if (Integer.bitCount(dir.value) == 2)
            len /= Math.sqrt(2.0);
        if ((dir.value & LEFT.value) != 0)
            return -len;
        if ((dir.value & RIGHT.value) != 0)
            return len;
        return 0.0;
    }
    
    public static double unitVectorY(Direction dir) {
        double len = 1.0;
        if (Integer.bitCount(dir.value) == 2)
            len /= Math.sqrt(2.0);
        if ((dir.value & UP.value) != 0)
            return -len;
        if ((dir.value & DOWN.value) != 0)
            return len;
        return 0.0;
    }
    
    public static Direction compose(Direction base, Direction dir) {
        int b = base.value, d = dir.value;
        b |= d;
        b &= ~(d << 2);
        b &= ~(d >> 2);
        return forDirection(b);
    }
    public static Direction erase(Direction base, Direction dir) {
        int b = base.value, d = dir.value;
        b &= ~d;
        return forDirection(b);
    }
    
    public static boolean includes(Direction base, Direction dir) {
        return (base.value & dir.value) == dir.value;
    }
    public boolean includes(Direction dir) {
        return (this.value & dir.value) == dir.value;
    }
    public int dimension() {
        return Integer.bitCount(this.value);
    }
}
