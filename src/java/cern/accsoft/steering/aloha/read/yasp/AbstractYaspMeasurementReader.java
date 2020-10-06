package cern.accsoft.steering.aloha.read.yasp;

import java.io.File;
import java.net.URI;
import java.util.List;

import cern.accsoft.steering.aloha.meas.ModelAwareMeasurement;
import cern.accsoft.steering.aloha.read.MeasurementReader;
import cern.accsoft.steering.util.meas.data.yasp.ReadingData;
import cern.accsoft.steering.util.meas.data.yasp.YaspHeader;
import cern.accsoft.steering.util.meas.data.yasp.YaspHeaderData;
import cern.accsoft.steering.util.meas.read.yasp.YaspFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractYaspMeasurementReader<T extends ModelAwareMeasurement> implements MeasurementReader<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractYaspMeasurementReader.class);

    @Override
    public URI proposedModelDefinitionUri(List<File> files) {
        if (files.isEmpty()) {
            return null;
        }
        try {
            ReadingData data = new YaspFileReader().read(files.get(0), null);
            if (data == null) {
                LOGGER.warn("Proposing no model URI: can not open {} as YASP file", files.get(0));
                return null;
            }
            YaspHeader header = data.getHeader();
            String modelUri = header.getStringData(YaspHeaderData.MODEL_URI);
            if (modelUri == null || modelUri.isEmpty() || modelUri.equals("null")) {
                LOGGER.info("Found no model URI.");
                return null;
            }
            LOGGER.info("Proposing URI: {}", modelUri);
            return URI.create(modelUri);
        } catch (Exception e) {
            LOGGER.error("Proposing no model URI: {}", e.getMessage(), e);
            return null;
        }
    }
}
