package cern.accsoft.steering.aloha.thirdparty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import Jama.Matrix;

public class JamaTest {

	@Test
	public void testMatrix() {
		Matrix matrix = new Matrix(2,2);
		assertEquals(2, matrix.getRowDimension());
		assertEquals(2, matrix.getColumnDimension());
		
		try {
			assertEquals(1, matrix.get(2, 2), 0.001);
			fail("should throw exception. Only indizes from 0 to 1 are allowd.");
		} catch (ArrayIndexOutOfBoundsException e) {
			// do nothing, rows / columns 0 to N-1
		}
		
		assertEquals(0, matrix.get(0, 0), 0.001);
		assertEquals(0, matrix.get(1, 1), 0.001);
	}
	
}
