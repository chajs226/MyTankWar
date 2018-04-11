package cjs.tankwar.component.tank;

public class PlayerTank extends Tank{
	public int step = 6;
	private final Skill skillList[] = {
			new Healing();
	}

}
