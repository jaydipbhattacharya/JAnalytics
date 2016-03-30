package tests;

import java.util.Random;

import Matrices.JMatrixAbstract;
import Matrices.LowerMatrix;
import Matrices.RectangularMatrix;
import Matrices.SymmSquareMatrix;
import Matrices.Utility;

public class InvertTest {
	public static void main(String[] args) throws Exception {
		//verifyLowerMatrix();
		verifySquareMatrix();

	}

	static void verifyLowerMatrix() throws Exception {
		int n = 10;
		boolean useThreads = true;
		boolean debugThreads = false;
		Random rand = new Random();
		JMatrixAbstract m = new LowerMatrix(n);
		for (int i = 0; i < m.getNRows(); i++) {
			for (int j = 0; j <= i; j++) {
				m.set(i, j, rand.nextDouble());
			}
		}
		long start = System.currentTimeMillis();
		JMatrixAbstract inverted = Utility.invertLowerMatrix(m, useThreads, debugThreads);

		System.out.println("Original \n" + m + " Inverted \n" + inverted);

		boolean b = Utility.verifylnvert(m, inverted);
		long end = System.currentTimeMillis();
		System.out.println("verifying invert, time taken " + b + " " + (end - start));
	}

	static void verifySquareMatrix() throws Exception {
		Random rand = new Random();
		boolean useThreads = true;
		boolean debugThreads = false;
		int n = 10;
		JMatrixAbstract m = new RectangularMatrix(n, n);
		JMatrixAbstract mcopy = new RectangularMatrix(n, n);
		for (int i = 0; i < m.getNRows(); i++) {
			for (int j = 0; j < m.getNRows(); j++) {
				m.set(i, j, rand.nextDouble());
			}
		}
		Utility.copyOperation(mcopy, m);
		long start = System.currentTimeMillis();
		JMatrixAbstract inverted = Utility.invert(m, useThreads, debugThreads);

		System.out.println("Original \n" + m + " Inverted \n" + inverted);

		boolean b = Utility.verifylnvert(mcopy, inverted);
		long end = System.currentTimeMillis();
		System.out.println("verifying invert, time taken " + b + " " + (end - start));
	}
}
