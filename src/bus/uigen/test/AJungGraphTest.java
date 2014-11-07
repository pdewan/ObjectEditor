package bus.uigen.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Context;
import bus.uigen.ObjectEditor;
import bus.uigen.jung.AJungGraphManager;
import bus.uigen.jung.JungGraphManager;
import bus.uigen.jung.LayoutType;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.shapes.AnOvalModel;
import bus.uigen.shapes.OEShapeModel;

public class AJungGraphTest {
	public static void main (String[] args) {
		SquaringCounterWithButtons counter = new SquaringCounterWithButtons();
		JPanel textPanel = new JPanel();
		JPanel jungPanel = new JPanel();
		JFrame frame=  new JFrame();
		frame.setLayout(new BorderLayout());
//		JTextField textField = new JTextField("foo");
//		JPanel textPanel = new JPanel();
		frame.add(textPanel, BorderLayout.NORTH);
		frame.add(jungPanel, BorderLayout.CENTER);
		Graph<String, String> aGraph = new UndirectedSparseMultigraph<>();
		String aNode = "A";
		String bNode = "B";
		String cNode = "C";
		String dNode = "D";
//		aGraph.addVertex(aNode);
//		aGraph.addVertex(bNode);
//		aGraph.addVertex(cNode);
//		aGraph.addVertex(dNode);
		
		aGraph.addEdge("A2B", aNode, bNode );		
		aGraph.addEdge("C2D", cNode, dNode );
		aGraph.addEdge("A2C", aNode, cNode );
		ObservableGraph anObservableGraph = new ObservableGraph<>(aGraph);
		JungGraphManager jungGraphManager = new AJungGraphManager<>(aGraph,
				jungPanel);
		List<Color> aColors = new ArrayList();
		aColors.add(Color.RED);
		aColors.add(Color.GREEN);
		jungGraphManager.setColors(aNode, aColors);
//		Transformer<String, Shape> vertexTransformer = jungGraphManager.getVertexShapeTransformer();
//		Transformer<Context<Graph<String, String>, String>, Shape> edgeTransformer = jungGraphManager.getEdgeShapeTransformer();
		OEShapeModel oeShapeModel = new AnOvalModel();
		oeShapeModel.setBounds(new Rectangle(0, 0, 100, 100));
		System.out.println("Adding oval to jung graph");
		jungGraphManager.getJungShapeModelDisplayer().getShapes().add(oeShapeModel);
//		shapes.add(oeShapeModel);
		jungGraphManager.setLayoutType(LayoutType.Spring);
		ObjectEditor.editInMainContainer(counter, textPanel);

		frame.setSize(600, 600);
		frame.setVisible(true);

		ObjectEditor.edit(jungGraphManager);
	}

}
