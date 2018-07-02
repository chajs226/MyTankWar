package cjs.tankwar.module;


import static cjs.tankwar.module.PropertiesManager.*;
import java.awt.event.*;


import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import cjs.tankwar.component.*;
import cjs.tankwar.component.tank.*;

import static cjs.tankwar.component.Direction.*;
import static cjs.tankwar.component.tank.ComTank.ComTankType;
import static java.awt.event.KeyEvent.*;
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
    public static PlayerTank myTank = null;
    public static List<ComTank> tanks = null;
    public static List<ComTank> friends = null;
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
        addKeyListener(new MainKeyAdapter());
        
        //TODO: gameRestart. 게임 속 캐릭터 등의 객체를 clear하고 재생성
        gameRestart();
        
        //게임상태값을 STAT_START로 설정
        stat = STAT_START;
        //TODO: friend와 com 탱크의 객체를 생성해서 리스트에 추가한다.
        // 이작업은 스레드간의 동기화 처리를 해서 서로 간섭되지 않도록 한다. 
        //게임 시작을 위한 생성자 부분에서 멀티스레드 처리가 필요한지는 의문이다.
        synchronized (friends) {
            friends.add(new ComTank(FAKE_PLAYER, 2));
            friends.add(new ComTank(FRIEND));
            friends.add(new ComTank(FRIEND));
        }
        synchronized (tanks) {
            tanks.add(new ComTank(ENEMY));
            tanks.add(new ComTank(ENEMY));
            tanks.add(new ComTank(ENEMY));
        }
        stat = STAT_START;
        
        //GameRunThread 생성
        newThreads();
        //GameRunThread.run 실행
        startThreads();
        
        //TODO: Console 화면
        //Console.setVisible(true);
        setVisible(true);
  
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
    
    /* Game Control Methods */
    public static void addKilled() {
        if (stat != STAT_GAME)
            return;
        synchronized (killedLock) {
            ++killed;
            //Tank를 1000개 죽였을 때의 처리
            //if (killed == 1000)
                //myTank.speak("Thousand Killed.");
            //myTank.hintNewSkill(killed);
        }
    }
    
    public static int getKilled() {
    	return killed;
    }
    
    public static void setKilled(int k) {
    	killed = k;
    }
    
    //TODO : 우선 ComTank만 생성
    public static void gameRestart() {
    	valid = !DEBUG;
    	tanks = new ArrayList<ComTank>();
    	friends = new ArrayList<ComTank>();
    	stat = STAT_GAME;
    	freezed = 0;
    	gameID = VERSION +
    			playerName + "," +
    			System.currentTimeMillis() + "," +
    			myTank.hashCode();
    	if (!DEBUG) {
    		killed = 0;
    		waveNum = 0;
    	}
    }
    
	
    private void newThreads() {
        gameRunThread = new GameRunThread();

    }
    
    
    //gameRunThread.start 함수를 호출하면 GameRunThread 클래스의 run 함수가 호출된다. 
    //(Thread 클래스의 자식 클래스) 
    private void startThreads() {
        gameRunThread.start();
    }
	
    private class GameRunThread extends Thread {
        private long threadStartTime = 0;
        private Long millisRefreshTime = 0l;
        private Long millisRefreshInterval = 0l;
        private long loopCount = 0L;
    	
        public long getMillisRefreshInterval() {
            return millisRefreshInterval;
        }
        
        //Automatic을 상속받는 모든 객체들.. tank, weapon 등이 죽어있으면.. 리스트에서 지우고
        //살아있으면 각 객체에서 구현한 autoAct를 실행한다. 각 객체의 특징에 맞게 AutoAct를 위한 구현이 되어 있다
        private void actAll(List c) {
            for (int i = 0; i < c.size(); ++i) {
                if (!((Automatic)(c.get(i))).isAlive())
                    c.remove(i);
                else
                    ((Automatic)(c.get(i))).autoAct();
            }
        }
        
        public void run() {
        	threadStartTime = System.currentTimeMillis();
        	setPriority(MAX_PRIORITY);
        	while (MainWindow.gameRunThread == this) {
        		//아래의 로직을 계속 돌리는데,, refresh 인터벌시간(20 ms) 내에 한번 돌렸으면 수행을 안하고,, 
        		//그 시간이 넘어갔으면 다시 수행한다.
        		if (threadStartTime + loopCount * REFRESH_INTERVAL < System.currentTimeMillis()) {
        			++loopCount;
        			millisRefreshInterval = System.currentTimeMillis() - millisRefreshTime;
        			millisRefreshTime = System.currentTimeMillis();
        			//Game Run
        			//TODO: 
        			// 1. myTank 를 움직인다.
        			// 2. firends, tanks, supplies, weapos, explosions 를 act한다.
        			// 3. loopCount 5번째마다, HP, MP, useSkill
        			// 4. MP값이나 여러값을 고려해서,, skill 을 stop처리. 혹은 skill use 처리하는듯
        			// 5. mytank 슛방향이 널이 아니면 launch시킴 (미사일을 쏠 수 있는 상태이면 미사일을 발사함.)
        			// 6. 게임이 시작상태이고, looCount를 7번 이후, 50번마다.. com 탱크를 생성한다. 
        		}
        	}
        	
        }
        
        
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
    
    private class MainKeyAdapter extends KeyAdapter {
    	
    	//key가 눌러지면.. System.currentTimeMillis(); 값이 assign된다. 값을 비교함으로 해서,
    	//최근에 눌러진 값에 따라 dir 방향이 정해진다.
        private long pressedW = 0;
        private long pressedA = 0;
        private long pressedS = 0;
        private long pressedD = 0;
        private long pressedQ = 0;
        private long pressedUp = 0;
        private long pressedDown = 0;
        private long pressedLeft = 0;
        private long pressedRight = 0;
        
        //키값 변수가 가지고 있는 값에 따라서 방향을 재설정한다.
        private void locateMoveDirection() {
            Direction dir = STOP;
            if (pressedUp > pressedDown)
                dir = compose(dir, UP);
            if (pressedUp < pressedDown)
                dir = compose(dir, DOWN);
            if (pressedLeft > pressedRight)
                dir = compose(dir, LEFT);
            if (pressedLeft < pressedRight)
                dir = compose(dir, RIGHT);
            //TODO:mytank 방향변경
            //myTank.setMoveDir(dir);
        }
        
        //키값 변수가 가지고 있는 값에 따라서 슛 방향을 재설정한다.
        private void locateShootDirection() {
            if (pressedQ > pressedW && pressedQ > pressedA
                    && pressedQ > pressedS && pressedQ > pressedD) {
                //TODO: myTank 슛 방향 변경
            	//myTank.setShootDir(null);
                //myTank.setCannonDir(myTank.getMoveDir());
                return;
            }
            Direction dir = STOP;
            if (pressedW > pressedS)
                dir = compose(dir, UP);
            if (pressedW < pressedS)
                dir = compose(dir, DOWN);
            if (pressedA > pressedD)
                dir = compose(dir, LEFT);
            if (pressedA < pressedD)
                dir = compose(dir, RIGHT);
            //TODO: myTank 슛 방향 변경
            //myTank.setShootDir(dir);
        }
        
        private void resetKey() {
            pressedW = 0;
            pressedA = 0;
            pressedS = 0;
            pressedD = 0;
            pressedQ = 0;
            pressedUp = 0;
            pressedDown = 0;
            pressedLeft = 0;
            pressedRight = 0;
          //TODO: myTank 방향 Reset
            //myTank.setShootDir(STOP);
            //myTank.setMoveDir(STOP);
        }
        
        private void helpingKeyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case VK_SPACE:
                case VK_ENTER:
                case VK_F1:
                    stat = STAT_GAME;
                    resetKey();
                    gameResume();
                    break;
                case VK_F4:
                    showConsole = !showConsole;
                    //TODO:ConsoleWindow 구현
                    //ConsoleWindow.console.setVisible(showConsole);
                    break;
            }

        }
        
        private void gameKeyPressed(KeyEvent e) {
        	//ConsoleWindow.println("" + e.getKeyCode());
            switch (e.getKeyCode()) {
                case VK_F1: /* Help */
                    gamePause();
                    repaint();
                    stat = STAT_HELP;
                    break;
                case VK_F2: /* Restart */
                    //gameRestart();
                    break;
                case VK_F4:
                    showConsole = !showConsole;
                    //TODO :
                    //ConsoleWindow.console.setVisible(showConsole);
                    break;
                case VK_ESCAPE:
                case VK_P:
                    gamePause();
                    break;
                case VK_UP:
                    pressedUp = System.currentTimeMillis();
                    break;
                case VK_DOWN:
                    pressedDown = System.currentTimeMillis();
                    break;
                case VK_LEFT:
                    pressedLeft = System.currentTimeMillis();
                    break;
                case VK_RIGHT:
                    pressedRight = System.currentTimeMillis();
                    break;
                case VK_W:
                    pressedW = System.currentTimeMillis();
                    break;
                case VK_S:
                    pressedS = System.currentTimeMillis();
                    break;
                case VK_A:
                    pressedA = System.currentTimeMillis();
                    break;
                case VK_D:
                    pressedD = System.currentTimeMillis();
                    break;
                case VK_Q:
                    pressedQ = System.currentTimeMillis();
                    break;
                case VK_BACK_QUOTE: /* Healing */
                    //TODO
                	//myTank.setSK(0);
                    break;
                case VK_1: /* Summon Enemy */
                	//TODO
                    //myTank.setSK(1);
                    break;
                case VK_2: /* Set Mine */
                	//TODO
                	//myTank.setSK(2);
                    break;
                case VK_3: /* Summon Friend */
                	//TODO
                	//myTank.setSK(3);
                    break;
                case VK_E: /* All Directions Shoot */
                	//TODO
                	//myTank.setSK(4);
                    break;
                case VK_F: /* Dash */
                	//TODO
                	//myTank.setSK(5);
                    break;
                case VK_X: /* Become Invisible */
                	//TODO
                	//myTank.setSK(6);
                    break;
                case VK_R: /* Line Shoot */
                	//TODO
                	//myTank.setSK(7);
                    break;
                case VK_C: /* Big Explosion */
                	//TODO
                	//myTank.setSK(8);
                    break;
                case VK_Z: /* Ice Age */
                	//TODO
                	//myTank.setSK(9);
                    break;
                case VK_V: /* Earthquake */
                	//TODO
                	//myTank.setSK(10);
                    break;
                case VK_SPACE: /* Long Distance Shoot */
                    if (DEBUG)
                    	//TODO
                    	//myTank.setSK(11);
                    break;
                case VK_F11:
                    if (ENABLE_SAVE)
                    	//TODO
                    	//ArchiveManager.saveGame();
                    break;
                case VK_F12:
                    if (ENABLE_SAVE)
                    	//TODO
                    	//ArchiveManager.loadGame();
                    break;
                default:
                    if (DEBUG)
                    	//TODO
                    	//debugKeyPressed(e);
                    break;
            }
            locateMoveDirection();
            locateShootDirection();

        }
        
        private void startingKeyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case VK_F1:
                    stat = STAT_HELP;
                    break;
                case VK_F2: /* Restart */
                    stat = STAT_GAME;
                    resetKey();
                    //TODO : 게임 restart 처리
                    //gameRestart();
                    break;
                case VK_F4:
                    showConsole = !showConsole;
                    //TODO : 콘솔윈도우 구현
                    //ConsoleWindow.console.setVisible(showConsole);
                    break;
                case VK_ENTER:
                    stat = STAT_HELP;
                    //TODO : 게임리스타트
                    //gameRestart();
                    stat = STAT_HELP;
                    break;
                case VK_F12:
                    stat = STAT_GAME;
                    //TODO : Archive 기록 관련 구현
                    //ArchiveManager.loadGame();
                    break;
                case VK_F3:
                    requestPlayerName();
                    break;
            }
        }
        
        private void pausingKeyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case VK_ENTER:
                case VK_ESCAPE:
                case VK_P:
                    resetKey();
                    gameResume();
                    break;
                case VK_F4:
                    showConsole = !showConsole;
                    //TODO:콘솔구현
                    //ConsoleWindow.console.setVisible(showConsole);
                    break;
                case VK_F11:
                    if (ENABLE_SAVE)
                    	//TODO:기록구현
                        //ArchiveManager.saveGame();
                    break;
                case VK_F12:
                    if (ENABLE_SAVE) {
                    	//TODO:기록구현
                        //ArchiveManager.loadGame();
                        resetKey();
                        gameResume();
                    }
                    break;
            }
        }
        
        private void overKeyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case VK_ALT:
                case VK_CONTROL:
                    break;
                case VK_F4:
                    showConsole = !showConsole;
                    //ConsoleWindow.console.setVisible(showConsole);
                    break;
                case VK_ENTER:
                    if (valid)
                        //RanklistManager.insertRecord(playerName, killed, gameID);
                    stat = STAT_RANKLIST;
                    break;

            }
        }
        
        private void ranklistKeyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case VK_ALT:
                case VK_CONTROL:
                    break;
                case VK_F3:
                    requestPlayerName();
                    break;
                case VK_F4:
                    showConsole = !showConsole;
                    //ConsoleWindow.console.setVisible(showConsole);
                    break;
                case VK_ENTER:
                    stat = STAT_GAME;
                    resetKey();
                    //gameRestart();
                    break;
                default:
                    stat = STAT_GAME;
                    resetKey();
                    //gameRestart();

            }
        }
        
        //키 이벤트를 받을 때, 게임이 어떤상태인지를 판단해서 그에 따른 키 이벤트를 발생시킨다.
        public void keyPressed(KeyEvent e) {
            switch (stat) {
                case STAT_GAME:
                    gameKeyPressed(e);
                    break;
                case STAT_HELP:
                    helpingKeyPressed(e);
                    break;
                case STAT_START:
                    startingKeyPressed(e);
                    break;
                case STAT_PAUSE:
                    pausingKeyPressed(e);
                    break;
                case STAT_OVER:
                    overKeyPressed(e);
                    break;
                case STAT_RANKLIST:
                    ranklistKeyPressed(e);
                    break;
            }
        }
        
        //키를 땠을 때, 발생하는 이벤트
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case VK_UP:
                    pressedUp = 0;
                    break;
                case VK_DOWN:
                    pressedDown = 0;
                    break;
                case VK_LEFT:
                    pressedLeft = 0;
                    break;
                case VK_RIGHT:
                    pressedRight = 0;
                    break;
                case VK_W:
                    pressedW = 0;
                    break;
                case VK_S:
                    pressedS = 0;
                    break;
                case VK_A:
                    pressedA = 0;
                    break;
                case VK_D:
                    pressedD = 0;
                    break;
                case VK_Q:
                    pressedQ = 0;
                    break;
                case VK_BACK_QUOTE:
                case VK_1:
                case VK_2:
                case VK_3:
                case VK_E:
                case VK_R:
                case VK_F:
                case VK_C:
                case VK_X:
                case VK_Z:
                case VK_V:
                case VK_SPACE:
                    //myTank.setSK(-1);
                    break;
            }
            locateMoveDirection();
            locateShootDirection();
        }
        
    }
    
    //TODO : 일단 skip
    //게임 시작전 생타로 돌리고 스레드를 발생시킨다.
    void gameResume() {
        stat = STAT_GAME;
        //newThreads();
        //startThreads();
    }
    
    //게임을 멈춤.
    void gamePause() {
        if (stat != STAT_GAME)
            return;
        gameRunThread = null;
        stat = STAT_PAUSE;
        repaint();
    }
    


	
	
}
