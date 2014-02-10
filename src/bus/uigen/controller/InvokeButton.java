package bus.uigen.controller;import bus.uigen.uiFrame;
import bus.uigen.ars.*;import java.awt.Frame;import javax.swing.JButton;

public class InvokeButton extends JButton {
  private Frame frame;
  
  public InvokeButton(String name) {
    super(name);
  }

  public void setFrame(Frame f) {
    frame = f;
  }
  public Frame getFrame() {
    return frame;
  }
}
