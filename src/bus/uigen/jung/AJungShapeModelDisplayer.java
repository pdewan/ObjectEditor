package bus.uigen.jung;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import shapes.FlexibleShape;
import slgv.SLGView;

import bus.uigen.shapes.AnOvalModel;
import bus.uigen.shapes.OEShapeModel;

import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class AJungShapeModelDisplayer implements JungShapeModelDisplayer {
	List<OEShapeModel> shapes = new ArrayList();
	VisualizationViewer visualizationViewer;
	public static final Color DEFAULT_GRAPHICS_COLOR = Color.BLACK;
	JungGraphManager jungGraphManager;
	
	public AJungShapeModelDisplayer(JungGraphManager aJungGraphManager) {
//		System.out.println("Adding oval to jung graph");
		jungGraphManager = aJungGraphManager;
		visualizationViewer = aJungGraphManager.getVisualizationViewer();
//		OEShapeModel oeShapeModel = new AnOvalModel();
//		oeShapeModel.setBounds(new Rectangle(0, 0, 100, 100));
//		shapes.add(oeShapeModel);
	}
	
	public List<OEShapeModel> getShapes() {
		return shapes;
	}
	
//	public void setShapes( List<OEShape> newVal) {
//		shapes = newVal;
//	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
    	AffineTransform oldXform = g2d.getTransform();
    	Color oldColor = g2d.getColor();
    	g2d.setColor(DEFAULT_GRAPHICS_COLOR);
        AffineTransform lat = 
        	visualizationViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).getTransform();
        AffineTransform vat = 
        		visualizationViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).getTransform();
        AffineTransform at = new AffineTransform();
        at.concatenate(g2d.getTransform());
        at.concatenate(vat);
        at.concatenate(lat);
        Color color = g2d.getColor();
        
//        g2d.setColor(Color.BLACK);
//        g2d.setClip(0, 0, 300, 300);
        g2d.setTransform(at);
		for (OEShapeModel anOEShapeModel:shapes) {
		// need the compoenent here
		SLGView.paintShape(g, anOEShapeModel.getRemoteShape(), null);			
	}
//        g2d.drawLine(0, 0, 100, 100);
        g2d.setTransform(oldXform);
        g2d.setColor(oldColor);
//        g2d.setTransform(oldXform);
//		for (OEShapeModel anOEShapeModel:shapes) {
//			SLGView.paintShape(g, anOEShapeModel.getRemoteShape());			
//		}		
	}

	@Override
	public boolean useTransform() {
		return false;
	}

	@Override
	public void update(Observable o, Object arg) {
		jungGraphManager.renderGraph();
		
	}
	

}
