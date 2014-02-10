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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JRootPane;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import edu.uci.ics.jung.visualization.transform.MutableTransformerDecorator;
import edu.uci.ics.jung.algorithms.layout.Layout;


/**
 * Demonstrates visualization of a graph being actively updated.
 *
 * @author danyelf
 */
public class JungGraphApplet<VertexType, EdgeType> extends javax.swing.JApplet {

    /**
	 *
	 */
	private static final long serialVersionUID = -5345319851341875800L;

//	private ObservableGraph<VertexType, EdgeType> observableGraph = null;
//
//    private VisualizationViewer<VertexType, EdgeType> vv = null;
//
//    private Layout<VertexType, EdgeType> layout = null;
//
//    Timer timer;
//
//    boolean done;
//
//    protected JButton switchLayout;
//    public static int DEFAULT_DISTX = 100;
//    
//    /**
//     * The default vertical vertex spacing.  Initialized to 50.
//     */
//    public static int DEFAULT_DISTY = 100;
//
////    public static final LengthFunction<Number> UNITLENGTHFUNCTION = new SpringLayout.UnitLengthFunction<Number>(
////            100);
//    public static final int EDGE_LENGTH = 100;
//    Graph<VertexType, EdgeType> graph;
//    
//    Transformer<VertexType, String>  vertexLabelTransformer = new ToStringLabeller();
//    Transformer<VertexType, String>  vertexToolTipTransformer = vertexLabelTransformer;
//
//    Transformer<EdgeType, String> edgeToolTipTransformer  = new ToStringLabeller();
//    Transformer<EdgeType, String> edgeLabelTransformer  = edgeToolTipTransformer;
//    
//    boolean isForest;
    
//    public GenericJungGraphApplet(Graph<VertexType, EdgeType> aGraph, 
//    		Transformer<VertexType, String>  aVertexTransformer,
//    		Transformer<EdgeType, String> anEdgeTransformer, boolean anIsForest) {
//    	graph = aGraph;
//    	vertexLabelTransformer = aVertexTransformer;
//    	edgeLabelTransformer = anEdgeTransformer;
//    	isForest = anIsForest;
//    	
//    }
	
    @Override
    public void init() {

//        //create a graph
////    	Graph<VertexType, EdgeType> ig = Graphs.<VertexType, EdgeType>synchronizedDirectedGraph(new DirectedSparseMultigraph<VertexType, EdgeType>());
//
//        observableGraph = new ObservableGraph<VertexType, EdgeType>(graph);
//        observableGraph.addGraphEventListener(new GraphEventListener<VertexType, EdgeType>() {
//
//			public void handleGraphEvent(GraphEvent<VertexType, EdgeType> evt) {
//				System.err.println("got "+evt);
//
//			}});
////        this.observableGraph = og;
//        //create a graphdraw
//        if (!isForest)
//        layout = new FRLayout2<VertexType, EdgeType>(observableGraph);
//        else   
//        layout = new TreeLayout<VertexType, EdgeType> ((Forest<VertexType, EdgeType>) graph, DEFAULT_DISTX, DEFAULT_DISTY);
////        ((FRLayout)layout).setMaxIterations(200);
//        
       
//        vv = new VisualizationViewer<VertexType, EdgeType>(layout, new Dimension(600,600));

        JRootPane rp = this.getRootPane();
        rp.putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);

//        getContentPane().setLayout(new BorderLayout());
//        getContentPane().setBackground(java.awt.Color.lightGray);
//        getContentPane().setFont(new Font("Serif", Font.PLAIN, 12));
//        
//        if (!isForest)
//
//        vv.getModel().getRelaxer().setSleepTime(500);
//        vv.setGraphMouse(new DefaultModalGraphMouse<VertexType, EdgeType>());
//
//        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
//        vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);
//
//        vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);
////        vv.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer(vv.getPickedEdgeState(), Color.black, Color.cyan));
//        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.S);
//       
////        vv.setForeground(Color.white);
//        vv.setForeground(Color.black);
//
//        getContentPane().add(vv);
//        switchLayout = new JButton("Switch to SpringLayout");
//        switchLayout.addActionListener(new ActionListener() {
//
//            @SuppressWarnings("unchecked")
//            public void actionPerformed(ActionEvent ae) {
//            	Dimension d = new Dimension(600,600);
//                if (switchLayout.getText().indexOf("Spring") > 0) {
//                    switchLayout.setText("Switch to FRLayout");
//                    layout = new SpringLayout<VertexType, EdgeType>(observableGraph,
//                        new ConstantTransformer(EDGE_LENGTH));
//                    layout.setSize(d);
//                    vv.getModel().setGraphLayout(layout, d);
//                } else {
//                    switchLayout.setText("Switch to SpringLayout");
//                    layout = new FRLayout<VertexType, EdgeType>(observableGraph, d);
//                    vv.getModel().setGraphLayout(layout, d);
//                }
//            }
//        });
//
//        getContentPane().add(switchLayout, BorderLayout.SOUTH);
//
//        timer = new Timer();
    }

    @Override
    public void start() {
        validate();
        //set timer so applet will change
//        timer.schedule(new RemindTask(), 1000, 1000); //subsequent rate
//        vv.repaint();
    }

   
 
}