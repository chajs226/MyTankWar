package cjs.tankwar.module.tankgenerator;

import java.util.Random;


public class WaveGeneratorManager {

	private static Random random = new Random();
	
	public static AbstractWaveTankGenerator getWaveGen(int killed) {
		int rnd = random.nextInt(100);
		int diff = 5 + killed / 100 + random.nextInt(2);
		//rnd = 100 아래의 랜덤값
		//diff 값을 통해서 탱크가 생성되는 위치가 랜덤하게 정해진다.
		
		// 3/1000의 확률로 SOY_SAUCE 탱크를 만든다.
        if (random.nextInt(1000) < 3)
            return new SoysauceWaveGenerator(diff);
        
        //killed 수가 50보다 작으면 일반적인 ENEMY 탱크를 생성한다.
        if (killed < 50) {
            return new NormalWaveGenerator(diff);
        }
        
        //killed 수가 100이하인 경우에 ENGINEER 탱크를 생성하는데, 
        //만약에 rnd 랜덤값이 85이하면 ENEMY 탱크도 생성한다.
        if (killed < 100) {
        	if (rnd < 85)
        		return new NormalWaveGenerator(diff);
        	return new EngineerWaveGenerator(diff);
        }
        
		return null;
		
	}
	
}
