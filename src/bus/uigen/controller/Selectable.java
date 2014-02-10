package bus.uigen.controller;import bus.uigen.uiFrame;

public interface Selectable {
  public void select();
  public void unselect();
  public String getChildSelectableIndex(Selectable child);
  public Selectable getChildSelectable(String index);
  public Selectable getParentSelectable();
  public Object getObject();
}
