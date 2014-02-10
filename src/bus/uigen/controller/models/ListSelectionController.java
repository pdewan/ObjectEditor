package bus.uigen.controller.models;

import util.models.AListenableString;
import util.models.VectorListener;

public interface ListSelectionController extends VectorListener {

	public void all();

	public String getSelected();

	public AListenableString getSearch();

	public void setSearch(AListenableString newVal);

}