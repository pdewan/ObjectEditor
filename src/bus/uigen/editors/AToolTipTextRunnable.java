package bus.uigen.editors;

import java.awt.Point;
import java.awt.Rectangle;

import shapes.RemoteShape;
import shapes.StringModel;
import slm.ShapesList;

public class AToolTipTextRunnable  implements  ToolTipTextRunnable {
	public static long PRE_HOVER_TIME = 1000;
//	public static int TOOL_TIP_TEXT_WIDTH = 100;
//	public static int TOOL_TIP_TEXT_HEIGHT = 20;
	public static int OFFSET_X = 15;
	public static int OFFSET_Y = 15;
	public static String TOOLTIP_TEXT_KEY = "Tooltip Text";
//	RemoteShape tooltipTextShape;
	String toolTipText;
	int toolTipX;
	int toolTipY;
	ShapesList shapesList;
	Object component;
	ToltipTextMonitor toolTipTextMonitor = new AToolTipTextMonitor();
	StringModel toolTipShape;
	public AToolTipTextRunnable() {
		try {
//			toolTipShape = new StringModel(0, 0, TOOL_TIP_TEXT_WIDTH, TOOL_TIP_TEXT_HEIGHT, "");
			toolTipShape = new StringModel("", 0, 0);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public synchronized String getToolTipText() {
		return toolTipText;
	}
	
	public synchronized int getToolTipX() {
		return toolTipX;
	}
	
	public synchronized int getToolTipY() {
		return toolTipY;
	}
	
	
	public synchronized void mouseEntered(RemoteShape aComponent, String aToolTipText, Point aCursorPoint) {
		toolTipText = aToolTipText;

		try {
			if (aComponent != null) {
		toolTipX = aComponent.getX() + aComponent.getWidth() + OFFSET_X;
		toolTipY = aComponent.getY() + aComponent.getHeight() + OFFSET_Y;
//		toolTipText = "X:" + aComponent.getX() + " Y: " + aComponent.getY();

			} 
			else {
//				toolTipX = aCursorPoint.x;
//				toolTipY = aCursorPoint.y;
				toolTipX = OFFSET_X;
				toolTipY = OFFSET_Y;
//				toolTipText = "X:" + toolTipX + " Y: " + toolTipY;

			}
		component = aComponent;
		toolTipTextMonitor.notifyMouseEntered();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public synchronized void mouseExited(RemoteShape aComponent, Point aMousePoint) {
//		System.out.println("mouse exited: " + aComponent);

		if (component != aComponent)
			return;
		
		shapesList.remove(TOOLTIP_TEXT_KEY);
		toolTipText = null;
	}
	
	public  RemoteShape getTooltipTextShape (int x, int y, String aTooltipText) {
		try {
			toolTipShape.setX(x);
			toolTipShape.setY(y);
//			toolTipShape.setWidth(width);
//			toolTipShape.setHeight(height);
			toolTipShape.setText(aTooltipText);
			return toolTipShape;
//			return new StringModel(x, y, width, height, aTooltipText);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	public  void setTooltipTextShape (int x, int y,  String aTooltipText) {
		try {
			toolTipShape.setX(x);
			toolTipShape.setY(y);
			toolTipShape.setText(aTooltipText);
//			toolTipShape.setBounds(x, y, width, height);
//			toolTipShape.setText(aTooltipText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	
	@Override
	public void run() {
		while (true) {
			try {
//				System.out.println("Tool Tip Thread Waits");
				toolTipTextMonitor.waitForMouseEntered();
				Thread.sleep(PRE_HOVER_TIME);
				if (shapesList != null && toolTipText != null && toolTipText != "") {
					setTooltipTextShape(getToolTipX(), getToolTipY(), toolTipText);
					
//					RemoteShape shape = getTooltipTextShape(getToolTipX(), getToolTipY(), TOOL_TIP_TEXT_WIDTH, TOOL_TIP_TEXT_HEIGHT, toolTipText);
//					RemoteShape shape = getTooltipTextShape(getToolTipX(), getToolTipY(), TOOL_TIP_TEXT_WIDTH, TOOL_TIP_TEXT_HEIGHT, toolTipText);

//					shapesList.put(TOOLTIP_TEXT_KEY, shape );
					shapesList.put(TOOLTIP_TEXT_KEY, toolTipShape );

				}
				
				
			} catch (Exception e) {
				
			}
		}
		
	}

	@Override
	public ShapesList getShapesList() {
		// TODO Auto-generated method stub
		return shapesList;
	}

	@Override
	public void setShapesList(ShapesList aShapesList) {
		shapesList = aShapesList;
		
	}

}
