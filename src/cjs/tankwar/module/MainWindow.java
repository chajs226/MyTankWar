package cjs.tankwar.module;


import static cjs.tankwar.module.PropertiesManager.*;


import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Console;
import java.util.List;
import java.util.Random;



//import cjs.tankwar.component.tank.PlayerTank;



public class MainWindow extends Frame{

	/* Display */
	public static final int X = 200, Y = 70;
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public static final int REFRESH_INTERVAL = 20;
	public static final int SHAKE_RANGE = 20;
	
    public static final int STAT_GAME = 0;
    public static final int STAT_START = 1;
    public static final int STAT_HELP = 2;
    public static final int STAT_PAUSE = 3;
    public static final int STAT_OVER = 4;
    public static final int STAT_RANKLIST = 5;
    
    /* Component Area */
    public static boolean archiveAvalible = false;
    public static boolean showConsole = true;
    
    public static int stat = 0;

    //TODO : ComTank, Weapon
    //public static PlayerTank myTank = null;
//    public static List<ComTank> tanks = null;
//    public static List<ComTank> friends = null;
//    public static List<Weapon> weapons = null;
//    public static List<Weapon> supplies = null;
//    public static List<Weapon> explosions = null;
    
    /* Threads */
    static Thread gameRunThread = null;
    //TODO : AbstractWaveTankGenerator
    //public static AbstractWaveTankGenerator waveGen = null; 
    
    /* Statistics */
    private static int killed;
    private static int waveNum;
    private static Integer killedLock = 0;
    static String gameID;
    static boolean valid = true;
    
	/* Others */
	//public static ConsoleWindow ConSole = ConsoleWindow.console;
	public static final MainWindow MW = new MainWindow();

    Random random = new Random();
    public static int freezed = 0;
    public static int shake = 0;
    static int HPreg = 2; /* 20 per second */
    static int MPreg = 3; /* 30 per second */
    
	/* Constructor */
	private MainWindow() {
			
	}
	
    /* Main */
    public static void main(String args[]) {
        if (args.length != 0)
            if (args[0].startsWith("-")) {
                if (args[0].contains("s"))
                    ENABLE_SAVE = true;
                if (args[0].contains("d"))
                    DEBUG = true;
                if (args[0].contains("c"));
              //TODO: RanklistManager
                //if (args[0].contains("r"))                	
                    //RanklistManager.clearRanklist();
            }
    }
	
	
}
