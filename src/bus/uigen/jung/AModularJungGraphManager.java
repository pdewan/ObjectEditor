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
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JSlider;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.xmlbeans.XmlCursor.ChangeStamp;

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
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.Vertex;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel;
import edu.uci.ics.jung.visualization.subLayout.DelegatingGraphCollapser;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

/**
 * Parameterizes Jung capabilites in a facade
 * 
 * @author Prasun Dewan
 */
public class AModularJungGraphManager<VertexType, EdgeType> implements
		JungGraphManager<VertexType, EdgeType> {

	/**
	 *
	 */
	Container graphContainer;
	private static final long serialVersionUID = -5345319851341875800L;
//	JungShapeModelDisplayer jungShapeModelDisplayer;
	JungGraphManagerCustomization<VertexType, EdgeType> jungGraphCustomization = 
			new AJungGraphManagerCustomization<>();
	



	protected CompleteOEFrame oeFrame;

	private ObservableGraph<VertexType, EdgeType> observableGraph = null;

	private VisualizationViewer<VertexType, EdgeType> vv = null;

	private Layout<VertexType, EdgeType> layout = null;
	final ScalingControl scaler = new CrossoverScalingControl();

	Timer timer;

	boolean done;

//	protected JButton switchLayout;
//	public static final int MAX_DISTANCE_X = 400;
//	public static final int MAX_DISTANCE_Y = 400;
//	int distanceX = 100;
//	int distanceY = 100;
	int width = 600;
	int height = 600;
//	int edgeLength = 100;
//	public static final int MAX_SCALE_VALUE = 10;
//	public static final int MIN_SCALE_VALUE = 1;
//	public static final int NORMAL_SCALE_VALUE = 5;
	public static final float SCALE_UNIT = 1.1f;
//
//	int scale = NORMAL_SCALE_VALUE;

	Graph<VertexType, EdgeType> graph;

//	Transformer<VertexType, String> vertexLabelTransformer = new ToStringLabeller();
//	Transformer<VertexType, String> vertexToolTipTransformer = vertexLabelTransformer;
//	Transformer<VertexType, Paint> vertexPaintTransformer;
//
//	Transformer<EdgeType, String> edgeToolTipTransformer = new ToStringLabeller();
//	Transformer<EdgeType, String> edgeLabelTransformer = edgeToolTipTransformer;

	boolean isForest;
	boolean isRadial;

//	LayoutType layoutType = LayoutType.FruchtermanReingold;
//	CollapserType collapserType = CollapserType.CollapseDuplicateClasses;
    protected GraphCollapser collapser;
	protected Relaxer relaxer;
//
//	VisualizationViewer.Paintable postRenderer;
	Map<VertexType, List<Color>> vertexToColors = new HashMap();
	TableDrivenColorer<VertexType> vertexFillColorer = new ATableDrivenColorer<VertexType>(
			this);

	TableDrivenColorer<VertexType> vertexDrawColorer = new ATableDrivenColorer<VertexType>(
			this);

	TableDrivenColorer<EdgeType> edgeColorer = new ATableDrivenColorer<EdgeType>(
			this);
	// Map<VertexType, Color> vertexToColor = new HashMap();
	// Map<EdgeType, Color> edgeToColor = new HashMap();
//	TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, VertexType> vertexIncludePredicate;
//	TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, EdgeType> edgeIncludePredicate;

	// Transformer<VertexType, Shape> vertexTransformer

//	public AJungGraphManager() {
//
//		// isForest = anIsForest;
//		init();
//	}

	public AModularJungGraphManager(Graph<VertexType, EdgeType> aGraph,
			Container aGraphContainer) {
		graph = aGraph;
		graphContainer = aGraphContainer;
		// isForest = anIsForest;
		init();
	}
	public AModularJungGraphManager(Graph<VertexType, EdgeType> aGraph,
			Container aGraphContainer, JungGraphManagerCustomization<VertexType, EdgeType> aJungGraphManagerCustomization) {
		jungGraphCustomization = aJungGraphManagerCustomization;
		graph = aGraph;
		graphContainer = aGraphContainer;
		// isForest = anIsForest;
		init();
	}

	

	@PreferredWidgetClass(JSlider.class)
	@MaxValue(AJungGraphManagerCustomization.MAX_DISTANCE_X)
	public int getTreeNodeDistanceX() {
		return jungGraphCustomization.getTreeNodeDistanceX();
	}

	public void setTreeNodeDistanceX(int distanceX) {
//		maybeSetTreeLayout();
		jungGraphCustomization.setTreeNodeDistanceX(distanceX);

	}
	

	@PreferredWidgetClass(JSlider.class)
	@MaxValue(AJungGraphManagerCustomization.MAX_DISTANCE_Y)
	public int getTreeNodeDistanceY() {
		return jungGraphCustomization.getTreeNodeDistanceY();
	}

	public void setTreeNodeDistanceY(int distanceY) {
		jungGraphCustomization.setTreeNodeDistanceY(distanceY);
//		maybeSetTreeLayout();
	}

	@PreferredWidgetClass(JSlider.class)
	@MaxValue(AJungGraphManagerCustomization.MAX_SCALE_VALUE)
	public int getScale() {
		return jungGraphCustomization.getScale();
	}

	public void setScale(int newVal) {
		jungGraphCustomization.setScale(newVal);
//		int oldScaleValue = scale;
//		int multiplierUnits = newVal - oldScaleValue;
//		float multiplier = multiplierUnits * SCALE_UNIT;
//		if (multiplierUnits == 0)
//			return;
//		if (multiplierUnits < 0) {
//			multiplier = 1 / -multiplier;
//		}
//		scaler.scale(vv, multiplier, vv.getCenter());
//		this.scale = newVal;

	}
	
	protected void adjustScale(int oldScaleValue, int newVal) {
		int multiplierUnits = newVal - oldScaleValue;
		float multiplier = multiplierUnits * SCALE_UNIT;
		if (multiplierUnits == 0)
			return;
		if (multiplierUnits < 0) {
			multiplier = 1 / -multiplier;
		}
		scaler.scale(vv, multiplier, vv.getCenter());
	}

	public int getEdgeLength() {
		return jungGraphCustomization.getEdgeLength();
	}

	public void setEdgeLength(int newVal) {
		jungGraphCustomization.setEdgeLength(newVal);
	}

	TreeLayout createTreeLayout() {
		try {
			TreeLayout treeLayout = new TreeLayout<VertexType, EdgeType>(
					(Forest<VertexType, EdgeType>) graph, jungGraphCustomization.getTreeNodeDistanceX(), jungGraphCustomization.getTreeNodeDistanceY());
			return treeLayout;
		} catch (Exception e) {
			Tracer.error("Tree layout can be associated only with trees");
			return null;
		}

	}

	BalloonLayout createBallonLayout() {
		try {
			BalloonLayout balloonLayout = new BalloonLayout<VertexType, EdgeType>(
					(Forest<VertexType, EdgeType>) graph);
			return balloonLayout;
		} catch (Exception e) {
			Tracer.error("Balloon layout can be associated only with trees");
			return null;
		}

	}

	SpringLayout createSpringLayout() {

		SpringLayout retVal = new SpringLayout(graph, new ConstantTransformer(
				EDGE_LENGTH));
		retVal.setSize(new Dimension(width, height));
		return retVal;

	}

	void setSpringLayout() {
		layout = createSpringLayout();
		vv.getModel().setGraphLayout(layout);

	}

	SpringLayout createSpringLayout2() {
		SpringLayout retVal = new SpringLayout2(graph, new ConstantTransformer(
				EDGE_LENGTH));
		retVal.setSize(new Dimension(width, height));
		return retVal;
	}

	void setSpringLayout2() {
		layout = createSpringLayout2();
		vv.getModel().setGraphLayout(layout);

	}

	// void setKKLayout() {
	// layout = createKKLayout();
	// vv.getModel().setGraphLayout(layout);
	//
	// }

	ISOMLayout createISOMLayout() {
		ISOMLayout retVal = new ISOMLayout(graph);
		retVal.setSize(new Dimension(width, height));
		return retVal;

	}

	// void seISOMLayout() {
	// layout = createISOMLayout();
	// vv.getModel().setGraphLayout(layout);
	//
	// }

	KKLayout createKKLayout() {
		KKLayout retVal = new KKLayout(graph);
		retVal.setSize(new Dimension(width, height));
		return retVal;

	}

	void maybeSetTreeLayout() {
		if (layout instanceof TreeLayout && !isRadial) {
			setTreeLayout();
		}
	}

	void setTreeLayout() {
//		layoutType = LayoutType.Tree;
		layout = createTreeLayout();
		vv.getModel().setGraphLayout(layout);

	}

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
		return jungGraphCustomization.getLayoutType();
	}

	@Override
	public void setLayoutType(LayoutType newVal) {
		jungGraphCustomization.setLayoutType(newVal);

//		setLayout();
		

	}
	public void setLayout(LayoutType layoutType) {
		jungGraphCustomization.setLayoutType(layoutType);
	}
	@Visible(false)
	public void adjustLayout(LayoutType layoutType) {
		Layout<VertexType, EdgeType> newLayout = null;
		switch (layoutType) {
		case Tree:
			newLayout = createTreeLayout();
			break;

		case Radial:
			newLayout = createRadialTreeLayout();
			break;
		case FruchtermanReingold:
			newLayout = createFRLayout();
			break;
		case Spring:
			newLayout = createSpringLayout();
			break;
		case Spring2:
			newLayout = createSpringLayout2();
			break;
		case KamadaKawai:
			newLayout = createKKLayout();
			break;
		case MeyersSelfOrganizing:
			newLayout = createISOMLayout();
		}
		if (newLayout != null) {
			layout = newLayout;

			vv.getModel().setGraphLayout(layout);
		}

	}

