package cjs.tankwar.component;

import java.io.Serializable;
import java.util.Random;

import cjs.tankwar.component.Direction;


/* enum의 구현.. 1은 UP, 3은 UP_LEFT...
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
    
  //TODO : 용도는???
    public static Direction forDirection(int dirValue) {
        for (Direction dir : DIRECTIONS_WITH_STOP)
            if (dirValue == dir.value)
                return dir;
        return null;
    }
    
    //TODO : 회전.. x값에 따라.. 변화되는 direction을 리턴하는데.. 정확한 원리는 추후에 확인필요
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
    
  //TODO : 랜덤은 언제 쓰는 걸까?
    public static Direction randomDirection() {
        return DIRECTIONS[new Random().nextInt(DIRECTIONS.length)];
    }
    
    public static Direction randomDirectionIncludingStop() {
        return DIRECTIONS_WITH_STOP[new Random().nextInt(DIRECTIONS_WITH_STOP.length)];
    }
    
    /*
     * dir.value의 값의 1비트 갯수가 2개이면 len은 1/루트2 의 몫
     * dir.value가 UP.value
     */
    //TODO : 아래의 방향에 대한 로직은 추후 확인. 어렵다;;
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
