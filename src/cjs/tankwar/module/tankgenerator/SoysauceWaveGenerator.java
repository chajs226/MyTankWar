package cjs.tankwar.module.tankgenerator;

import cjs.tankwar.component.tank.ComTank;
import static cjs.tankwar.component.tank.ComTank.ComTankType.*;

//TankList에 SAY_SAUCE 탱크를 추가한다.
public class SoysauceWaveGenerator extends AbstractWaveTankGenerator {

	public SoysauceWaveGenerator(int difficulty) {
		for (int i = 0; i < 4; i++) {
			tankList.add(new ComTank(SOY_SAUCE, difficulty % 4));
		}
	}
}
