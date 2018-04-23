package cjs.tankwar.component.tank;

public class PlayerTank extends Tank{
	
	protected int invisibleTime = 0;
	public int step = 6;
	private int sk = -1;
	
	private final Skill skillList[] = {
			new Healing();
	}

    public boolean isInvisible() {
        return (invisibleTime != 0);
    }
	
    public void hintNewSkill(int killed) {
        for (int i = 2; i < skillUse.length; ++i) {
            if (killed == SKILL_LIMIT[i])
                this.speak("I've learned a new skill:\n"
                        + skillList[i].toString()
                        + "!  Key <" + SKILL_KEY[i] + ">");
        }
    }
    
    public int getSK() {
        return sk;
    }
}
