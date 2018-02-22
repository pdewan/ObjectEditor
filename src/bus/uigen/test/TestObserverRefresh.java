package bus.uigen.test;
 
import java.awt.Color;
import java.awt.Point;
 
import shapes.FlexibleShape;
import util.models.AListenableVector;
import util.models.ListenableVector;
import util.trace.TraceableLog;
import util.trace.TraceableLogFactory;
import bus.uigen.ObjectEditor;
import bus.uigen.shapes.ARectangleModel;
 
public class TestObserverRefresh {
     
      public static void main(String[] args){
    	  TraceableLogFactory.setEnableTraceableLog(true);
    	  TraceableLog log = TraceableLogFactory.getTraceableLog();
    	  TestObserverRefresh obj = new TestObserverRefresh();
          ObjectEditor.edit(obj);
          System.out.println(log.getLog());
//          obj.changeColor();

           
//            ObjectEditor.edit(new TestObsreverRefresh());
      }
      private ListenableVector<FlexibleShape> shapes = new AListenableVector<FlexibleShape>();
      private FlexibleShape r = new ARectangleModel(20,20,20,20);
     
      public TestObserverRefresh(){
            shapes.add(r);
//            r.setFilled(true);
            r.setColor(Color.green);
      }
      public ListenableVector<FlexibleShape> getShapes(){
            return shapes;
      }
      public void changeColor(){
           
            shapes.get(0).setColor(Color.red);
            try {
            	Thread.sleep(1000);
            } catch (Exception e) {
            	
            }
            System.out.println("Changed color");
//            shapes.get(0).setPosition(new Point(30,30));
//            shapes.get(0).setColor(Color.blue);
//            shapes.get(0).setPosition(new Point(40,40));
//            shapes.get(0).setColor(Color.black);
//            shapes.get(0).setPosition(new Point(50,50));
      }
}
