/*
 * Created on Aug 17, 2003
 *
 * 
 */
package cern.accsoft.steering.aloha.calc.solve.matrix.micado;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class was copied directly from Yasp (original author jwenning) and
 * adopted for our purposes. All the Yasp-specific stuff was thrown away and we
 * only kept the basic MIKADO algorithm (plus some stuff we do not want to touch ;-).
 * 
 * The names might be sometimes confusing, since in aloha we are not restricted
 * to corrector kicks only. So in our sense, everytime 'kicks' are mentioned,
 * they are fit - 'parameters' for aloha in general. All values referred as 
 * 'bpm-readings' or 'orbit' are general elements of the difference vector between 
 * measurement and model in aloha.
 * 
 * @author kfuchsbe
 */

/**
 * This class implements the MICADO algorithm.
 * 
 * @author jwenning
 */
public class MICADO {
	private final static Logger LOGGER = LoggerFactory.getLogger(MICADO.class);

	private static final double NUM_CUT = 1.0e-6;

	//
	// Now come the MICADO internal methods - DO NOT touch !
	//

	/**
	 * Householder transformations : Householder transformation on response
	 * matrix a
	 * 
	 * @param m
	 *            = # monitors
	 * @param n
	 *            = # correctors
	 * @param k
	 *            = iteration-1/line #
	 */

	private static void htal(double[][] a, int m, int n, int k,
							 double beta) {
		int j, nc, k1, k2;
		double h;

		nc = n - (k + 1);

		for (j = 0; j < nc; j++) {

			h = 0.0;
			k2 = k + j + 1;

			for (k1 = k; k1 < m; k1++) {
				h += a[k1][k] * a[k1][k2];
			}

			h *= beta;

			for (k1 = k; k1 < m; k1++) {
				a[k1][k2] -= a[k1][k] * h;
			}
		}
	}

	/**
	 * Householder transformations : Householder transformation of vector b
	 * 
	 * @param m
	 *            = # monitors
	 * @paramn = # correctors
	 * @paramk = from 0 to iteration-1/line #
	 */

	private static void htbl(double[][] a, double[] b, int m, int n,
							 int k, double beta) {
		int k1;
		double h;

		h = 0.0;

		for (k1 = k; k1 < m; k1++) {
			h += a[k1][k] * b[k1];
		}

		h *= beta;

		for (k1 = k; k1 < m; k1++) {
			b[k1] -= a[k1][k] * h;
		}
	}

	/**
	 * Householder transformations : Calculate residual orbit vector rho must
	 * have dimension 3*n
	 * 
	 * @param m
	 *            = # monitors
	 * @param n
	 *            = # correctors
	 * @param k
	 *            = iteration-1/line #
	 */
	private static void htrl(double[][] a, double[] b, int m, int n,
							 int k, double[] rho) {
		int i, kk, kn, kl;
		double beta;

		for (i = 0; i <= k; i++)
			b[i] = 0;

		for (kk = 0; kk <= k; kk++) {

			kl = k - kk;
			kn = kl + n;

			beta = -1 / (rho[kn] * a[kl][kl]);
			htbl(a, b, m, n, kl, beta);
		}
	}

	/**
	 * Householder transformations : Calculate vector u
	 * 
	 * @param m
	 *            = # monitors
	 * @param m
	 *            = # correctors
	 * @param k
	 *            = iteration-1/line #
	 */
	private static double htul(double[][] a, int m, int k) {
		int i;
		double sig;

		sig = 0.0;
		for (i = k; i < m; i++)
			sig += a[i][k] * a[i][k];

		sig = Math.sqrt(sig);

		/* choose proper sign */

		if (a[k][k] < 0)
			sig *= -1;

		return (sig);
	}

	/**
	 * return RMS for vector "vec"
	 * 
	 * @param n0
	 *            = first element of vec to consider
	 * @param n
	 *            = # of elements of vec to consider
	 */

