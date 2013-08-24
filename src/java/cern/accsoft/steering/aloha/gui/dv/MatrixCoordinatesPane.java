package cern.accsoft.steering.aloha.gui.dv;

import cern.accsoft.steering.util.gui.dv.ds.MatrixDataSet;
import cern.jdve.data.DataSet;
import cern.jdve.interactor.CoordinatesPane;
import cern.jdve.utils.DisplayPoint;

public class MatrixCoordinatesPane extends CoordinatesPane {

	private static final long serialVersionUID = 1L;


	/**
	   * Computes label for Y coordinate. 
	   */
	  protected String computeYLabel(DisplayPoint displayPoint) {
		  DataSet dataset = displayPoint.getDataSet();
		  
		  if (dataset instanceof MatrixDataSet) {
			  String label = ((MatrixDataSet) dataset).getYLabel((int) displayPoint.getY());
			  if (label != null) {
				  return label;
			  }
		  }
		  
		  return super.computeYLabel(displayPoint);
	  }
	  
	  
	  /**
	   * Computes label for X coordinate. 
	   */
	  protected String computeXLabel(DisplayPoint displayPoint) {
		  DataSet dataset = displayPoint.getDataSet();
		  
		  if (dataset instanceof MatrixDataSet) {
			  String label = ((MatrixDataSet) dataset).getXLabel((int) displayPoint.getX());
			  if (label != null) {
				  return label;
			  }
		  }
		  return super.computeXLabel(displayPoint);
	  }
}
