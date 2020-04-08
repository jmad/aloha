/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.sensity;

import Jama.Matrix;
import cern.accsoft.steering.aloha.calc.sensitivity.PerturbedColumn;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixContributor;
import cern.accsoft.steering.aloha.meas.Measurement;

/**
 * @author kfuchsbe
 * 
 */
public class MultiturnPhaseSensityMatrixContributor implements
		SensitivityMatrixContributor {

//	private void calcPhase() {
//		try {
//			for (int i = 0; i < numberBunches; i++) {
//				bunches[i] = (double) (DataHandler.getBunchSet()
//						.getBunchName(i));
//				names[i] = new String("Bunch "
//						+ (DataHandler.getBunchSet().getBunchName(i)));
//				if (monitorIndex == 0) {
//					mux1 = MonitorHandler.getMonitorList(beam)
//							.get(monitorIndex).getAnalysisManager()
//							.getFFT1Ddata().getPhaseAdvanceX(i);
//					muy1 = MonitorHandler.getMonitorList(beam)
//							.get(monitorIndex).getAnalysisManager()
//							.getFFT1Ddata().getPhaseAdvanceY(i);
//					mux0 = mux1;
//					muy0 = muy1;
//
//					phaseH[i] = mux1 - mux0;
//					phaseV[i] = muy1 - muy0;
//				} else {
//					mux1 = MonitorHandler.getMonitorList(beam)
//							.get(monitorIndex).getAnalysisManager()
//							.getFFT1Ddata().getPhaseAdvanceX(i);
//					mux0 = MonitorHandler.getMonitorList(beam).get(
//							monitorIndex - monitorDifference)
//							.getAnalysisManager().getFFT1Ddata()
//							.getPhaseAdvanceX(i);
//
//					muy1 = MonitorHandler.getMonitorList(beam)
//							.get(monitorIndex).getAnalysisManager()
//							.getFFT1Ddata().getPhaseAdvanceY(i);
//					muy0 = MonitorHandler.getMonitorList(beam).get(
//							monitorIndex - monitorDifference)
//							.getAnalysisManager().getFFT1Ddata()
//							.getPhaseAdvanceY(i);
//
//					if ((mux0 + 0.5) < mux1) {
//						phaseH[i] = mux1 - (mux0 + 1.);
//					} else if ((mux0 - 0.5) >= mux1) {
//						phaseH[i] = mux1 - (mux0 - 1.);
//					} else {
//						phaseH[i] = mux1 - mux0;
//					}
//
//					if ((muy0 + 0.5) < muy1) {
//						phaseV[i] = muy1 - (muy0 + 1.);
//					} else if ((muy0 - 0.5) >= muy1) {
//						phaseV[i] = muy1 - (muy0 - 1.);
//					} else {
//						phaseV[i] = muy1 - muy0;
//					}
//				}
//			}
//
//			plotPhaseH.rescale();
//			plotPhaseV.rescale();
//			plotPhaseH.updateWithNames(bunches, phaseH, names);
//			plotPhaseV.updateWithNames(bunches, phaseV, names);
//			dvPhaseH.setName("Horizontal Phase Difference to Monitor before: "
//					+ MonitorHandler.getNameArray(beam)[monitorIndex]);
//			dvPhaseV.setName("Vertical Phase Difference to Monitor before: "
//					+ MonitorHandler.getNameArray(beam)[monitorIndex]);
//
//		} catch (Exception ex) {
//			MessageManager.error("Cannot plot phase advance", ex, null);
//		}
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecern.accsoft.steering.aloha.calc.sensity.SensityMatrixContributor#
	 * calcCorrectorSensityMatrix()
	 */
	@Override
	public Matrix calcCorrectorSensityMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecern.accsoft.steering.aloha.calc.sensity.SensityMatrixContributor#
	 * calcMonitorSensityMatrix()
	 */
	@Override
	public Matrix calcMonitorSensityMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecern.accsoft.steering.aloha.calc.sensity.SensityMatrixContributor#
	 * calcPerturbedColumn(double, java.lang.Double)
	 */
	@Override
	public PerturbedColumn calcPerturbedColumn(double delta,
			Double normalizationFactor) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecern.accsoft.steering.aloha.calc.sensity.SensityMatrixContributor#
	 * getDifferenceVector()
	 */
	@Override
	public Matrix getDifferenceVector() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecern.accsoft.steering.aloha.calc.sensity.SensityMatrixContributor#
	 * getMatrixRowCount()
	 */
	@Override
	public int getMatrixRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecern.accsoft.steering.aloha.calc.sensity.SensityMatrixContributor#
	 * getMeasurement()
	 */
	@Override
	public Measurement getMeasurement() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cern.accsoft.steering.aloha.calc.sensity.SensityMatrixContributor#getName
	 * ()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecern.accsoft.steering.aloha.calc.sensity.SensityMatrixContributor#
	 * initUnperturbed()
	 */
	@Override
	public void initUnperturbed() {
		// TODO Auto-generated method stub

	}

	@Override
	public Matrix getDifferenceVectorErrors() {
		// TODO Auto-generated method stub
		return null;
	}

}
