package bus.uigen.controller;
import java.lang.reflect.Method;

public interface MethodLogger {
  public void methodCalled(Object target,
			   Method method,
			   Object[] args);
}
