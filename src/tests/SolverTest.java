package tests;

import java.util.Random;
import Matrices.JMatrixAbstract;
import Matrices.RectangularMatrix;
import Matrices.Utility;
import algos.GESolver;

public class SolverTest {
	public static void main(String[] args) throws Exception {
		int n = 20;
		boolean useThreads = true;
		boolean debugThreads = false;
		RectangularMatrix m = new RectangularMatrix(n, n);
		Random rand = new Random();
		for (int i = 0; i < m.getNRows(); i++) {
			for (int j = 0; j < m.getNCols(); j++) {
				m.set(i, j, rand.nextDouble() * 0.8);
			}
		}
		RectangularMatrix mcopy = new RectangularMatrix(n, n);
		Utility.copyOperation(mcopy, m);
		double[] rhsValues = new double[n] ;
		for( int i = 0; i< n; i++ ) rhsValues[i] = rand.nextDouble()*0.8;
		JMatrixAbstract rhs = new RectangularMatrix(n, 1);
		for (int i = 0; i < n; i++)
			rhs.set(i, 0, rhsValues[i]);
		JMatrixAbstract solution = GESolver.solve(m, rhs, useThreads, debugThreads);
		
		System.out.println( GESolver.verifySolve(mcopy, solution,  rhs) + "\n" + solution);
	}
}
