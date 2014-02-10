package bus.uigen.view;
public class AClassWidgetPair {
	Object component;
	Object widget;
	public AClassWidgetPair(Object theComponent, Object theWidget) {
		component = theComponent;
		widget = theWidget;
	}
	public boolean equals(AClassWidgetPair otherObject) {
		return component.equals(otherObject.component) && 
			   widget.equals(otherObject.widget);
		
	}
}