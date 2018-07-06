package cjs.tankwar.module.tankgenerator;

import static cjs.tankwar.component.tank.ComTank.ComTankType.*;

import cjs.tankwar.component.tank.ComTank;

public class NormalWaveGenerator extends AbstractWaveTankGenerator {

    public NormalWaveGenerator(int difficulty) {
        for (int i = 0; i < difficulty + 1; ++i)
            tankList.add(new ComTank(ENEMY, i % 4));
    }
}
