package cjs.tankwar.module.tankgenerator;

import static cjs.tankwar.component.tank.ComTank.ComTankType.*;

import cjs.tankwar.module.tankgenerator.AbstractWaveTankGenerator;
import cjs.tankwar.component.tank.ComTank;

public class EngineerWaveGenerator extends AbstractWaveTankGenerator {

    public EngineerWaveGenerator(int difficulty) {
        for (int i = 0; i < difficulty / 2 + 1; ++i) {
            tankList.add(new ComTank(ENGINEER, i % 4));
        }
    }
}