	private static double micado_rms(double[] vec, int n0, int n) {
		int i, imax;
		double mean, mean2, nn;

		if (n <= 0)
			return (0.0);

		imax = n0 + n;
		mean = mean2 = nn = 0;
		for (i = n0; i < imax; i++) {
			mean += vec[i];
			mean2 += vec[i] * vec[i];
			nn += 1;
		}

		if (nn == 0)
			return (0.0);

		mean /= nn;
		mean2 /= nn;
		mean2 -= Math.pow(mean, 2);

		if (mean2 > 0)
			mean2 = Math.sqrt(mean2);
		else
			mean2 = 0;

		return (mean2);
	}

	/**
	 * return RMS for vector "vec", but as the sqrt of the variance (without
	 * taking into account mean value --> for kicks !).
	 * 
	 * @param n0
	 *            = first element of vec to consider
	 * @param n
	 *            = # of elements of vec to consider
	 */

	private static double micado_variance(double[] vec, int n0, int n) {
		int i, imax;
		double mean2, nn;

		if (n <= 0)
			return (0.0);

		imax = n0 + n;
		mean2 = nn = 0;
		for (i = n0; i < imax; i++) {
			mean2 += vec[i] * vec[i];
			nn += 1;
		}

		if (nn == 0)
			return (0.0);

		mean2 = Math.sqrt(mean2 / nn);

		return (mean2);
	}

