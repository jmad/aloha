package cern.accsoft.steering.aloha.gui.panels.fit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import cern.accsoft.steering.aloha.calc.variation.VariationData;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import com.csvreader.CsvWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultExportActionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(ResultExportActionHandler.class);

    enum ExportData {
        MONITOR_GAINS("monitor gains"), CORRECTOR_GAINS("corrector gains"), VARIED_PARAMETERS("varied parameters");

        private final String displayName;

        ExportData(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

    }

    private MachineElementsManager machineElementsManager;
    private VariationData variationData;

    public void exportToCsv(ExportData exportData, File destination) {
        LOGGER.info("Exporting {} to {}", exportData.getDisplayName(), destination);
        try {
            if (exportData == ExportData.MONITOR_GAINS) {
                exportValues(destination,  //
                        machineElementsManager.getActiveMonitorNames(), //
                        machineElementsManager.getActiveMonitorGains(), //
                        machineElementsManager.getActiveMonitorGainErrors());
            } else if (exportData == ExportData.CORRECTOR_GAINS) {
                exportValues(destination,  //
                        machineElementsManager.getActiveCorrectorNames(), //
                        machineElementsManager.getActiveCorrectorGains(), //
                        machineElementsManager.getActiveCorrectorGainErrors());
            } else if (exportData == ExportData.VARIED_PARAMETERS) {
                exportValues(destination, //
                        variationData.getVariationParameterNames(), //
                        variationData.getVariationParameterValues(), //
                        variationData.getVariationParameterValueErrors());
            } else {
                throw new IllegalArgumentException("Unknown export type: " + exportData);
            }
            LOGGER.info("Export complete!");
        } catch (Exception e) {
            LOGGER.error("Error exporting to {}: {}", destination, e.getMessage(), e);
        }
    }

    private void exportValues(File destination, List<String> names, List<Double> values, List<Double> errors)
            throws IOException {
        try (FileWriter fileWriter = new FileWriter(destination)) {
            CsvWriter csvWriter = new CsvWriter(fileWriter, ',');
            csvWriter.writeComment("Name,Value,Error");
            for (int i = 0; i < names.size(); i++) {
                csvWriter.writeRecord( //
                        new String[] { //
                                names.get(i), //
                                values.get(i).toString(), //
                                errors.get(i).toString() //
                        });
            }
            csvWriter.close();
        }
    }

    public void setMachineElementsManager(MachineElementsManager machineElementsManager) {
        this.machineElementsManager = machineElementsManager;
    }

    public void setVariationData(VariationData variationData) {
        this.variationData = variationData;
    }
}
