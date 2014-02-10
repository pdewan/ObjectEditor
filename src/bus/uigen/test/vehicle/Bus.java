package bus.uigen.test.vehicle;

public interface Bus {

	public Shape getBody();

	public void setBody(Shape body);

	public Shape getFrontTire();

	public void setFrontTire(Shape frontTire);

	public Shape getBackTire();

	public void setBackTire(Shape backTire);

	public int getBusWidth();

	public void setBusWidth(int busWidth);

	public int getBusX();

	public void setBusX(int busX);

	public int getBusY();

	public void setBusY(int busY);

	public void move(int increment);

	public void moveLeft();

	public void moveRight();

	public void magnify();
	
	public void shrink();

}