	/**
	 * MICADO minimization :
	 * 
	 * @param a
	 *            : response matrix (correctors -&gt; orbit)
	 * @param b
	 *            : input orbit (to be corrected)
	 * @param m
	 *            : # of monitors (or lines !)
	 * @param n
	 *            : # of correctors
	 * @param iter
	 *            : # of iterations
	 * @param dyw
	 *            : weigth factor for the dispersion
	 * @param kiw
	 *            : weigth factor for the kicks
	 * @param x
	 *            : strength of the correctors
	 * @param ipiv
	 *            : sequence of the correctors
	 * @param r
	 *            : residual orbit
	 */
	public static final boolean solveMicado(double[][] a, double[] b, int m,
			int n, int iter, double dyw, double kiw, double[] x, int[] ipiv,
			double[] r, double[] pivots) {
		int i, j, j1, k, k2, k3, ki, kk;
		int kpiv, ip;
		double[] rho;
		double g, h, sig, beta;
		double piv, pivt;
		// the rms vector - the 0'th entry is the initial rms
		double[][] rms = new double[iter + 1][3];

		rho = new double[3 * n];
		for (j = 0; j < 3 * n; j++)
			rho[j] = 0;
		for (j = 0; j < n; j++)
			x[j] = 0;

		/* Set the pivot-data to zero */
		for (j = 0; j < n; j++) {
			pivots[j] = 0.0;
		}

		/* we first evaluate the input rms of r */

		if (kiw > 0)
			j = n;
		else
			j = 0;

		if (dyw > 0 && dyw < 1) {
			kk = (m - j) / 2;
			i = 0;
			h = micado_rms(b, i, kk); /* orbit ... */
			rms[0][0] = h / (1.0 - dyw);
			i = (m - j) / 2;
			h = micado_rms(b, i, kk); /* dy ... */
			rms[0][1] = h / dyw;
		} else if (dyw == 0) {
			i = 0;
			kk = m - j;
			h = micado_rms(b, i, kk); /* orbit ... */
			rms[0][0] = h;
			rms[0][1] = 0;
		} else if (dyw == 1) {
			i = 0;
			kk = m - j;
			h = micado_rms(b, i, kk); /* dy ... */
			rms[0][1] = h;
			rms[0][0] = 0;
		}

		rms[0][2] = 0; /* kicks ... */

		/* find the best (and first) pivot */

		piv = 0.0;
		kpiv = 0;

		for (k = 0; k < n; k++) {
			ipiv[k] = k;

			h = g = 0.0;

			for (i = 0; i < m; i++) {
				h += a[i][k] * a[i][k];
				g += a[i][k] * b[i];
			}

			rho[k] = h;
			rho[k + n] = g;

			pivt = g * g / h;

			if (pivt > piv) {
				piv = pivt;
				kpiv = k;
			}
		}

		/* main iteration loop of MICADO */

		for (k = 0; k < iter; k++) {

			/* remember the pivot */
			pivots[k] = piv;

			/*
			 * exchange row k with row kpiv. we want the most efficient pivot
			 */

			if (kpiv != k) {

				h = rho[k];
				rho[k] = rho[kpiv];
				rho[kpiv] = h;

				k2 = n + k;
				k3 = n + kpiv;

				g = rho[k2];
				rho[k2] = rho[k3];
				rho[k3] = g;

				/* exchange also columns of matrix a ! */

				for (i = 0; i < m; i++) {
					h = a[i][k];
					a[i][k] = a[i][kpiv];
					a[i][kpiv] = h;
				}
			}

			/* build vector u for householder transform */

			sig = htul(a, m, k);

			beta = a[k][k] + sig;
			a[k][k] = beta;
			beta = 1.0 / (sig * beta);

			j = n + k;
			rho[j] = -sig;

			ip = ipiv[kpiv];
			ipiv[kpiv] = ipiv[k];
			ipiv[k] = ip;

			/* we have not exhaused the correctors */

			/* we transform a and b by Householder */

			if (k < n - 1) {

				htal(a, m, n, k, beta);

			}

			htbl(a, b, m, n, k, beta);

			/*
			 * find next pivot ! -----------------------------
			 */

			rho[k] = Math.sqrt(piv);

			/* we have not exhaused the correctors */

			if (k < n - 1) {

				piv = 0.0;

				kpiv = k + 1;

				j1 = kpiv;

				for (j = j1; j < n; j++) {
					h = rho[j] - a[k][j] * a[k][j];

					if (h < NUM_CUT) { // flag the suspicious elements
						LOGGER.warn("MICADO problem : suspected parameter. # "
								+ j);
						// return false;
						continue;
					}

					rho[j] = h;

					k2 = n + j;
					g = rho[k2] - a[k][j] * b[k];
					rho[k2] = g;

					pivt = g * g / h;

					if (pivt >= piv) {
						kpiv = j;
						piv = pivt;
					}
				}
			}

			/* get corrections x */

			x[k] = b[k] / rho[n + k];

			/* reconstruct other kicks if iter > 1 */

			if (k > 0) {

				for (i = 1; i <= k; i++) {

					kk = k - i;
					x[kk] = b[kk];
					ki = kk + 1;

					for (j = ki; j <= k; j++) {
						x[kk] -= a[kk][j] * x[j];
					}
					x[kk] /= rho[n + kk];
				}
			}

			/* save residual orbit and invert signs */

			for (i = 0; i < m; i++)
				r[i] = b[i];
			for (i = 0; i <= k; i++)
				x[i] *= -1;

			/* transform orbit r back to normal space */

			htrl(a, r, m, n, k, rho);

			/*
			 * we could now evaluate the rms of r PLEASE NOTE that we fill
			 * element k+1 which corresponds to the real iter # (no C offset
			 * ...)
			 */

			if (kiw > 0)
				j = n;
			else
				j = 0;

			if (dyw > 0 && dyw < 1) {
				kk = (m - j) / 2;
				i = 0;
				h = micado_rms(r, i, kk); /* orbit ... */
				rms[k + 1][0] = h / (1.0 - dyw);
				i = (m - j) / 2;
				h = micado_rms(r, i, kk); /* dy ... */
				rms[k + 1][1] = h / dyw;
			} else if (dyw == 0) {
				i = 0;
				kk = m - j;
				h = micado_rms(r, i, kk); /* orbit ... */
				rms[k + 1][0] = h;
				rms[k + 1][1] = 0;
			} else if (dyw == 1) {
				i = 0;
				kk = m - j;
				h = micado_rms(r, i, kk); /* dy ... */
				rms[k + 1][1] = h;
				rms[k + 1][0] = 0;
			}

			i = 0;
			j = k + 1;
			h = micado_variance(x, i, j); /* kicks ... */
			rms[k + 1][2] = h;

		}
		rho = null;

		/* over */
		return true;
	}
}