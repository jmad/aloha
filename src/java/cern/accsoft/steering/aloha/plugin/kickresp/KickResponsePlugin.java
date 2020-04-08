package cern.accsoft.steering.aloha.plugin.kickresp;

import cern.accsoft.steering.aloha.analyzer.Analyzer;
import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.bean.aware.ChartFactoryAware;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.gui.display.DisplaySet;
import cern.accsoft.steering.aloha.gui.dv.ChartFactory;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.plugin.api.AbstractAlohaPlugin;
import cern.accsoft.steering.aloha.plugin.api.AnalyzerFactory;
import cern.accsoft.steering.aloha.plugin.api.DisplaySetFactory;
import cern.accsoft.steering.aloha.plugin.api.ReaderProvider;
import cern.accsoft.steering.aloha.plugin.api.SensitivityMatrixContributorFactory;
import cern.accsoft.steering.aloha.plugin.kickresp.analyzer.ResponseTrajAnalyzer;
import cern.accsoft.steering.aloha.plugin.kickresp.display.KickResponseDisplaySet;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurement;
import cern.accsoft.steering.aloha.plugin.kickresp.meas.KickResponseMeasurementImpl;
import cern.accsoft.steering.aloha.plugin.kickresp.read.yasp.YaspKickResponseDataReader;
import cern.accsoft.steering.aloha.plugin.kickresp.sensitivity.KickResponseSensitivityMatrixContributor;
import cern.accsoft.steering.aloha.read.Reader;

import java.util.ArrayList;
import java.util.List;

public class KickResponsePlugin extends AbstractAlohaPlugin implements
		SensitivityMatrixContributorFactory, DisplaySetFactory, AnalyzerFactory,
		ReaderProvider, ChartFactoryAware {

	/** The chart factory */
	private ChartFactory chartFactory;

	/** all the readers this plugin provides */
	private List<Reader> readers = new ArrayList<Reader>();

	/**
	 * this method is called automatically by the {@link AlohaBeanFactory}
	 */
	@InitMethod
	public void init() {
		this.readers.add(getAlohaBeanFactory().create(
				YaspKickResponseDataReader.class));
	}

	@Override
	public List<SensitivityMatrixContributor> createContributors(
			Measurement measurement) {
		List<SensitivityMatrixContributor> contributors = new ArrayList<SensitivityMatrixContributor>();
		if (measurement instanceof KickResponseMeasurement) {
			KickResponseSensitivityMatrixContributor kickResponseSensityMatrixContributor = this.alohaBeanFactory
					.create(KickResponseSensitivityMatrixContributor.class);
			kickResponseSensityMatrixContributor
					.setMeasurement((KickResponseMeasurementImpl) measurement);
			contributors.add(kickResponseSensityMatrixContributor);
		}
		return contributors;
	}

	@Override
	public DisplaySet createDisplaySet(Measurement measurement) {
		if (measurement instanceof KickResponseMeasurement) {
			KickResponseDisplaySet displaySet = new KickResponseDisplaySet(
					(KickResponseMeasurementImpl) measurement, getChartFactory());
			getAlohaBeanFactory().configure(displaySet);
			return displaySet;
		}
		return null;
	}

	@Override
	public List<Analyzer> createAnalyzers(Measurement measurement) {
		List<Analyzer> analyzers = new ArrayList<Analyzer>();
		if (measurement instanceof KickResponseMeasurement) {
			// NormalizedResponseAnalyzer a1 =
			// createNormalizedResponseAnalyzer();
			// a1.setMeasurement((KickResponseMeasurement) measurement);
			// analyzers.add(a1);
			//
			// NormalizedResponseDiffAnalyzer a2 =
			// createNormalizedResponseDiffAnalyzer();
			// a2.setMeasurement((KickResponseMeasurement) measurement);
			// analyzers.add(a2);

			ResponseTrajAnalyzer a3 = getAlohaBeanFactory().create(
					ResponseTrajAnalyzer.class);
			a3.setMeasurement((KickResponseMeasurement) measurement);
			analyzers.add(a3);
		}
		return analyzers;
	}

	@Override
	public String getName() {
		return "Yasp kickrespons analysis";
	}

	@Override
	public void setChartFactory(ChartFactory chartFactory) {
		this.chartFactory = chartFactory;
	}

	private ChartFactory getChartFactory() {
		return this.chartFactory;
	}

	@Override
	public List<Reader> getReaders() {
		return this.readers;
	}

}
