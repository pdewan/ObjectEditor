package bus.uigen.jung;
import java.awt.Paint;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections15.Transformer;

public interface TableDrivenColorer<ElementType> extends Transformer<ElementType, Paint> {
	void setColor(ElementType anElement, Paint aPaint);
	Paint getColor(ElementType anElement);
	void setDefaultColor(Paint aPaint);
	Paint getDefaultColor();

}
