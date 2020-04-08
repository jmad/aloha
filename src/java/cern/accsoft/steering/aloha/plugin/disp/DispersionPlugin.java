/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.disp;

import cern.accsoft.steering.aloha.analyzer.Analyzer;
import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.plugin.api.AbstractAlohaPlugin;
import cern.accsoft.steering.aloha.plugin.api.AnalyzerFactory;
import cern.accsoft.steering.aloha.plugin.api.ReaderProvider;
import cern.accsoft.steering.aloha.plugin.api.SensitivityMatrixContributorFactory;
import cern.accsoft.steering.aloha.plugin.disp.analyzer.DispersionAnalyzer;
import cern.accsoft.steering.aloha.plugin.disp.analyzer.DispersionMuAnalyzer;
import cern.accsoft.steering.aloha.plugin.disp.analyzer.NormalizedDispersionDiffAnalyzer;
import cern.accsoft.steering.aloha.plugin.disp.meas.DispersionMeasurement;
import cern.accsoft.steering.aloha.plugin.disp.meas.DispersionMeasurementImpl;
import cern.accsoft.steering.aloha.plugin.disp.read.yasp.YaspDispersionMeasurementReader;
import cern.accsoft.steering.aloha.plugin.disp.sensitivity.DispersionSensitivityMatrixContributor;
import cern.accsoft.steering.aloha.read.Reader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kfuchsbe
 */
public class DispersionPlugin extends AbstractAlohaPlugin implements SensitivityMatrixContributorFactory,
        AnalyzerFactory, ReaderProvider {

    /** all the readers provided by this plugin */
    private List<Reader> readers = new ArrayList<Reader>();

    /**
     * this method will be called automatically by the {@link AlohaBeanFactory}
     */
    @InitMethod
    public void init() {
        this.readers.add(getAlohaBeanFactory().create(YaspDispersionMeasurementReader.class));
    }

    @Override
    public List<SensitivityMatrixContributor> createContributors(Measurement measurement) {
        List<SensitivityMatrixContributor> contributors = new ArrayList<SensitivityMatrixContributor>();
        if (measurement instanceof DispersionMeasurement) {

            DispersionSensitivityMatrixContributor dispersionSensityMatrixContributor = this.alohaBeanFactory
                    .create(DispersionSensitivityMatrixContributor.class);
            dispersionSensityMatrixContributor.setDispersionMeasurement((DispersionMeasurementImpl) measurement);
            contributors.add(dispersionSensityMatrixContributor);
        }
        return contributors;
    }

    @Override
    public List<Analyzer> createAnalyzers(Measurement measurement) {
        List<Analyzer> analyzers = new ArrayList<Analyzer>();
        if (measurement instanceof DispersionMeasurement) {
            DispersionAnalyzer a1 = getAlohaBeanFactory().create(DispersionAnalyzer.class);
            a1.setMeasurement((DispersionMeasurement) measurement);
            analyzers.add(a1);

            DispersionMuAnalyzer a2 = getAlohaBeanFactory().create(DispersionMuAnalyzer.class);
            a2.setMeasurement((DispersionMeasurement) measurement);
            analyzers.add(a2);

            NormalizedDispersionDiffAnalyzer a3 = getAlohaBeanFactory().create(NormalizedDispersionDiffAnalyzer.class);
            a3.setMeasurement((DispersionMeasurement) measurement);
            analyzers.add(a3);
        }
        return analyzers;
    }

    @Override
    public String getName() {
        return "Yasp dispersion analysis";
    }

    @Override
    public List<Reader> getReaders() {
        return this.readers;
    }

}
