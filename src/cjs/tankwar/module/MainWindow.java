package cjs.tankwar.module;


import static cjs.tankwar.module.PropertiesManager.*;
import static xz.tankwar.component.tank.ComTank.ComTankType.ENEMY;
import static xz.tankwar.component.tank.ComTank.ComTankType.FAKE_PLAYER;
import static xz.tankwar.component.tank.ComTank.ComTankType.FRIEND;

import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Console;
import java.util.List;
import java.util.Random;

import xz.tankwar.component.tank.ComTank;
import xz.tankwar.module.MainWindow.MainKeyAdapter;



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
			
        setTitle(TITLE + "   " + VERSION);
        setBounds(X, Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        enableInputMethods(false);
        
        //메인 윈도우 창에 대한 이벤트를 받는 리스너
        addWindowListener(new MainWindowAdapter());
        //TODO: 키보드 input 키에 대한 리스너 등록
        //addKeyListener(new MainKeyAdapter());
        
        //TODO: gameRestart. 게임 속 캐릭터 등의 객체를 clear하고 재생성
        //gameRestart();
        
        //게임상태값을 STAT_START로 설정
        stat = STAT_START;
        //TODO: friend와 com 탱크의 객체를 생성해서 리스트에 추가한다.
        // 이작업은 스레드간의 동기화 처리를 해서 서로 간섭되지 않도록 한다. 
        //게임 시작을 위한 생성자 부분에서 멀티스레드 처리가 필요한지는 의문이다.
//        synchronized (friends) {
//            friends.add(new ComTank(FAKE_PLAYER, 2));
//            friends.add(new ComTank(FRIEND));
//            friends.add(new ComTank(FRIEND));
//        }
//        synchronized (tanks) {
//            tanks.add(new ComTank(ENEMY));
//            tanks.add(new ComTank(ENEMY));
//            tanks.add(new ComTank(ENEMY));
//        }
        stat = STAT_START;
        
        
        //TODO: Console 화면
        //Console.setVisible(true);
        setVisible(true);
  
	}
	
    private class MainWindowAdapter extends WindowAdapter {
        
    	//윈도우 창의 X버튼 클릭 시, 화면 종료 시킴
        public void windowClosing(WindowEvent e) {
            setVisible(false);
            //Console.setVisible(false);
            System.exit(0);
        }

        public void windowDeactivated(WindowEvent arg0) {
            //if (stat == STAT_GAME)
                //gamePause();
        }

        public void windowIconified(WindowEvent arg0) {
            //Console.setState(ICONIFIED);
            //if (stat == STAT_GAME)
                //gamePause();

        }

        public void windowActivated(WindowEvent arg0) {
            super.windowActivated(arg0);
        }

        public void windowDeiconified(WindowEvent e) {
            //Console.setState(NORMAL);
        }
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
