package bus.uigen.jung;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;

public enum LayoutType {
	Balloon,
	Tree,	
	Radial,
	Spring,
	Spring2,
	FruchtermanReingold,
	KamadaKawai,
	MeyersSelfOrganizing	
}
