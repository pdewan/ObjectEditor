/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 *
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 *
 * Created on May 10, 2004
 */

package bus.uigen.jung;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javafx.scene.shape.VertexFormat;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JSlider;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import bus.uigen.CompleteOEFrame;
import util.annotations.MaxValue;
import util.annotations.PreferredWidgetClass;
import util.annotations.Visible;
import util.trace.Tracer;
import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.GraphDecorator;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.MultiLayerTransformer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.VisualizationViewer.GraphMouse;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.Vertex;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel;
import edu.uci.ics.jung.visualization.subLayout.DelegatingGraphCollapser;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import edu.uci.ics.jung.visualization.util.VertexShapeFactory;

/**
 * Parameterizes Jung capabilites in a facade
 * 
 * @author Prasun Dewan
 */
public class AJungGraphManagerCustomization<VertexType, EdgeType> implements
		JungGraphManagerCustomization<VertexType, EdgeType>{
	
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	
//	Container graphContainer;
	private static final long serialVersionUID = -5345319851341875800L;
	JungShapeModelDisplayer jungShapeModelDisplayer;
//	protected CompleteOEFrame oeFrame;

//	private ObservableGraph<VertexType, EdgeType> observableGraph = null;

//	private VisualizationViewer<VertexType, EdgeType> vv = null;

//	private Layout<VertexType, EdgeType> layout = null;
//	final ScalingControl scaler = new CrossoverScalingControl();

//	Timer timer;

//	boolean done;

//	protected JButton switchLayout;
	public static final int MAX_DISTANCE_X = 400;
	public static final int MAX_DISTANCE_Y = 400;
	int distanceX = 100;
	int distanceY = 100;
//	int width = 600;
//	int height = 600;
	int edgeLength = 100;
	public static final int MAX_SCALE_VALUE = 10;
	public static final int MIN_SCALE_VALUE = 1;
	public static final int NORMAL_SCALE_VALUE = 5;
	public static final float SCALE_UNIT = 1.1f;

	int scale = NORMAL_SCALE_VALUE;

//	Graph<VertexType, EdgeType> graph;

	Transformer<VertexType, String> vertexLabelTransformer = new ToStringLabeller();
	Transformer<VertexType, String> vertexToolTipTransformer = vertexLabelTransformer;
	Transformer<VertexType, Paint> vertexPaintTransformer;

	Transformer<EdgeType, String> edgeToolTipTransformer = new ToStringLabeller();
	Transformer<EdgeType, String> edgeLabelTransformer = edgeToolTipTransformer;

//	boolean isForest;
//	boolean isRadial;

	LayoutType layoutType = LayoutType.FruchtermanReingold;
	CollapserType collapserType = CollapserType.CollapseDuplicateClasses;
//    protected GraphCollapser collapser;
//	Relaxer relaxer;

	VisualizationViewer.Paintable postRenderer;
//	Map<VertexType, List<Color>> vertexToColors = new HashMap();
//	TableDrivenColorer<VertexType> vertexFillColorer = new ATableDrivenColorer<VertexType>(
//			this);
//
//	TableDrivenColorer<VertexType> vertexDrawColorer = new ATableDrivenColorer<VertexType>(
//			this);
//
//	TableDrivenColorer<EdgeType> edgeColorer = new ATableDrivenColorer<EdgeType>(
//			this);
	// Map<VertexType, Color> vertexToColor = new HashMap();
	// Map<EdgeType, Color> edgeToColor = new HashMap();
	TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, VertexType> vertexIncludePredicate;
	TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, EdgeType> edgeIncludePredicate;

	// Transformer<VertexType, Shape> vertexTransformer

//	public AJungGraphManager() {
//
//		// isForest = anIsForest;
//		init();
//	}

	public AJungGraphManagerCustomization() {
	
		init();
	}

	@PreferredWidgetClass(JSlider.class)
	@MaxValue(MAX_DISTANCE_X)
	public int getTreeNodeDistanceX() {
		return distanceX;
	}

	public void setTreeNodeDistanceX(int newValue) {
		int oldValue = distanceX;
		this.distanceX = newValue;
		propertyChangeSupport.firePropertyChange("TreeNodeDistanceX", oldValue, newValue);
//		maybeSetTreeLayout();

	}

	@PreferredWidgetClass(JSlider.class)
	@MaxValue(MAX_DISTANCE_Y)
	public int getTreeNodeDistanceY() {
		return distanceY;
	}

	public void setTreeNodeDistanceY(int newValue) {
		int oldValue = distanceY;
		this.distanceY = newValue;
		propertyChangeSupport.firePropertyChange("TreeNodeDistanceY", oldValue, newValue);
//		maybeSetTreeLayout();
	}

	@PreferredWidgetClass(JSlider.class)
	@MaxValue(MAX_SCALE_VALUE)
	@Override
	public int getScale() {
		return scale;
	}
	@Override
	public void setScale(int newVal) {
		int oldScaleValue = scale;
//		int multiplierUnits = newVal - oldScaleValue;
//		float multiplier = multiplierUnits * SCALE_UNIT;
//		if (multiplierUnits == 0)
//			return;
//		if (multiplierUnits < 0) {
//			multiplier = 1 / -multiplier;
//		}
//		scaler.scale(vv, multiplier, vv.getCenter());
		this.scale = newVal;
		propertyChangeSupport.firePropertyChange("Scale", oldScaleValue, newVal);

	}
	@Override
	public int getEdgeLength() {
		return edgeLength;
	}
	@Override
	public void setEdgeLength(int newValue) {
		int oldValue = edgeLength;
		this.edgeLength = newValue;
		propertyChangeSupport.firePropertyChange("EdgeLength", oldValue, newValue);

	}

//	TreeLayout createTreeLayout() {
//		try {
//			TreeLayout treeLayout = new TreeLayout<VertexType, EdgeType>(
//					(Forest<VertexType, EdgeType>) graph, distanceX, distanceY);
//			return treeLayout;
//		} catch (Exception e) {
//			Tracer.error("Tree layout can be associated only with trees");
//			return null;
//		}
//
//	}
//
//	BalloonLayout createBallonLayout() {
//		try {
//			BalloonLayout balloonLayout = new BalloonLayout<VertexType, EdgeType>(
//					(Forest<VertexType, EdgeType>) graph);
//			return balloonLayout;
//		} catch (Exception e) {
//			Tracer.error("Balloon layout can be associated only with trees");
//			return null;
//		}
//
//	}
//
//	SpringLayout createSpringLayout() {
//
//		SpringLayout retVal = new SpringLayout(graph, new ConstantTransformer(
//				EDGE_LENGTH));
//		retVal.setSize(new Dimension(width, height));
//		return retVal;
//
//	}
//
//	void setSpringLayout() {
//		layout = createSpringLayout();
//		vv.getModel().setGraphLayout(layout);
//
//	}
//
//	SpringLayout createSpringLayout2() {
//		SpringLayout retVal = new SpringLayout2(graph, new ConstantTransformer(
//				EDGE_LENGTH));
//		retVal.setSize(new Dimension(width, height));
//		return retVal;
//	}
//
//	void setSpringLayout2() {
//		layout = createSpringLayout2();
//		vv.getModel().setGraphLayout(layout);
//
//	}

	// void setKKLayout() {
	// layout = createKKLayout();
	// vv.getModel().setGraphLayout(layout);
	//
	// }

//	ISOMLayout createISOMLayout() {
//		ISOMLayout retVal = new ISOMLayout(graph);
//		retVal.setSize(new Dimension(width, height));
//		return retVal;
//
//	}

	// void seISOMLayout() {
	// layout = createISOMLayout();
	// vv.getModel().setGraphLayout(layout);
	//
	// }

//	KKLayout createKKLayout() {
//		KKLayout retVal = new KKLayout(graph);
//		retVal.setSize(new Dimension(width, height));
//		return retVal;
//
//	}
//
//	void maybeSetTreeLayout() {
//		if (layout instanceof TreeLayout && !isRadial) {
//			setTreeLayout();
//		}
//	}
//
//	void setTreeLayout() {
//		layoutType = LayoutType.Tree;
//		layout = createTreeLayout();
//		vv.getModel().setGraphLayout(layout);
//
//	}

	// public int getWidth() {
	// return width;
	// }
	// public void setWidth(int width) {
	// this.width = width;
	// vv.setSize(width, height);
	// }
	// public int getHeight() {
	// return height;
	// }
	// public void setHeight(int height) {
	// this.height = height;
	// vv.setSize(width, height);
	// }
	
	@Override
	public LayoutType getLayoutType() {
		return layoutType;
	}

	@Override
	public void setLayoutType(LayoutType newVal) {
		LayoutType oldVal = layoutType;
		if (layoutType == newVal)
			return;
		this.layoutType = newVal;
		propertyChangeSupport.firePropertyChange("LayoutType", oldVal, newVal);
//		setLayout();
		

	}

//	public void setLayout() {
//		Layout<VertexType, EdgeType> newLayout = null;
//		switch (layoutType) {
//		case Tree:
//			newLayout = createTreeLayout();
//			break;
//
//		case Radial:
//			newLayout = createRadialTreeLayout();
//			break;
//		case FruchtermanReingold:
//			newLayout = createFRLayout();
//			break;
//		case Spring:
//			newLayout = createSpringLayout();
//			break;
//		case Spring2:
//			newLayout = createSpringLayout2();
//			break;
//		case KamadaKawai:
//			newLayout = createKKLayout();
//			break;
//		case MeyersSelfOrganizing:
//			newLayout = createISOMLayout();
//		}
//		if (newLayout != null) {
//			layout = newLayout;
//
//			vv.getModel().setGraphLayout(layout);
//		}
//
//	}
	@Visible(false)
	public CollapserType getCollapserType() {
		return collapserType;
	}
	@Visible(false)
	public void setCollapserType(CollapserType collapserType) {
		this.collapserType = collapserType;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#getGraphContainer()
	 */
//	@Visible(false)
//	@Override
//	public Container getGraphContainer() {
//		return graphContainer;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bus.uigen.jung.GenericJungGraph#setGraphContainer(java.awt.Container)
	 */
//	@Override
//	public void setGraphContainer(Container graphContainer) {
//		this.graphContainer = graphContainer;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#getLayout()
	 */
//	@Visible(false)
//	@Override
//	public Layout<VertexType, EdgeType> getGraphLayout() {
//		return layout;
////		return vv.getGraphLayout();
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bus.uigen.jung.GenericJungGraph#setLayout(edu.uci.ics.jung.algorithms
	 * .layout.Layout)
	 */
//	@Override
//	@Visible(false)
//	public void setLayout(Layout<VertexType, EdgeType> newValue) {
//		Layout<VertexType, EdgeType> oldValue = layout;
//		layout = newValue;
//		propertyChangeSupport.firePropertyChange("Layout", oldValue, newValue);
////		vv.getModel().setGraphLayout(layout);
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#getGraph()
	 */
//	@Visible(false)
//	@Override
//	public Graph<VertexType, EdgeType> getGraph() {
//		return graph;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bus.uigen.jung.GenericJungGraph#setGraph(edu.uci.ics.jung.graph.Graph)
	 */
//	@Visible(false)
//	@Override
//	public void setGraph(Graph<VertexType, EdgeType> graph) {
//		this.graph = graph;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#getVertexLabelTransformer()
	 */
	@Visible(false)
	@Override
	public Transformer<VertexType, String> getVertexLabelTransformer() {
		return vertexLabelTransformer;
//		return vv.getRenderContext().getVertexLabelTransformer();

		// return vertexLabelTransformer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bus.uigen.jung.GenericJungGraph#setVertexLabelTransformer(org.apache.
	 * commons.collections15.Transformer)
	 */
	@Override
	@Visible(false)
	public void setVertexLabelTransformer(
			Transformer<VertexType, String> newValue) {
		Transformer<VertexType, String> oldValue = vertexLabelTransformer;
		vertexLabelTransformer = newValue;
		propertyChangeSupport.firePropertyChange("VertexLabelTransformer", oldValue, newValue);
		// this.vertexLabelTransformer = vertexLabelTransformer;
//		vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#getVertexToolTipTransformer()
	 */
	@Override
	@Visible(false)
	public Transformer<VertexType, String> getVertexToolTipTransformer() {
		return vertexToolTipTransformer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bus.uigen.jung.GenericJungGraph#setVertexToolTipTransformer(org.apache
	 * .commons.collections15.Transformer)
	 */
	@Visible(false)
	@Override
	public void setVertexToolTipTransformer(
			Transformer<VertexType, String> newValue) {
		Transformer<VertexType, String> oldValue = vertexToolTipTransformer;
		this.vertexToolTipTransformer = newValue;
//		vv.setVertexToolTipTransformer(vertexToolTipTransformer);
		propertyChangeSupport.firePropertyChange("VertexToolTipTransformer", oldValue, newValue);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#getEdgeToolTipTransformer()
	 */
	@Override
	@Visible(false)
	public Transformer<EdgeType, String> getEdgeToolTipTransformer() {
		return edgeToolTipTransformer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bus.uigen.jung.GenericJungGraph#setEdgeToolTipTransformer(org.apache.
	 * commons.collections15.Transformer)
	 */
	@Visible(false)
	@Override
	public void setEdgeToolTipTransformer(
			Transformer<EdgeType, String> newValue) {
		Transformer<EdgeType, String> oldValue = edgeToolTipTransformer;
		this.edgeToolTipTransformer = newValue;
		propertyChangeSupport.firePropertyChange("EdgeToolTipTransformer", oldValue, newValue);		
//		vv.setEdgeToolTipTransformer(edgeToolTipTransformer);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#getEdgeLabelTransformer()
	 */
	@Override
	@Visible(false)
	public Transformer<EdgeType, String> getEdgeLabelTransformer() {
		return edgeLabelTransformer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bus.uigen.jung.GenericJungGraph#setEdgeLabelTransformer(org.apache.commons
	 * .collections15.Transformer)
	 */
	@Override
	@Visible(false)
	public void setEdgeLabelTransformer(
			Transformer<EdgeType, String> newValue) {
		Transformer<EdgeType, String> oldValue = edgeLabelTransformer;
		this.edgeLabelTransformer = newValue;
		propertyChangeSupport.firePropertyChange("EdgeLabelTransformer", oldValue, newValue);
//		vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#isForest()
	 */
//	@Override
//	public boolean isForest() {
//		return isForest;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#setForest(boolean)
	 */
//	FRLayout2 createFRLayout() {
//		return new FRLayout2<VertexType, EdgeType>(observableGraph);
//	}
//
//	void setFRLayout() {
//		layoutType = LayoutType.FruchtermanReingold;
//		layout = createFRLayout();
//		vv.getModel().setGraphLayout(layout);
//
//	}
//
//	@Override
//	public void setForest(boolean newVal) {
//		if (isForest == newVal)
//			return;
//		this.isForest = newVal;
//		if (!newVal)
//			// layout = new FRLayout2<VertexType, EdgeType>(observableGraph);
//			setFRLayout();
//		else
//			setTreeLayout();
//		// layout = new TreeLayout<VertexType, EdgeType>(
//		// (Forest<VertexType, EdgeType>) graph, distanceX,
//		// distanceY);
//		vv.getModel().setGraphLayout(layout);
//		// vv.setLayout(layout);
//
//	}

	// @Override
	// public boolean isRadial() {
	// return isRadial;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#setForest(boolean)
	 */
//	RadialTreeLayout createRadialTreeLayout() {
//		try {
//			RadialTreeLayout retVal = new RadialTreeLayout<VertexType, EdgeType>(
//					(Forest<VertexType, EdgeType>) graph);
//			retVal.setSize(new Dimension(width, height));
//			return retVal;
//		} catch (Exception e) {
//			Tracer.error("Radial layout can be associated only with forests");
//			return null;
//		}
//	}
//
//	void setRadialTreeLayout() {
//		layout = createRadialTreeLayout();
//		vv.getModel().setGraphLayout(layout);
//	}

	// @Override
	// public void setRadial(boolean newVal) {
	// isRadial = newVal;
	// if (isRadial) {
	// setRadialTreeLayout();
	// // layout = new RadialTreeLayout<VertexType,
	// EdgeType>((Forest<VertexType, EdgeType>) graph);
	// // layout.setSize(new Dimension(600, 600));
	// } else {
	// setTreeLayout();
	// // layout = new TreeLayout<VertexType, EdgeType>(
	// // (Forest<VertexType, EdgeType>) graph, distanceX,
	// // distanceY);
	// }
	// // vv.getModel().setGraphLayout(layout);
	// // vv.setLayout(layout);
	//
	// }

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#init()
	 */
	ModalGraphMouse.Mode mouseMode = ModalGraphMouse.Mode.PICKING;
	@Override
	public ModalGraphMouse.Mode getMouseMode() {
		return mouseMode;
	}
	@Override
	public void setMouseMode(ModalGraphMouse.Mode newValue) {
		ModalGraphMouse.Mode oldValue = mouseMode;
		mouseMode = newValue;
		propertyChangeSupport.firePropertyChange("MouseMode", oldValue, newValue);

	}

//	protected ModalGraphMouse graphMouse;

	@Override
	@Visible(false)
	public void init() {

		// create a graph
		// Graph<VertexType, EdgeType> ig = Graphs.<VertexType,
		// EdgeType>synchronizedDirectedGraph(new
		// DirectedSparseMultigraph<VertexType, EdgeType>());

//		observableGraph = new ObservableGraph<VertexType, EdgeType>(graph);
//		observableGraph
//				.addGraphEventListener(new GraphEventListener<VertexType, EdgeType>() {
//
//					public void handleGraphEvent(
//							GraphEvent<VertexType, EdgeType> evt) {
//						System.err.println("got " + evt);
//
//					}
//				});
		// this.observableGraph = og;
		// create a graphdraw
//		if (!isForest)
//			// layout = new FRLayout2<VertexType, EdgeType>(observableGraph);
//			layout = createFRLayout();
//
//		else
//			layout = createTreeLayout();
		// layout = new TreeLayout<VertexType, EdgeType>(
		// (Forest<VertexType, EdgeType>) graph, distanceX,
		// distanceY);
		// ((FRLayout)layout).setMaxIterations(200);

//		vv = new VisualizationViewer<VertexType, EdgeType>(layout,
//				new Dimension(width, height));
//		relaxer = vv.getModel().getRelaxer();
//		jungShapeModelDisplayer = new AJungShapeModelDisplayer(this);
		
//		setPostRenderer(jungShapeModelDisplayer);
		setVertexIncludePredicate((TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, VertexType>) new ATableBasedGraphElementInclusionPredicate<>());
		setEdgeIncludePredicate((TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, EdgeType>) new ATableBasedGraphElementInclusionPredicate<>());

		// JRootPane rp = graphApplet.getRootPane();
		// rp.putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);

//		graphContainer.setLayout(new BorderLayout());
//		graphContainer.setBackground(java.awt.Color.lightGray);
//		graphContainer.setFont(new Font("Serif", Font.PLAIN, 12));

//		if (!isForest)
//
//			vv.getModel().getRelaxer().setSleepTime(500);
//		graphMouse = new DefaultModalGraphMouse<VertexType, EdgeType>();
//		// vv.setGraphMouse(new DefaultModalGraphMouse<VertexType, EdgeType>());
//		graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
//		vv.setGraphMouse(graphMouse);

		setVertexRenderer(new AVertexListShapeModelRenderer());
		setVertexLabelRenderer(new AVertexListShapeModelLabelRenderer());
		setVertexShapeTransformer(new AVertexListShapeModelTransformer<VertexType>());;
//		setVertexShapeTransformer(new AVertexListShapeModelTransformer<VertexType>(
//				(JungGraphManager<VertexType, Object>) this));
//		vv.getRenderer().getVertexLabelRenderer()
//				.setPosition(Renderer.VertexLabel.Position.CNTR);
//		vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);
//
//		vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);
//		setVertexFillPaintTransformer(vertexFillColorer);
//		setVertexDrawPaintTransformer(vertexDrawColorer);
//		edgeColorer.setDefaultColor(Color.BLACK);
//		setEdgeDrawPaintTransformer(edgeColorer);
		// vv.getRenderContext().setVertexShapeTransformer(new
		// ClusterVertexShapeTransformer<VertexType>());

		// vv.getRenderContext().setEdgeDrawPaintTransformer(new
		// PickableEdgePaintTransformer(vv.getPickedEdgeState(), Color.black,
		// Color.cyan));
//		vv.getRenderer().getVertexLabelRenderer()
//				.setPosition(Renderer.VertexLabel.Position.S);
		// vv.getRenderContext().setVertexShapeTransformer(new
		// ClusterVertexShapeFunction());

//		attachRenderContextTrapper((RenderContextTrapper<VertexType, EdgeType>) new ARenderContextTrapper<>());
//		attachGraphDecoratorTrapper(new AGraphicsDecoratorTrapper());
		
//		collapser = new DelegatingGraphCollapser(graph);

		// vv.setForeground(Color.white);
//		vv.setForeground(Color.black);
//
//		graphContainer.add(vv);
		// switchLayout = new JButton("Switch to SpringLayout");
		// switchLayout.addActionListener(new ActionListener() {
		//
		// @SuppressWarnings("unchecked")
		// public void actionPerformed(ActionEvent ae) {
		// Dimension d = new Dimension(width, height);
		// if (switchLayout.getText().indexOf("Spring") > 0) {
		// switchLayout.setText("Switch to FRLayout");
		// layout = new SpringLayout<VertexType, EdgeType>(
		// observableGraph, new ConstantTransformer(
		// EDGE_LENGTH));
		// layout.setSize(d);
		// vv.getModel().setGraphLayout(layout, d);
		// } else {
		// switchLayout.setText("Switch to SpringLayout");
		// layout = new FRLayout<VertexType, EdgeType>(
		// observableGraph, d);
		// vv.getModel().setGraphLayout(layout, d);
		// }
		// }
		// });
		//
		// graphContainer.add(switchLayout, BorderLayout.SOUTH);
		//
		// timer = new Timer();
	}
    public void collapse() {
    	propertyChangeSupport.firePropertyChange("@collapse", null, true);
//    	
//    	Collection<VertexType> picked = new HashSet(vv.getPickedVertexState().getPicked());
//        if(picked.size() > 1) {
//            Graph<VertexType, EdgeType> inGraph = layout.getGraph();
//            Graph<VertexType, EdgeType> clusterGraph = collapser.getClusterGraph(inGraph, picked);
//
//            Graph g = collapser.collapse(layout.getGraph(), clusterGraph);
//            double sumx = 0;
//            double sumy = 0;
//            for(VertexType v : picked) {
//            	Point2D p = (Point2D)layout.transform(v);
//            	sumx += p.getX();
//            	sumy += p.getY();
//            }
//            Point2D cp = new Point2D.Double(sumx/picked.size(), sumy/picked.size());
//            vv.getRenderContext().getParallelEdgeIndexFunction().reset();
//            layout.setGraph(g);
//            
//            ((Layout)layout).setLocation(clusterGraph, cp);
//            vv.getPickedVertexState().clear();
//            vv.repaint();
//        }
    }
    public void expand() {
    	propertyChangeSupport.firePropertyChange("@expand", null, true);

//        Collection picked = new HashSet(vv.getPickedVertexState().getPicked());
//        for(Object v : picked) {
//            if(v instanceof Graph) {
//                
//                Graph g = collapser.expand(layout.getGraph(), (Graph)v);
//                vv.getRenderContext().getParallelEdgeIndexFunction().reset();
//                layout.setGraph(g);
//            }
//            vv.getPickedVertexState().clear();
//           vv.repaint();
//        }
    }
    public void reset() {
    	propertyChangeSupport.firePropertyChange("@reset", null, true);
//        layout.setGraph(graph);
//        vv.repaint();
    }
//	@Override
//	public void addAndDisplayVertex(VertexType aVertex) {
//
//		// Relaxer relaxer = vv.getModel().getRelaxer();
//		relaxer.pause();
//		observableGraph.addVertex(aVertex);
//		layout.initialize();
//		relaxer.resume();
//
//	}
//
//	@Override
//	public void addAndLayoutVertex(VertexType aVertex) {
//
//		// Relaxer relaxer = vv.getModel().getRelaxer();
//		relaxer.pause();
//		observableGraph.addVertex(aVertex);
//		layout.initialize();
//		// relaxer.resume();
//
//	}
//
//	@Override
//	public void removeAndUndisplayVertex(VertexType aVertex) {
//
//		// Relaxer relaxer = vv.getModel().getRelaxer();
//		relaxer.pause();
//		observableGraph.removeVertex(aVertex);
//		layout.initialize();
//		relaxer.resume();
//
//	}
//
//	@Override
//	public void addAndDisplayEdge(EdgeType anEdge, VertexType aVertex1,
//			VertexType aVertex2) {
//		relaxer.pause();
//		observableGraph.addEdge(anEdge, aVertex1, aVertex2);
//		getGraphLayout().initialize();
//		relaxer.resume();
//	}
//
//	@Override
//	public void removeAndDisplayEdge(EdgeType anEdge) {
//		relaxer.pause();
//		observableGraph.removeEdge(anEdge);
//		getGraphLayout().initialize();
//		relaxer.resume();
//	}
//
//	@Override
//	public void setVertexColors(VertexType aVertex, List<Color> aColors) {
//		vertexToColors.put(aVertex, aColors);
//	}
//
//	@Override
//	public List<Color> getVertexColors(VertexType aVertex) {
//		return vertexToColors.get(aVertex);
//	}
//
//	@Override
//	public void setVertexFillColor(VertexType aVertex, Paint aColor) {
//
//		vertexFillColorer.setColor(aVertex, aColor);
//	}
//
//	@Override
//	public Paint getVertexFillColor(VertexType aVertex) {
//		return vertexFillColorer.getColor(aVertex);
//	}
//
//	@Override
//	public void setEdgeDrawColor(EdgeType aVertex, Paint aColor) {
//
//		edgeColorer.setColor(aVertex, aColor);
//	}
//
//	@Override
//	@Visible(false)
//	public Paint getEdgeDrawColor(EdgeType aVertex) {
//		return edgeColorer.getColor(aVertex);
//	}
//
//	//
//	// @Override
//	// public void addChild(EdgeType anEdge, VertexType aParentVertex,
//	// VertexType aChildVertex) {
//	// addAndDisplayEdge(anEdge, aParentVertex, aChildVertex);
//	// relaxer.pause();
//	// observableGraph.addVertex(aChildVertex);
//	// observableGraph.addEdge(anEdge, aParentVertex, aChildVertex);
//	// layout.initialize();
//	// relaxer.resume();
//	// }
//
//	@Override
//	public void renderGraph() {
//		relaxer.resume();
//	}
    
    @Override
	@Visible(false)
	public VisualizationViewer.Paintable getPostRenderer() {
		return postRenderer;
	}


	@Override
	@Visible(false)
	public void setPostRenderer(VisualizationViewer.Paintable newValue) {
		VisualizationViewer.Paintable oldValue = postRenderer;
		postRenderer = newValue;
		propertyChangeSupport.firePropertyChange("PostRenderer", oldValue, newValue);
//		vv.addPostRenderPaintable(postRenderer);
	}

//	@Override
//	@Visible(false)
//	public VisualizationViewer<VertexType, EdgeType> getVisualizationViewer() {
//		return vv;
//	}
//
//	@Override
//	public void setVisualizationViewer(
//			VisualizationViewer<VertexType, EdgeType> newVal) {
//		vv = newVal;
//	}

	@Visible(false)
	@Override
	public TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, VertexType> getVertexIncludePredicate() {
		// vv.getRenderContext().getVertexIncludePredicate();
		return vertexIncludePredicate;
	}

	@Visible(false)
	@Override
	public void setVertexIncludePredicate(
			TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, VertexType> newValue) {
		TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, VertexType> oldValue = vertexIncludePredicate;
		vertexIncludePredicate = newValue;
		propertyChangeSupport.firePropertyChange("VertexIncludePredicate", oldValue, newValue);
//		vv.getRenderContext().setVertexIncludePredicate(newVal);
	}
	@Visible(false)
	@Override
	public TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, EdgeType> getEdgeIncludePredicate() {
		return edgeIncludePredicate;
	}
	@Visible(false)
	@Override
	public void setEdgeIncludePredicate(
			TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, EdgeType> newValue) {
		TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, EdgeType> oldValue = edgeIncludePredicate;
		edgeIncludePredicate = newValue;
//		vv.getRenderContext().setEdgeIncludePredicate(edgeIncludePredicate);
		propertyChangeSupport.firePropertyChange("EdgeIncludePredicate", oldValue, newValue);

	}
//
//	@Override
//	public void setVertexVisibile(VertexType aVertex, boolean newVal) {
//		vertexIncludePredicate.setIncludeGraphElement(aVertex, newVal);
//	}
//
//	@Override
//	public boolean getVertexVisibile(VertexType aVertex) {
//		return vertexIncludePredicate.getIncludeGraphElement(aVertex);
//	}
//
//	@Override
//	public void setEdgeVisibile(EdgeType aVertex, boolean newVal) {
//		edgeIncludePredicate.setIncludeGraphElement(aVertex, newVal);
//	}
//
//	@Override
//	public boolean getEdgeVisibile(EdgeType aVertex) {
//		return edgeIncludePredicate.getIncludeGraphElement(aVertex);
//	}
//
//	public Shape getVertexShape(VertexType aVertex) {
//		return getVertexShapeTransformer().transform(aVertex);
//	}
//
//	public Point2D getLocation(VertexType aVertex) {
//		return vv.getGraphLayout().transform(aVertex);
//	}
	protected Transformer<VertexType, Shape> vertexShapeTransformer;
	protected Transformer<VertexType, Paint> vertexDrawPaintTransformer;

	protected Transformer<VertexType, Paint> vertexFillPaintTransformer;


	@Visible(false)
	@Override
	public Transformer<VertexType, Shape> getVertexShapeTransformer() {
		return vertexShapeTransformer;
//		return vv.getRenderContext().getVertexShapeTransformer();
	}

	@Override
	@Visible(false)
	public Transformer<VertexType, Paint> getVertexFillPaintTransformer() {
		
		return vertexFillPaintTransformer;
	}

	@Override
	@Visible(false)
	public Transformer<VertexType, Paint> getVertexDrawPaintTransformer() {
		return vertexDrawPaintTransformer;
	}
	protected Transformer<VertexType, Stroke>  vertexStrokeTransformer;

	@Visible(false)
	public Transformer<VertexType, Stroke> getVertexStrokeTransformer() {
		return vertexStrokeTransformer;
	}
	protected Transformer<VertexType, Icon> vertexIconTransformer;

	@Override
	@Visible(false)
	public Transformer<VertexType, Icon> getVertexIconTransformer() {
		return vertexIconTransformer;
	}
	Vertex<VertexType, EdgeType> vertexRenderer;
	@Override
	@Visible(false)
	public Vertex<VertexType, EdgeType> getVertexRenderer() {
		return vertexRenderer;
	}

	@Override
	@Visible(false)
	public void setVertexRenderer(Vertex<VertexType, EdgeType> newVal) {
//		vv.getRenderer().setVertexRenderer(newVal);
	}
	VertexLabel<VertexType, EdgeType> vertexLabelRenderer;
	@Override
	@Visible(false)
	public VertexLabel<VertexType, EdgeType> getVertexLabelRenderer() {
		return vertexLabelRenderer;
	}

	@Override
	@Visible(false)
	public void setVertexLabelRenderer(VertexLabel<VertexType, EdgeType> newVal) {
//		vv.getRenderer().setVertexLabelRenderer(newVal);
	}
	@Override
	@Visible(false)
	public void setVertexShapeTransformer(Transformer<VertexType, Shape> newVal) {
//		vv.getRenderContext().setVertexShapeTransformer(newVal);
	}
	Transformer<Context<Graph<VertexType, EdgeType>, EdgeType>, Shape> edgeShapeTransformer;
	@Override
	@Visible(false)
	public Transformer<Context<Graph<VertexType, EdgeType>, EdgeType>, Shape> getEdgeShapeTransformer() {
		return edgeShapeTransformer;
	}
	Transformer<EdgeType, Paint> edgeDrawPaintTransformer;

	@Override
	@Visible(false)
	public Transformer<EdgeType, Paint> getEdgeDrawPaintTransformer() {
		return edgeDrawPaintTransformer;
	}

	@Override
	@Visible(false)
	public void setEdgeShapeTransformer(
			Transformer<Context<Graph<VertexType, EdgeType>, EdgeType>, Shape> newVal) {
//		vv.getRenderContext().setEdgeShapeTransformer(newVal);

	}

//	public void attachRenderContextTrapper(
//			RenderContextTrapper<VertexType, EdgeType> newVal) {
//		RenderContext<VertexType, EdgeType> oldContext = vv.getRenderContext();
//		newVal.setRenderContext(oldContext);
//		vv.setRenderContext(newVal);
//	}
//
//	public void detachRenderContextTrapper() {
//		try {
//			RenderContextTrapper<VertexType, EdgeType> trapper = (RenderContextTrapper<VertexType, EdgeType>) vv
//					.getRenderContext();
//			vv.setRenderContext(trapper.getRenderContext());
//
//		} catch (ClassCastException e) {
//			Tracer.error("No context trapper to detach");
//		}
//	}

//	public void attachGraphDecoratorTrapper(AGraphicsDecoratorTrapper newVal) {
//		GraphicsDecorator oldDecorator = vv.getRenderContext()
//				.getGraphicsContext();
//		newVal.setGraphicsDecorator(oldDecorator);
//		vv.getRenderContext().setGraphicsContext(newVal);
//	}
//
//	public void detachGraphDecoratorTrapper() {
//		try {
//			AGraphicsDecoratorTrapper trapper = (AGraphicsDecoratorTrapper) vv
//					.getRenderContext().getGraphicsContext();
//			vv.getRenderContext().setGraphicsContext(
//					trapper.getGraphicsDecorator());
//
//		} catch (ClassCastException e) {
//			Tracer.error("No context trapper to detach");
//		}
//	}

	@Override
	@Visible(false)
	public JungShapeModelDisplayer getJungShapeModelDisplayer() {
		return jungShapeModelDisplayer;
	}
//	Transformer<VertexType, Paint> vertexFillPaintTransformer;
	@Override
	@Visible(false)
	public void setVertexFillPaintTransformer(
			Transformer<VertexType, Paint> newVal) {
	
//		vv.getRenderContext().setVertexFillPaintTransformer(newVal);
	}
//	Transformer<VertexType, Paint> vertexDrawPaintTransformer;

	@Override
	@Visible(false)
	public void setVertexDrawPaintTransformer(
			Transformer<VertexType, Paint> newVal) {
//		vv.getRenderContext().setVertexDrawPaintTransformer(newVal);
	}
//	Transformer<VertexType, Paint> edgeDrawPaintTransformer;

	@Override
	@Visible(false)
	public void setEdgeDrawPaintTransformer(Transformer<EdgeType, Paint> newVal) {
		// vv.getRenderContext().setEdgeFillPaintTransformer(newVal);
//		vv.getRenderContext().setEdgeDrawPaintTransformer(newVal);

	}

//	@Override
//	public void setVertexDrawColor(VertexType aVertex, Paint aColor) {
//		vertexDrawColorer.setColor(aVertex, aColor);
//	}

//	@Override
//	@Visible(false)
//	public Paint getVertexDrawColor(VertexType aVertex) {
//		// TODO Auto-generated method stub
//		return vertexDrawColorer.getColor(aVertex);
//	}
//
//	@Override
//	@Visible(false)
//	public CompleteOEFrame getOEFrame() {
//		return oeFrame;
//	}

//	@Override
//	public void setOEFrame(CompleteOEFrame anOEFrame) {
//		oeFrame = anOEFrame;
//	}

//	public TableDrivenColorer<VertexType> getVertexFillColorer() {
//		return vertexFillColorer;
//	}
//
//	public void setVertexFillColorer(
//			TableDrivenColorer<VertexType> vertexFillColorer) {
//		this.vertexFillColorer = vertexFillColorer;
//		setVertexFillPaintTransformer(vertexFillColorer);
//
//	}
//	@Visible(false)
//	public TableDrivenColorer<VertexType> getVertexDrawColorer() {
//		return vertexDrawColorer;
//	}

//	public void setVertexDrawColorer(
//			TableDrivenColorer<VertexType> vertexDrawColorer) {
//		this.vertexDrawColorer = vertexDrawColorer;
//		setVertexDrawPaintTransformer(vertexDrawColorer);
//	}
	
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		propertyChangeSupport.addPropertyChangeListener(aListener);
		
	}
	@Visible(false)
	@Override
	public Transformer<VertexType, Paint> getVertexPaintTransformer() {
		return vertexPaintTransformer;
	}
	@Visible(false)
	@Override
	public void setVertexPaintTransformer(
			Transformer<VertexType, Paint> vertexPaintTransformer) {
		this.vertexPaintTransformer = vertexPaintTransformer;
	}
	@Visible(false)
	@Override
	public void setJungShapeModelDisplayer(
			JungShapeModelDisplayer jungShapeModelDisplayer) {
		this.jungShapeModelDisplayer = jungShapeModelDisplayer;
	}
	@Visible(false)
	public void setVertexStrokeTransformer(
			Transformer<VertexType, Stroke> vertexStrokeTransformer) {
		this.vertexStrokeTransformer = vertexStrokeTransformer;
	}

	@Visible(false)
	public void setVertexIconTransformer(
			Transformer<VertexType, Icon> vertexIconTransformer) {
		this.vertexIconTransformer = vertexIconTransformer;
	}
	

}