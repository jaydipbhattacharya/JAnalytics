package tests;

import java.util.Random;
import Matrices.JMatrixAbstract;
import Matrices.RectangularMatrix;
import Matrices.Utility;
import algos.LUDecomp;

public class LUTest {
	public static void main(String[] args) throws Exception {
		int sz = 10;
		RectangularMatrix m = new RectangularMatrix(sz,sz);
		Random rand = new Random();
		for (int i = 0; i < m.getNRows(); i++) {
			for (int j = 0; j < m.getNCols(); j++) {
				m.set(i, j, rand.nextDouble() );
			}
		}
		RectangularMatrix mcopy = new RectangularMatrix(sz,sz);
		Utility.copyOperation(mcopy, m);
		/// System.out.println( m);
		LUDecomp lu = new LUDecomp(m);
		long start = System.currentTimeMillis();
		lu.decompose(0);
		long end = System.currentTimeMillis();
		System.out.println(lu.verifyLUDecomposition(mcopy) + " Decomposition time taken " + (end - start));
	}
}