//	public CollapserType getCollapserType() {
//		return jungGraphCustomization.get;
//	}
//
//	public void setCollapserType(CollapserType collapserType) {
//		this.collapserType = collapserType;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#getGraphContainer()
	 */
	@Visible(false)
	@Override
	public Container getGraphContainer() {
		return graphContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bus.uigen.jung.GenericJungGraph#setGraphContainer(java.awt.Container)
	 */
	@Override
	@Visible(false)
	public void setGraphContainer(Container graphContainer) {
		this.graphContainer = graphContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#getLayout()
	 */
	@Visible(false)
	@Override
	public Layout<VertexType, EdgeType> getGraphLayout() {
		return vv.getGraphLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bus.uigen.jung.GenericJungGraph#setLayout(edu.uci.ics.jung.algorithms
	 * .layout.Layout)
	 */
	@Override
	@Visible(false)
	public void setLayout(Layout<VertexType, EdgeType> layout) {
		vv.getModel().setGraphLayout(layout);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#getGraph()
	 */
	@Visible(false)
	@Override
	public Graph<VertexType, EdgeType> getGraph() {
		return graph;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bus.uigen.jung.GenericJungGraph#setGraph(edu.uci.ics.jung.graph.Graph)
	 */
	@Visible(false)
	@Override
	public void setGraph(Graph<VertexType, EdgeType> graph) {
		this.graph = graph;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#getVertexLabelTransformer()
	 */
	@Visible(false)
	@Override
	public Transformer<VertexType, String> getVertexLabelTransformer() {
		return vv.getRenderContext().getVertexLabelTransformer();

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
			Transformer<VertexType, String> vertexLabelTransformer) {
		// this.vertexLabelTransformer = vertexLabelTransformer;
		vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#getVertexToolTipTransformer()
	 */
	@Override
	@Visible(false)
	public Transformer<VertexType, String> getVertexToolTipTransformer() {
		return jungGraphCustomization.getVertexToolTipTransformer();
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
			Transformer<VertexType, String> vertexToolTipTransformer) {
		jungGraphCustomization.setVertexToolTipTransformer(vertexToolTipTransformer);
//		this.vertexToolTipTransformer = vertexToolTipTransformer;
//		vv.setVertexToolTipTransformer(vertexToolTipTransformer);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#getEdgeToolTipTransformer()
	 */
	@Override
	@Visible(false)
	public Transformer<EdgeType, String> getEdgeToolTipTransformer() {
		return jungGraphCustomization.getEdgeToolTipTransformer();
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
			Transformer<EdgeType, String> edgeToolTipTransformer) {
		jungGraphCustomization.setEdgeToolTipTransformer(edgeToolTipTransformer);
//		this.edgeToolTipTransformer = edgeToolTipTransformer;
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
		return jungGraphCustomization.getEdgeLabelTransformer();
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
			Transformer<EdgeType, String> edgeLabelTransformer) {
		jungGraphCustomization.setEdgeLabelTransformer(edgeLabelTransformer);
//		this.edgeLabelTransformer = edgeLabelTransformer;
//		vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#isForest()
	 */
	@Override
	public boolean isForest() {
		return isForest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#setForest(boolean)
	 */
	FRLayout2 createFRLayout() {
		return new FRLayout2<VertexType, EdgeType>(observableGraph);
	}

	void setFRLayout() {
//		layoutType = LayoutType.FruchtermanReingold;
		layout = createFRLayout();
		vv.getModel().setGraphLayout(layout);

	}

	@Override
	public void setForest(boolean newVal) {
		if (isForest == newVal)
			return;
		this.isForest = newVal;
		if (!newVal)
			// layout = new FRLayout2<VertexType, EdgeType>(observableGraph);
			setFRLayout();
		else
			setTreeLayout();
		// layout = new TreeLayout<VertexType, EdgeType>(
		// (Forest<VertexType, EdgeType>) graph, distanceX,
		// distanceY);
		vv.getModel().setGraphLayout(layout);
		// vv.setLayout(layout);

	}

	// @Override
	// public boolean isRadial() {
	// return isRadial;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#setForest(boolean)
	 */
	RadialTreeLayout createRadialTreeLayout() {
		try {
			RadialTreeLayout retVal = new RadialTreeLayout<VertexType, EdgeType>(
					(Forest<VertexType, EdgeType>) graph);
			retVal.setSize(new Dimension(width, height));
			return retVal;
		} catch (Exception e) {
			Tracer.error("Radial layout can be associated only with forests");
			return null;
		}
	}

	void setRadialTreeLayout() {
		layout = createRadialTreeLayout();
		vv.getModel().setGraphLayout(layout);
	}

	

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see bus.uigen.jung.GenericJungGraph#init()
	 */
	ModalGraphMouse.Mode mouseMode = ModalGraphMouse.Mode.PICKING;

	public ModalGraphMouse.Mode getMouseMode() {
		return mouseMode;
	}

	public void setMouseMode(ModalGraphMouse.Mode newValue) {
		jungGraphCustomization.setMouseMode(newValue);
//		mouseMode = aMouseMode;
//		graphMouse.setMode(aMouseMode);

	}
	
	protected void processMouseMode(ModalGraphMouse.Mode oldValue, ModalGraphMouse.Mode newValue ) {
		graphMouse.setMode(newValue);
		
	}

	protected ModalGraphMouse graphMouse;

	@Override
	@Visible(false)
	public void init() {

		// create a graph
		// Graph<VertexType, EdgeType> ig = Graphs.<VertexType,
		// EdgeType>synchronizedDirectedGraph(new
		// DirectedSparseMultigraph<VertexType, EdgeType>());

		observableGraph = new ObservableGraph<VertexType, EdgeType>(graph);
		observableGraph
				.addGraphEventListener(new GraphEventListener<VertexType, EdgeType>() {

					public void handleGraphEvent(
							GraphEvent<VertexType, EdgeType> evt) {
						System.err.println("got " + evt);

					}
				});
		jungGraphCustomization.addPropertyChangeListener(this);
		// this.observableGraph = og;
		// create a graphdraw
		if (!isForest)
			// layout = new FRLayout2<VertexType, EdgeType>(observableGraph);
			layout = createFRLayout();

		else
			layout = createTreeLayout();
		// layout = new TreeLayout<VertexType, EdgeType>(
		// (Forest<VertexType, EdgeType>) graph, distanceX,
		// distanceY);
		// ((FRLayout)layout).setMaxIterations(200);

		vv = new VisualizationViewer<VertexType, EdgeType>(layout,
				new Dimension(width, height));
		relaxer = vv.getModel().getRelaxer();
//		jungShapeModelDisplayer = new AJungShapeModelDisplayer(this);
		jungGraphCustomization.setPostRenderer(new AJungShapeModelDisplayer(this));
		setVertexIncludePredicate((TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, VertexType>) new ATableBasedGraphElementInclusionPredicate<>());
		setEdgeIncludePredicate((TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, EdgeType>) new ATableBasedGraphElementInclusionPredicate<>());

		// JRootPane rp = graphApplet.getRootPane();
		// rp.putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);

		graphContainer.setLayout(new BorderLayout());
		graphContainer.setBackground(java.awt.Color.lightGray);
		graphContainer.setFont(new Font("Serif", Font.PLAIN, 12));

		if (!isForest)

			vv.getModel().getRelaxer().setSleepTime(500);
		graphMouse = new DefaultModalGraphMouse<VertexType, EdgeType>();
		// vv.setGraphMouse(new DefaultModalGraphMouse<VertexType, EdgeType>());
		graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(graphMouse);

//		setVertexRenderer(new AVertexListShapeModelRenderer());
//		setVertexLabelRenderer(new AVertexListShapeModelLabelRenderer());
//		setVertexShapeTransformer(new AVertexListShapeModelTransformer<VertexType>(
//				(JungGraphManager<VertexType, Object>) this));
		vv.getRenderer().getVertexLabelRenderer()
				.setPosition(Renderer.VertexLabel.Position.CNTR);
		vv.getRenderContext().setVertexLabelTransformer(jungGraphCustomization.getVertexLabelTransformer());

		vv.getRenderContext().setEdgeLabelTransformer(jungGraphCustomization.getEdgeLabelTransformer());
		setVertexFillPaintTransformer(vertexFillColorer);
		setVertexDrawPaintTransformer(vertexDrawColorer);
		edgeColorer.setDefaultColor(Color.BLACK);
		setEdgeDrawPaintTransformer(edgeColorer);
		// vv.getRenderContext().setVertexShapeTransformer(new
		// ClusterVertexShapeTransformer<VertexType>());

		// vv.getRenderContext().setEdgeDrawPaintTransformer(new
		// PickableEdgePaintTransformer(vv.getPickedEdgeState(), Color.black,
		// Color.cyan));
		vv.getRenderer().getVertexLabelRenderer()
				.setPosition(Renderer.VertexLabel.Position.S);
		// vv.getRenderContext().setVertexShapeTransformer(new
		// ClusterVertexShapeFunction());

		attachRenderContextTrapper((RenderContextTrapper<VertexType, EdgeType>) new ARenderContextTrapper<>());
		attachGraphDecoratorTrapper(new AGraphicsDecoratorTrapper());
		
		collapser = new DelegatingGraphCollapser(graph);

		// vv.setForeground(Color.white);
		vv.setForeground(Color.black);

		graphContainer.add(vv);
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
    	Collection<VertexType> picked = new HashSet(vv.getPickedVertexState().getPicked());
        if(picked.size() > 1) {
            Graph<VertexType, EdgeType> inGraph = layout.getGraph();
            Graph<VertexType, EdgeType> clusterGraph = collapser.getClusterGraph(inGraph, picked);

            Graph g = collapser.collapse(layout.getGraph(), clusterGraph);
            double sumx = 0;
            double sumy = 0;
            for(VertexType v : picked) {
            	Point2D p = (Point2D)layout.transform(v);
            	sumx += p.getX();
            	sumy += p.getY();
            }
            Point2D cp = new Point2D.Double(sumx/picked.size(), sumy/picked.size());
            vv.getRenderContext().getParallelEdgeIndexFunction().reset();
            layout.setGraph(g);
            
            ((Layout)layout).setLocation(clusterGraph, cp);
            vv.getPickedVertexState().clear();
            vv.repaint();
        }
    }
    public void expand() {
        Collection picked = new HashSet(vv.getPickedVertexState().getPicked());
        for(Object v : picked) {
            if(v instanceof Graph) {
                
                Graph g = collapser.expand(layout.getGraph(), (Graph)v);
                vv.getRenderContext().getParallelEdgeIndexFunction().reset();
                layout.setGraph(g);
            }
            vv.getPickedVertexState().clear();
           vv.repaint();
        }
    }
    public void reset() {
        layout.setGraph(graph);
        vv.repaint();
    }
	@Visible(false)
	@Override
	public void addAndDisplayVertex(VertexType aVertex) {

		// Relaxer relaxer = vv.getModel().getRelaxer();
		relaxer.pause();
		observableGraph.addVertex(aVertex);
		layout.initialize();
		relaxer.resume();

	}
	@Visible(false)
	@Override
	public void addAndLayoutVertex(VertexType aVertex) {

		// Relaxer relaxer = vv.getModel().getRelaxer();
		relaxer.pause();
		observableGraph.addVertex(aVertex);
		layout.initialize();
		// relaxer.resume();

	}

	@Visible(false)
	@Override
	public void removeAndUndisplayVertex(VertexType aVertex) {

		// Relaxer relaxer = vv.getModel().getRelaxer();
		relaxer.pause();
		observableGraph.removeVertex(aVertex);
		layout.initialize();
		relaxer.resume();

	}

	@Visible(false)
	@Override
	public void addAndDisplayEdge(EdgeType anEdge, VertexType aVertex1,
			VertexType aVertex2) {
		relaxer.pause();
		observableGraph.addEdge(anEdge, aVertex1, aVertex2);
		getGraphLayout().initialize();
		relaxer.resume();
	}

	@Visible(false)
	@Override
	public void removeAndDisplayEdge(EdgeType anEdge) {
		relaxer.pause();
		observableGraph.removeEdge(anEdge);
		getGraphLayout().initialize();
		relaxer.resume();
	}

	@Visible(false)
	@Override
	public void setVertexColors(VertexType aVertex, List<Color> aColors) {
		vertexToColors.put(aVertex, aColors);
	}

	@Visible(false)
	@Override
	public List<Color> getVertexColors(VertexType aVertex) {
		return vertexToColors.get(aVertex);
	}

	@Visible(false)
	@Override
	public void setVertexFillColor(VertexType aVertex, Paint aColor) {

		vertexFillColorer.setColor(aVertex, aColor);
	}

	@Visible(false)
	@Override
	public Paint getVertexFillColor(VertexType aVertex) {
		return vertexFillColorer.getColor(aVertex);
	}

	@Visible(false)
	@Override
	public void setEdgeDrawColor(EdgeType aVertex, Paint aColor) {

		edgeColorer.setColor(aVertex, aColor);
	}

	@Override
	@Visible(false)
	public Paint getEdgeDrawColor(EdgeType aVertex) {
		return edgeColorer.getColor(aVertex);
	}

	//
	// @Override
	// public void addChild(EdgeType anEdge, VertexType aParentVertex,
	// VertexType aChildVertex) {
	// addAndDisplayEdge(anEdge, aParentVertex, aChildVertex);
	// relaxer.pause();
	// observableGraph.addVertex(aChildVertex);
	// observableGraph.addEdge(anEdge, aParentVertex, aChildVertex);
	// layout.initialize();
	// relaxer.resume();
	// }

	@Visible(false)
	@Override
	public void renderGraph() {
		relaxer.resume();
	}

	@Visible(false)
	@Override
	public void setPostRenderer(VisualizationViewer.Paintable newVal) {
		jungGraphCustomization.setPostRenderer(newVal);
//		vv.addPostRenderPaintable(postRenderer);
	}

	@Override
	@Visible(false)
	public VisualizationViewer<VertexType, EdgeType> getVisualizationViewer() {
		return vv;
	}

	@Visible(false)
	@Override
	public void setVisualizationViewer(
			VisualizationViewer<VertexType, EdgeType> newVal) {
		vv = newVal;
	}

	@Visible(false)
	public TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, VertexType> getVertexIncludePredicate() {
		// vv.getRenderContext().getVertexIncludePredicate();
		return jungGraphCustomization.getVertexIncludePredicate();
	}

	@Visible(false)
	@Override
	public void setVertexIncludePredicate(
			TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, VertexType> newVal) {
		jungGraphCustomization.setVertexIncludePredicate(newVal);
//		vv.getRenderContext().setVertexIncludePredicate(newVal);
	}

	@Visible(false)
	@Override
	public void setEdgeIncludePredicate(
			TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, EdgeType> newVal) {
		jungGraphCustomization.setEdgeIncludePredicate(newVal);
//		vv.getRenderContext().setEdgeIncludePredicate(edgeIncludePredicate);

	}

	@Visible(false)
	@Override
	public void setVertexVisibile(VertexType aVertex, boolean newVal) {
		jungGraphCustomization.getVertexIncludePredicate().setIncludeGraphElement(aVertex, newVal);
	}

	@Visible(false)
	@Override
	public boolean getVertexVisibile(VertexType aVertex) {
		return jungGraphCustomization.getVertexIncludePredicate().getIncludeGraphElement(aVertex);
	}

	@Visible(false)
	@Override
	public void setEdgeVisibile(EdgeType aVertex, boolean newVal) {
		jungGraphCustomization.getEdgeIncludePredicate().setIncludeGraphElement(aVertex, newVal);
	}

	@Visible(false)
	@Override
	public boolean getEdgeVisibile(EdgeType aVertex) {
		return jungGraphCustomization.getEdgeIncludePredicate().getIncludeGraphElement(aVertex);
	}

	@Visible(false)
	public Shape getVertexShape(VertexType aVertex) {
		return getVertexShapeTransformer().transform(aVertex);
	}

	@Visible(false)
	public Point2D getLocation(VertexType aVertex) {
		return vv.getGraphLayout().transform(aVertex);
	}

	@Visible(false)
	@Override
	public Transformer<VertexType, Shape> getVertexShapeTransformer() {
		return vv.getRenderContext().getVertexShapeTransformer();
	}

	@Override
	@Visible(false)
	public Transformer<VertexType, Paint> getVertexFillPaintTransformer() {
		return vv.getRenderContext().getVertexFillPaintTransformer();
	}

	@Override
	@Visible(false)
	public Transformer<VertexType, Paint> getVertexDrawPaintTransformer() {
		return vv.getRenderContext().getVertexDrawPaintTransformer();
	}

	@Visible(false)
	public Transformer<VertexType, Stroke> getVertexStrokeTransformer() {
		return vv.getRenderContext().getVertexStrokeTransformer();
	}

	@Override
	@Visible(false)
	public Transformer<VertexType, Icon> getVertexIconTransformer() {
		return vv.getRenderContext().getVertexIconTransformer();
	}

	@Override
	@Visible(false)
	public Vertex<VertexType, EdgeType> getVertexRenderer() {
		return vv.getRenderer().getVertexRenderer();
	}

	@Override
	@Visible(false)
	public void setVertexRenderer(Vertex<VertexType, EdgeType> newVal) {
		vv.getRenderer().setVertexRenderer(newVal);
	}

	@Override
	@Visible(false)
	public VertexLabel<VertexType, EdgeType> getVertexLabelRenderer() {
		return vv.getRenderer().getVertexLabelRenderer();
	}

	@Override
	public void setVertexLabelRenderer(VertexLabel<VertexType, EdgeType> newVal) {
		vv.getRenderer().setVertexLabelRenderer(newVal);
	}

	@Override
	public void setVertexShapeTransformer(Transformer<VertexType, Shape> newVal) {
		vv.getRenderContext().setVertexShapeTransformer(newVal);
	}

	@Override
	@Visible(false)
	public Transformer<Context<Graph<VertexType, EdgeType>, EdgeType>, Shape> getEdgeShapeTransformer() {
		return vv.getRenderContext().getEdgeShapeTransformer();
	}

	@Override
	@Visible(false)
	public Transformer<EdgeType, Paint> getEdgeDrawPaintTransformer() {
		return (Transformer<EdgeType, Paint>) vv.getRenderContext()
				.getEdgeDrawPaintTransformer();
	}

	@Override
	public void setEdgeShapeTransformer(
			Transformer<Context<Graph<VertexType, EdgeType>, EdgeType>, Shape> newVal) {
		vv.getRenderContext().setEdgeShapeTransformer(newVal);

	}

	public void attachRenderContextTrapper(
			RenderContextTrapper<VertexType, EdgeType> newVal) {
		RenderContext<VertexType, EdgeType> oldContext = vv.getRenderContext();
		newVal.setRenderContext(oldContext);
		vv.setRenderContext(newVal);
	}

	public void detachRenderContextTrapper() {
		try {
			RenderContextTrapper<VertexType, EdgeType> trapper = (RenderContextTrapper<VertexType, EdgeType>) vv
					.getRenderContext();
			vv.setRenderContext(trapper.getRenderContext());

		} catch (ClassCastException e) {
			Tracer.error("No context trapper to detach");
		}
	}

	public void attachGraphDecoratorTrapper(AGraphicsDecoratorTrapper newVal) {
		GraphicsDecorator oldDecorator = vv.getRenderContext()
				.getGraphicsContext();
		newVal.setGraphicsDecorator(oldDecorator);
		vv.getRenderContext().setGraphicsContext(newVal);
	}

	public void detachGraphDecoratorTrapper() {
		try {
			AGraphicsDecoratorTrapper trapper = (AGraphicsDecoratorTrapper) vv
					.getRenderContext().getGraphicsContext();
			vv.getRenderContext().setGraphicsContext(
					trapper.getGraphicsDecorator());

		} catch (ClassCastException e) {
			Tracer.error("No context trapper to detach");
		}
	}

	@Override
	@Visible(false)
	public JungShapeModelDisplayer getJungShapeModelDisplayer() {
		return jungGraphCustomization.getJungShapeModelDisplayer();
	}

	@Override
	public void setVertexFillPaintTransformer(
			Transformer<VertexType, Paint> newVal) {
		vv.getRenderContext().setVertexFillPaintTransformer(newVal);
	}

	@Override
	public void setVertexDrawPaintTransformer(
			Transformer<VertexType, Paint> newVal) {
		vv.getRenderContext().setVertexDrawPaintTransformer(newVal);
	}

	@Override
	public void setEdgeDrawPaintTransformer(Transformer<EdgeType, Paint> newVal) {
		// vv.getRenderContext().setEdgeFillPaintTransformer(newVal);
		vv.getRenderContext().setEdgeDrawPaintTransformer(newVal);

	}

	@Override
	public void setVertexDrawColor(VertexType aVertex, Paint aColor) {
		vertexDrawColorer.setColor(aVertex, aColor);
	}

	@Override
	@Visible(false)
	public Paint getVertexDrawColor(VertexType aVertex) {
		// TODO Auto-generated method stub
		return vertexDrawColorer.getColor(aVertex);
	}

	@Override
	@Visible(false)
	public CompleteOEFrame getOEFrame() {
		return oeFrame;
	}

	@Override
	public void setOEFrame(CompleteOEFrame anOEFrame) {
		oeFrame = anOEFrame;
	}
	@Visible(false)
	public TableDrivenColorer<VertexType> getVertexFillColorer() {
		return vertexFillColorer;
	}

	public void setVertexFillColorer(
			TableDrivenColorer<VertexType> vertexFillColorer) {
		this.vertexFillColorer = vertexFillColorer;
		setVertexFillPaintTransformer(vertexFillColorer);

	}
	@Visible(false)
	public TableDrivenColorer<VertexType> getVertexDrawColorer() {
		return vertexDrawColorer;
	}

	@Visible(false)
	public void setVertexDrawColorer(
			TableDrivenColorer<VertexType> vertexDrawColorer) {
		this.vertexDrawColorer = vertexDrawColorer;
		setVertexDrawPaintTransformer(vertexDrawColorer);
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String aPropertyName = evt.getPropertyName();
		switch (aPropertyName) {
		case "@expand": expand();
		break;
		case "@collapse": collapse();
		break;
		case "@reset": reset();
		break;
		case "Scale": adjustScale((int) evt.getOldValue(), (int) evt.getNewValue());
		break;
		case "TreeNodeDistanceX":
		case "TreeNodeDistanceY":
		case "EdgeLength":
		case "LayoutType": maybeSetTreeLayout();
		break;
		case "MouseMode": processMouseMode((ModalGraphMouse.Mode) evt.getOldValue(), (Mode) evt.getNewValue());

		case "VertexLabelTransformer":
		case "VertexToolTipTransformer":
		case "EdgeToolTipTransformer":
		case "EdgeLabelTransformer":
		case "PostRenderer":
		case "VertexIncludePredicate":
		case "EdgeIncludePredicate":
		
		}
		
	}
	@Override
	@Visible(false)
	public JungGraphManagerCustomization<VertexType, EdgeType> getJungGraphCustomization() {
		return jungGraphCustomization;
	}
	@Override
	@Visible(false)
	public void setJungGraphCustomization(
			JungGraphManagerCustomization<VertexType, EdgeType> jungGraphCustomization) {
		this.jungGraphCustomization = jungGraphCustomization;
	}

}