package cern.accsoft.steering.aloha.gui.icons;

import javax.swing.ImageIcon;

public enum Icon {
	
	MAIN("hawaii-dancer-icon.gif"),
	FILE_OPEN("file-open.png"),
	EXIT("exit.gif"),
	SAVE("save.png"),
	MADX("madx.png"),
	SELECT_RANGE("tool.png"),
	VIEW_DATA("barchart.png"),
	SINE_FIT("linechart.png"),
	CALC_ONCE("small_playstop2.gif"),
	CALC_ONCE_DISABLED("small_playstop_d.gif"),
	SPLASH("aloha-splash.jpg"),
	ABOUT("hawaii-sunset-left.jpg");
	
	private final static String PATH_PREFIX = "data/";
	
	private String name = null;
	
	private Icon(String name) {
		this.name = name;
	}

	public ImageIcon getImageIcon() {
		return new ImageIcon(Icon.class.getResource(PATH_PREFIX + name));
	}
}
