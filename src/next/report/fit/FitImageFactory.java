package cern.accsoft.steering.aloha.report.fit;

import java.util.ArrayList;
import java.util.List;

public class FitImageFactory {

	public final static List<FitImage> createImageList() {
		List<FitImage> list = new ArrayList<FitImage>();
		list.add(new FitImage("this is the first test-image.",
				FitImageFactory.class.getResourceAsStream("example.jpg")));
		return list;
	}
}
