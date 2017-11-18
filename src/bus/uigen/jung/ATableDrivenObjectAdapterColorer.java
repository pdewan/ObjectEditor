package bus.uigen.jung;

import java.awt.Color;
import java.awt.Paint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bus.uigen.oadapters.ObjectAdapter;

public class ATableDrivenObjectAdapterColorer extends ATableDrivenColorer<ObjectAdapter> {
	public ATableDrivenObjectAdapterColorer(JungGraphManager aJungGraphManager) {
		super(aJungGraphManager);
		// TODO Auto-generated constructor stub
	}
	public static Color VIOLET = new Color(148, 0, 211);
	public static Color INDIGO = new Color(75, 0, 130);	

	public static Color[] LEVEL_COLORS = {
		Color.MAGENTA, // levels start from 1, but just in case we go to zero
		Color.BLACK, 
		VIOLET, 
		INDIGO,
		Color.BLUE,
		Color.GREEN,
		Color.YELLOW,
		Color.ORANGE,
		Color.RED,
		Color.DARK_GRAY,
		Color.LIGHT_GRAY,
		Color.PINK,
//		Color.CYAN,
		Color.WHITE		
		};
	protected Paint defaultColor(ObjectAdapter input) {
		int aLevel = input.getLevel();
		String aPath = input.getPath();
		if (aLevel < LEVEL_COLORS.length) {
			return LEVEL_COLORS[aLevel];
		}
		return defaultColor;
	}


}
