package cjs.tankwar.module;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.io.Console;
import java.util.List;

import cjs.tankwar.component.tank.ComTank;
import cjs.tankwar.component.tank.PlayerTank;
import cjs.tankwar.component.weapon.Weapon;
import cjs.tankwar.module.MainWindow;

//import static를 하면 해당 클래스의 static 멤버를 fully qualified name 없이 사용할수있게 한다.
import static cjs.tankwar.module.ProPertiesManager.*;

public class MainWindow extends Frame{

	/* Display */
	public static final int X = 200, Y = 70;
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public static final int REFRESH_INTERVAL = 20;
	public static final int SHAKE_RANGE = 20;
	
    /* Component Area */
    public static boolean archiveAvalible = false;
    public static boolean showConsole = true;
    
    public static PlayerTank myTank = null;
    public static List<ComTank> tanks = null;
    public static List<ComTank> friends = null;
    public static List<Weapon> weapons = null;
    public static List<Weapon> supplies = null;
    public static List<Weapon> explosions = null;
    
	/* Others */
	public static ConsoleWindow ConSole = ConsoleWindow.console;
	public static final MainWindow MW = new MainWindow();

	/* Constructor */
	private MainWindow() {
		
		setTitle(TITLE + " " + VERSION);
		setBounds(X, Y, WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(false);
		enableInputMethods(false);
		
		//윈도우가 이벤트를 받을 수 있게 함.
		addWindowListener(new MainWindowAdapter());
		
		
	}
	
	/* Listeners */
	private class MainWindowAdapter extends WindowAdapter {
		
		public void windowClosing(windowEvent e) {
			setVisible(false);
			//->class 선언 Console.
			System.exit(0);
		}
		
	}
	
}
