/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.trim;

import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.gui.display.DisplaySet;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.plugin.api.AbstractAlohaPlugin;
import cern.accsoft.steering.aloha.plugin.api.DisplaySetFactory;
import cern.accsoft.steering.aloha.plugin.api.ReaderProvider;
import cern.accsoft.steering.aloha.plugin.trim.display.TrimDisplaySet;
import cern.accsoft.steering.aloha.plugin.trim.meas.TrimMeasurement;
import cern.accsoft.steering.aloha.read.Reader;
import cern.accsoft.steering.aloha.read.yasp.YaspTrimDataReader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kfuchsbe
 * 
 */
public class TrimPlugin extends AbstractAlohaPlugin implements ReaderProvider,
		DisplaySetFactory {

	private List<Reader> readers = new ArrayList<Reader>();

	@InitMethod
	public void init() {
		this.readers
				.add(getAlohaBeanFactory().create(YaspTrimDataReader.class));
	}

	@Override
	public String getName() {
		return "Corrector Trim analysis";
	}

	@Override
	public List<Reader> getReaders() {
		return this.readers;
	}

	@Override
	public DisplaySet createDisplaySet(Measurement measurement) {
		if (!(measurement instanceof TrimMeasurement)) {
			return null;
		}

		return new TrimDisplaySet((TrimMeasurement) measurement);
	}

}
