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
    
    //회전.. dir값이 STOP이면 STOP을 리턴하고, 
    //STOP이 아니면 dir과 x를 더해서 8로 나눈 나머지의 DIRECTIONS배열값을 리턴한다.
    //기존의 dir방향에서 x 값이 들어왔을 때,, 배열 인덱스를 조정해서 rotate된 값을 리턴함
    public static Direction rotate(Direction dir, int x) {
        if (dir == STOP)
            return STOP;
        int ord;
        //Directions.lenght = 8
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
    // dir 방향이 주어졌을 때, X로 이동할 단위 값을 리턴함.
    // - left나 ,right라면 1또는 -1을 리턴.. 대각선일 때는 1/루트2 값을 +- 로 리턴
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
    
    // dir 방향이 주어졌을 때, Y로 이동할 단위 값을 리턴함.
    // - Up나 ,Down라면 1또는 -1을 리턴.. 대각선일 때는 1/루트2 값을 +- 로 리턴
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
    
    //TODO: 방향을 조합해서 결정하는건데.. 마지막 2개의 비트연산은 왜하는건지?
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
