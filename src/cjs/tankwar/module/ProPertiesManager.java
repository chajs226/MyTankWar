package cjs.tankwar.module;

import java.awt.Font;
import java.util.Properties;

public class ProPertiesManager {

	public static final String TITLE = "MyTankWar by CJS";
	public static final String VERSION = "Version 1.0.0";
	public static final String CONSOLE_TITLE = "MyTankWar Console";
	
	public static final Properties PROP = System.getProperties();
	public static final String OPERATING_SYSTEM = PROP.getProperty("os.name").toUpperCase();
	
	
    public static final int MAX_NAME_LENGTH = 15;
    public static String playerName = null;
    
    public static final String SAVE_FILE_NAME;
    public static final String RANKLIST_FILE_NAME;
    
    public static final Font SUBTITLE_FONT;
    public static final Font TITLE_FONT;
    public static final Font ARROW_FONT;
    public static final Font DIALOG_FONT;
    public static final Font HELP_FONT;
    public static final Font CONSOLE_FONT;
    public static final Font NAME_FONT;
    public static final Font BUBBLE_FONT;
	
	static {
		
        /* Player Name */
        playerName = PROP.getProperty("user.name");
            playerName = cut(playerName);

		/* Fonts */
		if (OPERATING_SYSTEM.contains("LINUX")) {
            NAME_FONT = new Font("Ubuntu Mono", Font.PLAIN, 24);
            SUBTITLE_FONT = new Font("Ubuntu Mono", Font.BOLD, 70);
            TITLE_FONT = new Font("Ubuntu Mono", Font.BOLD, 140);
            ARROW_FONT = new Font("Dialog", Font.PLAIN, 24);
            DIALOG_FONT = new Font("Dialog", Font.PLAIN, 12);
            HELP_FONT = new Font("Ubuntu Mono", Font.PLAIN, 24);
            CONSOLE_FONT = new Font("Ubuntu Mono", Font.PLAIN, 14);
            BUBBLE_FONT = new Font("Ubuntu Mono", Font.PLAIN, 14);
            SAVE_FILE_NAME = "./.tankwar_save.sav";
            RANKLIST_FILE_NAME = "./.tankwar_ranklist.bin";
        } else {
            if (OPERATING_SYSTEM.contains("WINDOWS")) {
                NAME_FONT = new Font("SansSerif", Font.PLAIN, 20);
                SUBTITLE_FONT = new Font("Courier New", Font.BOLD, 70);
                TITLE_FONT = new Font("Courier New", Font.BOLD, 120);
                ARROW_FONT = new Font("Dialog", Font.PLAIN, 20);
                DIALOG_FONT = new Font("Dialog", Font.PLAIN, 12);
                HELP_FONT = new Font("Courier New", Font.BOLD, 20);
                CONSOLE_FONT = new Font("Courier New", Font.PLAIN, 13);
                BUBBLE_FONT = new Font("Monospaced", Font.PLAIN, 12);
                SAVE_FILE_NAME = "./.tankwar_save.sav";
                RANKLIST_FILE_NAME = "./.tankwar_ranklist.bin";
            } else {
                NAME_FONT = new Font("Dialog", Font.PLAIN, 20);
                SUBTITLE_FONT = new Font("Monospaced", Font.BOLD, 70);
                TITLE_FONT = new Font("Monospaced", Font.BOLD, 120);
                ARROW_FONT = new Font("Dialog", Font.PLAIN, 20);
                DIALOG_FONT = new Font("Dialog", Font.PLAIN, 12);
                HELP_FONT = new Font("Monospaced", Font.BOLD, 20);
                CONSOLE_FONT = new Font("Monospaced", Font.PLAIN, 12);
                BUBBLE_FONT = new Font("Monospaced", Font.PLAIN, 12);
                SAVE_FILE_NAME = "./.tankwar_save.sav";
                RANKLIST_FILE_NAME = "./.tankwar_ranklist.bin";
            }
        }
	}
	
    private static String cut(String s) {
        while (s.getBytes().length > MAX_NAME_LENGTH) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
